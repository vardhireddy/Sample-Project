/**
 * 
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

	public static void validateImageSeriesFilterParamMap(Map<String, Object> paramMap) throws DataCatalogException {
    	StringBuilder errorMessage = new StringBuilder();
		if(paramMap.containsKey(ApplicationConstants.DATE_FROM) && paramMap.containsKey(ApplicationConstants.DATE_TO)){
			try{
				validateDateFromAndToDateContent(paramMap.get(ApplicationConstants.DATE_FROM), paramMap.get(ApplicationConstants.DATE_TO));
				validateDateFromAndToDateFormat(paramMap);
			}catch(DataCatalogException exception){
				errorMessage.append(exception.getMessage());
			}
		}else if(paramMap.containsKey(ApplicationConstants.DATE_FROM)){
			errorMessage.append(ErrorCodes.MISSING_DATE_TO.getErrorMessage());
    	}else if(paramMap.containsKey(ApplicationConstants.DATE_TO)){
			errorMessage.append(ErrorCodes.MISSING_DATE_FROM.getErrorMessage());
		}
		
    	if(errorMessage.length() > 0){
    		throw new DataCatalogException(errorMessage.toString());
    	}
	}
	
	private static void validateDateFromAndToDateContent(Object dateFrom, Object dateTo) throws DataCatalogException {
		StringBuilder errorMessage = new StringBuilder();
		
		if(dateFrom == null || !(dateFrom instanceof String) || ((String)dateFrom).trim().isEmpty()){
			errorMessage.append(ErrorCodes.MISSING_DATE_FROM_VALUE.getErrorMessage());
		}
		
		if(dateTo == null || !(dateTo instanceof String) || ((String)dateTo).trim().isEmpty()){
			errorMessage.append(ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		}
		
		if(errorMessage.length() > 0){
    		throw new DataCatalogException(errorMessage.toString());
    	}
		
	}

	private static void validateDateFromAndToDateFormat(Map<String, Object> paramMap) throws DataCatalogException{
		
		StringBuilder errorMessage = new StringBuilder();
		DateTimeFormatter sourceFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		LocalDateTime dateAndTimeFrom = null;
		LocalDateTime dateAndTimeTo = null;
		
		try{
			dateAndTimeFrom = LocalDateTime.parse((String)paramMap.get(ApplicationConstants.DATE_FROM), sourceFormat);
		}catch(Exception exception){
			errorMessage.append(ErrorCodes.INVALID_DATE_FROM_FORMAT.getErrorMessage());
		}
		
		try{
			dateAndTimeTo = LocalDateTime.parse((String)paramMap.get(ApplicationConstants.DATE_TO), sourceFormat);
		}catch(Exception exception){
			errorMessage.append(ErrorCodes.INVALID_DATE_TO_FORMAT.getErrorMessage());
		}

		if(errorMessage.length() == 0){
			if(dateAndTimeFrom != null && dateAndTimeTo != null){
				if(dateAndTimeTo.isBefore(dateAndTimeFrom)){
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
