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

import com.gehc.ai.app.common.constants.ApplicationConstants;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
	 * Returns whether the provided string represents a valid date.
	 *
	 * @param dateAsStr The string that claims to be a valid representation of a date
	 * @return {@code true} if the string is a valid representation of a date; otherwise, {@code false}.
	 */
	public static boolean isValidDate(String dateAsStr){
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			formatter.setLenient(false);
			formatter.parse(dateAsStr);
			return true;
		} catch (ParseException e) {
			return false;
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

	/**
	 * Validates the upload Id required for fetching upload details
	 *
	 * @param uploadId
	 * @return
	 * @throws DataCatalogException
	 */
	public static void validateUploadId(Long uploadId) throws DataCatalogException {

		if(uploadId == null){
			throw new DataCatalogException(ErrorCodes.MISSING_UPLOAD_ID.getErrorMessage(),HttpStatus.BAD_REQUEST);
		}

		if(uploadId <= 0){
			throw new DataCatalogException(ErrorCodes.INVALID_UPLOAD_ID.getErrorMessage(),HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Retrieves orgID from Auth token
	 * @param httpServletRequest - httpServletRequest object
	 * @return org ID
	 */
	public static String getOrgIdFromAuth(HttpServletRequest httpServletRequest) throws DataCatalogException{

		// Gate 1 - The HttpServletRequest object must be accessible.  Otherwise, we can't extract the org ID
		if(Objects.isNull(httpServletRequest)){
			throw new DataCatalogException("Http Request not found to validate for Authorization.",HttpStatus.BAD_REQUEST);
		}

		// Gate 2 - The org ID is required to be defined
		if (Objects.isNull(httpServletRequest.getAttribute("orgId"))) {
			throw new DataCatalogException("Request cannot be validated for Authorization by orgId.",HttpStatus.BAD_REQUEST);
		}

		return httpServletRequest.getAttribute("orgId").toString();
	}

    /**
     * Verifies if the contract can be deleted
     * @param contractToBeDeleted - contract entity to be deleted
     * @param contractId - contract entity unique id from delete request
     * @param orgId - orgId to verify the access to delete contract
     * @throws DataCatalogException
     * -> if contract is not found
     * -> if user is not allowed to delete contract
     * -> if contract is already deleted
     */
	public static void validateContractToBeDeleted(Optional<Contract> contractToBeDeleted, Long contractId, String orgId) throws DataCatalogException{
	    String status = "false";

        if (contractToBeDeleted == null) {
            logger.info("No contract exists with given id :", contractId);
            throw new DataCatalogException("No contract exists with given id", HttpStatus.NOT_FOUND);
        }

        if (!contractToBeDeleted.get().getOrgId().equals(orgId))
        {
            logger.info("User does not have access to delete the contract as token orgId does not match the contract orgId.", orgId);
            throw new DataCatalogException("User does not have access to delete the contract.", HttpStatus.FORBIDDEN);
        }

        String contractStatus = contractToBeDeleted.get().getActive();
        if (contractStatus.equalsIgnoreCase(status)) {
            throw new DataCatalogException("Contract with given id is already inactive", HttpStatus.OK);
        }

    }
}
