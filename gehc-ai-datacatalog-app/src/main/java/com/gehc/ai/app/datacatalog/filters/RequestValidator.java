/*
 * RequestValidator.java
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.gehc.ai.app.common.constants.ApplicationConstants;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;

/**
 * @author dipshah
 *
 */
public class RequestValidator {

	/**
     * The logger.
     */
    private static Logger logger = LoggerFactory.getLogger(RequestValidator.class);
    
	private static Map<String, String> supportedContractFileFormats = new HashMap<String, String>();
	private static Map<String, String> supportedMetadataFileFormats = new HashMap<String, String>();
	
	static{
		supportedContractFileFormats.put("pdf", "pdf");
		supportedMetadataFileFormats.put("json", "json");
	}
	
	/**
	 * Validates the filter parameters for fetching image series
	 * 
	 * @param paramMap
	 * @throws DataCatalogException
	 */
	public static void validateImageSeriesFilterParamMap(Map<String, Object> paramMap) throws DataCatalogException {
    	StringBuilder errorMessage = new StringBuilder();
		if(paramMap.containsKey(ApplicationConstants.DATE_FROM) && paramMap.containsKey(ApplicationConstants.DATE_TO)){
			try{
				validateDateFromAndToDateContent(paramMap.get(ApplicationConstants.DATE_FROM), paramMap.get(ApplicationConstants.DATE_TO));
				
				//If one of dateFrom and dateTo is not present, no further date validation will be done.
				validateDateFromAndToDateFormat(paramMap.get(ApplicationConstants.DATE_FROM), paramMap.get(ApplicationConstants.DATE_TO));
			}catch(DataCatalogException exception){
				errorMessage.append(exception.getMessage());
			}
		}else if(paramMap.containsKey(ApplicationConstants.DATE_FROM)){ // Checks if only dateFrom is present
			errorMessage.append(ErrorCodes.MISSING_DATE_TO.getErrorMessage());
    	}else if(paramMap.containsKey(ApplicationConstants.DATE_TO)){ // Checks if only dateTo is present
			errorMessage.append(ErrorCodes.MISSING_DATE_FROM.getErrorMessage());
		}
		
    	if(errorMessage.length() > 0){
    		throw new DataCatalogException(errorMessage.toString());
    	}
	}
	
