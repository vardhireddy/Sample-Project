/*
 * DataCatalogServiceImpl.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.AnnotationImgSetDataCol;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */
@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService {
    @Autowired
    private IDataCatalogDao dataCatalogDao;
 
    @Override
    public List<AnnotationImgSetDataCol> getAnnotationByDataColId(String dataCollectionId, String annotationType) throws Exception {
        List l = null;
        try {
            l = dataCatalogDao.getAnnotationByDataColId(dataCollectionId, annotationType);
        } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving target data ", e );
        }
        return l;
    }
}
