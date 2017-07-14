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
package com.gehc.ai.app.datacatalog.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.AnnotationImgSetDataCol;

/**
 * @author 212071558
 *
 */
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger( DataCatalogDaoImpl.class ); 
    private static final String GET_ANNOTATION_DATA_BY_DC_ID = "SELECT dc.id dc_id, im.id  im_id, an.id annotation_id,"
            + "im.patient_dbid, im.uri,"
            + "an.type annotation_type, an.item annotation_item, an.annotator_id, an.annotation_date "
            + "FROM data_collection dc "
            + "inner join image_set im ON JSON_SEARCH(dc.data, 'one', im.id) IS NOT NULL "
            + "inner join annotation an ON im.id = an.image_set ";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public List<AnnotationImgSetDataCol> getAnnotationByDataColId(String dataCollectionId, String annotationType) {
            List<AnnotationImgSetDataCol> annotationImgSetDataCols = new ArrayList<AnnotationImgSetDataCol>();

    StringBuilder builder = new StringBuilder(GET_ANNOTATION_DATA_BY_DC_ID);

    builder = builder.append(" WHERE dc.id = ? ");
    //Called by experiment so will not get the org
  //  builder = builder.append(" AND im.orgId = ? ");
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
                 //   ps.setString(++index, orgId);
                    if(null != annotationType && annotationType.length() > 0) {
                        ps.setString(++index, annotationType);
                    }
                }
            },
            new AnnotationImgSetDataColRowMapper());


            return annotationImgSetDataCols;
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
