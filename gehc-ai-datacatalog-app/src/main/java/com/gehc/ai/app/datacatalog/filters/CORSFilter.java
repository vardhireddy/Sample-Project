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

package com.gehc.ai.app.datacatalog.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
        String traceId = request.getHeader("X-Amzn-Trace-Id");
		MDC.put("amzn-trace-id", traceId);
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
    public void destroy() { // NOSONAR
        logger.info( "Destroying the CORSFilter" );
    } // NOSONAR

    private void setHeaderIfNotPresent( HttpServletResponse response, String key, String value ) {
        if ( !response.containsHeader( key ) ) {
            response.setHeader( key, value );
        }
    }
}