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
package com.gehc.ai.app.datacatalog.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataCollection;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.ImageSet;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Study;

/**
 * @author 212071558
 */
public interface IDataCatalogRest {
    /**
     * Get Image Set by Org Id
     *
     * @param params
     * @param request, to get an org id based on authentication token
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSet(Map<String, String> params, HttpServletRequest request );

    /**
     * Get Image Set by Data Collection Id
     * 
     * @param dataCollectionId
     * @param request, to get an org id based on authentication token
     * @return List<ImageSet>
     */
    List<ImageSet> getImgSetByDataCollId( String dataCollectionId, HttpServletRequest request );

    /**
     * Get Data Collection
     * 
     * @param type, DC type like Annotation or Experiment
     * @return List<DataCollection>
     */
    List<DataCollection> getDataCollection(String id, String type, HttpServletRequest request);
    
    /**
     * Create Data Collection
     * 
     * @param dataCollection
     * @param request, to get an org id based on authentication token
     * @return response
     */
    Response createDataCollection( DataCollection dataCollection, HttpServletRequest request );

    /**
     * @param imageSet
     * @param request, to get an org id based on authentication token
     * @return
     */
    String insertImageSet( ImageSet imageSet, HttpServletRequest request );

    /**
     * @return String Success
     */
    String healthCheck();

    /**
     * @param id id of target data
     * @param type type of target data
     * @param request, to get an org id based on authentication token
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
      List<Patient> getPatient( HttpServletRequest request );

    /**
     * @param queryMap list of studies satisfying search criteria
     * @return
     */
      List<Study> getStudy(HttpServletRequest request );
      
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
     * @param request, to get an org id based on authentication token
     * @return studies given a patient
     */
    List<Study> getStudies( String patientId, HttpServletRequest request );

    /**
     * @param studyId
     * @param request, to get an org id based on authentication token
     * @return image sets associated with the studyId
     */
    List<ImageSet> getImageSetByStudyId( String studyId, HttpServletRequest request );

    /**
     * @param ids
     * @param request, to get an org id based on authentication token
     * @return patients based on a list of comma separated id's
     */
    List<Patient> getPatients( String ids, HttpServletRequest request );

    /**
     * @param ids
     * @param request, to get an org id based on authentication token
     * @return studies based on a list of comma separated id's
     */
    List<Study> getStudiesById( String ids, HttpServletRequest request );

    /**
     * @param ids
     * @param request, to get an org id based on authentication token
     */
    ApiResponse deleteAnnotation( String ids, HttpServletRequest request );

    /**
     * @param imageSet
     * @param request, to get an org id based on authentication token
     * @return
     */
    List<Annotation> getAnnotationsByImgSet(String imageSet, HttpServletRequest request );

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
     * @param request, to get an org id based on authentication token
     * @return
     */
    List<ImageSet> getImageSetByPatientId( String patientid, HttpServletRequest request );
    
    /**
     * @param dataCollection
     * @param request, to get an org id based on authentication token
     * @return
     */
    ApiResponse updateDataCollection( DataCollection dataCollection, HttpServletRequest request );
    
    /**
     * @param request, to get an org id based on authentication token
     * @param id
     * @return
     */
    List<Annotation> getAnnotationsById( String ids, HttpServletRequest request );
    
    /**
     * 
     * @param org_id
     * @return list of annotation properties
     */
    List<AnnotationProperties> getAnnotationProperties(String org_id);
    
    /**
     * 
     * @param annotationProp
     * @return API response with annotation id
     */
    ApiResponse saveAnnotationProperties(AnnotationProperties annotationProp );
    
    /**
     * Insert or update an DataSet
     * 
     * @param d data set object
     * @return recently saved object
     */
    DataSet saveDataSet( DataSet d );
    
    /**
     * Insert or update an image series
     * 
     * @param i image series object
     * @return recently saved object
     */
    ImageSeries saveImageSeries( ImageSeries i );
    
    /**
     * Get Image Series by Patient id
     * @param patientId
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesByPatientId( String patientId );
    
    /**
     * Get Image Series by Data Set Id
     * 
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesByDSId( Long id, HttpServletRequest request );
    
    /**
     * Get Data Set by it's id
     * 
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Data Set
     */
    List<DataSet> getDataSetById( Long id, HttpServletRequest request);
    
    /**
     * Get All Data Set by it's id
     * 
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Data Set
     */
    List<DataSet> getAllDataSet(HttpServletRequest request);
    
    /**
     * Get Data Set by type
     * @param type
     * @param request, to get an org id based on authentication token
     * @return list of Data Set
     */
    List<DataSet> getDataSetByType( String type, HttpServletRequest request);

    /**
     * Get Image Set by Org Id
     *
     * @param params
     * @param request, to get an org id based on authentication token
     * @return List<ImageSeries>
     */
    List<ImageSeries> getImgSeries(Map<String, String> params, HttpServletRequest request );
}
