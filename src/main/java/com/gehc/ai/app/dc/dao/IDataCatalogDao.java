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

import com.gehc.ai.app.dc.entity.*;

/**
 * @author 212071558
 *
 */
public interface IDataCatalogDao {
    /**
     * Gets the data catalog.
     * 
     * @param params
     * @param orgId TODO
     * @return List<ImageSet>
     * @throws Exception
     */
    List<ImageSet> getImgSet(Map<String, String> params, String orgId) throws Exception;
    
    /**
     * Get Image Set Ids for a Data collection Id
     * @param id
     * @return  String[] of Image set Ids
     * @throws Exception
     */
    String[] getImgSetIdForDC(String id) throws Exception;
    
    /**
     * Get Data Collection
     * @param id TODO
     * @param type TODO
     * @param orgId TODO
     * @return List<DataCollection>
     * @throws Exception
     */
      List<DataCollection> getDataCollection(String id  , String type , String orgId  ) throws Exception;
    
    /**
     * Get Image Set by Id
     * 
     * @param dataCollectionId
     * @param orgId TODO
     * @return List<ImageSet>
     * @throws Exception
     */
    List<ImageSet> getImgSetByDataCollId(String dataCollectionId, String orgId) throws Exception;
    
    /**
     * Create a Data Collection
     * 
     * @param dataCollection
     * @param orgId TODO
     * @return number of n=rows inserted
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
     * inserts annotation set into the datacatalog database
     * @param annotationSetJson annotation set in json format
     * @return number of rows inserted
     * @throws Exception
     */
	int insertAnnotationSet(AnnotationSet annotationSetJson) throws Exception;

	/**
	 * 
	 * @param imageSets image set ID's
	 * @param fields columns to be returned 
	 * @param queryMap key-value pair for the query
	 * @return list of image sets satisfying the criteria
	 * @throws Exception
	 */
	List getAnnotationSet(String imageSets, String fields, Map<String, String> queryMap) throws Exception;

	/**
	 * 
	 * @param dataCollectionIds data collection id
	 * @param orgId TODO
	 * @return experiment target data for the collection, specifically from those image sets with annotation
	 * @throws Exception
	 */
	List<TargetData> getExperimentTargetData(String dataCollectionIds, String orgId) throws Exception;

	/**
	 *
	 * @param dataCollectionId data collection id
	 * @param orgId TODO
	 * @return Annotation data for imagesets for a give data collection
	 * @throws Exception
	 */
	List<AnnotationImgSetDataCol> getAnnotationByDataColId(String dataCollectionId, String annotationType, String orgId) throws Exception;

	/**
	 * 
	 * @param studyId
	 * @return imagesets matching studyId
	 */
	List<ImageSet> getImageSetByStudyId(String studyId);
	List<ImageSet> getImageSetByPatientId(String patientid, String orgId);
	/**
	 * @param dataCollection
	 * @param orgId TODO
	 * @return
	 * @throws Exception
	 */
	String updateDataCollection(DataCollection dataCollection, String orgId) throws Exception;
}