	/**
	 * Validates that dateFrom and dateTo values are present.
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @throws DataCatalogException
	 */
	private static void validateDateFromAndToDateContent(Object dateFrom, Object dateTo) throws DataCatalogException {
		StringBuilder errorMessage = new StringBuilder();
		
		//Checks that dateFrom has data
		if(dateFrom == null || !(dateFrom instanceof String) || ((String)dateFrom).trim().isEmpty()){
			errorMessage.append(ErrorCodes.MISSING_DATE_FROM_VALUE.getErrorMessage());
		}
		
		//Checks that dateTo has data
		if(dateTo == null || !(dateTo instanceof String) || ((String)dateTo).trim().isEmpty()){
			errorMessage.append(ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		}
		
		if(errorMessage.length() > 0){
    		throw new DataCatalogException(errorMessage.toString());
    	}
		
	}

	/**
	 * Validates the format for the date objects
	 * @param paramMap
	 * @throws DataCatalogException
	 */
	private static void validateDateFromAndToDateFormat(Object dateFrom, Object dateTo) throws DataCatalogException{
		
		StringBuilder errorMessage = new StringBuilder();
		DateTimeFormatter sourceFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		LocalDateTime dateAndTimeFrom = null;
		LocalDateTime dateAndTimeTo = null;
		
		//Checks dateFrom format
		try{
			dateAndTimeFrom = LocalDateTime.parse((String)dateFrom, sourceFormat);
		}catch(Exception exception){
			logger.error(ErrorCodes.INVALID_DATE_FROM_FORMAT.getErrorMessage(),exception);
			errorMessage.append(ErrorCodes.INVALID_DATE_FROM_FORMAT.getErrorMessage());
		}
		
		//Checks dateTo format
		try{
			dateAndTimeTo = LocalDateTime.parse((String)dateTo, sourceFormat);
		}catch(Exception exception){
			errorMessage.append(ErrorCodes.INVALID_DATE_TO_FORMAT.getErrorMessage());
		}

		if(errorMessage.length() == 0){
			if(dateAndTimeFrom != null && dateAndTimeTo != null){
				if(dateAndTimeTo.isBefore(dateAndTimeFrom)){ //Check if dateTo is before dateFrom
					errorMessage.append(ErrorCodes.DATE_FROM_AFTER_DATE_TO.getErrorMessage());
				}
			}else{
				errorMessage.append(ErrorCodes.INVALID_DATE_FROM_FORMAT.getErrorMessage());
				errorMessage.append(ErrorCodes.INVALID_DATE_TO_FORMAT.getErrorMessage());
			}
		}
		
		if(errorMessage.length() > 0){
    		throw new DataCatalogException(errorMessage.toString());
    	}
		
	}

	/**
	 * 
	 * @param contractFiles
	 * @param metadataJson
	 * @throws DataCatalogException
	 */
	public static Contract validateContractAndParseMetadata(List<MultipartFile> contractFiles, MultipartFile metadataJson) throws DataCatalogException {
		StringBuilder errorMessage = new StringBuilder();
		Contract contract = null;
		checkFilesExists(contractFiles, metadataJson, errorMessage);
		
		if(contractFiles != null && metadataJson != null && errorMessage.length() == 0){
			
			validateSupportedContractFileFormats(contractFiles, errorMessage);
			
			ObjectMapper mapper = new ObjectMapper();
			String metadataFileExt = FilenameUtils.getExtension(metadataJson.getOriginalFilename());
			if(supportedMetadataFileFormats.containsKey(metadataFileExt)){
				try {
					contract = mapper.readValue(metadataJson.getInputStream(), Contract.class);
				} catch(InvalidFormatException invalidFormatException){
					logger.error(ErrorCodes.INVALID_CONTRACT_METADATA_FILE.getErrorMessage()+" : "
							+invalidFormatException.getOriginalMessage(),invalidFormatException);
					errorMessage.append(ErrorCodes.INVALID_CONTRACT_METADATA_FILE.getErrorMessage());
					errorMessage.append(" : ");
					errorMessage.append(invalidFormatException.getOriginalMessage());
				} catch (Exception exception) {
					logger.error(ErrorCodes.INVALID_CONTRACT_METADATA_FILE.getErrorMessage(),exception);
					errorMessage.append(ErrorCodes.INVALID_CONTRACT_METADATA_FILE.getErrorMessage());
				}
			}else{
				errorMessage.append(ErrorCodes.UNSUPPORTED_CONTRACT_METADATA_FILE_TYPE.getErrorMessage());
			}
		}
		
		// Throw error if metadata or contract file is missing
		if(errorMessage.length() > 0){
    		throw new DataCatalogException(errorMessage.toString());
    	}
		
		return contract;
	}

	/**
	 * @param contractFiles
	 * @param errorMessage
	 */
	private static void validateSupportedContractFileFormats(List<MultipartFile> contractFiles,
			StringBuilder errorMessage) {
		contractFiles.forEach(contractFile -> {
			final String contractFileExt = FilenameUtils.getExtension(contractFile.getOriginalFilename());
			if(!supportedContractFileFormats.containsKey(contractFileExt)){
				errorMessage.append(ErrorCodes.UNSUPPORTED_CONTRACT_FILE_TYPE.getErrorMessage() + contractFile.getOriginalFilename());
			}
			if (contractFile.getSize() == 0){
				errorMessage.append(ErrorCodes.EMPTY_CONTRACT_FILE_TYPE.getErrorMessage() + contractFile.getOriginalFilename());
			}
		});
	}

	/**
	 * @param contractFiles
	 * @param metadataJson
	 * @param errorMessage
	 */
	private static void checkFilesExists(List<MultipartFile> contractFiles, MultipartFile metadataJson,
			StringBuilder errorMessage) {
		// Check if contract file(s) is(are) present
		if(contractFiles == null || contractFiles.isEmpty()){
			errorMessage.append(ErrorCodes.MISSING_CONTRACT.getErrorMessage());
		}
		// Check if metadata file is present
		if(metadataJson == null || metadataJson.isEmpty() || metadataJson.getSize() == 0){
			errorMessage.append(ErrorCodes.MISSING_CONTRACT_METADATA.getErrorMessage());
		}
	}

	/**
	 * Validates the contract Id required for fetching contract details
	 * 
	 * @param contractId
	 * @return 
	 * @throws DataCatalogException
	 */
	public static void validateContractId(Long contractId) throws DataCatalogException {
		
		if(contractId == null){
			throw new DataCatalogException(ErrorCodes.MISSING_CONTRACT_ID.getErrorMessage());
		}
		
		if(contractId <= 0){
			throw new DataCatalogException(ErrorCodes.INVALID_CONTRACT_ID.getErrorMessage());
		}
		
	}
}
