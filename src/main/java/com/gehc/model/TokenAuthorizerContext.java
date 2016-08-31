/*
 * TokenAuthorizerContext.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.model;

/**
 * Request object to Custom Authorizer
 */
public class TokenAuthorizerContext {

    /**
     * Type
     */
    private String type;
    /**
     * Authorization Token
     */
    private String authorizationToken;
    /**
     * Method Arn
     */
    private String methodArn;

    /**
     * TokenAuthorizerContext constructor
     */
    public TokenAuthorizerContext(String type, String authorizationToken, String methodArn) {
        this.type = type;
        this.authorizationToken = authorizationToken;
        this.methodArn = methodArn;
    }

    /**
     * TokenAuthorizerContext constructor
     */
    public TokenAuthorizerContext() {}

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken( String authorizationToken ) {
        this.authorizationToken = authorizationToken;
    }

    public String getMethodArn() {
        return methodArn;
    }

    public void setMethodArn( String methodArn ) {
        this.methodArn = methodArn;
    }
}
