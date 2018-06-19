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
package com.gehc.ai.app.datacatalog.service;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidContractException;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IDataCatalogService {
    
    /**
     * @param params
     * @param orgId
     * @return
     */
    Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId);
    
    /**
     * @param params filter parameters
     * @param maxImageSeriesRows maximum number of rows to be returned by the service
     * @return
     * @throws Exception 
     */
    List<ImageSeries> getImgSeriesByFilters(Map<String, Object> params, boolean randomize, int maxImageSeriesRows);

    /**
     * Returns the annotation details for every specified image set ID as a list of beans encapsulating their JSON representations.
     *
     * @param imageSetIDList the {@code List} of image set IDs for which their annotation details will be returned
     * @return a {@code List} of {@link AnnotationJson} beans
     * @throws InvalidAnnotationException if at least one of the specified image sets is associated with a malformed annotation
     */
    List<AnnotationJson> getAnnotationDetailsByImageSetIDs(List<Long> imageSetIDList) throws InvalidAnnotationException;

    /**
     * Returns the annotation details for every specified image set ID as a CSV string.  The CSV string is formatted such
     * that is ingestable by the Learning Factory ingestion pipeline.
     *
     * @param imageSetIDList the {@code List} of image set IDs for which their annotation details will be returned
     * @return a CSV string
     * @throws InvalidAnnotationException if at least one of the specified image sets is associated with a malformed annotation
     * @throws CsvConversionException     if at least one of the specified image sets cannot successfully be transformed into its CSV representation
     */
    String getAnnotationDetailsAsCsvByImageSetIDs(List<Long> imageSetIDList) throws InvalidAnnotationException, CsvConversionException;

    List<Integer> getAnnotationsById(Annotation annotation);
    
    List<ImageSeries> getImgSeriesWithPatientByIds(List<Long> imgSerIdLst);

	Contract saveContract(Contract contract);

	Contract getContract(Long contractId);

    /**
     * Fetches all contracts and their details for the given org Id.
     * @param orgId the id of the organization whose contracts will be returned
     * @throws InvalidContractException if the data usage period is invalid or the agreement begin date is invalid
     * @return list of contracts and their details. The list is sorted by attributes - {@code active} and {@code contractId} in descending order.
     * If the given org id does not exists or if there are no contracts associated with the given org id, then an empty list will be returned.
     */
    List<Contract> getAllContracts(String orgId) throws InvalidContractException;

    /**
     * @param params
     * @return
     */
    List<Long> getImgSeriesIdsByFilters(Map<String, Object> params);


    /**
     * Returns a map of active and inactive contracts associated with a given data collection id
     * @param dataCollectionId - data collection unique identifier
     * @return Map<String,List<ContactsByDataSetId>>, where the keys will be "active" and "inactive". If "dataCollectionId" does not exist an empty map will be returned
     */
    Map<String,List<ContractByDataSetId>> getContractsByDataCollectionId(Long dataCollectionId);

    /**
     * Saves the given upload entity to the repository
     * @param uploadEntity - Upload entity object
     * @return - a copy of the upload entity saved to the database repository
     */
    Upload saveUpload(Upload uploadEntity);

        /**
         *  validates the request parameters and creates a upload entity in the repository
         * @param uploadRequest - Request body parameters from the POST Upload API
         * @return
         * if valid request - returns a copy of upload entity created in the repository
         * if invalid - returns an error message with the reason specifying why the Upload could not be created
         */
    Upload createUpload(Upload uploadRequest) throws DataCatalogException;

    /**
     * Fetches all uploads entities for the organisation
     * @param orgId - organisation ID
     *
     * @return list of upload entity details.
     * if there are no uploads associated with the given organisation, then an empty list will be returned.
     */
    List<Upload> getAllUploads( String orgId );

}
