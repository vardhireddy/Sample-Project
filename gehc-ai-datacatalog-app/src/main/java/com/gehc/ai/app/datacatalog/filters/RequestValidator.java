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
import java.util.Map;

import com.gehc.ai.app.common.constants.ApplicationConstants;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;

/**
 * @author dipshah
 *
 */
public class RequestValidator {

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

}
