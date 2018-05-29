/*
 * DataCatalogUtils.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util;

import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import jersey.repackaged.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {DataCatalogUtils} provides utility methods for Data Catalog Service.
 *
 * @author Madhu Y (305024964), andrew.c.wong@ge.com (212069153)
 */
public class DataCatalogUtils {

    private static Logger logger = LoggerFactory.getLogger(DataCatalogUtils.class);

    private static final String INVALID_DATA_SET = "Invalid Dataset";
    private static final String INVALID_NUMBER_OF_DATASETS = "Invalid number of Data Collections";
    private static final String NO_IMAGE_SETS_IN_DATA_SET = "No Image Sets found in Data Set";
    private static final String NO_OF_COLLECTIONS_MORE_THAN_NO_OF_IMAGE_SETS_IN_DATA_SET = "Number of Collections is more than number of Image Sets found in Data Set";

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Batches the given data collection into one or more data collections
     *
     * @param initialDataCollection (Required) The source data collection that will be split into one or more data collections
     *                              based on the specified data collection size
     * @param dataCollectionSize    (Optional) The number of image sets to include in a data collection.
     *                              For example, if the initial data collection contains 9 image sets and the collection
     *                              size is set to 2 image sets, then the initial data collection will be split into 4 collections
     *                              with 2 image sets and 1 collection with 1 image set. The collection size must
     *                              be <i>at least 1 and no greater than the number of image sets available</i>.
     * @return The resulting list of data collections
     */
    public static List<DataSet> getDataCollectionBatches(DataSet initialDataCollection, Integer dataCollectionSize) throws DataCatalogException {

        logger.info("Getting the Data Collection Batches {}", dataCollectionSize);

        /* Toll gate checks */

        // Gate 1 - An initial data collection is required to be defined.  Otherwise, there is nothing to batch!
        if (Objects.isNull(initialDataCollection)) {
            logger.error(INVALID_DATA_SET);
            throw new DataCatalogException(INVALID_DATA_SET);
        }

        // Gate 2 - An initial data collection cannot be defined without any image set IDs.  Otherwise, there is nothing to put inside a data collection
        List<Long> imageSets = initialDataCollection.getImageSets();
        if (Objects.isNull(imageSets) || imageSets.isEmpty()) {
            logger.error(NO_IMAGE_SETS_IN_DATA_SET);
            throw new DataCatalogException(NO_IMAGE_SETS_IN_DATA_SET);
        }

        // Gate 3 - The data collection size cannot be less than 1 since it doesn't make sense to create a collection with less than 1 image set
        if(!Objects.isNull(dataCollectionSize) && dataCollectionSize < 1){
            logger.error(INVALID_NUMBER_OF_DATASETS + " {}", dataCollectionSize);
            throw new DataCatalogException(INVALID_NUMBER_OF_DATASETS);
        }

        // Gate 4 - The data collection size cannot be greater than the number of image sets since it doesn't make sense to create a collection with more image sets than what you have in hand
        int numImageSets = imageSets.size();
        if (!Objects.isNull(dataCollectionSize) && dataCollectionSize > numImageSets) {
            logger.error(NO_OF_COLLECTIONS_MORE_THAN_NO_OF_IMAGE_SETS_IN_DATA_SET);
            throw new DataCatalogException(NO_OF_COLLECTIONS_MORE_THAN_NO_OF_IMAGE_SETS_IN_DATA_SET);
        }

        /* All gates passed! */

        // The collection size is not required to be specified.  The default behavior is to create a single data collection.
        if (Objects.isNull(dataCollectionSize)) {
            List<DataSet> dataSetToReturnAsIs = new ArrayList<>(1);
            dataSetToReturnAsIs.add(initialDataCollection);
            return dataSetToReturnAsIs;
        }

        List<List<Long>> partitionedImageSetIds = Lists.partition(imageSets, dataCollectionSize);
        return getDataCollectionsWith(initialDataCollection, partitionedImageSetIds);
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private static List<DataSet> getDataCollectionsWith(DataSet dataCollection, List<List<Long>> partitionedImageSetIds) throws DataCatalogException {
        List<DataSet> dataCollections = new ArrayList<>();

        int collectionSuffix = 1;
        for(List<Long> imageSetIds : partitionedImageSetIds){
            DataSet dataSetWithImageSet = createDataCollection(dataCollection, imageSetIds, collectionSuffix);
            dataCollections.add(dataSetWithImageSet);
            collectionSuffix++;
        }

        return dataCollections;
    }

    private static DataSet createDataCollection(DataSet originalDataSet, List<Long> partitionedImageSet, int collectionSuffix) {
        DataSet dataSetToReturn = new DataSet();
        dataSetToReturn.setCreatedBy(originalDataSet.getCreatedBy());
        dataSetToReturn.setCreatedDate(originalDataSet.getCreatedDate());
        dataSetToReturn.setDescription(originalDataSet.getDescription());
        dataSetToReturn.setFilters(originalDataSet.getFilters());
        dataSetToReturn.setId(originalDataSet.getId());
        dataSetToReturn.setImageSets(partitionedImageSet);
        dataSetToReturn.setName(originalDataSet.getName() + " - " + collectionSuffix);
        dataSetToReturn.setOrgId(originalDataSet.getOrgId());
        dataSetToReturn.setProperties(originalDataSet.getProperties());
        dataSetToReturn.setSchemaVersion(originalDataSet.getSchemaVersion());
        dataSetToReturn.setType(originalDataSet.getType());
        return dataSetToReturn;
    }
}