/*
 * CORSFilter.java
 *
 * Copyright (c) 2015 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.dc.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORSFilter class
 *
 */
@Component
public class CORSFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger( CORSFilter.class );

    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse)res;
        HttpServletRequest request = (HttpServletRequest)req;
        setHeaderIfNotPresent( response, "Access-Control-Allow-Origin", "*" );
        if ( request.getMethod().equalsIgnoreCase( "OPTIONS" ) ) {
            logger.debug( "Request [{}] : [{}]", request.getMethod(), request.getRequestURI() );
            setHeaderIfNotPresent( response, "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH" );
            setHeaderIfNotPresent( response, "Access-Control-Allow-Headers", "Authorization, Origin, X-Requested-With, Content-Type, Accept, g-captcha-response" );
            setHeaderIfNotPresent( response, "Access-Control-Max-Age", "1" );

        }
        chain.doFilter( req, response );

    }

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
        logger.info( "Initializaing the CORSFilter" );
    }

    @Override
    public void destroy() {
        logger.info( "Destroying the CORSFilter" );
    }

    private void setHeaderIfNotPresent( HttpServletResponse response, String key, String value ) {
        logger.debug( "Checking for header [{}]:[{}]", key, value );
        if ( !response.containsHeader( key ) ) {
            logger.debug( "Header not present [{}]:[{}] : Setting Now", key, value );
            response.setHeader( key, value );
        }
    }
}