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
import java.util.Map;

import javax.ws.rs.core.Response;

import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.entity.TargetData;

/**
 * @author 212071558
 *
 */
public interface IDataCatalogRest {
    /**
     * Get Image Set by Org Id
     *
     * @param params
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSet(Map<String, String> params);

    /**
     * Get Image Set by Data Collection Id
     * 
     * @param dataCollectionId
     * @return  List<ImageSet>
     */
    List<ImageSet> getImgSetByDataCollId(String dataCollectionId);
    
    /**
     * Get Data Collection
     * 
     * @return List<DataCollection>
     */
    List<DataCollection> getDataCollection();
    
    /**
     * Create Data Collection
     * 
     * @param dataCollection
     * @return response
     */
    Response createDataCollection(DataCollection dataCollection);
    
    /**
     * @param imageSet
     * @return
     */
    String insertImageSet(ImageSet imageSet);
    
    /**
     *  
     * @return String Success 
     */
    String healthCheck();
    
	Response insertRandomAnnotationSet(String jsonString);
	Response insertAnnotationSet(String annotationSetJson);
	List getAnnotationSet(String imageSetIds, String fields);
	List getAnnotationSet(Map<String, String> queryMap);

	List<TargetData> getExperimentTargetData(String dataCollectionIds);

}
