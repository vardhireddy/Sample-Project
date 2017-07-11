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
package com.gehc.ai.app.datacatalog.rest.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gehc.ai.app.common.constants.ApplicationConstants;
//import com.gehc.ai.app.common.constants.ApplicationConstants;
import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.common.responsegenerator.ResponseGenerator;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationImgSetDataCol;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataCollection;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.ImageSet;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.entity.TargetData;
import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
//import COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.IDataCatalogRest;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;

/**
 * @author 212071558
 */
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping(value = "/api/v1")
@PropertySource({ "classpath:application.yml" })
public class DataCatalogRestImpl implements IDataCatalogRest {
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(DataCatalogRestImpl.class);

	public static final String ORG_ID = "orgId";
	public static final String MODALITY = "modality";
	public static final String ANATOMY = "anatomy";
	public static final String ANNOTATIONS = "annotations";
	public static final String SERIES_INS_UID = "series_instance_uid";
	public static final String ID = "im.id";
	public static final String ABSENT = "absent";
	// TODO: Implememnt to get img set by ID
	@Autowired
	private IDataCatalogService dataCatalogService;

	@Autowired
	private ResponseGenerator responseGenerator;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${uom.user.me.url}")
	private String uomMeUrl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getDataCollection()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/image-set", method = RequestMethod.GET)
	public List<ImageSet> getImgSet(@RequestParam Map<String, String> params, HttpServletRequest request) {
		logger.info("!!! In REST getImgSet, orgId = " + request.getAttribute("orgId"));
		Map<String, String> validParams = constructValidParams(params,
				Arrays.asList(ORG_ID, MODALITY, ANATOMY, ANNOTATIONS, SERIES_INS_UID, ID));
		ResponseBuilder responseBuilder;
		// List of img set based on filter criteria other than annotation
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		// List of img set which annotation
		List<ImageSet> imgSetWithAnnotation = new ArrayList<ImageSet>();
		// List of img set which does not have annotation of particular type
		List<ImageSet> imgSetWithNotThatTypeOfAnn = new ArrayList<ImageSet>();
		// List of final img set which has no annotation + img set of particular
		// type
		List<ImageSet> imageSetFinalLst = new ArrayList<ImageSet>();
		boolean dataWithNoAnnNeeded = false;
		try {
			if (null != request.getAttribute("orgId")) {
				imageSet = dataCatalogService.getImgSet(validParams, request.getAttribute("orgId").toString());
			} else {
				imageSet = dataCatalogService.getImgSet(validParams, null);
			}
			// Get Annotation
			if (params.containsKey(ANNOTATIONS)) {
				logger.info("!!! Params has annotations ");
				String values = params.get(ANNOTATIONS);
				if (null != imageSet && !imageSet.isEmpty()) {
					logger.info("!!! Img set is not null " + imageSet.size());
					List<String> imgSetIds = new ArrayList<String>();
					for (Iterator<ImageSet> imgSetItr = imageSet.iterator(); imgSetItr.hasNext();) {
						ImageSet imgSet = (ImageSet) imgSetItr.next();
						imgSetIds.add(imgSet.getId());
					}
					List<String> typeLst = getAnnTypesLst(values);
					if (typeLst.contains(ABSENT))
						dataWithNoAnnNeeded = true;
					if (null != typeLst && !typeLst.isEmpty()) {
						if (typeLst.size() == 1 && typeLst.contains(ABSENT)) {
							logger.info("*** Annotation filter only has absent type = " + typeLst.toString());
							imgSetWithNotThatTypeOfAnn.addAll(imageSet);
						} else {
							logger.info("*** Annotation filter types = " + typeLst.toString());
							List<Annotation> annotationLst = annotationRepository.findByImageSetInAndTypeIn(imgSetIds,
									typeLst);
							Set<String> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
							if (null != uniqueImgSetIds && !uniqueImgSetIds.isEmpty()) {
								logger.info("*** uniqueImgSetIds which has annotations " + uniqueImgSetIds.toString());
								for (Iterator<ImageSet> imgSetItr = imageSet.iterator(); imgSetItr.hasNext();) {
									ImageSet imgSet = (ImageSet) imgSetItr.next();
									logger.info(
											"*** Check if this img set has annotation of that type " + imgSet.getId());
									if (uniqueImgSetIds.contains(imgSet.getId())) {
										logger.info("*** Found imgset which satisfied all criterias " + imgSet.getId());
										imgSetWithAnnotation.add(imgSet);
									} else if (dataWithNoAnnNeeded) {
										logger.info(
												"*** Found imgset which does not have that particular annotation type "
														+ imgSet.getId());
										imgSetWithNotThatTypeOfAnn.add(imgSet);
									}
								}
							} else {
								logger.info(
										"*** Found no imgset which has that particular annotation type so returning nothing");
								return imgSetWithNotThatTypeOfAnn;
							}
						}
					}
					if (dataWithNoAnnNeeded && null != imgSetWithNotThatTypeOfAnn
							&& !imgSetWithNotThatTypeOfAnn.isEmpty()) {
						if (null != imgSetWithAnnotation && !imgSetWithAnnotation.isEmpty()) {
							logger.info("*** Img set which has annotation, imgSetWithAnnotation.toString() = "
									+ imgSetWithAnnotation.toString());
							imageSetFinalLst.addAll(imgSetWithAnnotation);
						}
						for (Iterator<ImageSet> imgSetItr = imgSetWithNotThatTypeOfAnn.iterator(); imgSetItr
								.hasNext();) {
							ImageSet imgSet = (ImageSet) imgSetItr.next();
							List<Annotation> annotationLst = annotationRepository.findByImageSet(imgSet.getId());
							if (null != annotationLst && !annotationLst.isEmpty()) {
								logger.info(
										"*** Found imgset which has annotation of other type so remove it from the list "
												+ imgSet.getId());
							} else {
								logger.info("*** Found imgset which has no annotation so adding to the list "
										+ imgSet.getId());
								imageSetFinalLst.add(imgSet);
							}
						}
						if (null != imageSetFinalLst && !imageSetFinalLst.isEmpty()) {
							logger.info("*** imageSetFinalLst.toString() " + imageSetFinalLst.toString());
							responseBuilder = Response.ok(imageSetFinalLst);
							return (List<ImageSet>) responseBuilder.build().getEntity();
						} else {
							return imageSet;
						}
					} else if (null != imgSetWithAnnotation && !imgSetWithAnnotation.isEmpty()) {
						logger.info("*** imgSetWithAnnotation.toString() " + imgSetWithAnnotation.toString());
						responseBuilder = Response.ok(imgSetWithAnnotation);
						return (List<ImageSet>) responseBuilder.build().getEntity();
					} else {
						return imageSet;
					}
				} else {
					// Do we need to get img set for annotation only? As per my
					// conversation with Aruna, we will have at least filter by
					// org id so no need of below logic
					/*
					 * List<String> typeLst = getAnnTypesLst(values);
					 * List<Annotation> annotationLst =
					 * annotationRepository.findByTypeIn( typeLst ); Set<String>
					 * uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
					 * if(null != uniqueImgSetIds && uniqueImgSetIds.size()>0){
					 * logger.info( "*** uniqueImgSetIds which has annotations "
					 * + uniqueImgSetIds.toString() ); }
					 */
					return imageSet;
				}
			} else if (imageSet != null && !imageSet.isEmpty()) {
				logger.info("!!! No annotations params ");
				responseBuilder = Response.ok(imageSet);
				return (List<ImageSet>) responseBuilder.build().getEntity();
			} else {
				return imageSet;
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while filtering image set data").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while filtering image set data").build());
		}
	}

	private List<String> getAnnTypesLst(String values) {
		List<String> typeLst = new ArrayList<String>();
		if (null != values && !values.isEmpty()) {
			String[] typeStrings = values.split(",");
			if (null != typeStrings && typeStrings.length > 0) {
				for (int i = 0; i < typeStrings.length; i++)
					typeLst.add(typeStrings[i].toLowerCase());
			}
		}
		return typeLst;
	}

	private Set<String> getUniqueImgSetIds(List<Annotation> annotationLst) {
		Set<String> uniqueImgSetIds = new HashSet<String>();
		if (null != annotationLst && !annotationLst.isEmpty()) {
			logger.info("*** Annotations from list " + annotationLst.toString());
			for (Iterator<Annotation> annotationItr = annotationLst.iterator(); annotationItr.hasNext();) {
				Annotation annotation = (Annotation) annotationItr.next();
				uniqueImgSetIds.add(annotation.getImageSet());
			}
		}
		return uniqueImgSetIds;
	}

	Map<String, String> constructValidParams(Map<String, String> params, List<String> allowedParams) {
		Map<String, String> validParams = new HashMap<>();
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
	 * @see IDataCatalogRest#getImageSetByDataCollectionId()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/imgSetByDataCollectionId", method = RequestMethod.GET)
	public List<ImageSet> getImgSetByDataCollId(@QueryParam("dataCollectionId") String dataCollectionId,
			HttpServletRequest request) {
		logger.info("!!! In REST getImgSetByDataCollId, orgId = " + request.getAttribute("orgId"));
		ResponseBuilder responseBuilder;
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		try {
			if (null != request.getAttribute("orgId")) {
				imageSet = dataCatalogService.getImgSetByDataCollId(dataCollectionId,
						request.getAttribute("orgId").toString());
			} else {
				imageSet = dataCatalogService.getImgSetByDataCollId(dataCollectionId, null);
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving image set by data collection id").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving image set by data collection id").build());
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
	 * @see IDataCatalogRest#getDataCollection()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/dataCollection", method = RequestMethod.GET)
	public List<DataCollection> getDataCollection(@QueryParam("id") String id, @QueryParam("type") String type,
			HttpServletRequest request) {
		logger.info("!!! In REST getDataCollection, orgId = " + request.getAttribute("orgId"));
		ResponseBuilder responseBuilder;
		List<DataCollection> dataCollection = new ArrayList<DataCollection>();
		try {
			if (null != request.getAttribute("orgId")) {
				dataCollection = dataCatalogService.getDataCollection(id, type,
						request.getAttribute("orgId").toString());
			} else {
				dataCollection = dataCatalogService.getDataCollection(id, type, null);
				// dataCollection = dataCatalogService.getDataCollection( id,
				// type, "61939267-d195-499f-bfd8-7d92875c7035" );
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving the data collection").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving the data collection").build());
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
	@RequestMapping(value = "/dataCatalog/createDataCollection", method = RequestMethod.POST)
	public Response createDataCollection(@RequestBody DataCollection dataCollection, HttpServletRequest request) {
		logger.info("%%% In REST  create DataCollection, orgId = " + request.getAttribute("orgId"));
		Response response = null;
		String dcId;
		try {
			if (null != request.getAttribute("orgId")) {
				dcId = dataCatalogService.createDataCollection(dataCollection,
						request.getAttribute("orgId").toString());
			} else {
				dcId = dataCatalogService.createDataCollection(dataCollection, null);
			}
			if (StringUtils.isEmpty(dcId)) {
				response = Response.status(Status.NO_CONTENT).entity("No data collection got created").build();
			} else {
				response = ResponseGenerator.responseOnCreate(dcId);
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while creating the data collection").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while creating the data collection").build());
		}
		return response;
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/dataCatalog/image-set", method = RequestMethod.POST)
	public String insertImageSet(@RequestBody ImageSet imageSet, HttpServletRequest request) {
		logger.info("%%% In REST  insertImageSet, orgId = " + request.getAttribute("orgId"));
		ResponseBuilder responseBuilder;
		String imageSetId = null;
		try {
			if (null != request.getAttribute("orgId")) {
				imageSetId = dataCatalogService.insertImageSet(imageSet, request.getAttribute("orgId").toString());
			} else {
				logger.info("DCRI.insertImageSet orgId is not available");
				imageSetId = dataCatalogService.insertImageSet(imageSet, null);
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while inserting the image set").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while inserting the image set").build());
		}
		if (imageSetId != null) {
			responseBuilder = Response.ok(imageSetId);
			return (String) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return responseBuilder.build().toString();
		}
	}

	@Override
	@RequestMapping(value = "/dataCatalog/healthCheck", method = RequestMethod.GET)
	public String healthCheck() {
		return ApplicationConstants.SUCCESS;
	}

	// TODO: Remove the comment code below if it's not being used
	/*
	 * @Override
	 * 
	 * @Consumes ( MediaType.APPLICATION_JSON )
	 * 
	 * @Produces ( MediaType.APPLICATION_JSON )
	 * 
	 * @RequestMapping ( value = "/dataCatalog/random-annotation-set", method =
	 * RequestMethod.POST ) public Response
	 * insertRandomAnnotationSet( @RequestBody String blob ) {
	 * 
	 * Response response = null; int numOfRowsInserted = 0; try {
	 * numOfRowsInserted = dataCatalogService.insertAnnotationSet(
	 * AnnotationSet.createRandom() ); if ( 0 == numOfRowsInserted ) { response
	 * = Response.status( Status.NO_CONTENT ).entity(
	 * "No image set got inserted" ).build(); } else { response =
	 * Response.status( Status.OK ).entity( numOfRowsInserted ).build(); } }
	 * catch ( ServiceException e ) { throw new WebApplicationException(
	 * Response.status( Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while inserting the image set" ).build() ); } catch (
	 * Exception e ) { throw new WebApplicationException( Response.status(
	 * Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while inserting the image set" ).build() ); } // return
	 * response; return null; }
	 */

	// TODO: Remove the comment code below if it's not being used
	/*
	 * @Override
	 * 
	 * @Consumes ( MediaType.APPLICATION_JSON )
	 * 
	 * @Produces ( MediaType.APPLICATION_JSON )
	 * 
	 * @RequestMapping ( value = "/dataCatalog/annotation-set", method =
	 * RequestMethod.POST ) public Response insertAnnotationSet( @RequestBody
	 * AnnotationSet as ) { Response response = null; int numOfRowsInserted = 0;
	 * try { numOfRowsInserted = dataCatalogService.insertAnnotationSet( as );
	 * if ( 0 == numOfRowsInserted ) { response = Response.status(
	 * Status.NO_CONTENT ).entity( "No image set got inserted" ).build(); } else
	 * { response = Response.status( Status.OK ).entity( numOfRowsInserted
	 * ).build(); } } catch ( ServiceException e ) { throw new
	 * WebApplicationException( Response.status( Status.INTERNAL_SERVER_ERROR
	 * ).entity( "Operation failed while inserting the image set" ).build() ); }
	 * catch ( Exception e ) { throw new WebApplicationException(
	 * Response.status( Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while inserting the image set" ).build() ); } // return
	 * response; return null; }
	 */

	// TODO: Remove the comment code below if it's not being used
	/*
	 * @SuppressWarnings ( "unchecked" )
	 * 
	 * @Override
	 * 
	 * @RequestMapping ( value = "/dataCatalog/annotation-set", method =
	 * RequestMethod.GET ) public List getAnnotationSet( @RequestParam
	 * Map<String, String> queryMap ) { ResponseBuilder responseBuilder; List l
	 * = new ArrayList(); try { String fields = queryMap.remove( "fields" ); l =
	 * dataCatalogService.getAnnotationSet( null, fields, queryMap ); } catch (
	 * ServiceException e ) { throw new WebApplicationException(
	 * Response.status( Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while retrieving the data collection" ).build() ); }
	 * catch ( Exception e ) { throw new WebApplicationException(
	 * Response.status( Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while retrieving the data collection" ).build() ); } if
	 * ( l != null ) { responseBuilder = Response.ok( l ); return
	 * (List)responseBuilder.build().getEntity(); } else { responseBuilder =
	 * Response.status( Status.NOT_FOUND ); return
	 * (List)responseBuilder.build(); } }
	 */
	// TODO: Remove the comment code below if it's not being used
	/*
	 * @SuppressWarnings ( "unchecked" )
	 * 
	 * @Override
	 * 
	 * @RequestMapping ( value = "/dataCatalog/annotation-set/{imageSetIds}",
	 * method = RequestMethod.GET ) public List getAnnotationSet( @PathVariable
	 * String imageSetIds, @QueryParam ( "fields" ) String fields ) {
	 * ResponseBuilder responseBuilder; List l = new ArrayList(); try { l =
	 * dataCatalogService.getAnnotationSet( imageSetIds, fields, null ); } catch
	 * ( ServiceException e ) { throw new WebApplicationException(
	 * Response.status( Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while retrieving the data collection" ).build() ); }
	 * catch ( Exception e ) { throw new WebApplicationException(
	 * Response.status( Status.INTERNAL_SERVER_ERROR ).entity(
	 * "Operation failed while retrieving the data collection" ).build() ); } if
	 * ( l != null ) { responseBuilder = Response.ok( l ); return
	 * (List)responseBuilder.build().getEntity(); } else { responseBuilder =
	 * Response.status( Status.NOT_FOUND ); return
	 * (List)responseBuilder.build(); } }
	 */

	@Value("${experiment.targetData.gtMaskLocation}")
	private String gtMaskLocation;

	@Value("${experiment.targetData.imgLocation}")
	private String imgLocation;

	@Value("${experiment.targetData.locationType}")
	private String locationType;

	/**
	 * @deprecated
	 * @param id
	 * @param type
	 * @param request
	 * @return hashmap
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@RequestMapping(value = "/dataCatalog/data-collection-target", method = RequestMethod.GET)
	public Map getExperimentTargetData(@QueryParam("id") String id, @QueryParam("type") String type,
			HttpServletRequest request) {
		logger.info("!!! *** In REST getExperimentTargetData, orgId = " + request.getAttribute("orgId"));
		logger.info("Entering method getExperimentTargetData --> id: " + id);
		Map tdmap = new HashMap();
		List<TargetData> l = new ArrayList<TargetData>();
		try {
			if (null != request.getAttribute("orgId")) {
				l = dataCatalogService.getExperimentTargetData(id, request.getAttribute("orgId").toString());
			} else {
				l = dataCatalogService.getExperimentTargetData(id, null);
			}
			logger.info("getExperimentTargetData service call return a list of size --> : " + l.size());

			if (!l.isEmpty()) {
				LinkedHashMap fileMap = new LinkedHashMap();
				for (int i = 0; i < l.size(); i++) {
					TargetData td = l.get(i);
					HashMap hm = new HashMap();
					// String imgFullPath = td.img;
					if (null != td.gtMask && !td.gtMask.isEmpty()) {
						hm.put("gtMask", td.gtMask.startsWith(gtMaskLocation)
								? td.gtMask.substring(gtMaskLocation.length()) : td.gtMask);
					}
					hm.put("img", td.img.startsWith(imgLocation) ? td.img.substring(imgLocation.length()) : td.img);
					fileMap.put(td.patientId, hm);
				}
				tdmap.put("files", fileMap);
				tdmap.put("locationType", locationType);
				tdmap.put("gtMaskLocation", gtMaskLocation);
				tdmap.put("imgLocation", imgLocation);
				logger.info(
						"getExperimentTargetData --> received data with annotations and converted them to targetData structure and this is the data hashmap --> : "
								+ tdmap.toString());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("getExperimentTargetData --> Converted targetData : " + tdmap.toString());
		return tdmap;
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/annotation-by-datacollectionid", method = RequestMethod.GET)
	public List getAnnotationByDataColId(@QueryParam("id") String id,
			@QueryParam("annotationType") String annotationType) {
		logger.info("*** Entering method getAnnotationByDataColId --> id: " + id);

		// TODO:Check if this API is being used or not
		if ((id == null) || (id.length() == 0)) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Datacollection id is required to get annotation for a data collection").build());
		}

		ResponseBuilder responseBuilder;
		List<AnnotationImgSetDataCol> l;
		try {
			l = dataCatalogService.getAnnotationByDataColId(id, annotationType);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving annotation for data collection").build());
		}
		if (l != null) {
			responseBuilder = Response.ok(l);
			return (List) responseBuilder.build().getEntity();
		}
		responseBuilder = Response.status(Status.NOT_FOUND);
		return (List) responseBuilder.build();

	}

	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private StudyRepository studyRepository;
	@Autowired
	private AnnotationRepository annotationRepository;
	@Autowired
	private COSNotificationRepository cosNotificationRepository;
	@Autowired
	private DataSetRepository dataSetRepository;
	@Autowired
	private AnnotationPropRepository annotationPropRepository;
	@Autowired
	private ImageSeriesRepository imageSeriesRepository;

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/patient", method = RequestMethod.GET)
	public List<Patient> getPatient(HttpServletRequest request) {
		logger.info("*** In REST get all Patients, orgId = " + request.getAttribute("orgId"));
		if (null != request.getAttribute("orgId")) {
			return patientRepository.findByOrgId(request.getAttribute("orgId").toString());
		} else {
			return patientRepository.findByOrgId(null);
		}
	}

	/*
	 * public List<Patient> getPatient( @RequestParam Map<String, String>
	 * queryMap ) { Set<String> keys = queryMap.keySet(); Patient p = new
	 * Patient(); for ( Iterator<String> it = keys.iterator(); it.hasNext(); ) {
	 * String key = it.next(); if ( "patientName".equalsIgnoreCase( key ) )
	 * p.setPatientName( queryMap.get( key ) ); else if (
	 * "patientId".equalsIgnoreCase( key ) ) p.setPatientId( queryMap.get( key )
	 * ); else if ( "birthdate".equalsIgnoreCase( key ) ) p.setBirthDate(
	 * queryMap.get( key ) ); else if ( "gender".equalsIgnoreCase( key ) )
	 * p.setGender( queryMap.get( key ) ); else if ( "age".equalsIgnoreCase( key
	 * ) ) p.setAge( queryMap.get( key ) ); else if ( "orgId".equalsIgnoreCase(
	 * key ) ) p.setOrgId( queryMap.get( key ) ); else if (
	 * "uploadBy".equalsIgnoreCase( key ) ) p.setUploadBy( queryMap.get( key )
	 * ); else if ( "uploadDate".equalsIgnoreCase( key ) ) { String dd =
	 * queryMap.get( key ); int y = Integer.valueOf( dd.substring( 0, 4 ) ) -
	 * 1900; int m = Integer.valueOf( dd.substring( 4, 6 ) ) - 1; int day =
	 * Integer.valueOf( dd.substring( 6 ) ); p.setUploadDate( new Date( y, m,
	 * day ) ); } } return patientRepository.findAll( Example.of( p ) ); }
	 */

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/patient/{ids}", method = RequestMethod.GET)
	public List<Patient> getPatients(@PathVariable String ids, HttpServletRequest request) {
		logger.info("*** In REST getPatients, orgId = " + request.getAttribute("orgId"));
		List<Long> pids = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length; i++)
			pids.add(Long.valueOf(idStrings[i]));
		// return patientRepository.findByIdIn( pids );
		if (null != request.getAttribute("orgId")) {
			return patientRepository.findByIdInAndOrgId(pids, request.getAttribute("orgId").toString());
		} else {
			return patientRepository.findByIdInAndOrgId(pids, null);
		}
	}

	@Override
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.GET)
	public List<Study> getStudy(HttpServletRequest request) {
		logger.info("*** In REST get all syudy, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? studyRepository.findByOrgId(null)
				: studyRepository.findByOrgId(request.getAttribute("orgId").toString());
	}
	/*
	 * public List<Study> getStudy( @RequestParam Map<String, String> queryMap )
	 * { Set<String> keys = queryMap.keySet(); Study s = new Study(); String
	 * sortCol = null; String sortdir = null; for ( Iterator<String> it =
	 * keys.iterator(); it.hasNext(); ) { String key = it.next(); if (
	 * "studyInstanceUid".equalsIgnoreCase( key ) ) s.setStudyInstanceUid(
	 * queryMap.get( key ) ); else if ( "studyDate".equalsIgnoreCase( key ) )
	 * s.setStudyDate( queryMap.get( key ) ); else if (
	 * "studyTime".equalsIgnoreCase( key ) ) s.setStudyTime( queryMap.get( key )
	 * ); else if ( "studyId".equalsIgnoreCase( key ) ) s.setStudyId(
	 * queryMap.get( key ) ); else if ( "patientDbId".equalsIgnoreCase( key ) )
	 * s.setPatientDbId( Long.valueOf( queryMap.get( key ) ) ); else if (
	 * "studyDescription".equalsIgnoreCase( key ) ) s.setStudyDescription(
	 * queryMap.get( key ) ); else if ( "referringPhysician".equalsIgnoreCase(
	 * key ) ) s.setReferringPhysician( queryMap.get( key ) ); else if (
	 * "orgId".equalsIgnoreCase( key ) ) s.setOrgId( queryMap.get( key ) ); else
	 * if ( "studyUrl".equalsIgnoreCase( key ) ) s.setStudyUrl( queryMap.get(
	 * key ) ); else if ( "uploadBy".equalsIgnoreCase( key ) ) s.setUploadBy(
	 * queryMap.get( key ) ); else if ( "uploadDate".equalsIgnoreCase( key ) ) {
	 * String dd = queryMap.get( key ); int y = Integer.valueOf( dd.substring(
	 * 0, 4 ) ) - 1900; int m = Integer.valueOf( dd.substring( 4, 6 ) ) - 1; int
	 * day = Integer.valueOf( dd.substring( 6 ) ); s.setUploadDate( new Date( y,
	 * m, day ) ); } else if ( key.startsWith( "_sort" ) ) { sortCol =
	 * queryMap.get( key ); sortdir = key.split( ":" )[1]; }
	 * 
	 * } return sortCol == null ? studyRepository.findAll( Example.of( s ) ) :
	 * studyRepository.findAll( Example.of( s ), new Sort( Direction.fromString(
	 * sortdir ), sortCol ) ); }
	 */

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/study/{ids}", method = RequestMethod.GET)
	public List<Study> getStudiesById(@PathVariable String ids, HttpServletRequest request) {
		logger.info("*** In REST getStudiesById, orgId = " + request.getAttribute("orgId"));
		List<Long> pids = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length; i++)
			pids.add(Long.valueOf(idStrings[i]));
		// return studyRepository.findByIdIn(pids);
		if (null != request.getAttribute("orgId")) {
			return studyRepository.findByIdInAndOrgId(pids, request.getAttribute("orgId").toString());
		} else {
			return studyRepository.findByIdInAndOrgId(pids, null);
		}

	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/patient", method = RequestMethod.POST)
	public Patient postPatient(@RequestBody Patient p) {
		p.setUploadDate(new Date(System.currentTimeMillis()));
		patientRepository.save(p);
		return p;
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.POST)
	public Study postStudy(@RequestBody Study s) {
		s.setUploadDate(new Date(System.currentTimeMillis()));
		studyRepository.save(s);
		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/patient/{patientId}/study", method = RequestMethod.GET)
	public List<Study> getStudies(@PathVariable String patientId, HttpServletRequest request) {
		logger.info("*** In REST getStudies, orgId = " + request.getAttribute("orgId"));
		// return studyRepository.findByPatientDbId( Long.valueOf( patientId ));
		if (null != request.getAttribute("orgId")) {
			return studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(patientId),
					request.getAttribute("orgId").toString());
		} else {
			return studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(patientId), null);
		}
	}

	//TODO:Delete below as other API will support this
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/dataCatalog/study/{studyId}/image-set", method = RequestMethod.GET)
	public List<ImageSet> getImageSetByStudyId(@PathVariable String studyId, HttpServletRequest request) {
		logger.info("*** In REST getImageSetByStudyId, orgId = " + request.getAttribute("orgId"));
		ResponseBuilder responseBuilder;
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		try {
			// imageSet = dataCatalogService.getImageSetByStudyId( studyId,
			// null);
			if (null != request.getAttribute("orgId")) {
				imageSet = dataCatalogService.getImageSetByStudyId(studyId, request.getAttribute("orgId").toString());
			} else {
				imageSet = dataCatalogService.getImageSetByStudyId(studyId, null);
			}
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while filtering image set data").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while filtering image set data").build());
		}
		if (imageSet != null) {
			responseBuilder = Response.ok(imageSet);
			return (List<ImageSet>) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List<ImageSet>) responseBuilder.build();
		}
	}

	@Override
	@RequestMapping(value = "/annotation", method = RequestMethod.GET)
	public List<Annotation> getAnnotationsByImgSet(@QueryParam("imagesetid") String imagesetid,
			HttpServletRequest request) {
		logger.info("*** In REST getAnnotationsByImgSet, orgId = " + request.getAttribute("orgId"));
		// Commented out org id as get annotation by id is not part of
		// interceptor as C2M is using it
		if (null != imagesetid && !imagesetid.isEmpty()) {
			return annotationRepository.findByImageSet(imagesetid);
		} else {
			return new ArrayList<Annotation>();
		}
		/*
		 * if ( null != imagesetid && !imagesetid.isEmpty() && null !=
		 * request.getAttribute( "orgId" )) { return
		 * annotationRepository.findByImageSetAndOrgId(imagesetid,
		 * request.getAttribute( "orgId" ).toString()); }else { return
		 * annotationRepository.findByImageSetAndOrgId(imagesetid, null); }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#saveAnnotation(com.gehc.ai.app.
	 * dc.entity.Annotation)
	 */
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/annotation", method = RequestMethod.POST)
	public ApiResponse saveAnnotation(@RequestBody Annotation annotation) {
		ApiResponse apiResponse = null;
		try {
			Annotation newAnnotation = annotationRepository.save(annotation);
			apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
					ApplicationConstants.SUCCESS, newAnnotation.getId().toString());
		} catch (Exception e) {
			logger.error("Exception occured while calling save annotation ", e);
			apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.INTERNAL_SERVER_ERROR_CODE,
					ApplicationConstants.FAILURE, null);
		}
		return apiResponse;
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/dataCatalog/cos-notification", method = RequestMethod.POST)
	public void postCOSNotification(@RequestBody CosNotification n) {
		cosNotificationRepository.save(n);
	}

	@Override
	@RequestMapping(value = "/annotation/{ids}", method = RequestMethod.DELETE)
	public ApiResponse deleteAnnotation(@PathVariable String ids, HttpServletRequest request) {
		logger.info("+++ !!! In REST deleteAnnotation, orgId = " + request.getAttribute("orgId"));
		ApiResponse apiResponse = null;
		Annotation ann = new Annotation();
		try {
			if (null != ids && ids.length() > 0) {
				String[] idStrings = ids.split(",");
				if (null != idStrings && idStrings.length > 0) {
					for (int i = 0; i < idStrings.length; i++) {
						ann.setId(Long.valueOf(idStrings[i]));
						if (null != request.getAttribute("orgId")) {
							ann.setOrgId(request.getAttribute("orgId").toString());
							annotationRepository.delete(ann);
							apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
									ApplicationConstants.SUCCESS, ids);
						} else {
							// annotationRepository.delete( Long.valueOf(
							// idStrings[i] ) );
							// apiResponse = new
							// ApiResponse(ApplicationConstants.FAILURE,
							// ApplicationConstants.BAD_REQUEST_CODE, "Org Id is
							// ", null);
							// commented above as org id will not be avaiable
							// for get annotation by ids as C2M is using it
							annotationRepository.delete(ann);
							apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
									ApplicationConstants.SUCCESS, ids);
						}
					}
				} else {
					apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.BAD_REQUEST_CODE,
							"Org Id does not exist", ids);
				}
			}

		} catch (Exception e) {
			logger.error("Exception occured while calling delete annotation ", e);
			apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.BAD_REQUEST_CODE,
					"Id does not exist", ids);
		}
		return apiResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getImageSetBypatientId(java.lang .String)
	 */
	@Override
	@RequestMapping(value = "/dataCatalog/image-set-by-patientid", method = RequestMethod.GET)
	public List<ImageSet> getImageSetByPatientId(@QueryParam("patientid") String patientid,
			HttpServletRequest request) {
		// logger.info( "+++ !!! In REST getImageSetByPatientId, orgId = " +
		// request.getAttribute( "orgId" ) );
		/*
		 * if(null != request.getAttribute( "orgId" )){ return
		 * dataCatalogService.getImageSetByPatientId( patientid,
		 * request.getAttribute( "orgId" ).toString() ); }else{ return
		 * dataCatalogService.getImageSetByPatientId( patientid, null ); }
		 */
		// commented above as this is alos being used from SNS Manager
		return dataCatalogService.getImageSetByPatientId(patientid, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#updateDataCollection(com.gehc.ai
	 * .app.dc.entity.DataCollection)
	 */
	@Override
	@RequestMapping(value = "/datacatalog", method = RequestMethod.PUT)
	public ApiResponse updateDataCollection(@RequestBody DataCollection dataCollection, HttpServletRequest request) {
		logger.info("+++ !!! In REST updateDataCollection, orgId = " + request.getAttribute("orgId"));
		ApiResponse apiResponse = null;
		try {
			logger.info(" *** In updateDataCollection rest");
			if (null != request.getAttribute("orgId")) {
				apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
						ApplicationConstants.SUCCESS, dataCatalogService.updateDataCollection(dataCollection,
								request.getAttribute("orgId").toString()));
			} else {
				apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
						ApplicationConstants.SUCCESS, dataCatalogService.updateDataCollection(dataCollection, null));
			}
		} catch (Exception e) {
			logger.error("Exception occured while updating the data collection ", e);
			apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.INTERNAL_SERVER_ERROR_CODE,
					"Failed to update the data collection ", dataCollection.getId());
		}
		return apiResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getAnnotationsById(java.lang. Long)
	 */
	@Override
	@RequestMapping(value = "/annotation/{ids}", method = RequestMethod.GET)
	public List<Annotation> getAnnotationsById(@PathVariable String ids, HttpServletRequest request) {
		if (null != ids && ids.length() > 0) {
			List<Long> idsLst = new ArrayList<Long>();
			String[] idStrings = ids.split(",");
			for (int i = 0; i < idStrings.length; i++)
				idsLst.add(Long.valueOf(idStrings[i]));
			return annotationRepository.findByIdIn(idsLst);
		} else {
			return new ArrayList<Annotation>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/annotation-properties", method = RequestMethod.GET)
	public List<AnnotationProperties> getAnnotationProperties(@QueryParam("orgId") String orgId) {
		logger.info("--- In getAnnotationProperties, orgId = " + orgId);
		ResponseBuilder responseBuilder;
		List<AnnotationProperties> annotationProperties = new ArrayList<AnnotationProperties>();
		try {
			// Get all the properties for that particular org and where org is
			// null
			annotationProperties = annotationPropRepository.findByOrgId(orgId);
			annotationProperties.addAll(annotationPropRepository.findByOrgId(null));
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving the annotation prop").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving the annotation prop").build());
		}
		if (annotationProperties != null) {
			responseBuilder = Response.ok(annotationProperties);
			return (List<AnnotationProperties>) responseBuilder.build().getEntity();
		} else {
			responseBuilder = Response.status(Status.NOT_FOUND);
			return (List<AnnotationProperties>) responseBuilder.build();
		}
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/annotation-properties", method = RequestMethod.POST)
	public ApiResponse saveAnnotationProperties(@RequestBody AnnotationProperties annotationProp) {
		logger.info("*** In saveAnnotationProperties, annotationProp = " + annotationProp);
		ApiResponse apiResponse = null;
		try {
			AnnotationProperties newAnnotationProp = annotationPropRepository.save(annotationProp);
			apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
					ApplicationConstants.SUCCESS, newAnnotationProp.getId().toString());
		} catch (Exception e) {
			logger.error("Exception occured while calling save AnnotationProperties ", e);
			apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.INTERNAL_SERVER_ERROR_CODE,
					ApplicationConstants.FAILURE, null);
		}
		return apiResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#saveDataSet(DataSet)
	 */
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/data-collection", method = RequestMethod.POST)
	public DataSet saveDataSet(@RequestBody DataSet d) {
		// TODO: Need to add interceptor
		dataSetRepository.save(d);
		return d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#saveImageSeries(ImageSeries)
	 */
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/image-series", method = RequestMethod.POST)
	public ImageSeries saveImageSeries(@RequestBody ImageSeries i) {
		// TODO: Need to add interceptor
		imageSeriesRepository.save(i);
		return i;
	}

	/*
	 * To get the image set based on the patient id like LIDC-IDRI-0286
	 * 
	 * @see IDataCatalogRest#getImgSerByPatientId(java.lang. String)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/patient/{ids}/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByPatientId(@PathVariable String ids) {
		logger.info("[Image Series] Get img series for patient id " + ids);
		List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
		// TODO:Use ManyToOne mapping between Image Series and Patient
		if (null != ids && ids.length() > 0) {
			List<Patient> patLst = patientRepository.findByPatientId(ids);
			if (null != patLst && patLst.size() > 0) {
				logger.info("*** Patient DB id " + ((Patient) (patLst.get(0))).getId());
				imgSerLst = imageSeriesRepository.findByPatientDbId(((Patient) (patLst.get(0))).getId());
			}
		}
		return imgSerLst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getImgSerByDSId(java.lang.Long,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/data-collection/{id}/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByDSId(@PathVariable Long id, HttpServletRequest request) {
		// TODO: Need to add Interceptor
		logger.info("[Image Series] Get img series for DC id " + id);
		List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
		List<DataSet> dsLst = new ArrayList<DataSet>();
		if (null != id) {
			// Hard coding the org Id until interceptor has been added
			dsLst = dataSetRepository.findByIdAndOrgId(id, "61939267-d195-499f-bfd8-7d92875c7035");
			if (null != dsLst && dsLst.size() > 0) {
				@SuppressWarnings("unchecked")
				List<Object> imgSeries = (ArrayList<Object>) ((DataSet) (dsLst.get(0))).getImageSets();
				if (null != imgSeries && imgSeries.size() > 0) {
					// TODO:Use findByIdIn to avoid the loop
					for (int i = 0; i < imgSeries.size(); i++) {
						Long imgSerId = Long.valueOf(imgSeries.get(i).toString());
						logger.info("*** Now get the img set by id = " + imgSerId);
						imgSerLst.addAll(imageSeriesRepository.findById(Long.valueOf(imgSeries.get(i).toString())));
					}
				}
			}
		}
		return imgSerLst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getDataSetById(java.lang.Long,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/data-collection/{id}", method = RequestMethod.GET)
	public List<DataSet> getDataSetById(@PathVariable Long id, HttpServletRequest request) {
		// TODO: Need to add Interceptor
		logger.info("Get DC for id " + id);
		// Hard coding the org Id until interceptor has been added
		return dataSetRepository.findByIdAndOrgId(id, "61939267-d195-499f-bfd8-7d92875c7035");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getDataSetById(java.lang.Long,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/data-collection", method = RequestMethod.GET)
	public List<DataSet> getAllDataSet(HttpServletRequest request) {
		// TODO: Need to add Interceptor
		// Hard coding the org Id until interceptor has been added
		return dataSetRepository.findByOrgId("61939267-d195-499f-bfd8-7d92875c7035");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getDataSetByType(java.lang.String,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/data-collection/type/{type}", method = RequestMethod.GET)
	public List<DataSet> getDataSetByType(@PathVariable String type, HttpServletRequest request) {
		logger.info("+++ !!! In REST getDataSetByType, orgId = " + request.getAttribute("orgId"));
		logger.info("Get DC for type " + type);
		if (null != request.getAttribute("orgId")) {
			return dataSetRepository.findByTypeAndOrgId(type, request.getAttribute("orgId").toString());
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IDataCatalogRest#getDataCollection()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/datacatalog/image-series", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeries(@RequestParam Map<String, String> params, HttpServletRequest request) {
		{
			// TODO: Add interceptor if needed as we are not using the org id
			// from the token but using the one which is being passed as a
			// parameter
			// as user can have access to more than 1 org in future
			Map<String, String> validParams = constructValidParams(params,
					Arrays.asList(ORG_ID, MODALITY, ANATOMY, ANNOTATIONS));
			// List of img set based on filter criteria other than annotation
			List<ImageSeries> imageSeriesLst;// = new ArrayList<ImageSeries>();
			// List of img set with annotation
			List<ImageSeries> imgSetWithAnnotation = new ArrayList<ImageSeries>();
			// List of img set which does not have annotation
			List<ImageSeries> imgSetWithOutAnn = new ArrayList<ImageSeries>();

			try {
				if (null != validParams) {
					if (validParams.containsKey(ORG_ID)) {
						List<String> orgIdLst = getListFromParams(validParams.get(ORG_ID));

						if (validParams.containsKey(MODALITY)) {
							List<String> modalityLst = getListFromParams(validParams.get(MODALITY));
							if (validParams.containsKey(ANATOMY)) {
								List<String> anatomyLst = getListFromParams(validParams.get(ANATOMY));
								imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(orgIdLst,
										anatomyLst, modalityLst);
							} else {
								imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityIn(orgIdLst,
										modalityLst);
							}
						} else if (validParams.containsKey(ANATOMY)) {
							List<String> anatomyLst = getListFromParams(validParams.get(ANATOMY));
							imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyIn(orgIdLst, anatomyLst);
						} else {
							imageSeriesLst = imageSeriesRepository.findByOrgIdIn(orgIdLst);
						}

						// Get the data with annotation filter
						if (validParams.containsKey(ANNOTATIONS)) {
							List<String> typeLst = getListFromParams(validParams.get(ANNOTATIONS));
							if (null != typeLst && !typeLst.isEmpty()) {
								List<Annotation> annotationLst = new ArrayList<Annotation>();
								// List of image series id based on criteria
								// other than annotation
								List<String> imgSeriesIdLst = new ArrayList<String>();
								for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr
										.hasNext();) {
									ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
									logger.info("Get imageSeries id " + imageSeries.getId());
									imgSeriesIdLst.add((imageSeries.getId()).toString());
								}
								if (typeLst.contains(ABSENT)) {
									annotationLst = annotationRepository.findByImageSetIn(imgSeriesIdLst);
									Set<String> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
									logger.info("# Get uniqueImgSetIds size() " + uniqueImgSetIds.size());
									if (null != uniqueImgSetIds && !uniqueImgSetIds.isEmpty()) {
										for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst
												.iterator(); imgSeriesItr.hasNext();) {
											ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
											logger.info("Get imageSeries id " + imageSeries.getId());
											if (!uniqueImgSetIds.contains((imageSeries.getId()).toString())) {
												imgSetWithOutAnn.add(imageSeries);
											}
										}
									}
								} else {
									annotationLst = annotationRepository.findByImageSetInAndTypeIn(imgSeriesIdLst,
											typeLst);
									Set<String> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
									logger.info("Get uniqueImgSetIds.size() " + uniqueImgSetIds.size());
									if (null != uniqueImgSetIds && !uniqueImgSetIds.isEmpty()) {
										for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst
												.iterator(); imgSeriesItr.hasNext();) {
											ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
											logger.info("Get imageSeries id " + imageSeries.getId());
											if (uniqueImgSetIds.contains((imageSeries.getId()).toString())) {
												imgSetWithAnnotation.add(imageSeries);
											}
										}
									}
								}
							}
						}

						if (null != imgSetWithAnnotation && imgSetWithAnnotation.size() > 0) {
							// Data with Annotation
							return getPatientForImgSeriesLst(imgSetWithAnnotation);
						} else if (null != imgSetWithOutAnn && imgSetWithOutAnn.size() > 0) {
							// Data with no Annotation
							return getPatientForImgSeriesLst(imgSetWithOutAnn);
						} else if (!validParams.containsKey(ANNOTATIONS)) { // DC
																			// without
																			// Annotation
																			// criteria
							return getPatientForImgSeriesLst(imageSeriesLst);
						}
					}
				}
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity("Operation failed while filtering image set data").build());
			}
			return new ArrayList<ImageSeries>();
		}
	}

	private List<String> getListFromParams(String values) {
		List<String> valueLst = new ArrayList<String>();
		if (null != values && !values.isEmpty()) {
			String[] valueStrings = values.split(",");
			if (null != valueStrings && valueStrings.length > 0) {
				for (int i = 0; i < valueStrings.length; i++)
					valueLst.add(valueStrings[i].toLowerCase());
			}
		}
		return valueLst;
	}

	private List<ImageSeries> getPatientForImgSeriesLst(List<ImageSeries> imageSeriesLst) {
		List<ImageSeries> imgSerWithPatientLst = new ArrayList<ImageSeries>();
		for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
			ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
			imageSeries.setPatient(patientRepository.findById(new Long(imageSeries.getPatientDbId())).get(0));
			logger.info("Get Patient Id in imageSeriesLst " + imageSeries.getPatient().getPatientId());
			imgSerWithPatientLst.add(imageSeries);
		}
		return imgSerWithPatientLst;
	}

	@Override
	@RequestMapping(value = "/datacatalog/image-series/series-instance-uid/{seriesInstanceUid}", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesBySeriesUId(@PathVariable String seriesInstanceUid) {
		return imageSeriesRepository.findBySeriesInstanceUid(seriesInstanceUid);
	}

	@Override
	@RequestMapping(value = "/datacatalog/image-series/id/{id}", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesById(@PathVariable Long id, HttpServletRequest request) {
		// TODO: Add interceptor
		// Hard coding the org Id until interceptor has been added
		return imageSeriesRepository.findByIdAndOrgId(id, "61939267-d195-499f-bfd8-7d92875c7035");
	}

	@Override
	@RequestMapping(value = "/datacatalog/image-series/study-dbid/{studyDbId}", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByStudyDbId(@PathVariable Long studyDbId, HttpServletRequest request) {
		// TODO: Add interceptor
		// Hard coding the org Id until interceptor has been added
		return imageSeriesRepository.findByStudyDbIdAndOrgId(studyDbId, "61939267-d195-499f-bfd8-7d92875c7035");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/datacatalog/study/patient/{patientDbid}", method = RequestMethod.GET)
	public List<Study> getStudiesByPatientDbid(@PathVariable String patientDbid, HttpServletRequest request) {
		logger.info("*** In REST getStudiesByPatientDbid, orgId = " + request.getAttribute("orgId"));
		if (null != request.getAttribute("orgId")) {
			return studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(patientDbid),
					request.getAttribute("orgId").toString());
		} else {
			return studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(patientDbid), null);
		}
	}
}
