/*
 * IDataCatalogRest.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;

/**
 * @author 212071558
 *
 */
public interface IDataCatalogRest {

    List<ImageSet> getDataCatalogResource();
    
    String testDataCatalogResource();
    
    List<ImageSet> getImageSet(String orgId);
    
    Response getDataCatalog();
    
    List<ImageSet> getImageSetByDataCollectionId(String id);
    
    List<DataCollection> getDataCollection();
}
