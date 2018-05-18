package com.gehc.ai.app.datacatalog.util;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;

public class DataCatalogUtilsTest {
	
	private final static ObjectMapper mapper = new ObjectMapper();
	private static final DefaultResourceLoader resLoader = new DefaultResourceLoader();
	
	@Test (expected = DataCatalogException.class)
	public void testGetDataCollectionBatchesWithNullFilteredDataSet() throws Exception {
		DataCatalogUtils.getDataCollectionBatches(null, 2);
	}

	@Test
	public void testGetDataCollectionBatchesWithNoCollectionSize() throws Exception {
		DataSet validDataSetFromResource = getDataSetFromResource("ValidFileteredDataSet.json");
		Assert.assertEquals(validDataSetFromResource.getImageSets().size(), DataCatalogUtils.getDataCollectionBatches(validDataSetFromResource, null).get(0).getImageSets().size());
	}
	
	@Test
	public void testGetDataCollectionBatches_ForValidFilteredDataSet_with_BoundaryConditions() throws Exception {
		
		int [] dataCollectionSizesToTest = new int [] {-1, 0, 1, 5, 2, 8, 10};
		
		for (int i = 0; i < dataCollectionSizesToTest.length; i++) {
			DataSet dataSetFromResource = getDataSetFromResource("ValidFileteredDataSet.json");
			if (dataCollectionSizesToTest[i] <= 0 || dataCollectionSizesToTest[i] > 8) { // 8 is the number of image sets defined in ValidFileteredDataSet.json
				try {
					DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, dataCollectionSizesToTest[i]);
					Assert.fail("Unexpected Execution Path");
				} catch (Exception e) {
					Assert.assertTrue(e instanceof DataCatalogException);
				}
			} else  {
				try {
					List<DataSet> dataCollectionBatches = DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, dataCollectionSizesToTest[i]);
					for (Iterator<DataSet> iterator = dataCollectionBatches.iterator(); iterator.hasNext();) {
						DataSet dataSet = (DataSet) iterator.next();
						Assert.assertTrue(dataSet.getImageSets().size() <= dataCollectionSizesToTest[i]);
					}
				} catch (Exception e) {
					Assert.fail("Unexpected Execution Path");
				}
			}

		}
	}
	
	@Test (expected = DataCatalogException.class)
	public void testGetDataCollectionBatchesWithInvalidFilteredDataSet() throws Exception {
		DataSet dataSetFromResource = getDataSetFromResource("InvalidFileteredDataSetWithEmptyImageSets.json");
		DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, 2);
	}
	
	private DataSet getDataSetFromResource(String resource) throws Exception{
		return mapper.readValue(resLoader.getResource(resource).getInputStream(), DataSet.class);
	}
}