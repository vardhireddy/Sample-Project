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
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

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
    private static final String GET_IMAGESET_DATA = "SELECT * FROM imageset limit 1";
    
    private static final String GET_IMAGESET_ID = "SELECT json_extract(a.data, '$.imageSets') as imageSetId FROM data_collection a where id = '1474403308'";
    
    private static final String GET_DATA_COLLECTION = "SELECT json_extract(a.data, '$.id') as id ,json_extract(a.data, '$.name') as name, "
            + " json_extract(a.data, '$.description') as description, "
            + " json_extract(a.data, '$.createdDate') as createdDate, "
            + " json_extract(a.data, '$.creator.creatorName') as creatorName,"
            + " json_extract(a.data, '$.creator.creatorId') as creatorId FROM data_collection a ";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#getDataCatalog()
     */
    @Override
    public List<ImageSet> getImageSet() throws Exception {
        List<ImageSet> imageSetList = new ArrayList<ImageSet>();
        imageSetList.add(jdbcTemplate.query(GET_IMAGESET_DATA, new ResultSetExtractor<ImageSet>() {
            @Override
            public ImageSet extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {                     
                    ImageSet imageSet = new ImageSet();
                    imageSet.setId(rs.getString("id"));
                    imageSet.setPatientId( rs.getString("patientId"));
                    return imageSet;
                }
                return null;
            }
        }));
   
         return imageSetList;
    }
    
    @Override
    public String[] getImgSetIdForDC( String id ) throws Exception {
           jdbcTemplate.query( GET_IMAGESET_ID, new ResultSetExtractor<String[]>() {
            String[] imgSetId;
            @Override
            public String[] extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {   
                    Array imgSet = rs.getArray("imageSetId");
                    imgSetId = (String[])imgSet.getArray();                   
                 }
                return imgSetId;
            }
        });        
        return null;
    }


    @Override
    public List<DataCollection> getDataCollection() throws Exception {
        List<DataCollection> dataCollectionList = new ArrayList<DataCollection>();
        dataCollectionList = jdbcTemplate.query( GET_DATA_COLLECTION, new DataCollectionRowMapper());
        return dataCollectionList;
    }
}

class DataCollectionRowMapper implements RowMapper<DataCollection> {
    @Override
    public DataCollection mapRow( ResultSet rs, int rowNum ) throws SQLException {
        DataCollection dataCollection = new DataCollection();
        Creator creator = new Creator();
        try { 
                dataCollection.setId(rs.getString("id"));  
                dataCollection.setName( rs.getString("name") );
                dataCollection.setDescription( rs.getString("description") );
                dataCollection.setCreatedData(rs.getString("createdDate") );
                creator.setName(rs.getString("creatorName"));
                creator.setId(rs.getString("creatorId"));
                dataCollection.setCreator(creator);
        } catch ( Exception e ) {
          throw new SQLException( e );}
        return dataCollection;
    }    
}
