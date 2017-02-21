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

import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.dc.entity.Annotation;
import com.gehc.ai.app.dc.entity.AnnotationSet;
import com.gehc.ai.app.dc.entity.CosNotification;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.entity.Patient;
import com.gehc.ai.app.dc.entity.Study;

/**
 * @author 212071558
 */
public interface IDataCatalogRest {
    /**
     * Get Image Set by Org Id
     *
     * @param params
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSet( Map<String, String> params );

    /**
     * Get Image Set by Data Collection Id
     * 
     * @param dataCollectionId
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSetByDataCollId( String dataCollectionId );

    /**
     * Get Data Collection
     * 
     * @param type TODO
     * @return List<DataCollection>
     */
    List<DataCollection> getDataCollection( String id, String type );

    /**
     * Create Data Collection
     * 
     * @param dataCollection
     * @return response
     */
    Response createDataCollection( DataCollection dataCollection );

    /**
     * @param imageSet
     * @return
     */
    String insertImageSet( ImageSet imageSet );

    /**
     * @return String Success
     */
    String healthCheck();

    /**
     * inserts a random annotation set into the database
     * 
     * @param jsonString ignored
     * @return Response object wrapping the result of the insertion.
     */
    Response insertRandomAnnotationSet( String jsonString );

    /**
     * inserts a random annotation set into the database
     * 
     * @param jsonString annotation set to be inserted to the database
     * @return Response object wrapping the result of the insertion.
     */
    Response insertAnnotationSet( AnnotationSet as );

    /**
     * @param imageSetIds image sets id
     * @param fields fields defined in the JSON structure of the annotation
     * @return List of annotation sets given image set ids
     */
    List getAnnotationSet( String imageSetIds, String fields );

    /**
     * @param queryMap a map of annotation set key-values
     * @return annotation sets given the query map
     */
    List getAnnotationSet( Map<String, String> queryMap );

    /**
     * @param id id of target data
     * @param type type of target data
     * @return target data that matches id and type
     */
    Map getExperimentTargetData( String id, String type );

    /**
     * @param queryMap fields from patient table columns
     * @return list of patient satisfying search criteria
     */
    List<Patient> getPatient( Map<String, String> queryMap );

    /**
     * @param queryMap list of studies satisfying search criteria
     * @return
     */
    List<Study> getStudy( Map<String, String> queryMap );

    /**
     * add patient to database
     * 
     * @param p patient
     * @return patient added
     */
    Patient postPatient( Patient p );

    /**
     * save study to database
     * 
     * @param s study
     * @return study added
     */
    Study postStudy( Study s );

    /**
     * @param patientId
     * @return studies given a patient
     */
    List<Study> getStudies( String patientId );

    /**
     * @param studyId
     * @return image sets associated with the studyId
     */
    List<ImageSet> getImageSetByStudyId( String studyId );

    /**
     * @param ids
     * @return patients based on a list of comma separated id's
     */
    List<Patient> getPatients( String ids );

    /**
     * @param ids
     * @return studies based on a list of comma separated id's
     */
    List<Study> getStudiesById( String ids );

    /**
     * @param ids
     */
    void deleteAnnotation( String ids );

    /**
     * @param imageSet
     * @return
     */
    List<Annotation> getAnnotationsByImgSet( String imageSet );

    /**
     * Save an Annotation
     * @param Annotation
     * @return Annotation object created based on the Annotation object given
     */
    ApiResponse saveAnnotation(Annotation annotation);
    
    void postCOSNotification(CosNotification n );
    
    //void deleteAnnotation(Long id);
}
