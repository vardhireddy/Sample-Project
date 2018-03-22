/*
 * RequestValidatorTest.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.filters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;

/**
 * @author dipshah
 *
 */
public class RequestValidatorTest {
	
	Map<String, Object> paramMap = new HashMap<String, Object>();
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void init(){
		paramMap.put("orgId", "159fa151-4cb9-4c78-8124-86be34d97954");
		paramMap.put("dateFrom", "2017-12-14T19:00:00Z");
		paramMap.put("dateTo", "2017-12-14T20:00:00Z");
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 */
	@Test
	public void testValidateImageSeriesFilterParamMap(){
		Map<String, Object> expectedMap = paramMap;
		try {
			RequestValidator.validateImageSeriesFilterParamMap(paramMap);
			assertEquals(expectedMap, paramMap);
		} catch (DataCatalogException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapNullDates() throws DataCatalogException{
		paramMap.put("dateFrom", null);
		paramMap.put("dateTo", null);
		
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_DATE_FROM_VALUE.getErrorMessage()+ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapEmptyDates() throws DataCatalogException{
		paramMap.put("dateFrom", "");
		paramMap.put("dateTo", "");
		
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_DATE_FROM_VALUE.getErrorMessage()+ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapInvalidObject() throws DataCatalogException{
		paramMap.put("dateFrom", Integer.valueOf(1));
		paramMap.put("dateTo", new Object());
		
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_DATE_FROM_VALUE.getErrorMessage()+ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapInvalidDateFormat() throws DataCatalogException{
		paramMap.put("dateFrom", "2017-12-1419:00:00Z");
		paramMap.put("dateTo", "2017-12-14T20:00");
		
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.INVALID_DATE_FROM_FORMAT.getErrorMessage()+ErrorCodes.INVALID_DATE_TO_FORMAT.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapInvalidDateFromNullDateTo() throws DataCatalogException{
		paramMap.put("dateFrom", "2017-12-1419:00:00Z");
		paramMap.put("dateTo", null);

		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapMissingDateFrom() throws DataCatalogException{
		paramMap.remove("dateFrom");
		
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_DATE_FROM.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapMissingDateTo() throws DataCatalogException{
		paramMap.remove("dateTo");
		
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_DATE_TO.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateImageSeriesFilterParamMap(java.util.Map)}.
	 * @throws DataCatalogException 
	 */
	@Test
	public void testValidateImageSeriesFilterParamMapToDateBeforeFromDate() throws DataCatalogException{
		paramMap.put("dateFrom", "2017-12-14T20:00:00Z");
		paramMap.put("dateTo", "2017-12-14T19:00:00Z");

		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.DATE_FROM_AFTER_DATE_TO.getErrorMessage());
		
		RequestValidator.validateImageSeriesFilterParamMap(paramMap);
	}
	

}
