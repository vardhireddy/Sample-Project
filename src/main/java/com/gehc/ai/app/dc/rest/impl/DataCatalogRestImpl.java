/*
 * DataCatalogRestImpl.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.rest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.rest.IDataCatalogRest;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */

@RestController
public class DataCatalogRestImpl implements IDataCatalogRest {
   
    @Autowired
    private IDataCatalogService dataCatalogService;
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCatalogResource()
     */
    @Override
    @RequestMapping("/dataCatalog")
    public List<ImageSet> getDataCatalogResource() {
        List<ImageSet> imageSet;
        try {
            imageSet = dataCatalogService.getDataCatalog();
            return imageSet;
        } catch ( Exception e ) {
             e.printStackTrace();
        }
    return null;
    }
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#testDataCatalogResource()
     */
    @Override
    @RequestMapping("/deepLearning")
    public String testDataCatalogResource() {
        return "Welcome to Deep Learning";
    }
}
