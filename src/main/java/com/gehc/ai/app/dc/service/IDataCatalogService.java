/*
 * IDataCatalogService.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.service;

import java.util.List;

import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;

/**
 * @author 212071558
 *
 */
/**
 * @author 212071558
 *
 */
public interface IDataCatalogService {
    /**
     * Get Image set by Org Id
     * 
     * @param orgId
     * @return List<ImageSet>
     * @throws Exception
     */
    List<ImageSet> getImgSetByOrgId(String orgId) throws Exception;
    /**
     * Get Image Set Ids for a Data collection Id
     * @param id
     * @return String[]
     * @throws Exception
     */
    String[] getImgSetIdForDC(String id) throws Exception;
    /**
     * Get Data Collection
     * 
     * @return List<DataCollection>
     * @throws Exception
     */
    List<DataCollection> getDataCollection() throws Exception;
   /**
    *  Get Image set by Id
    *  
    * @param dataCollectionId
    * @return List<ImageSet>
    * @throws Exception
    */
    List<ImageSet> getImgSetByDataCollId(String dataCollectionId) throws Exception;
    /**
     * Create Data Collection
     * 
     * @param dataCollection
     * @return number of rows inserted
     * @throws Exceptions
     */
    int createDataCollection(DataCollection dataCollection) throws Exception;
}
