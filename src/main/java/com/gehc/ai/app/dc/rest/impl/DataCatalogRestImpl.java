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

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping(value = "/dataCatalog")
public class DataCatalogRestImpl implements IDataCatalogRest {

	@Autowired
	private IDataCatalogService dataCatalogService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/v1.0/imgSetByOrgId", method = RequestMethod.GET)
	public List<ImageSet> getImgSetByOrgId(@QueryParam("orgId") String orgId) {
		ResponseBuilder responseBuilder;
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		try {
			imageSet = dataCatalogService.getImgSetByOrgId(orgId);
		} catch (ServiceException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving image set by org id")
							.build());
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity("Operation failed while retrieving image set by org id")
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
	 * @see
	 * com.gehc.ai.app.dc.rest.IDataCatalogRest#getImageSetByDataCollectionId()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/v1.0/imgSetByDataCollectionId", method = RequestMethod.GET)
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
	@RequestMapping(value = "/v1.0/dataCollection", method = RequestMethod.GET)
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
	@RequestMapping(value = "/v1.0/createDataCollection", method = RequestMethod.POST)
	public Response createDataCollection(
			@RequestBody DataCollection dataCollection) {
		Response response = null;
		int numOfRowsInserted = 0;
		try {
			numOfRowsInserted = dataCatalogService
					.createDataCollection(dataCollection);
			if (0 == numOfRowsInserted) {
				response = Response.status(Status.NO_CONTENT)
						.entity("No data collection got created")
						.build();
			} else {
				response = Response.status(Status.OK).entity(numOfRowsInserted)
						.build();
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
		//return response;
		return null;
	}
}
