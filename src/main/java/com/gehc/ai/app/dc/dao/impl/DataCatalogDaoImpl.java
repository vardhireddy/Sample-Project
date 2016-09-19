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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.dc.dao.IDataCatalogDao;
import com.gehc.ai.app.dc.entity.ImageSet;

/**
 * @author 212071558
 *
 */
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao {
    private static final String GET_IMAGESET_DATA = "SELECT * FROM imageset limit 1";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#getDataCatalog()
     */
    @Override
    public List<ImageSet> getDataCatalog() throws Exception {
        List<ImageSet> imageSetList = new ArrayList<ImageSet>();
        imageSetList.add(jdbcTemplate.query(GET_IMAGESET_DATA, new ResultSetExtractor<ImageSet>() {
            @Override
            public ImageSet extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {                     
                    ImageSet imageSet = new ImageSet();
                    imageSet.setId(rs.getInt("id"));
                    imageSet.setPatientId( rs.getString("patientId"));
                    return imageSet;
                }
                return null;
            }
        }));
        return imageSetList;
    }
}
