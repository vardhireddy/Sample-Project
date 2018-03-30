/*
 * ErrorCodes.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
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
	DATE_FROM_AFTER_DATE_TO("dateFrom should be before dateTo"),
	MISSING_CONTRACT("Missing Contract file(s) "), 
	MISSING_CONTRACT_METADATA("Missing Contract Metadata file"), 
	UNSUPPORTED_CONTRACT_FILE_TYPE("Only .pdf file is supported for contracts. Unsupported Contract File - "),
	EMPTY_CONTRACT_FILE_TYPE("Empty Contract File - "),
	UNSUPPORTED_CONTRACT_METADATA_FILE_TYPE("Only .json file is supported for metadata"),
	INVALID_CONTRACT_METADATA_FILE("Unable to parse Contract Metadata File"),
	MISSING_CONTRACT_ID("Contract Id is required for fetching contract details"),
	INVALID_CONTRACT_ID("Contract Id should be in Number format")
    ;

    private final String errorMessage;

    ErrorCodes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
