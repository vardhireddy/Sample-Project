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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;

import jersey.repackaged.com.google.common.collect.Lists;

/**
 * This class is utility class that can have helper methods for Data Catalog Service
 * 
 * @author Madhu Y (305024964)
 *
 */
public class DataCatalogUtils {
	
	private static Logger logger = LoggerFactory.getLogger(DataCatalogUtils.class);
	
	private static final String INVALID_DATA_SET = "Invalid Dataset";
	private static final String INVALID_NUMBER_OF_DATASETS = "Invalid number of Data Collections";
	private static final String NO_IMAGE_SETS_IN_DATA_SET = "No Image Sets found in Data Set";
	private static final String NO_IMAGESETS_AVAILABLE_IN_DATA_SET = "No Imagesets available in DataSet";
	private static final String NO_OF_COLLECTIONS_MORE_THAN_NO_OF_IMAGE_SETS_IN_DATA_SET = "Number of Collections is more than number of Image Sets found in Data Set";
	
	/**
	 * Splits the given filtered data set into given number of collections
	 * 
	 * @param filteredDataSet The source filtered dataset that needs to be split into
	 * @param dataCollectionSize Data Collection Size
	 * 
	 */
	public static List<DataSet> getDataCollectionBatches(DataSet filteredDataSet, Integer dataCollectionSize) throws DataCatalogException {
		
		logger.info("Getting the Data Collection Batches {}", dataCollectionSize);
		
		int numImageSets = 0;
		List<Long> imageSets = null;
		
		if (Objects.isNull(dataCollectionSize)) {
			ArrayList<DataSet> dataSetToReturnAsIs = new ArrayList<>(1);
			dataSetToReturnAsIs.add(filteredDataSet);
			return dataSetToReturnAsIs;
		}
		
		if (dataCollectionSize <= 0) {
			logger.error(INVALID_NUMBER_OF_DATASETS + " {}", dataCollectionSize);
			throw new DataCatalogException(INVALID_NUMBER_OF_DATASETS);
		}
		
		if (Objects.isNull(filteredDataSet)) {
			logger.error(INVALID_DATA_SET);
			throw new DataCatalogException(INVALID_DATA_SET);
		} else {
			imageSets = filteredDataSet.getImageSets();
			if (Objects.isNull(imageSets) || imageSets.isEmpty()) {
				logger.error(NO_IMAGE_SETS_IN_DATA_SET);
				throw new DataCatalogException(NO_IMAGE_SETS_IN_DATA_SET);
			} else {
				numImageSets = imageSets.size();
			}
			if (dataCollectionSize > numImageSets) {
				logger.error(NO_OF_COLLECTIONS_MORE_THAN_NO_OF_IMAGE_SETS_IN_DATA_SET);
				throw new DataCatalogException(NO_OF_COLLECTIONS_MORE_THAN_NO_OF_IMAGE_SETS_IN_DATA_SET);
			}
		}
		
		List<List<Long>> partitionedImageSetLists = Lists.partition(imageSets, dataCollectionSize);
		return getDataCollectionsWith(filteredDataSet, partitionedImageSetLists);
	}
	
	private static List<DataSet> getDataCollectionsWith(DataSet filteredDataSet, List<List<Long>> partitionedImageSetLists) throws DataCatalogException {
		List<DataSet> dataCollections = new ArrayList<>();
		if (Objects.isNull(partitionedImageSetLists) || partitionedImageSetLists.isEmpty()) {
			logger.error(NO_IMAGESETS_AVAILABLE_IN_DATA_SET);
			throw new DataCatalogException(NO_IMAGESETS_AVAILABLE_IN_DATA_SET);
		}
		
		for (Iterator<List<Long>> iterator = partitionedImageSetLists.iterator(); iterator.hasNext();) {
			List<Long> partitionedImageSet = iterator.next();
			DataSet dataSetWithImageSet = getDataSetWithImageSet(filteredDataSet, partitionedImageSet);
			dataCollections.add(dataSetWithImageSet);
		}
		
		return dataCollections;
	}

	private static DataSet getDataSetWithImageSet(DataSet orginalDataSet, List<Long> partitionedImageSet) {
		DataSet dataSetToReturn = new DataSet();
		dataSetToReturn.setCreatedBy(orginalDataSet.getCreatedBy());
		dataSetToReturn.setCreatedDate(orginalDataSet.getCreatedDate());
		dataSetToReturn.setDescription(orginalDataSet.getDescription());
		dataSetToReturn.setFilters(orginalDataSet.getFilters());
		dataSetToReturn.setId(orginalDataSet.getId());
		dataSetToReturn.setImageSets(partitionedImageSet);
		dataSetToReturn.setName(orginalDataSet.getName());
		dataSetToReturn.setOrgId(orginalDataSet.getOrgId());
		dataSetToReturn.setProperties(orginalDataSet.getProperties());
		dataSetToReturn.setSchemaVersion(orginalDataSet.getSchemaVersion());
		dataSetToReturn.setType(orginalDataSet.getType());
		return dataSetToReturn;
	}
}