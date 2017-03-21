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
import java.util.Map;

import com.gehc.ai.app.dc.entity.*;

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
     * @param params
     * @return List<ImageSet>
     * @throws Exception
     */
    List<ImageSet> getImgSet(Map<String, String> params) throws Exception;
    /**
     * Get Image Set Ids for a Data collection Id
     * @param id
     * @return String[]
     * @throws Exception
     */
    String[] getImgSetIdForDC(String id) throws Exception;
    /**
     * Get Data Collection
     * @param id TODO
     * @param type TODO
     * @param orgId TODO
     * @return List<DataCollection>
     *
     * @throws Exception
     */
     List<DataCollection> getDataCollection(String id  , String type , String orgId  ) throws Exception;
     
    /**
    *  Get Image set by Id
    *  
    * @param dataCollectionId
     * @param orgId TODO
    * @return List<ImageSet>
    * @throws Exception
    */
    List<ImageSet> getImgSetByDataCollId(String dataCollectionId, String orgId) throws Exception;
    /**
     * Create Data Collection
     * 
     * @param dataCollection
     * @param orgId TODO
     * @return String, DataCollection id created
     * @throws Exception
     */
    String createDataCollection(DataCollection dataCollection, String orgId) throws Exception;
    /**
     * @param imageSet
     * @param orgId TODO
     * @return
     * @throws Exception
     */
    String insertImageSet(ImageSet imageSet, String orgId) throws Exception;

    /**
     * 
     * @param annotationSetJson
     * @return number of rows inserted
     * @throws Exception
     */
	int insertAnnotationSet(AnnotationSet annotationSetJson) throws Exception;

	/**
	 * 
	 * @param imageSets
	 * @param fields
	 * @param queryMap
	 * @return list of annotation sets satisfying the query
	 * @throws Exception
	 */
	List getAnnotationSet(String imageSets, String fields, Map<String, String> queryMap) throws Exception;
	/**
	 *
	 * @param dataCollectionIds
	 * @return targe data associated with the data collection
	 * @throws Exception
	 */
	List<TargetData> getExperimentTargetData(String dataCollectionIds) throws Exception;

	/**
	 *
	 * @param dataCollectionId
     * @param annotationType mask, point, contour etc to return that particular type of annotation data
	 * @return annotation data associated with the data collection
	 * @throws Exception
	 */
	List<AnnotationImgSetDataCol> getAnnotationByDataColId(String dataCollectionId, String annotationType) throws Exception;

	/**
	 * 
	 * @param studyId
	 * @return image sets associated with the studyId
	 */
	List<ImageSet> getImageSetByStudyId(String studyId);
	
	List<ImageSet> getImageSetByPatientId(String patientid);
	
        /**
         * @param dataCollection
         * @return
         * @throws Exception
         */
        String updateDataCollection(DataCollection dataCollection) throws Exception;
}
