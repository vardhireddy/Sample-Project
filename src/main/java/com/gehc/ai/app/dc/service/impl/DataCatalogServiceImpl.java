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
package com.gehc.ai.app.dc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.dc.dao.IDataCatalogDao;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */
@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService {
    @Autowired
    private IDataCatalogDao dataCatalogDao;
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.service.IDataCatalogService#getDataCatalog()
     */
    @Override
    public List<ImageSet> getDataCatalog() throws Exception {
        List<ImageSet> imageSet = null;
        try {
            imageSet = dataCatalogDao.getDataCatalog();
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving data catalog ", e );
        }
        return imageSet;
    }
}
