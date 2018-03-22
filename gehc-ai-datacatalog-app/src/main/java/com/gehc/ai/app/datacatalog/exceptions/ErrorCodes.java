/**
 * 
 */
package com.gehc.ai.app.datacatalog.exceptions;

/**
 * @author dipshah
 *
 */
public enum ErrorCodes {

	MISSING_DATE_TO("dateTo is missing in the request"),
	MISSING_DATE_FROM("dateFrom is missing in the request"),
    MISSING_DATE_FROM_VALUE("Missing value of dateFrom"),
    MISSING_DATE_TO_VALUE("Missing value of dateTo"),
	INVALID_DATE_FROM_FORMAT("Inavlid format for dateFrom. Correct format is : yyyy-MM-ddTHH:mm:ssZ"),
	INVALID_DATE_TO_FORMAT("Inavlid format for dateTo. Correct format is : yyyy-MM-ddTHH:mm:ssZ"),
	DATE_FROM_AFTER_DATE_TO("dateFrom should be before dateTo")
    ;

    private final String errorMessage;

    ErrorCodes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
