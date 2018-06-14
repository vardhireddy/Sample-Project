/*
 * DataCatalogException.java
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

import org.springframework.http.HttpStatus;

/**
 * Created by sowjanyanaidu on 8/8/17.
 */
public class DataCatalogException extends Exception{

    private HttpStatus httpStatusCode;

    public DataCatalogException(String message) {
        super(message);
    }

    public DataCatalogException(String message, HttpStatus httpStatusCode) {

        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatus httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
