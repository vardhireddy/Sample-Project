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
package com.gehc.ai.app.datacatalog.dao;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidContractException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;

import java.util.List;
import java.util.Map;

public interface IDataCatalogDao {

    /**
     * Given a parameter map and orgId, this method returns a GE class data summary (counts) that can be used to
     * assist the user to filter image sets based on GE classes. the parameter map can contain image set
     * column filters such as modality etc
     * @param params parameters 
     * @param orgId ordId
     * @return ge class data summary (counts) given filtering GE class parameters
     */
    Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId);

    /**
     * @param params attribute list for the image set filters, eg <org_id, value>, <modality, value>
     * @param randomize randomize output
     * @maxImageSeriesRows maximum number of image series record to be returned
     * @return list of ImageSeries that satisfies query parameters, limited by a max image series rows permitted,
     * randomized if randomize flag is set
     * 
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

    /**
     * Retrieves annotation id associated with an annotation. This will be used to determine if an annotation is already present
     * in the DB such that a save request will result in an update instead of a new record being created
     * @param annotation annotation object
     * @return annotation id associated with this annotation
     */
    List<Integer> getAnnotationsIds(Annotation annotation);

    /**
     * 
     * @param imgSerIdLst list of image series id's
     * @return list of image series objects given the ids
     */
    List<ImageSeries> getImgSeriesWithPatientByIds(List<Long> imgSerIdLst);

    /**
     * this method stores contact into the database
     * 
     * @param contract contract to be ingested
     * @return contract id created for the ingested contract
     */
	Contract saveContract(Contract contract);

	/**
	 * given a contract id, return the contract details
	 * @param contractId contract id
	 * @return contract details, stored in a Contract object
	 */
	Contract getContractDetails(Long contractId);

	/**
	 * Return all the contracts details for the given a org id
	 * @param orgId the id of the organization whose contracts will be returned
	 * @throws InvalidContractException if the data usage period is invalid or the agreement begin date is invalid
	 * @return list of contracts and their details. If the given org id does not exists or if there are no contracts associated with the given org id,
	 * then an empty list will be returned
	 */
	List<Contract> getAllContractsDetails (String orgId) throws InvalidContractException;
	
	/**
	 * given a set of column filter criteria, return a list of image set id's satisfying the
	 * filter parameters
	 * @param params filter parameter for the image series
	 * @return list of image series ids satisfying the filter parameters
	 */
	List<Long> getImgSeriesIdsByFilters(Map<String, Object> params);

	/**
	 * Returns image set id list for given data collection id
	 * @param dataCollectionId - data collection unique identifier
	 * @return list of image set ids
	 */
	List<Long> getImageSetIdsByDataCollectionId(Long dataCollectionId);

	/**
	 * Returns list of contracts for given list of image set ids
	 * @param imageSetIdList - list of image set unique identifiers
	 * @return list of contract objects
	 */
	List<Contract> getContractsByImageSetIds(List<Long> imageSetIdList);

	/**
	 * Saves the given upload entity to the repository
	 * @param uploadEntity - Upload entity object
	 * @return - a copy of the upload entity saved to the database repository
	 */
	Upload saveUpload(Upload uploadEntity);

    /**
     * Returns list of upload entities for given orgId
     * @param orgId - organization Id
     * @return list of upload entities
     */
    List<Upload> getAllUploads(String orgId);

	/**
	 * given a upload id, return the upload entity details
	 * @param uploadId - unique Id of upload entity
	 * @return upload details, stored in a upload object
	 */
	 Upload getUploadById( Long uploadId);

	/**
	 * Returns the upload entity details for given query parameters
	 * @param spaceId - space ID of upload on COS
	 * @param orgId - organisation ID
	 * @param contractId - contract ID
	 * @return
	 * if upload exists -> returns the upload entity details
	 * if upload does not exist -> returns null
	 */
	Upload getUploadByQueryParameters(String spaceId, String orgId, Long contractId);

}
