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
import com.gehc.ai.app.dc.entity.AnnotationSet;
import com.gehc.ai.app.dc.entity.Creator;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.entity.TargetData;

/**
 * @author 212071558
 *
 */
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger( DataCatalogDaoImpl.class );

	private static final String DB_SCHEMA_VERSION = "v1.0";
	private static final String GET_IMGSET_DATA_BY_ORG_ID = "SELECT im.id, series_instance_uid, study_dbid, patient_dbid, orgId, modality, anatomy, dataFormat, uri, "
			+ " acq_date, acq_time, description, institution, equipment, instance_count, upload_by, properties  FROM image_set im ";
	private static final String GET_IMGSET_DATA_BY_STUDY_ID = "SELECT im.id, series_instance_uid, study_dbid, patient_dbid, orgId, modality, anatomy, dataFormat, uri, "
			+ " acq_date, acq_time, description, institution, equipment, instance_count, upload_by, properties FROM image_set im WHERE im.study_dbid = ";

	private static final String GET_IMAGESET_ID = "SELECT json_extract(a.data, '$.imageSets') as imageSetId FROM data_collection a where id = '1474403308'";

	private static final String GET_DATA_COLLECTION = "SELECT json_extract(a.data, '$.id') as id ,json_extract(a.data, '$.name') as name, "
			+ " json_extract(a.data, '$.description') as description, "
			+ " json_extract(a.data, '$.createdDate') as createdDate, "
			+ " json_extract(a.data, '$.creator.name') as creatorName,"
			+ " json_extract(a.data, '$.creator.id') as creatorId FROM data_collection a "
			+ " order by json_extract(a.data, '$.createdDate') desc ";

	private static final String GET_IMAGESET_BY_DATA_COLL_ID = "SELECT imgSet.id, series_instance_uid, study_dbid, patient_dbid, orgId, "
			+ " modality, anatomy, dataFormat, uri, acq_date, "
			+ " acq_time, description, institution, equipment, instance_count, upload_by, properties  "
			+ "FROM data_collection dataColl, image_set imgSet "
			+ "where dataColl.id = ? "
			+ "and JSON_SEARCH(dataColl.data, 'one', imgSet.id) is not null ";

	private static final String INSERT_DATA_COLLECTION = " insert into data_collection () values (?, ?) ";
	
	private static final String INSERT_IMAGE_SET = " insert into image_set (id, schemaVersion, orgId, modality, anatomy, "
			+ " dataFormat, uri, series_instance_uid, acq_date, acq_time, "
			+ " description, institution, equipment, instance_count, upload_by, "
			+ " properties, patient_dbid, study_dbid) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final java.lang.String PARAM_DELIM = ",";

	private static final String INSERT_ANNOTATION_SET = " insert into annotation_set () values (?, ?) ";

	
	private static final String ANNOTATION_JOIN = "INNER JOIN annotation_set an ON JSON_SEARCH(an.data, 'one', im.id, NULL, '$.imageSets') IS NOT NULL ";
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
		
		if (annotValue != null) {
			builder.append(ANNOTATION_JOIN);
		}
		builder.append(constructQuery(params));

		logger.info("*** getImgSet sql = " + builder);
		imageSetList = jdbcTemplate.query(builder.toString(), new ImageSetRowMapper());
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
	public List<DataCollection> getDataCollection() throws Exception {
		List<DataCollection> dataCollectionList = new ArrayList<DataCollection>();
		dataCollectionList = jdbcTemplate.query(GET_DATA_COLLECTION,
				new DataCollectionRowMapper());
		return dataCollectionList;
	}

	@Override
	public List<ImageSet> getImgSetByDataCollId(String dataCollectionId) throws Exception {
		List<ImageSet> imageSetList = new ArrayList<ImageSet>();
		logger.info("*** getImgSetByDataCollId GET_IMAGESET_BY_DATA_COLL_ID = " + GET_IMAGESET_BY_DATA_COLL_ID);
		if (null != dataCollectionId && dataCollectionId.length() > 0) {
			imageSetList = jdbcTemplate.query(GET_IMAGESET_BY_DATA_COLL_ID,
					new PreparedStatementSetter() {
						@Override
						public void setValues(java.sql.PreparedStatement ps)
								throws SQLException {
							int index = 0;
							ps.setString(++index, dataCollectionId);
						}
					}, new ImageSetRowMapper());
		}
		return imageSetList;
	}

	@Override
	public String createDataCollection(DataCollection dataCollection)
			throws Exception {
		int numOfRowsInserted = 0;
		if (null != dataCollection) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			dataCollection.setId(String.valueOf(calendar.getTimeInMillis()));
			dataCollection.setSchemaVersion("v1.0");
			//dataCollection.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			dataCollection.setCreatedDate(String.valueOf(calendar.getTimeInMillis()));
			ObjectMapper mapper = new ObjectMapper();
			numOfRowsInserted = jdbcTemplate.update(
					INSERT_DATA_COLLECTION,
					new Object[] { dataCollection.getId(),
							mapper.writeValueAsString(dataCollection) },
					new int[] { Types.VARCHAR, Types.VARCHAR });
		}
		return dataCollection.getId();
	}


	@Override
	public String insertImageSet(ImageSet imageSet) throws Exception {
		logger.info("*** Image set " + imageSet.toString());
		String imageSetId = null;
		if (null != imageSet) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			imageSetId = String.valueOf(calendar.getTimeInMillis());
			ObjectMapper mapper = new ObjectMapper();
			jdbcTemplate.update(
					INSERT_IMAGE_SET,
					new Object[] { imageSetId, DB_SCHEMA_VERSION, imageSet.getOrgId(), imageSet.getModality(), imageSet.getAnatomy(),
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
	public int insertAnnotationSet(String annotationSetJson) throws Exception {
		int numOfRowsInserted = 0;
		AnnotationSet annotationSet = AnnotationSet.readFromJson(annotationSetJson);
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


	@Override
	public List<TargetData> getExperimentTargetData(String dataCollectionIds) throws Exception {
		final String query = "select im.patientId as pid, im.uri as img, JSON_EXTRACT(an.data, '$**.mask.uri') "
				+ "as gtMask from image_set im inner join annotation_set an ON "
				+ "JSON_SEARCH(an.data, 'one', im.id, NULL, '$.imageSets') "
				+ "IS NOT NULL inner join data_collection dc ON "
				+ "JSON_SEARCH(dc.data, 'one', im.id, NULL, '$.imageSets') IS NOT NULL "
				+ "WHERE dc.id=" + dataCollectionIds+ ";";
		
		List<TargetData> alist = new ArrayList<TargetData>();
		alist = jdbcTemplate.query(query, new ResultSetExtractor<List<TargetData>>() {
			@Override
			public List<TargetData> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TargetData> asList = new ArrayList<TargetData>();
	            while(rs.next()) {
	            	TargetData td = new TargetData();
	            	td.patientId = rs.getString("pid");
	            	td.img =  rs.getString("img");
	                td.gtMask = rs.getString("gtMask").replaceAll("\\[\"", "").replaceAll("\"\\]", "");;
	                
	                
	                //System.err.println("gtmask -> " + td.gtMask);
	                
	                asList.add(td);
	            }
	            return asList;
			}
		});
		
		return alist;
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


}

class DataCollectionRowMapper implements RowMapper<DataCollection> {
	@Override
	public DataCollection mapRow(ResultSet rs, int rowNum) throws SQLException {
		DataCollection dataCollection = new DataCollection();
		Creator creator = new Creator();
		try {
			dataCollection.setId(rs.getString("id"));
			dataCollection.setName(rs.getString("name"));
			dataCollection.setDescription(rs.getString("description"));
			dataCollection.setCreatedDate(rs.getString("createdDate"));
			creator.setName(rs.getString("creatorName"));
			creator.setId(rs.getString("creatorId"));
			dataCollection.setCreator(creator);
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
			imageSet.setProperties(rs.getString("properties"));			
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return imageSet;
	}
}