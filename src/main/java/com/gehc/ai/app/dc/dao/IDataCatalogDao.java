/*
 * IDataCatalogDao.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.dao;

import java.util.List;
import java.util.Map;

import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;

/**
 * @author 212071558
 *
 */
public interface IDataCatalogDao {
    /**
     * Gets the data catalog.
     * 
     * @param params
     * @return List<ImageSet>
     * @throws Exception
     */
    List<ImageSet> getImgSet(Map<String, String> params) throws Exception;
    
    /**
     * Get Image Set Ids for a Data collection Id
     * @param id
     * @return  String[] of Image set Ids
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
     * Get Image Set by Id
     * 
     * @param dataCollectionId
     * @return List<ImageSet>
     * @throws Exception
     */
    List<ImageSet> getImgSetByDataCollId(String dataCollectionId) throws Exception;
    
    /**
     * Create a Data Collection
     * 
     * @param dataCollection
     * @return number of n=rows inserted
     * @throws Exception
     */
    String createDataCollection(DataCollection dataCollection) throws Exception;

    /**
     * @param imageSet
     * @return
     * @throws Exception
     */
    int insertImageSet(ImageSet imageSet) throws Exception;
    
	int insertAnnotationSet(String annotationSetJson) throws Exception;

	List getAnnotationSet(String imageSets, String fields, Map<String, String> queryMap) throws Exception;

}
