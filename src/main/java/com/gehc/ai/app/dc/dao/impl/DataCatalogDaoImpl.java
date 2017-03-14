/*
 * DataCatalogDaoImpl.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.dao.impl;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gehc.ai.app.dc.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.dc.dao.IDataCatalogDao;

/**
 * @author 212071558
 *
 */
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger( DataCatalogDaoImpl.class );

       private static final String GET_IMGSET_DATA_BY_ORG_ID = "SELECT distinct im.id, im.schemaVersion, series_instance_uid, study_dbid, patient_dbid, orgId, modality, anatomy, dataFormat, uri, "
			+ " acq_date, acq_time, description, institution, equipment, instance_count, im.upload_by, im.upload_date, im.properties, p.patient_id, im.instance_count FROM image_set im  "
			+ " join patient p on im.patient_dbid = p.id ";
	private static final String GET_IMGSET_DATA_BY_STUDY_ID = "SELECT im.id, series_instance_uid, study_dbid, patient_dbid, orgId, modality, anatomy, dataFormat, uri, "
			+ " acq_date, acq_time, description, institution, equipment, instance_count, upload_by, upload_date, properties FROM image_set im WHERE im.study_dbid = ";

	private static final String GET_IMAGESET_ID = "SELECT json_extract(a.data, '$.imageSets') as imageSetId FROM data_collection a where id = '1474403308'";
	
	private static final String GET_DC_PREFIX = "SELECT json_extract(a.data, '$.id') as id ,json_extract(a.data, '$.name') as name, "
                + " json_extract(a.data, '$.type') as type, "
                + " json_extract(a.data, '$.description') as description, "
                + " json_extract(a.data, '$.createdDate') as createdDate, "
                + " json_extract(a.data, '$.creator.name') as creatorName,"
                + " json_extract(a.data, '$.creator.id') as creatorId, "
                + " JSON_LENGTH(json_extract(a.data, '$.imageSets')) as imageSetsSize, a.properties as properties FROM data_collection a ";
	
	private static final String GET_DC_SUFFIX = " order by json_extract(a.data, '$.createdDate') desc ";

	private static final String GET_IMAGESET_BY_DATA_COLL_ID = "SELECT imgSet.id, imgSet.schemaVersion, series_instance_uid, study_dbid, patient_dbid, orgId, "
			+ " modality, anatomy, dataFormat, uri, acq_date, "
			+ " acq_time, description, institution, equipment, instance_count, imgSet.upload_by, imgSet.upload_date, imgSet.properties, p.patient_id, imgSet.instance_count "
			+ "FROM data_collection dataColl, image_set imgSet join patient p on imgSet.patient_dbid = p.id "
			+ "where dataColl.id = ? "
			+ "and JSON_SEARCH(dataColl.data, 'one', imgSet.id) is not null ";

	private static final String INSERT_DATA_COLLECTION = " insert into data_collection () values (?, ?) ";
	
	private static final String INSERT_IMAGE_SET = " insert into image_set (id, schemaVersion, orgId, modality, anatomy, "
			+ " dataFormat, uri, series_instance_uid, acq_date, acq_time, "
			+ " description, institution, equipment, instance_count, upload_by, "
			+ " properties, patient_dbid, study_dbid) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final java.lang.String PARAM_DELIM = ",";

	private static final String INSERT_ANNOTATION_SET = " insert into annotation_set (id, data) values (?, ?) ";
	
	private static final String GET_IMGSET_DATA_BY_PATIENT_ID = "SELECT im.id, orgId FROM image_set im join patient p on im.patient_dbid = p.id where p.patient_id = ";
	
	//private static final String ANNOTATION_JOIN = " INNER JOIN annotation an ON an.image_set = im.id ";

	private static final String GET_ANNOTATION_DATA_BY_DC_ID = "SELECT dc.id dc_id, im.id  im_id, an.id annotation_id,"
			+ "im.patient_dbid, im.uri,"
			+ "an.type annotation_type, an.item annotation_item, an.annotator_id, an.annotation_date "
			+ "FROM data_collection dc "
			+ "inner join image_set im ON JSON_SEARCH(dc.data, 'one', im.id) IS NOT NULL "
			+ "inner join annotation an ON im.id = an.image_set ";
	
	private static final String UPDATE_DATA_COLLECTION = "update data_collection set properties = ? where id = ? ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#getDataCatalog()
	 */
	@Override
	public List<ImageSet> getImgSet(Map<String, String> params) throws Exception {
		List<ImageSet> imageSetList;
		StringBuilder builder = new StringBuilder();
		String annotValue = null;
		if (params != null && params.get("annotations") != null) {
			annotValue = params.remove("annotations");
		}
		builder.append(GET_IMGSET_DATA_BY_ORG_ID);
		
		/*if (annotValue != null) {
			builder.append(ANNOTATION_JOIN);
		}*/
		builder.append(constructQuery(params));

		logger.info("!!! getImgSet by org id sql = " + builder);
		imageSetList = jdbcTemplate.query(builder.toString(), new ImageSetWithMoreInfoRowMapper());
		return imageSetList;
	}

	String constructQuery(Map<String, String> params) {
		StringBuilder builder = new StringBuilder();
		if (null != params && params.size() > 0) {
			builder.append("WHERE ");

			Iterator<String> paramIterator = params.keySet().iterator();
			while (paramIterator.hasNext()) {
				String key = paramIterator.next();
				String values = params.get(key);
				builder.append(constructWhereClause(key,values));
				if (paramIterator.hasNext()) {
					builder.append(" AND ");
				}
			}
		}
		return builder.toString();
	}

	private String constructWhereClause(String param, String values) {
		StringBuilder whereClause = new StringBuilder().append(param + " IN (") ;
        whereClause.append(quoteValues(values));
		whereClause.append(")");

		return whereClause.toString();
	}

	private String quoteValues(String values) {
		StringBuilder builder = new StringBuilder();
		Iterator<String> valuesIterator = Arrays.asList(values.split(",")).iterator();

		while (valuesIterator.hasNext()) {
			builder.append("\"").append(valuesIterator.next()).append("\"");
			if (valuesIterator.hasNext()) {
				builder.append(", ");
			}
		}

		return builder.toString();
	}

	@Override
	public String[] getImgSetIdForDC(String id) throws Exception {
		jdbcTemplate.query(GET_IMAGESET_ID, new ResultSetExtractor<String[]>() {
			String[] imgSetId;

			@Override
			public String[] extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Array imgSet = rs.getArray("imageSetId");
					imgSetId = (String[]) imgSet.getArray();
				}
				return imgSetId;
			}
		});
		return null;
	}

	@Override
	public List<DataCollection> getDataCollection(String id , String type ) throws Exception {
		List<DataCollection> dataCollectionList = new ArrayList<DataCollection>();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(GET_DC_PREFIX);
		if (null != id && id.length() > 0) {
		    if(null != type && type.length() > 0){
		        queryBuilder = queryBuilder.append(" where a.id = ? and json_extract(a.data, '$.type') = ? ");
                        logger.info("*** Get Data Collection By Id "+ id + " and type " + type + " query " + queryBuilder);
                        dataCollectionList = jdbcTemplate.query(queryBuilder.toString(),
                                        new PreparedStatementSetter() {
                                @Override
                                public void setValues(java.sql.PreparedStatement ps)
                                                throws SQLException {
                                        int index = 0;
                                        ps.setString(++index, id);
                                        ps.setString(++index, type);
                                }
                        },      new DataCollectionRowMapper());
		    }else{
		        queryBuilder = queryBuilder.append(" where a.id = ? ");
			logger.info("*** Get Data Collection By Id "+ id + " query " + queryBuilder);
			dataCollectionList = jdbcTemplate.query(queryBuilder.toString(),
					new PreparedStatementSetter() {
				@Override
				public void setValues(java.sql.PreparedStatement ps)
						throws SQLException {
					int index = 0;
					ps.setString(++index, id);
				}
			},	new DataCollectionRowMapper());
		    }
		}else if(null != type && type.length() > 0){
                        queryBuilder = queryBuilder.append(" where  json_extract(a.data, '$.type') = ? ");
                        logger.info("*** Get Data Collection By type " + type + " query " + queryBuilder);
                        dataCollectionList = jdbcTemplate.query(queryBuilder.toString(),
                                        new PreparedStatementSetter() {
                                @Override
                                public void setValues(java.sql.PreparedStatement ps)
                                                throws SQLException {
                                        int index = 0;
                                        ps.setString(++index, type);
                                }
                        },      new DataCollectionRowMapper());
                    }else{
		    queryBuilder = queryBuilder.append(GET_DC_SUFFIX);
		        logger.info("*** Get All Data Collection query " + queryBuilder);
			dataCollectionList = jdbcTemplate.query(queryBuilder.toString(),
					new DataCollectionRowMapper());
		}
		return dataCollectionList;
	}

	@Override
	public List<ImageSet> getImgSetByDataCollId(String dataCollectionId) throws Exception {
		List<ImageSet> imageSetList = new ArrayList<ImageSet>();
		logger.info("*** Get ImgSet and patient id by DataCollId = " + dataCollectionId);
		if (null != dataCollectionId && dataCollectionId.length() > 0) {
			imageSetList = jdbcTemplate.query(GET_IMAGESET_BY_DATA_COLL_ID,
					new PreparedStatementSetter() {
						@Override
						public void setValues(java.sql.PreparedStatement ps)
								throws SQLException {
							int index = 0;
							ps.setString(++index, dataCollectionId);
						}
					}, new ImageSetWithMoreInfoRowMapper());
		}
		return imageSetList;
	}

	@Override
	public String createDataCollection(DataCollection dataCollection)
			throws Exception {
	    String dataCollId = null;    
	    if (null != dataCollection) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			dataCollection.setId(String.valueOf(calendar.getTimeInMillis()));
			dataCollection.setSchemaVersion("v1.0");
			dataCollection.setCreatedDate(String.valueOf(calendar.getTimeInMillis()));
			ObjectMapper mapper = new ObjectMapper();
			 jdbcTemplate.update(
					INSERT_DATA_COLLECTION,
					new Object[] { dataCollection.getId(),
							mapper.writeValueAsString(dataCollection) },
					new int[] { Types.VARCHAR, Types.VARCHAR });
			 dataCollId = dataCollection.getId();
		}
		return dataCollId;
	}


	@Override
	public String insertImageSet(ImageSet imageSet) throws Exception {
		String imageSetId = null;
		if (null != imageSet && null != imageSet.getOrgId() && !imageSet.getOrgId().isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			imageSetId = String.valueOf(calendar.getTimeInMillis());
			ObjectMapper mapper = new ObjectMapper();
			jdbcTemplate.update(
					INSERT_IMAGE_SET,
					new Object[] { imageSetId, imageSet.getSchemaVersion(), imageSet.getOrgId(), imageSet.getModality(), imageSet.getAnatomy(),
							imageSet.getDataFormat(), imageSet.getUri(), imageSet.getSeriesInstanceUid(), imageSet.getAcqDate(), imageSet.getAcqTime(),
							imageSet.getDescription(), imageSet.getInstitution(), imageSet.getEquipment(), imageSet.getInstanceCount(), imageSet.getUploadBy(),
							mapper.writeValueAsString(imageSet.getProperties()), imageSet.getPatientDbId(), imageSet.getStudyDbId()},
					new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR,
						    Types.VARCHAR, Types.BIGINT, Types.BIGINT});
			}
		return imageSetId;
	}
	
	@Override
	public int insertAnnotationSet(AnnotationSet annotationSetJson) throws Exception {
		int numOfRowsInserted = 0;
		AnnotationSet annotationSet = annotationSetJson;
		if (annotationSet != null) {
			annotationSet.setId("" + System.currentTimeMillis());
			ObjectMapper mapper = new ObjectMapper();
			numOfRowsInserted = jdbcTemplate.update(
				INSERT_ANNOTATION_SET,
				new Object[] { annotationSet.getId(), mapper.writeValueAsString(annotationSet)},
				new int[] { Types.VARCHAR, Types.VARCHAR});
		}
		return numOfRowsInserted;
	}

	private static final String [] ANNOT_SET_COLUMNS = {
		"schemaVersion",
		"id",
		"orgId",
		"orgName",
		"imageSets",
		"creatorType",
		"creatorId",
		"items"};
	
	@Override
	public List getAnnotationSet(String imageSets, String fields, Map<String, String> queryMap) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT DISTINCT ");
		String [] fs = null;
		
		if (fields == null || fields.equals("")) {
			fs =  ANNOT_SET_COLUMNS;
		} else 
			fs = fields.split(",");
		
		boolean containsIdCol = false;
		for (int i = 0; i < fs.length; i++) {
			sql.append("json_extract(data, '$." + fs[i] + "') as " + fs[i]);
			if (fs[i].equals("id"))
				containsIdCol = true;
			if (i < fs.length - 1)
				sql.append(", ");
		}
		
		if (!containsIdCol)
			sql.append(", id ");
		
		sql.append(" from annotation_set");
		
		boolean search = false;
		if (imageSets != null && !imageSets.equals("")) {
			System.out.println("imagesets = " + imageSets);
			String[] isets = imageSets.split(",");
			sql.append(" where ");
			search = true;
			//where JSON_SEARCH(data, 'one', '0.09418948718780629', null, '$.imageSets') is not null
			for (int k = 0; k < isets.length; k++) {
				sql.append("JSON_SEARCH(data, 'one', " + isets[k] + ", null, '$.imageSets') is not null");
				if (k < isets.length - 1)
					sql.append(" and ");
			}
		}
		
		if (queryMap.size() > 0) {
			if (!search) {
				sql.append(" where ");
				Set<String> keys = queryMap.keySet();
				
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String key = it.next();
					String value = queryMap.get(key);
					sql.append("JSON_SEARCH(data, 'one', " + value + ", null, '$." + key + "') is not null");
					if (it.hasNext())
						sql.append(" and ");
				}
			}
		}
		sql.append(";");
		System.out.println(sql);
		
		
		List alist = new ArrayList();
