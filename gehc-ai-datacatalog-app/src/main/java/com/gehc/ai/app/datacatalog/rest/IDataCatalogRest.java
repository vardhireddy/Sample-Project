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

import org.springframework.web.bind.annotation.RequestBody;

import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;

/**
 * @author 212071558
 */
public interface IDataCatalogRest {

    /**
     * @return String Success
     */
    String healthCheck();

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
    Patient postPatient( Patient p ) throws DataCatalogException;

    /**
     * save study to database
     * 
     * @param s study
     * @return study added
     */
    Study postStudy( Study s ) throws DataCatalogException;

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
    ApiResponse deleteAnnotation( String ids );

    /**
     * @param imageSet
     * @param request, to get an org id based on authentication token
     * @return
     */
    List<Annotation> getAnnotationsByImgSet(Long imageSet );
    
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
     * @param request TODO
     * @return recently saved object
     */
    DataSet saveDataSet( DataSet d, HttpServletRequest request );
    
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
    List<ImageSeries> getImgSeriesByDSId( Long id );
    
    /**
     * Get Data Set by it's id
     * 
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Data Set
     */
    List<DataSet> getDataSetById( Long id, HttpServletRequest request);
    
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
    List<ImageSeries> getImgSeries(Map<String, String> params );
    
    /**
     * Get Image Series by Series Instance UUId
     * 
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesById( Long id);
    
    /**
     * Get Image Series by Series Instance UUId
     * 
     * @param id
     * @param request TODO
     * @param request, to get an org id based on authentication token
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesByStudyDbId( Long studyDbId, HttpServletRequest request);
    
    /**
     * @param patientId
     * @param request, to get an org id based on authentication token
     * @return studies given a patient
     */
    List<Study> getStudiesByPatientDbid(String patientDbid, HttpServletRequest request);
    
    /**
     * @param request
     * @return
     */
    List<Patient> getAllPatients(HttpServletRequest request);
    
    /**
     * @param id
     * @param annotationType
     * @return
     */
    List getRawTargetData(String id,String annotationType);
    /**
     * @return String Success
     */
    String healthcheck();
      
    /**
     * @param orgId
     * @return map of all the filters
     */
    Map<String, Object> filters(String orgId);
    
    /**
     * @param orgId
     * @return count of image set which has no annotations
     */
    List imgSetWithNoAnn(String orgId);
}
