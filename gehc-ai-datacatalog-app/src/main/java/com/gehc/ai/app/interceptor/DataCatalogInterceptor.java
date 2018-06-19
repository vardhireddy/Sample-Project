/*
 * DataCatalogInterceptor.java
 * 
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gehc.ai.app.datacatalog.service.IRemoteService;

/**
 * @author 212071558
 *
 */
@PropertySource ( {"classpath:application.yml"} )
@Component
public class DataCatalogInterceptor implements HandlerInterceptor{
    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger( DataCatalogInterceptor.class );

	@Value("${dev.mode}")
	private String devMode;

	@Value("${dev.orgId}")
	private String devOrgId;

    @Autowired
    private IRemoteService remoteServiceImpl;
 
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle( HttpServletRequest req, HttpServletResponse res, Object obj ) throws Exception {      
        if(null != req){
		   boolean foundAuthToken = false;
           logger.debug( " ### In preHandle method, req.getMethod() = " + req.getMethod() + ", req.getServletPath() = " + req.getServletPath());
           if(!("OPTIONS".equalsIgnoreCase( req.getMethod() ))){
	           Enumeration headerNames = req.getHeaderNames();
	           String key = null;
	           while (headerNames.hasMoreElements()) {
	                   key = (String) headerNames.nextElement();
	                   if(key.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)){
	                	   logger.debug( " ### In preHandle method, found the authorization key" );
	                	   foundAuthToken = true;
	                	   break;
	                   }
	           }
	           if (null != req.getMethod() && req.getMethod().equalsIgnoreCase("POST") && null != req.getServletPath() && req.getServletPath().equalsIgnoreCase("/api/v1/annotation")) {
                   logger.debug(" +++ In preHandle method, save annotation is getting called so not looking for org id");
               }
               if (req.getMethod() != null
                       && (req.getMethod().equalsIgnoreCase("PUT")
                            || req.getMethod().equalsIgnoreCase("GET"))
                       && null != req.getServletPath()
                       && req.getServletPath().startsWith("/api/v1/datacatalog/contract/")){
                   logger.debug( " +++ In preHandle method, put/get contract by ID is getting called so not looking for org id");
	           } if (req.getMethod() != null
                     && (req.getMethod().equalsIgnoreCase("POST"))
                     && null != req.getServletPath()
                     && req.getServletPath().startsWith("/api/v1/datacatalog/upload")){
                           logger.debug( " +++ In preHandle method, post upload is getting called so not looking for org id");
                   } else if(null != req.getMethod() && req.getMethod().equalsIgnoreCase("POST") && null != req.getServletPath() && req.getServletPath().endsWith("/patient")){
	        	   logger.debug( " +++ In preHandle method, save patient is getting called so not looking for org id");
	           } else if(null != req.getMethod() && req.getMethod().equalsIgnoreCase("POST") && null != req.getServletPath() && req.getServletPath().endsWith("/study")){
	        	   logger.debug( " +++ In preHandle method, save study is getting called so not looking for org id");
	           } else if(foundAuthToken){
	               req.setAttribute( "orgId", remoteServiceImpl.getOrgIdBasedOnSessionToken(req.getHeader( HttpHeaders.AUTHORIZATION )) );
	           } else if (!foundAuthToken && !StringUtils.isEmpty(devMode) && devMode.equalsIgnoreCase("true")) {
					   req.setAttribute("orgId", devOrgId);
			   } else {
	        	throw new WebApplicationException( Response.status( Status.FORBIDDEN ).entity( "User is not authorized" ).build() );
	           }
           }else{
               logger.debug( " **** In preHandle method req method is options ");
           }
        }else{
            logger.debug( " !!! In preHandle method req is null ");   
        }
        
            return true;
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion( HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3 ) throws Exception {
        logger.debug( " !!! In afterCompletion method" );        
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle( HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3 ) throws Exception {
        logger.debug( " !!! In postHandle method" );
    }

}
