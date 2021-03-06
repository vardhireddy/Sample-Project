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
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataCollectionsCreateRequest;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.InstitutionSet;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.rest.request.UpdateContractRequest;
import com.gehc.ai.app.datacatalog.rest.response.AnnotatorImageSetCount;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import org.springframework.data.domain.Pageable;
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
     * Returns all data collections associated with the provided collection type and org ID embedded in the request headers.
     *
     * @param type (Optional) The type of data collection to return
     * @param pageable (Optional) A {@link Pageable} object that will determine what subset of data collections will be returned
     * @param request The intercepted HTTP request object whose headers should contain the authorization token for the target org ID
     * @return A JSON string containing all matching data collections
     */
    ResponseEntity<?> getDataSetByType(String type, Pageable pageable, HttpServletRequest request);

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
    ResponseEntity<Map<String,String>> validateContractByIdAndOrgId(Long contractId, String orgId);
    /**
     * @param params
     * @return
     */
    List<Long> getImgSeriesIdsByFilters(Map<String, Object> params);

    /**
     * Saves the provided contract details.
     *
     * @param contract The contract details to save.  The required details are the following:
     *                 <ul>
     *                 <li>The contract's name</li>
     *                 <li>The email address of the primary contact</li>
     *                 <li>The deidentification status of the contract's data</li>
     *                 <li>The start date of the contract</li>
     *                 <li>The length of use of the contract's data</li>
     *                 <li>A list of countries from which contract's data originated</li>
     *                 <li>A specification of who can use the contract's data</li>
     *                 <li>A specification of how the contract's data can be used</li>
     *                 <li>A specification of where the contract's data may reside</li>
     *                 </ul>
     * @param request  The intercepted HTTP request object whose headers will be validated
     * @return a {@link ResponseEntity} containing a JSON representation of the contract entity that was saved
     */
    ResponseEntity<?> saveContract(Contract contract, HttpServletRequest request);

    /**
     * Returns the contract details if given contract ID is valid and active
     * @param contractId
     * @return Contract
     */
    ResponseEntity<Contract> getContracts(Long contractId);

    /**
     * Fetches all contracts and their details for the given org Id.
     * @param request  The intercepted HTTP request object whose headers will be validated and the user's org Id will be extracted for getting the contracts.
     * @return  a {@link ResponseEntity} containing a JSON representation of the list of contract entities. The list is sorted by attributes - {@code active} and {@code contractId} in descending order
     */
    ResponseEntity<List<Contract>> getAllContracts(HttpServletRequest request);

    /**
     * Gives the ability to update the status and uri of the contract
     *
     * @param contractId
     * @return updated Contract object
     */
    ResponseEntity<Contract> updateContract(Long contractId, UpdateContractRequest updateRequest);

    /**
     * A soft delete of contract by given ID through inactivating it.
     *
     * @param contractId - contract object unique ID
     * @return
     * if Contract is deleted -> (Status code : 200, message: Contract is inactivated successfully)
     * if Contract is already deleted -> (Status code : 200, message: Contract with given id is already inactive)
     * if Contract not found -> (Status code : 404, message: No contract exists with given id)
     * if user is forbidden from deleting contract -> (Status code : 403, message : User does not have access to delete the contract.)
     */
    ResponseEntity<Map<String,String>> deleteContract(Long contractId, HttpServletRequest httpServletRequest);

    /**
     * Returns a map of active and inactive contracts associated with a given data collection id
     * @param dataCollectionId - data collection unique identifier
     * @return Map<String,List<ContactsByDataSetId>>, where the keys will be "active" and "inactive". If "dataCollectionId" does not exist an empty map will be returned
     */
    ResponseEntity<?> getContractsForDataCollection(Long dataCollectionId);



    /**
     * Saves the provided upload entity details to repository.
     *
     * @param uploadRequest The upload details to save.  The required details are the following:
     *                 <ul>
     *                 <li>The orgID value</li>
     *                 <li>The data type of files being uploaded</li>
     *                 <li>The contract ID associated with the upload</li>
     *                 <li>The space ID for the upload</li>
     *                 <li>The tags specified for upload in manifest file</li>
     *                 <li>The uploader's name</li>
     *                 </ul>
     * @return a JSON representation of the upload entity that was saved
     * if required data is not provided -> throws exception with status code 400 and error message
     * if contract is invalid -> throws exception with status code 400 and error message
     * if upload is not unique on certain parameters -> throws exception with status code 409 and error message
     */
    ResponseEntity<?> createUpload(Upload uploadRequest);


    /**
     * Returns list of upload entities for authorized user
     * @param httpServletRequest - http servlet request to parse orgId from Authentication token
     * @return List<Upload>
     *     if user is Authorized -> returns list of upload entities
     *     if user is unauthorized -> returns 401 status code and error message
     *     if user is forbidden -> returns 403 status code and error message
     */
    ResponseEntity<?> getAllUploads(HttpServletRequest httpServletRequest);

    /**
     * Returns the upload entity details if given upload ID exists
     * @param uploadId
     * @return
     * if user is authorized and upload exists -> returns status code 200 and the upload entity details
     * if user is authorized and upload doe not exist -> returns status code 404 and error message
     * if user is not authorized -> returns 403 status code and error message
     */
    ResponseEntity<?> getUploadById(Long uploadId, HttpServletRequest httpServletRequest);

    /**
     * Returns the upload entity details for given query parameters
     * @param spaceId - space ID of upload on COS
     * @param orgId - organisation ID
     * @param contractId - contract ID
     * @return
     * if upload exists -> returns status code 200 and the upload entity details
     * if contract is invalid -> throws DataCatalog exception with status code 400 and error message
     * if upload does not exist -> returns status code 404 and response message
     */
    ResponseEntity<?> getUploadByQueryParameters(String spaceId, String orgId, Long contractId);

    /**
     * Updates the upload entity and saves to the database
     * @param updateRequest The upload details to save.  The required details are the following:
     *                      <ul>
     *                       <li>The orgID value</li>
     *                       <li>The data type of files being uploaded</li>
     *                       <li>The contract ID associated with the upload</li>
     *                       <li>The space ID for the upload</li>
     *                       <li>The tags specified for upload in manifest file</li>
     *                       <li>The uploader's name</li>
     *                       <li>The summary -> uri list  from COS</li>
     *                       <li>The status of no.of DICOM and NON_DICOM uploads success ratio</li>
     *                       </ul>
     * @return a JSON representation of the upload entity that was updated with status code 200
     *       if required data is not provided/ invalid -> throws exception with status code 400 and error message
     *       if updateRequest lastModified date does not match with the entity in DB -> throws exception with status code 409 and error message
     */
    ResponseEntity<?> updateUpload(Upload updateRequest);
}
