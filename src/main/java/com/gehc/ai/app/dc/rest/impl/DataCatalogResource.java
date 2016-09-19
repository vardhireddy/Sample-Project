/*
 * DataCatalogResource.java
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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */
@Service
@Configuration
@Component
public class DataCatalogResource {

    @Autowired
    private IDataCatalogService dataCatalogService;
    
    @GET
    @Produces ( MediaType.APPLICATION_JSON )
    public Response getDataCatalog() {
        System.out.println(" In DataCatalogResource");
        try {
            Response response = null;
            List<ImageSet> imageSet = dataCatalogService.getDataCatalog();
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
}
