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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.dc.entity.Annotation;
import com.gehc.ai.app.dc.entity.CosNotification;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.DataSet;
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
     * @param request TODO
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSet( Map<String, String> params, HttpServletRequest request );

    /**
     * Get Image Set by Data Collection Id
     * 
     * @param dataCollectionId
     * @param request TODO
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSetByDataCollId( String dataCollectionId, HttpServletRequest request );

    /**
     * Get Data Collection
     * 
     * @param type TODO
     * @return List<DataCollection>
     */
    List<DataCollection> getDataCollection( String id, String type, HttpServletRequest request);
    
    /**
     * Create Data Collection
     * 
     * @param dataCollection
     * @param request TODO
     * @return response
     */
    Response createDataCollection( DataCollection dataCollection, HttpServletRequest request );

    /**
     * @param imageSet
     * @param request TODO
     * @return
     */
    String insertImageSet( ImageSet imageSet, HttpServletRequest request );

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
  //  Response insertRandomAnnotationSet( String jsonString );

    /**
     * inserts a random annotation set into the database
     * 
     * @param as annotation set to be inserted to the database
     * @return Response object wrapping the result of the insertion.
     */
   // Response insertAnnotationSet( AnnotationSet as );

    /**
     * @param imageSetIds image sets id
     * @param fields fields defined in the JSON structure of the annotation
     * @return List of annotation sets given image set ids
     */
  //  List getAnnotationSet( String imageSetIds, String fields );

    /**
     * @param queryMap a map of annotation set key-values
     * @return annotation sets given the query map
     */
    //List getAnnotationSet( Map<String, String> queryMap );

    /**
     * @param id id of target data
     * @param type type of target data
     * @param request TODO
     * @return target data that matches id and type
     */
    Map getExperimentTargetData( String id, String type, HttpServletRequest request );

    /**
     * @param id id of data collection       
     * @return Annotation data for imagesets in a given Datacollection
     */

    @SuppressWarnings ("unchecked")
    @RequestMapping(value = "/dataCatalog/annotationByDataCollectionId", method = RequestMethod.GET)
    List getAnnotationByDataColId(@QueryParam("id") String id,
                                  @QueryParam("annotationType") String annotationType);

    /**
     * @param queryMap fields from patient table columns
     * @return list of patient satisfying search criteria
     */
//    List<Patient> getPatient( Map<String, String> queryMap );
      List<Patient> getPatient( HttpServletRequest request );

    /**
     * @param queryMap list of studies satisfying search criteria
     * @return
     */
    //List<Study> getStudy( Map<String, String> queryMap );
      List<Study> getStudy( HttpServletRequest request );
      
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
     * @param request TODO
     * @return studies given a patient
     */
    List<Study> getStudies( String patientId, HttpServletRequest request );

    /**
     * @param studyId
     * @param request TODO
     * @return image sets associated with the studyId
     */
    List<ImageSet> getImageSetByStudyId( String studyId, HttpServletRequest request );

    /**
     * @param ids
     * @param request TODO
     * @return patients based on a list of comma separated id's
     */
    List<Patient> getPatients( String ids, HttpServletRequest request );

    /**
     * @param ids
     * @param request TODO
     * @return studies based on a list of comma separated id's
     */
    List<Study> getStudiesById( String ids, HttpServletRequest request );

    /**
     * @param ids
     * @param request TODO
     */
    ApiResponse deleteAnnotation( String ids, HttpServletRequest request );

    /**
     * @param imageSet
     * @param request TODO
     * @return
     */
    List<Annotation> getAnnotationsByImgSet( String imageSet, HttpServletRequest request );

    /**
     * Save an Annotation
     * @param annotation
     * @return Annotation object created based on the Annotation object given
     */
    ApiResponse saveAnnotation(Annotation annotation);
    
    /**
     * @param n
     */
    void postCOSNotification(CosNotification n );
    
    /**
     * @param patientid
     * @param request TODO
     * @return
     */
    List<ImageSet> getImageSetByPatientId( String patientid, HttpServletRequest request );
    
    /**
     * @param dataCollection
     * @param request TODO
     * @return
     */
    ApiResponse updateDataCollection( DataCollection dataCollection, HttpServletRequest request );
    
    /**
     * @param request TODO
     * @param id
     * @return
     */
    List<Annotation> getAnnotationsById( String ids, HttpServletRequest request );
    
    /**
     * Get Data Set
     * 
     * @param type TODO
     * @return List<DataCollection>
     */
    List<DataSet> getDataSet( Long id, String type, HttpServletRequest request);
}
