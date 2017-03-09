///*
// * DataCatalogInterceptor.java
// * 
// * Copyright (c) 2017 by General Electric Company. All rights reserved.
// * 
// * The copyright to the computer software herein is the property of
// * General Electric Company. The software may be used and/or copied only
// * with the written permission of General Electric Company or in accordance
// * with the terms and conditions stipulated in the agreement/contract
// * under which the software has been supplied.
// */
//package com.gehc.ai.app.interceptor;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
///**
// * @author 212071558
// *
// */
//@PropertySource ( {"classpath:application.yml"} )
//@Component
//public class DataCatalogInterceptor implements HandlerInterceptor{
//    /** The logger. */
//    private static Logger logger = LoggerFactory.getLogger( DataCatalogInterceptor.class );
//
//    @Value("${uom.user.me.url}")
//    private String uomMeUrl;
//    
//    @Autowired
//    private RestTemplate restTemplate;
// 
// /*   @Autowired
//    private IDataCatalogRest dataCatalogRest;*/
// 
//    /* (non-Javadoc)
//     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
//     */
//    @Override
//    public boolean preHandle( HttpServletRequest req, HttpServletResponse res, Object obj ) throws Exception {
//        logger.info( " @@@ In preHandle method, token =" + req.getHeader( HttpHeaders.AUTHORIZATION ));
//        if(null != obj){
//            logger.info( " *** In preHandle method, obj =" + obj.toString());
//         }else{
//            logger.info( " *** In preHandle method obj is null" );
//        }        
//        req.setAttribute( "orgId", getOrgIdBasedOnSessionToken(req.getHeader( HttpHeaders.AUTHORIZATION )) );
//        return true;
//    }
//    /* (non-Javadoc)
//     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
//     */
//    @Override
//    public void afterCompletion( HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3 ) throws Exception {
//        logger.info( " *** In afterCompletion method" );
//        
//    }
//
//    /* (non-Javadoc)
//     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
//     */
//    @Override
//    public void postHandle( HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3 ) throws Exception {
//        logger.info( " *** In postHandle method" );
//        
//    }
//    
//    @SuppressWarnings ( "unchecked" )
//    public String getOrgIdBasedOnSessionToken(String authToken){
//        logger.info( "  In getOrgIdBasedOnSessionToken, authToken = " + authToken );
//        String orgId = null;
//        HttpHeaders headers = new HttpHeaders();
//        headers.set( HttpHeaders.AUTHORIZATION, authToken );
//        headers.setContentType( org.springframework.http.MediaType.APPLICATION_JSON );
//        HttpEntity<String> requestEntity = new HttpEntity<>( headers );
//        ResponseEntity<Object> responseEntity = null;
//        responseEntity = restTemplate.exchange( "https://6zpi3igymc.execute-api.us-east-1.amazonaws.com/prdge_idam_uomapi/v1/user/me?level.value=2", HttpMethod.GET, requestEntity, Object.class );
//        Object userObject = "{}";
//        if ( responseEntity.hasBody() ) {
//            userObject = responseEntity.getBody();
//            LinkedHashMap<String, Object> userData = (LinkedHashMap)userObject;
//            logger.info("----------Iterate thru userData");
//            logger.info("----------userData size" + userData.size());    
//            Iterator<Entry<String,Object>> itr = userData.entrySet().iterator();
//            while (itr.hasNext() && orgId==null) {
//                Entry<String,Object> entry = itr.next();
//                String key = entry.getKey();
//                logger.info("key: " + key);
//                if(key.equalsIgnoreCase( "role" )){
//                    ArrayList<Object> list = (ArrayList<Object>)entry.getValue();
//                    logger.info("user value: " + list.size());
//                    for(int i=0; i < list.size(); i++){
//                        LinkedHashMap<String, Object> roleValue = (LinkedHashMap)(list.get( i ));
//                        logger.info("----------roleValue size" + roleValue.size());    
//                        Iterator<Entry<String,Object>> roleItr = roleValue.entrySet().iterator();
//                        while (roleItr.hasNext()) {
//                            Entry<String,Object> roleEntry = roleItr.next();
//                            String roleKey = roleEntry.getKey();
//                            logger.info("roleKey: " + roleKey);
//                            if(roleKey.equalsIgnoreCase( "scopingOrganization" )){
//                                Object valueInRole = (Object)roleEntry.getValue();
//                                logger.info(" valueInRole : " + valueInRole.toString());
//                                Map<String, String> dataInRole = (HashMap<String, String>)valueInRole;
//                                logger.info("reference valueInRole : " + dataInRole.get( "reference" ));
//                                if(dataInRole.get( "reference" ).startsWith( "organization" )){
//                                    orgId = dataInRole.get( "reference" ).substring( (dataInRole.get( "reference" ).indexOf( "/" ))+1 );
//                                    logger.info( "Org id = " + orgId );
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }                
//            }
//        } else {
//            logger.info("----------userObject body has no content");
//        }
//        return orgId;
//    }
//
//}
