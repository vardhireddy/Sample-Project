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

import java.util.List;
import java.util.Map;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;

public interface IDataCatalogDao {

    /**
     * @param params
     * @param orgId
     * @return
     */
    Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId);

    /**
     * @param params attribute list for the image set filters, eg <org_id, value>, <modality, value>
     * @maxImageSeriesRows maximum number of image series record to be returned
     * @return
     * @throws Exception
     */
    List<ImageSeries> getImgSeriesByFilters(Map<String, Object> params, int maxImageSeriesRows);

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

    List<Integer> getAnnotationsIds(Annotation annotation);

    List<ImageSeries> getImgSeriesWithPatientByIds(List<Long> imgSerIdLst);

	Long ingestContractDetails(Contract contract);

	Contract getContractDetails(Long contractId);
	
	/**
	 * @param params
	 * @return
	 */
	List<Long> getImgSeriesIdsByFilters(Map<String, Object> params);
}