//		alist.add(imageSets);
//		alist.add(fields);
//		alist.addAll(queryMap.entrySet());
		final String[] fs1 = fs;
		alist = jdbcTemplate.query(sql.toString(), new ResultSetExtractor<List>() {
			@Override
			public List extractData(ResultSet rs) throws SQLException, DataAccessException {
				List asList = new ArrayList();
	            while(rs.next()) {
	                HashMap m = new HashMap();
	                for (int k = 0; k < fs1.length; k++) {
	                	Object o = rs.getObject(fs1[k]);
	                	System.out.println("o = " + o.getClass());
	                	if (o instanceof String) {
	                		String s = (String) o;
	                		System.out.println("before: " + s);
	                		s = s.replaceAll("\\\\\"", "\"");
	                		System.out.println("after: " + s);

	                		m.put(fs1[k],s);
	                	} else {
	                		m.put(fs1[k],o);
	                	}
	                }
	                asList.add(m);
	            }
	            return asList;
			}
		});
		
		return alist;
	}


	@Deprecated
	@Override
	public List<TargetData> getExperimentTargetData(String dataCollectionIds) throws Exception {
		/*final String query = "select im.patient_dbid as pid, im.uri as img, JSON_EXTRACT(an.data, '$**.mask.uri') "
				+ "as gtMask from image_set im inner join annotation_set an ON "
				+ "JSON_SEARCH(an.data, 'one', im.id, NULL, '$.imageSets') "
				+ "IS NOT NULL inner join data_collection dc ON "
				+ "JSON_SEARCH(dc.data, 'one', im.id, NULL, '$.imageSets') IS NOT NULL "
				+ "WHERE dc.id=" + dataCollectionIds+ ";";*/
	    
	    final String query = "select im.patient_dbid as pid, im.uri as img, JSON_EXTRACT(an.item, '$.uri') "
                    + "as gtMask from image_set im inner join annotation an ON "
                    + "an.image_set = im.id "
                    + "inner join data_collection dc ON "
                    + "JSON_SEARCH(dc.data, 'one', im.id, NULL, '$.imageSets') IS NOT NULL "
                    + "WHERE dc.id=" + dataCollectionIds+ " and an.type = 'mask' ;";
		
		List<TargetData> alist = new ArrayList<TargetData>();
		alist = jdbcTemplate.query(query, new ResultSetExtractor<List<TargetData>>() {
			@Override
			public List<TargetData> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TargetData> asList = new ArrayList<TargetData>();
	            while(rs.next()) {
	            	TargetData td = new TargetData();
	            	td.patientId = rs.getString("pid");
	            	td.img =  rs.getString("img");
	            	if(null != rs.getString("gtMask") && !rs.getString("gtMask").isEmpty()){
	            	   // td.gtMask = rs.getString("gtMask").replaceAll("\\[\"", "").replaceAll("\"\\]", "");
	            	     td.gtMask = rs.getString("gtMask").replace("\"", "");
	            	     td.gtMask.trim();
	            	}	                
	                asList.add(td);
	            }
	            return asList;
			}
		});
		
		return alist;
	}

	@Override
	public List<AnnotationImgSetDataCol> getAnnotationByDataColId(String dataCollectionId, String annotationType) {
		List<AnnotationImgSetDataCol> annotationImgSetDataCols = new ArrayList<AnnotationImgSetDataCol>();

        StringBuilder builder = new StringBuilder(GET_ANNOTATION_DATA_BY_DC_ID);

        builder = builder.append("WHERE dc.id = ?");

        if(null != annotationType && annotationType.length() > 0) {
            builder = builder.append(" AND an.type = ? ");
        }

        logger.info("Get Annotation by Data Collection id; SQL: " + builder + "dataCollectionId  = " + dataCollectionId + " and annotationType = " + annotationType);
        annotationImgSetDataCols = jdbcTemplate.query(builder.toString(),
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(java.sql.PreparedStatement ps)
                            throws SQLException {
                        int index = 0;
                        ps.setString(++index, dataCollectionId);
                        if(null != annotationType && annotationType.length() > 0) {
                            ps.setString(++index, annotationType);
                        }
                    }
                },
                new AnnotationImgSetDataColRowMapper());


		return annotationImgSetDataCols;
	}

	@Override
	public List<ImageSet> getImageSetByStudyId(String studyId) {
		List<ImageSet> imageSetList;
		StringBuilder builder = new StringBuilder(GET_IMGSET_DATA_BY_STUDY_ID);
		
		builder.append("'" + studyId + "'");

		logger.info("*** getImageSetByStudyId sql = " + builder);
		imageSetList = jdbcTemplate.query(builder.toString(), new ImageSetRowMapper());
		return imageSetList;
	}

    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#getImageSetByPatientId(java.lang.String)
     */
    @Override
    public List<ImageSet> getImageSetByPatientId( String patientid ) {
        List<ImageSet> imageSetList;
        StringBuilder builder = new StringBuilder(GET_IMGSET_DATA_BY_PATIENT_ID);
        builder.append("'" + patientid + "'");
        logger.info("!!!! getImageSetByPatientId sql = " + builder);
        imageSetList = jdbcTemplate.query(builder.toString(), new ImageSetInfoRowMapper());
        return imageSetList;
    }

    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#updateDataCollection(com.gehc.ai.app.dc.entity.DataCollection)
     */
    @Override
    public String updateDataCollection( DataCollection dataCollection ) throws Exception {
        String dataCollId = null;    
        if (null != dataCollection) {
            logger.info(" !!! In updateDataCollection dao");
            logger.info(" *** In updateDataCollection dao, id " +   dataCollection.getId());
                     ObjectMapper mapper = new ObjectMapper();
                     logger.info(" *** In updateDataCollection dao" +  mapper.writeValueAsString(dataCollection.getProperties()));
                     jdbcTemplate.update(
                                    UPDATE_DATA_COLLECTION,
                                    new Object[] { mapper.writeValueAsString(dataCollection.getProperties()), dataCollection.getId()},
                                    new int[] { Types.VARCHAR, Types.VARCHAR });
                     dataCollId = dataCollection.getId();
            }
            return dataCollId;        
    }
}

