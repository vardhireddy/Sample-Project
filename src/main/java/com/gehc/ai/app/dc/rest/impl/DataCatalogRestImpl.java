/*
 * DataCatalogRestImpl.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.rest.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.rest.IDataCatalogRest;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */

@RestController
@Produces ( MediaType.APPLICATION_JSON )
public class DataCatalogRestImpl implements IDataCatalogRest {
   
    @Autowired
    private IDataCatalogService dataCatalogService;
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCatalogResource()
     */
    @Override
    @RequestMapping("/dataCatalog")
    public List<ImageSet> getDataCatalogResource() {
        List<ImageSet> imageSet;
        try {
            imageSet = dataCatalogService.getImageSet();
            return imageSet;
        } catch ( Exception e ) {
             e.printStackTrace();
        }
    return null;
    }
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#testDataCatalogResource()
     */
    @Override
    @RequestMapping("/deepLearning")
    public String testDataCatalogResource() {
        return "Welcome to Deep Learning";
    }
    
/*    @Override
    @RequestMapping(value = "/dataCollection", method = RequestMethod.GET)
    public List<ImageSet> getDataCollection() {
        ResponseBuilder responseBuilder;
       // T entity = (T) daoApi.findById((Class<T>) getClassFromTableClassMap(tableName), id);
        List<ImageSet> imageSet = new ArrayList<ImageSet>();
        try {
            imageSet = dataCatalogService.getDataCatalog();           
        } catch ( Exception e ) {
             e.printStackTrace();
        }
        if (imageSet != null) {
            responseBuilder = Response.ok(imageSet);
            return (List<ImageSet>) responseBuilder.build().getEntity();
        } else {
            responseBuilder = Response.status(Status.NOT_FOUND);
            return (List<ImageSet>) responseBuilder.build();
        }
     }*/
    
    @RequestMapping(value = "/getDataCollection", method = RequestMethod.GET)
    public Response getDataCatalog() {
         try {
            Response response = null;
            List<ImageSet> imageSet = dataCatalogService.getImageSet();
            if ( (imageSet == null) || imageSet.isEmpty() ) {
                response = Response.status( Status.NO_CONTENT ).entity( "No image set  data found for the query" ).build();
            } 
            else {
                response = Response.status( Status.OK ).entity( imageSet ).build();
            }
  
            return response;
            
        } catch ( ServiceException e ) {
              throw new WebApplicationException( Response.status( Status.INTERNAL_SERVER_ERROR ).entity( "Operation failed for retrieving image set data" ).build() );
        } catch ( Exception e ) {
              throw new WebApplicationException( Response.status( Status.INTERNAL_SERVER_ERROR ).
                                               entity( "Operation failed for retrieving image set data " ).build() );
        }
    }
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()
     */
    @SuppressWarnings ( "unchecked" )
    @Override
    @RequestMapping(value = "/imageSet", method = RequestMethod.GET)
    public List<ImageSet> getImageSet() {
         ResponseBuilder responseBuilder;
         List<ImageSet> imageSet = new ArrayList<ImageSet>();
         try {
             imageSet = dataCatalogService.getImageSet();           
         } catch ( Exception e ) {
              e.printStackTrace();
         }
         if (imageSet != null) {
             responseBuilder = Response.ok(imageSet);
             return (List<ImageSet>) responseBuilder.build().getEntity();
         } else {
             responseBuilder = Response.status(Status.NOT_FOUND);
             return (List<ImageSet>) responseBuilder.build();
         }
    }
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getImageSetByDataCollectionId()
     */
    @SuppressWarnings ( "unchecked" )
    @Override
    @RequestMapping(value = "/imageSetByDataCollectionId", method = RequestMethod.GET)
    public List<ImageSet> getImageSetByDataCollectionId(String id) {
         ResponseBuilder responseBuilder;
         List<ImageSet> imageSet = new ArrayList<ImageSet>();
         try {
             String[] imgSetId = dataCatalogService.getImgSetIdForDC( id );
             if(null!=imgSetId && imgSetId.length>0){
                 System.out.println(" In getImageSetByDataCollectionId, imgSetId.length = " + imgSetId.length);
             }else{
                 System.out.println(" In getImageSetByDataCollectionId imgSetId is null");
             }
             imageSet = dataCatalogService.getImageSet();           
         } catch ( Exception e ) {
              e.printStackTrace();
         }
         if (imageSet != null) {
             responseBuilder = Response.ok(imageSet);
             return (List<ImageSet>) responseBuilder.build().getEntity();
         } else {
             responseBuilder = Response.status(Status.NOT_FOUND);
             return (List<ImageSet>) responseBuilder.build();
         }
    }
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()
     */
    @SuppressWarnings ( "unchecked" )
    @Override
    @RequestMapping(value = "/dataCollection", method = RequestMethod.GET)
    public List<DataCollection> getDataCollection() {
        ResponseBuilder responseBuilder;
         List<DataCollection> dataCollection = new ArrayList<DataCollection>();
         try {
             dataCollection = dataCatalogService.getDataCollection();           
         } catch ( Exception e ) {
              e.printStackTrace();
         }
         if (dataCollection != null) {
             responseBuilder = Response.ok(dataCollection);
             return (List<DataCollection>) responseBuilder.build().getEntity();
         } else {
             responseBuilder = Response.status(Status.NOT_FOUND);
             return (List<DataCollection>) responseBuilder.build();
         }
    }
}
