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

import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataCollectionsCreateRequest;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.InstitutionSet;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.rest.response.AnnotatorImageSetCount;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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
    List<Study> getStudy(HttpServletRequest request);

    /**
     * add patient to database
     *
     * @param p patient
     * @return patient added
     */
    Patient postPatient(Patient p) throws DataCatalogException;

    /**
     * save study to database
     *
     * @param s study
     * @return study added
     */
    Study postStudy(Study s) throws DataCatalogException;

    /**
     * @param ids
     * @param request, to get an org id based on authentication token
     * @return patients based on a list of comma separated id's
     */
    List<Patient> getPatients(String ids, HttpServletRequest request);

    /**
     * @param ids
     * @param request, to get an org id based on authentication token
     * @return studies based on a list of comma separated id's
     */
    List<Study> getStudiesById(String ids, HttpServletRequest request);

    /**
     * @param ids
     * @param request, to get an org id based on authentication token
     */
    ApiResponse deleteAnnotation(String ids);

    /**
     * @param imageSet
     * @param request, to get an org id based on authentication token
     * @return
     */
    List<Annotation> getAnnotationsByImgSet(Long imageSet);

    /**
     * Save an Annotation
     *
     * @param annotation
     * @return Annotation object created based on the Annotation object given
     */
    ApiResponse saveAnnotation(Annotation annotation);

    /**
     * @param n
     */
    void postCOSNotification(CosNotification n);

    /**
     * @param request, to get an org id based on authentication token
     * @param id
     * @return
     */
    List<Annotation> getAnnotationsById(String ids, HttpServletRequest request);

    /**
     * @param org_id
     * @return list of annotation properties
     */
    List<AnnotationProperties> getAnnotationProperties(String org_id);

    /**
     * @param annotationProp
     * @return API response with annotation id
     */
    ApiResponse saveAnnotationProperties(AnnotationProperties annotationProp);


    /**
     * Creates one or more new data collection resources if it does not already exist or updates an existing data collection
     * resource based on the resource's ID.
     *
     * @param dataCollectionsCreateRequest The POST request body which can contain the following two primary properties:
     *                                     <ol>
     *                                     <li>dataSet - (Required) The data collection to save</li>
     *                                     <li>dataCollectionSize - (Optional) The desired size of a data collection in
     *                                     terms of the number of image sets to comprise a data collection.
     *                                     <ul>
     *                                     <li>If the size is not defined, then the data collection will be saved as-is.</li>
     *                                     <li>If the size is set to 1 image set, then the provided data collection
     *                                     will be batched such that there will be as many data collections as there are image sets.
     *                                     For example, if the initial data collection defines 9 image sets and the size is set to 1,
     *                                     then the initial data collection will be split into 9 data collections with 1 image set.</li>
     *                                     <li>If the size is set to n image sets where n is the total number of image
     *                                     sets defined in the initial data collection,then the provided data collection
     *                                     will be saved as a single data collection. For example, if the initial data collection
     *                                     defines 9 image sets and the size is set to 9, then the initial data collection
     *                                     will be saved as-is without any batching.</li>
     *                                     <li>If the size is set to be between 1 and n image sets where n is the total number of image
     *                                     sets defined in the initial data collection, then the provided data collection
     *                                     will be batched such that there will be n/{$size} number of collections with {$size} image sets
     *                                     and one collection with n%{$size} image sets. For example, if the initial data collection
     *                                     defines 9 image sets and the size is set to 2, then the initial data collection
     *                                     will be split into 4 collections with 2 image sets and 1 collection with 1 image set</li>
     *                                     </ul>
     *                                     </li>
     *                                     <ol/>
     * @param request                      The intercepted HTTP request object whose headers will be validated
     * @return a {@link ResponseEntity} containing the IDs of the created or updated data collections.
     */
    ResponseEntity<?> saveDataSet(DataCollectionsCreateRequest dataCollectionsCreateRequest, HttpServletRequest request);

    /**
     * Insert or update an image series
     *
     * @param i image series object
     * @return recently saved object
     */
    ImageSeries saveImageSeries(ImageSeries i);

    /**
     * Get Image Series by Patient id
     *
     * @param patientId
     * @param org_id    TODO
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesByPatientId(String patientId, String orgId);

    /**
     * Get Image Series by Data Set Id
     *
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesByDSId(Long id);

    /**
     * Get Data Set by it's id
     *
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Data Set
     */
    List<DataSet> getDataSetById(Long id, HttpServletRequest request);

    /**
     * Get Data Set by type
     *
     * @param type
     * @param request, to get an org id based on authentication token
     * @return list of Data Set
     */
    List<DataSet> getDataSetByType(String type, HttpServletRequest request);

    /**
     * Get Image Series by Series Instance UUId
     *
     * @param id
     * @param request, to get an org id based on authentication token
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesById(Long id);

    /**
     * Get Image Series by Series Instance UUId
     *
     * @param id
     * @param request  TODO
     * @param request, to get an org id based on authentication token
     * @return list of Image Series
     */
    List<ImageSeries> getImgSeriesByStudyDbId(Long studyDbId, HttpServletRequest request);

    /**
     * @param patientId
     * @param request,  to get an org id based on authentication token
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
    @SuppressWarnings("rawtypes")
    List getRawTargetData(String id, String annotationType);

    /**
     * @return String Success
     */
    String healthcheck();

    /**
     * @param groupBy
     * @param request
     * @return map of all the filters
     */
    Map<String, Object> dataSummary(String groupby, HttpServletRequest request);

    /**
     * @param params
     * @param request
     * @return
     */
    Map<Object, Object> geClassDataSummary(Map<String, String> params, HttpServletRequest request);

    /**
     * @param ids
     */
    ApiResponse deleteDataCollection(String ids, HttpServletRequest request);

    ApiResponse updateInstitutionByImageSeriesList(InstitutionSet u, HttpServletRequest request);

    /**
     * @param params
     * @return
     */
    List<ImageSeries> getImgSeriesByFilters(Map<String, Object> params);

    /**
     * @param id
     * @return
     */
    // List<Annotation> getAnnotationDetailsByImageSetIDs(Long id, String orgId);

    /**
     * Exports the annotations of the specified data collection as a JSON string.
     *
     * @param id The ID of the collection whose annotations will be exported as a JSON string
     * @return a {@code List} pf {@code AnnotationDetail}s that will be transformed to a JSON string
     * @throws InvalidAnnotationException if the JSON representation of any of the collection's annotations is not well-formed
     */
    List<AnnotationJson> getAnnotationsByDSId(Long id) throws InvalidAnnotationException;

    /**
     * Exports the annotations of the specified data collection as a CSV string.
     *
     * @param id The ID of the collection whose annotations will be exported as a CSV string
     * @return A {@code ResponseEntity<String>} where the body is the annotations formatted as a CSV string
     * @throws InvalidAnnotationException If the JSON representation of any of the collection's annotations is not well-formed
     * @throws CsvConversionException     If the JSON representation of any of the collection's annotations could not be successfully mapped to a corresponding CSV representation
     */
    ResponseEntity<String> exportAnnotationsAsCsv(HttpServletResponse response, Long id) throws InvalidAnnotationException, CsvConversionException;

    /**
     * @param id
     * @return
     */
    ApiResponse deleteImageSeries(String id);

    /**
     * Returns the count of unique images sets per Annotator Id for given Organization Id
     *
     * @return list of Annotated ImageSetCount grouped by user
     */
    ResponseEntity<List<AnnotatorImageSetCount>> getCountOfImagesAnnotated(String orgId);

    /**
     * Returns a text specifying if given combination of contract-id and org-id exists
     *
     * @return String
     */
    ResponseEntity<Map<String,String>> validateContractIdAndOrgId(Long contractId, String orgId);
    /**
     * @param params
     * @return
     */
    List<Long> getImgSeriesIdsByFilters(Map<String, Object> params);
}


