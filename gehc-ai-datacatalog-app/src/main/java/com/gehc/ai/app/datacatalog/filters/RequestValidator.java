/**
 * 
 */
package com.gehc.ai.app.datacatalog.filters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;

/**
 * @author dipshah
 *
 */
public class RequestValidator {

	private final static String DATE_FROM = "dateFrom";
	private final static String DATE_TO = "dateTo";
	
	public static void validateImageSeriesFilterParamMap(Map<String, Object> paramMap) throws DataCatalogException {
		
		
    	StringBuilder stringBuilder = new StringBuilder();
		if(paramMap.containsKey(DATE_FROM) && paramMap.containsKey(DATE_TO)){
			try{
				validateDateFromAndToDateContent(paramMap.get(DATE_FROM), paramMap.get(DATE_TO));
				validateDateFromAndToDateFormat(paramMap);
			}catch(DataCatalogException exception){
				stringBuilder.append(exception.getMessage());
			}
		}else if(paramMap.containsKey(DATE_FROM)){
			stringBuilder.append(ErrorCodes.MISSING_DATE_TO.getErrorMessage());
			paramMap.remove(DATE_FROM);
    	}else if(paramMap.containsKey(DATE_TO)){
			stringBuilder.append(ErrorCodes.MISSING_DATE_FROM.getErrorMessage());
			paramMap.remove(DATE_TO);
		}
		
    	if(stringBuilder.length() > 0){
    		throw new DataCatalogException(stringBuilder.toString());
    	}
	}
	
	private static void validateDateFromAndToDateContent(Object dateFrom, Object dateTo) throws DataCatalogException {
		StringBuilder stringBuilder = new StringBuilder();
		
		if(dateFrom == null || !(dateFrom instanceof String) || ((String)dateFrom).isEmpty()){
			stringBuilder.append(ErrorCodes.MISSING_DATE_FROM_VALUE.getErrorMessage());
		}
		
		if(dateTo == null || !(dateTo instanceof String) || ((String)dateTo).isEmpty()){
			stringBuilder.append(ErrorCodes.MISSING_DATE_TO_VALUE.getErrorMessage());
		}
		
		if(stringBuilder.length() > 0){
    		throw new DataCatalogException(stringBuilder.toString());
    	}
		
	}

	private static void validateDateFromAndToDateFormat(Map<String, Object> paramMap) throws DataCatalogException{
		
		StringBuilder stringBuilder = new StringBuilder();
		
		DateTimeFormatter sourceFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateAndTimeFrom = null;
		LocalDateTime dateAndTimeTo = null;
		
		try{
			dateAndTimeFrom = LocalDateTime.parse((String)paramMap.get(DATE_FROM), sourceFormat);
		}catch(Exception exception){
			stringBuilder.append(ErrorCodes.INVALID_DATE_FROM_FORMAT.getErrorMessage());
		}
		
		try{
			dateAndTimeTo = LocalDateTime.parse((String)paramMap.get(DATE_TO), sourceFormat);
		}catch(Exception exception){
			stringBuilder.append(ErrorCodes.INVALID_DATE_TO_FORMAT.getErrorMessage());
		}

		if(stringBuilder.length() == 0){
			if(dateAndTimeFrom != null && dateAndTimeTo != null){
				if(dateAndTimeTo.isBefore(dateAndTimeFrom)){
					paramMap.remove(DATE_FROM);
					paramMap.remove(DATE_TO);
					stringBuilder.append(ErrorCodes.DATE_FROM_AFTER_DATE_TO.getErrorMessage());
				}else{
					paramMap.put(DATE_FROM, targetFormat.format(dateAndTimeFrom));
					paramMap.put(DATE_TO, targetFormat.format(dateAndTimeTo));
				}
			}else{
				paramMap.remove(DATE_FROM);
				paramMap.remove(DATE_TO);
			}
		}
		
		if(stringBuilder.length() > 0){
    		throw new DataCatalogException(stringBuilder.toString());
    	}
		
	}

}
