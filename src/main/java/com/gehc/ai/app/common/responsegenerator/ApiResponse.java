/*
 * ApiResponse.java
 * 
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.common.responsegenerator;

/**
 * @author 212071558
 */
public class ApiResponse {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    private String code;
    private String message;
    private String id;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public ApiResponse() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ApiResponse( String status, String code, String message, String id ) {
        super();
        this.status = status;
        this.code = code;
        this.message = message;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApiResponse [status=" + status + ", code=" + code + ", message=" + message + ", id=" + id + "]";
    }
}