class ImageSetWithMoreInfoRowMapper implements RowMapper<ImageSet> {
	@Override
	public ImageSet mapRow(ResultSet rs, int rowNum) throws SQLException {
		ImageSet imageSet = new ImageSet();
		try {
			imageSet.setId(rs.getString("id"));
			imageSet.setSchemaVersion( rs.getString("schemaVersion") );
			imageSet.setSeriesInstanceUid(rs.getString("series_instance_uid"));
			imageSet.setStudyDbId(rs.getLong("study_dbid"));
			imageSet.setPatientDbId(rs.getLong("patient_dbid"));
			imageSet.setOrgId(rs.getString("orgId"));
			imageSet.setModality(rs.getString("modality"));
			imageSet.setAnatomy(rs.getString("anatomy"));
			imageSet.setDataFormat(rs.getString("dataFormat"));
			imageSet.setUri(rs.getString("uri"));
			imageSet.setAcqDate(rs.getString("acq_date"));			
			imageSet.setAcqTime(rs.getString("acq_time"));
			imageSet.setDescription(rs.getString("description"));
			imageSet.setInstitution(rs.getString("institution"));
			imageSet.setEquipment(rs.getString("equipment"));
			imageSet.setInstanceCount(rs.getInt("instance_count"));
			imageSet.setUploadBy(rs.getString("upload_by"));
			imageSet.setUploadDate(rs.getString("upload_date"));
			imageSet.setProperties(rs.getString("properties"));	
			imageSet.setPatientId(rs.getString("patient_id"));
			imageSet.setInstanceCount(rs.getInt("instance_count"));
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return imageSet;
	}
}

class DataCollectionRowMapper implements RowMapper<DataCollection> {
	@Override
	public DataCollection mapRow(ResultSet rs, int rowNum) throws SQLException {
		DataCollection dataCollection = new DataCollection();
		Creator creator = new Creator();
		try {
			dataCollection.setId(rs.getString("id"));
			dataCollection.setName(rs.getString("name"));
			dataCollection.setType(rs.getString("type"));
			dataCollection.setDescription(rs.getString("description"));
			dataCollection.setCreatedDate(rs.getString("createdDate"));
			creator.setName(rs.getString("creatorName"));
			creator.setId(rs.getString("creatorId"));
			dataCollection.setCreator(creator);
			dataCollection.setImageSetsSize(rs.getInt("imageSetsSize"));
			dataCollection.setProperties( rs.getString("properties"));
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return dataCollection;
	}
	
}

class ImageSetRowMapper implements RowMapper<ImageSet> {
	@Override
	public ImageSet mapRow(ResultSet rs, int rowNum) throws SQLException {
		ImageSet imageSet = new ImageSet();
		try {
			imageSet.setId(rs.getString("id"));
			imageSet.setSeriesInstanceUid(rs.getString("series_instance_uid"));
			imageSet.setStudyDbId(rs.getLong("study_dbid"));
			imageSet.setPatientDbId(rs.getLong("patient_dbid"));
			imageSet.setOrgId(rs.getString("orgId"));
			imageSet.setModality(rs.getString("modality"));
			imageSet.setAnatomy(rs.getString("anatomy"));
			imageSet.setDataFormat(rs.getString("dataFormat"));
			imageSet.setUri(rs.getString("uri"));
			imageSet.setAcqDate(rs.getString("acq_date"));			
			imageSet.setAcqTime(rs.getString("acq_time"));
			imageSet.setDescription(rs.getString("description"));
			imageSet.setInstitution(rs.getString("institution"));
			imageSet.setEquipment(rs.getString("equipment"));
			imageSet.setInstanceCount(rs.getInt("instance_count"));
			imageSet.setUploadBy(rs.getString("upload_by"));
			imageSet.setUploadDate(rs.getString("upload_date"));
			imageSet.setProperties(rs.getString("properties"));			
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return imageSet;
	}
}

class AnnotationImgSetDataColRowMapper implements RowMapper<AnnotationImgSetDataCol> {
	@Override
	public AnnotationImgSetDataCol mapRow(ResultSet rs, int rowNum) throws SQLException {
		AnnotationImgSetDataCol annotationImgSetDataCol = new AnnotationImgSetDataCol();
		try {
			annotationImgSetDataCol.setDcId(rs.getString("dc_id"));
			annotationImgSetDataCol.setImId(rs.getString("im_id"));
			annotationImgSetDataCol.setAnnotationId(rs.getString("annotation_id"));
			annotationImgSetDataCol.setPatientDbid(rs.getString("patient_dbid"));
			annotationImgSetDataCol.setUri(rs.getString("uri"));
			annotationImgSetDataCol.setAnnotationType(rs.getString("annotation_type"));
			annotationImgSetDataCol.setAnnotatorId(rs.getString("annotator_id"));
			annotationImgSetDataCol.setAnnotationDate(rs.getString("annotation_date"));

            TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};

            ObjectMapper mapper = new ObjectMapper();

            HashMap<String,Object> o = mapper.readValue(rs.getString("annotation_item"), typeRef);
            annotationImgSetDataCol.setAnnotationItem(o);

        } catch (Exception e) {
			throw new SQLException(e);
		}
		return annotationImgSetDataCol;
	}

}

class ImageSetInfoRowMapper implements RowMapper<ImageSet> {
	        @Override
	        public ImageSet mapRow(ResultSet rs, int rowNum) throws SQLException {
	                ImageSet imageSet = new ImageSet();
	                try {
	                        imageSet.setId(rs.getString("id"));
	                        imageSet.setOrgId(rs.getString("orgId"));	                                            
	                } catch (Exception e) {
	                        throw new SQLException(e);
	                }
	                return imageSet;
	        }
}