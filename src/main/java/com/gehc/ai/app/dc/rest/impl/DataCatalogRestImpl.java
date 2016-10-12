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

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.gehc.ai.app.common.responsegenerator.ResponseGenerator;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.gehc.ai.app.dc.entity.AnnotationSet;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.rest.IDataCatalogRest;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping(value = "/api/v1/dataCatalog")
public class DataCatalogRestImpl implements IDataCatalogRest {

    private static final String SUCCESS = "SUCCESS";
    public static final String ORG_ID = "orgId";
    public static final String MODALITY = "modality";
    public static final String ANATOMY = "anatomy";

    @Autowired
	private IDataCatalogService dataCatalogService;

    @Autowired
    private ResponseGenerator responseGenerator;


	/*
     * (non-Javadoc)
     *
     * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/image-set", method = RequestMethod.GET)
	public List<ImageSet> getImgSet(@RequestParam Map<String, String> params) {

        Map<String,String> validParams = constructValidParams(params, Arrays.asList(ORG_ID, MODALITY, ANATOMY));
        String orgId = params.get(ORG_ID);
		ResponseBuilder responseBuilder;
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		try {

			imageSet = dataCatalogService.getImgSet(validParams);
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while filtering image set data")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while filtering image set data")
							.build());
		}
		if (imageSet != null) {
			responseBuilder = Response.ok(imageSet);
			return (List<ImageSet>) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List<ImageSet>) responseBuilder.build();
		}
	}

    Map<String,String> constructValidParams(Map<String, String> params, List<String> allowedParams) {
        Map<String,String> validParams = new HashMap<>();
        for (String key : allowedParams) {
            if (params.containsKey(key)) {
                String value = params.get(key);
                if (!value.isEmpty()) {
                    validParams.put(key, value);
                }
            }
        }

        return validParams;
     }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gehc.ai.app.dc.rest.IDataCatalogRest#getImageSetByDataCollectionId()
     */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/imgSetByDataCollectionId", method = RequestMethod.GET)
	public List<ImageSet> getImgSetByDataCollId(
			@QueryParam("dataCollectionId") String dataCollectionId) {
		ResponseBuilder responseBuilder;
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		try {
				imageSet = dataCatalogService.getImgSetByDataCollId(dataCollectionId);
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving image set by data collection id")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving image set by data collection id")
							.build());
		}
		if (imageSet != null) {
			responseBuilder = Response.ok(imageSet);
			return (List<ImageSet>) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List<ImageSet>) responseBuilder.build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCollection", method = RequestMethod.GET)
	public List<DataCollection> getDataCollection() {
		ResponseBuilder responseBuilder;
		List<DataCollection> dataCollection = new ArrayList<DataCollection>();
		try {
			dataCollection = dataCatalogService.getDataCollection();
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving the data collection")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving the data collection")
							.build());
		}
		if (dataCollection != null) {
			responseBuilder = Response.ok(dataCollection);
			return (List<DataCollection>) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List<DataCollection>) responseBuilder.build();
		}
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/createDataCollection", method = RequestMethod.POST)
	public Response createDataCollection(
			@RequestBody DataCollection dataCollection) {
		Response response = null;
		String dcId;
		try {
			dcId = dataCatalogService
					.createDataCollection(dataCollection);
			if (StringUtils.isEmpty(dcId)) {
				response = Response.status(Status.NO_CONTENT)
						.entity("No data collection got created")
						.build();
			} else {
				response = ResponseGenerator.responseOnCreate(dcId);
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while creating the data collection")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while creating the data collection")
							.build());
		}
		return response;
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/image-set", method = RequestMethod.POST)
	public String insertImageSet(@RequestBody ImageSet imageSet) {
		ResponseBuilder responseBuilder;
		String imageSetId = null;
		try {
			imageSetId = dataCatalogService.insertImageSet(imageSet);
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while inserting the image set")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while inserting the image set")
							.build());
		}
		if (imageSetId != null) {
			responseBuilder = Response.ok(imageSetId);
			return (String) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return  responseBuilder.build().toString();
		}
	}

	@Override
	@RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
	public String healthCheck() {
		return SUCCESS;
	}
	
	
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/random-annotation-set", method = RequestMethod.POST)
	public Response insertRandomAnnotationSet(@RequestBody String blob) {
		//System.out.println("blob = " + blob);
		Response response = null;
		int numOfRowsInserted = 0;
		try {
			numOfRowsInserted = dataCatalogService.insertAnnotationSet(AnnotationSet.getJson(AnnotationSet.createRandom()));
			if (0 == numOfRowsInserted) {
				response = Response.status(Status.NO_CONTENT)
						.entity("No image set got inserted")
						.build();
			} else {
				response = Response.status(Status.OK).entity(numOfRowsInserted)
						.build();
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while inserting the image set")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while inserting the image set")
							.build());
		}
		//return response;
		return null;
	}
	
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/annotation-set", method = RequestMethod.POST)
	public Response insertAnnotationSet(@RequestBody String annotationSetJson) {
		Response response = null;
		int numOfRowsInserted = 0;
		try {
			numOfRowsInserted = dataCatalogService.insertAnnotationSet(annotationSetJson);
			if (0 == numOfRowsInserted) {
				response = Response.status(Status.NO_CONTENT)
						.entity("No image set got inserted")
						.build();
			} else {
				response = Response.status(Status.OK).entity(numOfRowsInserted)
						.build();
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while inserting the image set")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while inserting the image set")
							.build());
		}
		//return response;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/annotation-set", method = RequestMethod.GET)
	public List getAnnotationSet(@RequestParam Map<String, String> queryMap) {
		ResponseBuilder responseBuilder;
		List l = new ArrayList();
		try {
			String fields = queryMap.remove("fields");
			l = dataCatalogService.getAnnotationSet(null, fields, queryMap);
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving the data collection")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving the data collection")
							.build());
		}
		if (l != null) {
			responseBuilder = Response.ok(l);
			return (List) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List) responseBuilder.build();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/annotation-set/{imageSetIds}", method = RequestMethod.GET)
	public List getAnnotationSet(@PathVariable String imageSetIds, @QueryParam("fields") String fields) {
		ResponseBuilder responseBuilder;
		List l = new ArrayList();
		try {
			l = dataCatalogService.getAnnotationSet(imageSetIds, fields, null);
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving the data collection")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving the data collection")
							.build());
		}
		if (l != null) {
			responseBuilder = Response.ok(l);
			return (List) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List) responseBuilder.build();
		}
	}

}
