/*
 * DataCatalogResponse.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataCatalogResponse {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    private String status;
    private String message;
    private Object responseObject;

    public DataCatalogResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public DataCatalogResponse(String status, Object responseObject) {
        this.status = status;
        this.responseObject = responseObject;
    }
    
    public DataCatalogResponse(ErrorCodes errorCodes) {
        this.status = FAILED;
        this.message = errorCodes.getErrorMessage();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static DataCatalogResponse getSuccessResponse () {
        return   new DataCatalogResponse (SUCCESS,  null);
    }
    
    public static DataCatalogResponse getSuccessResponse(String successMssg) {
        return   new DataCatalogResponse (SUCCESS,  successMssg);
    }
    
    public static DataCatalogResponse getSuccessResponse(Object responseObject) {
        return   new DataCatalogResponse (SUCCESS,  responseObject);
    }

    public static DataCatalogResponse getErrorResponse (String error) {
        return   new DataCatalogResponse (FAILED,  error);
    }

    public static DataCatalogResponse getErrorResponse (ErrorCodes error) {
        return   new DataCatalogResponse (FAILED,  error.getErrorMessage());
    }

	public Object getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}
}
