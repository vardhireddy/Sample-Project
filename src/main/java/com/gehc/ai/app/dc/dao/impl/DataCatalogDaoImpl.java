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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.dc.dao.IDataCatalogDao;
import com.gehc.ai.app.dc.entity.Creator;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;

/**
 * @author 212071558
 *
 */
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao {
	private static final String DB_SCHEMA_VERSION = "v1.0";
	private static final String GET_IMGSET_DATA_BY_ORG_ID = "SELECT id, seriesId, studyId, patientId, orgId, orgName, modality, anatomy, diseaseType, dataFormat, age, gender, uri FROM image_set where orgId = ?";

	private static final String GET_IMAGESET_ID = "SELECT json_extract(a.data, '$.imageSets') as imageSetId FROM data_collection a where id = '1474403308'";

	private static final String GET_DATA_COLLECTION = "SELECT json_extract(a.data, '$.id') as id ,json_extract(a.data, '$.name') as name, "
			+ " json_extract(a.data, '$.description') as description, "
			+ " json_extract(a.data, '$.createdDate') as createdDate, "
			+ " json_extract(a.data, '$.creator.creatorName') as creatorName,"
			+ " json_extract(a.data, '$.creator.creatorId') as creatorId FROM data_collection a ";

	private static final String GET_IMAGESET_BY_DATA_COLL_ID = "SELECT imgSet.id, seriesId, studyId, patientId, orgId, orgName, modality, anatomy, diseaseType, dataFormat, age, gender, uri "
			+ "FROM data_collection dataColl, image_set imgSet "
			+ "where dataColl.id = ? "
			+ "and JSON_SEARCH(dataColl.data, 'one', imgSet.id) is not null ";

	private static final String INSERT_DATA_COLLECTION = " insert into data_collection () values (?, ?) ";
	
	private static final String INSERT_IMAGE_SET = " insert into image_set (id, schemaVersion, seriesId, studyId, patientId, orgId, orgName, permissionId, "
			+ "modality, anatomy, diseaseType, dataFormat, age, gender, uri) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#getDataCatalog()
	 */
	@Override
	public List<ImageSet> getImgSetByOrgId(String orgId) throws Exception {
		List<ImageSet> imageSetList = new ArrayList<ImageSet>();
		if (null != orgId && orgId.length() > 0) {
			imageSetList = jdbcTemplate.query(GET_IMGSET_DATA_BY_ORG_ID,
					new PreparedStatementSetter() {
						@Override
						public void setValues(java.sql.PreparedStatement ps)
								throws SQLException {
							int index = 0;
							ps.setString(++index, orgId);
						}
					}, new ImageSetRowMapper());
		}
		return imageSetList;
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
			dataCollection.setCreatedDate(new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
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
	public int insertImageSet(ImageSet imageSet) throws Exception {
		int numOfRowsInserted = 0;
		if (null != imageSet) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date());
			numOfRowsInserted = jdbcTemplate.update(
					INSERT_IMAGE_SET,
					new Object[] { String.valueOf(calendar.getTimeInMillis()), DB_SCHEMA_VERSION, imageSet.getSeriesId(), imageSet.getStudyId(), imageSet.getPatientId(),
							imageSet.getOrgId(), imageSet.getOrgName(), imageSet.getPermissionId(), imageSet.getModality(), imageSet.getAnatomy(), imageSet.getDiseaseType(),
							imageSet.getDataFormat(), imageSet.getAge(), imageSet.getGender(), imageSet.getUri()},
					new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
							    Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
		}
		return numOfRowsInserted;
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