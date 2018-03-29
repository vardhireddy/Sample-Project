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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.Contract.DeidStatus;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;

/**
 * @author dipshah
 *
 */
public class RequestValidatorTest {
	
	Map<String, Object> paramMap = new HashMap<String, Object>();
	List<MultipartFile> contractFiles = new ArrayList<MultipartFile>();
	MockMultipartFile metadataJson;
	
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
	
	private void getContractFiles(String metaDataFileName, String ...contractFileNames){
		ClassLoader classLoader = getClass().getClassLoader();
		MockMultipartFile contractFile = null;
		try {
			for(String contractFileName : contractFileNames){
				contractFile = new MockMultipartFile("contract", contractFileName, MediaType.MULTIPART_FORM_DATA, 
					classLoader.getResourceAsStream("data/"+contractFileName));
				contractFiles.add(contractFile);
			}
			metadataJson = new MockMultipartFile("metadata", metaDataFileName, MediaType.MULTIPART_FORM_DATA, 
					classLoader.getResourceAsStream("data/"+metaDataFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#testvalidateContractAndParseMetadata(List<Multipart>,MultipartFile)}.
	 */
	@Test
	public void testvalidateContractAndParseMetadata(){
		try {
			getContractFiles("metadata_success_DEIDS_TO_LOCAL_STANDARDS.json","contract_success.pdf");
			Contract receivedContract = RequestValidator.validateContractAndParseMetadata(contractFiles, metadataJson);
			assertEquals(getExpectedContract(), receivedContract);
		} catch (DataCatalogException e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#testvalidateContractAndParseMetadata(List<Multipart>,MultipartFile)}.
	 */
	@Test
	public void testvalidateContractAndParseMetadataNullFiles() throws DataCatalogException{
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_CONTRACT.getErrorMessage()+ErrorCodes.MISSING_CONTRACT_METADATA.getErrorMessage());
		
		RequestValidator.validateContractAndParseMetadata(null, null);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#testvalidateContractAndParseMetadata(List<Multipart>,MultipartFile)}.
	 */
	@Test
	public void testvalidateContractAndParseMetadataInavlidFormat() throws DataCatalogException{
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.UNSUPPORTED_CONTRACT_FILE_TYPE.getErrorMessage()+ "contract_failure_invalid_format.pd" + ErrorCodes.UNSUPPORTED_CONTRACT_METADATA_FILE_TYPE.getErrorMessage());
		
		getContractFiles("metadata_failure_invalid_format.js","contract_failure_invalid_format.pd");
		RequestValidator.validateContractAndParseMetadata(contractFiles, metadataJson);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#testvalidateContractAndParseMetadata(List<Multipart>,MultipartFile)}.
	 */
	@Test
	public void testvalidateContractAndParseMetadataInavlidDeidStatus() throws DataCatalogException{
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.INVALID_CONTRACT_METADATA_FILE.getErrorMessage());
		
		getContractFiles("metadata_failure_invalid_deidstatus.json","contract_success.pdf");
		RequestValidator.validateContractAndParseMetadata(contractFiles, metadataJson);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateContractId()}.
	 */
	@Test
	public void testvalidateContractId(){
		try{
			RequestValidator.validateContractId(1L);
		}catch(Exception e){
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateContractId()}.
	 */
	@Test
	public void testvalidateContractIdNullContractId() throws DataCatalogException{
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.MISSING_CONTRACT_ID.getErrorMessage());
		
		RequestValidator.validateContractId(null);
	}
	
	/**
	 * Test method for {@link com.gehc.ai.app.datacatalog.filters.RequestValidator#validateContractId()}.
	 */
	@Test
	public void testvalidateContractIdEmptyContractId() throws DataCatalogException{
		thrown.expect(DataCatalogException.class);
		thrown.expectMessage(ErrorCodes.INVALID_CONTRACT_ID.getErrorMessage());
		
		RequestValidator.validateContractId(0L);
	}

	
	private Object getExpectedContract() {
    	Contract contract = new Contract();
    	contract.setActive("true");
    	contract.setBusinessCase("Business Case");
    	contract.setContactInfo("Contact Info");
    	contract.setContractName("Contract Name");
    	contract.setDataOriginCountry("Data Origin Country");
    	contract.setDeidStatus(DeidStatus.DEIDS_TO_LOCAL_STANDARDS);
    	contract.setOrgId("orgId");
    	contract.setProperties("[\"CKGS USA - Passport _ How To Apply.pdf\",\"CKGS USA - Passport _ How To Apply.pdf\" ]");
    	contract.setSchemaVersion("Schema Version");
    	contract.setUploadBy("radiologist");
    	contract.setUsageNotes("Usage Notes");
    	contract.setUsageRights("Usage Rights");
    	contract.setUsageLength(11);
    	
    	return contract;
	}
	

}
