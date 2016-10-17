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
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
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
	private static final String DB_SCHEMA_VERSION = "v1.0";
	private static final String GET_IMGSET_DATA_BY_ORG_ID = "SELECT im.id, im.seriesId, im.studyId, im.patientId, im.orgId, im.orgName, im.modality, im.anatomy, im.diseaseType, im.dataFormat, im.age, im.gender, im.uri FROM image_set im ";

	private static final String GET_IMAGESET_ID = "SELECT json_extract(a.data, '$.imageSets') as imageSetId FROM data_collection a where id = '1474403308'";

	private static final String GET_DATA_COLLECTION = "SELECT json_extract(a.data, '$.id') as id ,json_extract(a.data, '$.name') as name, "
			+ " json_extract(a.data, '$.description') as description, "
			+ " json_extract(a.data, '$.createdDate') as createdDate, "
			+ " json_extract(a.data, '$.creator.name') as creatorName,"
			+ " json_extract(a.data, '$.creator.id') as creatorId FROM data_collection a "
			+ " order by json_extract(a.data, '$.createdDate') desc ";

	private static final String GET_IMAGESET_BY_DATA_COLL_ID = "SELECT imgSet.id, seriesId, studyId, patientId, orgId, orgName, modality, anatomy, diseaseType, dataFormat, age, gender, uri "
			+ "FROM data_collection dataColl, image_set imgSet "
			+ "where dataColl.id = ? "
			+ "and JSON_SEARCH(dataColl.data, 'one', imgSet.id) is not null ";

	private static final String INSERT_DATA_COLLECTION = " insert into data_collection () values (?, ?) ";
	
	private static final String INSERT_IMAGE_SET = " insert into image_set (id, schemaVersion, seriesId, studyId, patientId, orgId, orgName, permissionId, "
			+ "modality, anatomy, diseaseType, dataFormat, age, gender, uri) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	private static final java.lang.String PARAM_DELIM = ",";

	private static final String INSERT_ANNOTATION_SET = " insert into annotation_set () values (?, ?) ";

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
			//System.err.println("hey Annotations=" + params.get("annotations"));
			annotValue = params.remove("annotations");
		}
		builder.append(GET_IMGSET_DATA_BY_ORG_ID);
		
		if (annotValue != null) {
			builder.append("INNER JOIN annotation_set an ON JSON_SEARCH(an.data, 'one', im.id, NULL, '$.imageSets') IS NOT NULL ");
		}
		builder.append(constructQuery(params));

		//System.err.println("sql = " + builder);
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
		String imageSetId = null;
		if (null != imageSet) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			imageSetId = String.valueOf(calendar.getTimeInMillis());
			jdbcTemplate.update(
					INSERT_IMAGE_SET,
					new Object[] { imageSetId, DB_SCHEMA_VERSION, imageSet.getSeriesId(), imageSet.getStudyId(), imageSet.getPatientId(),
							imageSet.getOrgId(), imageSet.getOrgName(), imageSet.getPermissionId(), imageSet.getModality(), imageSet.getAnatomy(), imageSet.getDiseaseType(),
							imageSet.getDataFormat(), imageSet.getAge(), imageSet.getGender(), imageSet.getUri()},
					new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
							    Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
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
		final String query = "select im.uri as img, JSON_EXTRACT(an.data, '$**.mask.uri') "
				+ "as gtMask from image_set im inner join annotation_set an ON "
				+ "JSON_SEARCH(an.data, 'one', im.id, NULL, '$.imageSets') "
				+ "IS NOT NULL inner join data_collection dc ON "
				+ "JSON_SEARCH(dc.data, 'one', im.id, NULL, '$.imageSets') IS NOT NULL;";
		
		List<TargetData> alist = new ArrayList<TargetData>();
		alist = jdbcTemplate.query(query, new ResultSetExtractor<List<TargetData>>() {
			@Override
			public List<TargetData> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TargetData> asList = new ArrayList<TargetData>();
	            while(rs.next()) {
	            	TargetData td = new TargetData();
	            	td.img =  rs.getString("img");
	                td.gtMask = rs.getString("gtMask");
	                asList.add(td);
	            }
	            return asList;
			}
		});
		
		return alist;
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
			imageSet.setSeriesId(rs.getString("seriesId"));
			imageSet.setPatientId(rs.getString("patientId"));
			imageSet.setStudyId(rs.getString("studyId"));
			imageSet.setOrgId(rs.getString("orgId"));
			imageSet.setOrgName(rs.getString("orgName"));
			imageSet.setModality(rs.getString("modality"));
			imageSet.setAnatomy(rs.getString("anatomy"));
			imageSet.setDiseaseType(rs.getString("diseaseType"));
			imageSet.setDataFormat(rs.getString("dataFormat"));
			imageSet.setAge(rs.getInt("age"));
			imageSet.setGender(rs.getString("gender"));
			imageSet.setUri(rs.getString("uri"));
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return imageSet;
	}
}