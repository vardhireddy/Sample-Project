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

	public static final String ORG_ID = "org-id";
	public static final String MODALITY = "modality";
	public static final String ANATOMY = "anatomy";
	public static final String ANNOTATIONS = "annotations";
	public static final String SERIES_INS_UID = "series-instance-uid";
	public static final String ABSENT = "absent";

	@Autowired
	private IDataCatalogService dataCatalogService;

	@Autowired
	private ResponseGenerator responseGenerator;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${uom.user.me.url}")
	private String uomMeUrl;
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

	@Override
	@RequestMapping(value = "/datacatalog/healthcheck", method = RequestMethod.GET)
	public String healthCheck() {
		return ApplicationConstants.SUCCESS;
	}

	@Value("${experiment.targetData.gtMaskLocation}")
	private String gtMaskLocation;

	@Value("${experiment.targetData.imgLocation}")
	private String imgLocation;

	@Value("${experiment.targetData.locationType}")
	private String locationType;
//TODO: To change the dataCatalog to datacatalog and update experiment service
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

/*	@SuppressWarnings("unchecked")
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

	}*/

	@Override
	@RequestMapping(value = "/datacatalog/patient/{ids}", method = RequestMethod.GET)
	public List<Patient> getPatients(@PathVariable String ids, HttpServletRequest request) {
		logger.info("*** In REST getPatients, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Patient>()
				: patientRepository.findByIdInAndOrgId(getListOfLongsFromParams(ids),
						request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.GET)
	public List<Study> getStudy(HttpServletRequest request) {
		logger.info("*** In REST get all syudy, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Study>()
				: studyRepository.findByOrgId(request.getAttribute("orgId").toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/datacatalog/study/{ids}", method = RequestMethod.GET)
	public List<Study> getStudiesById(@PathVariable String ids, HttpServletRequest request) {
		logger.info("*** In REST getStudiesById, orgId = " + request.getAttribute("orgId"));
		List<Long> pids = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length; i++)
			pids.add(Long.valueOf(idStrings[i]));
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
		return patientRepository.save(p);
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.POST)
	public Study postStudy(@RequestBody Study s) {
		s.setUploadDate(new Date(System.currentTimeMillis()));
		return studyRepository.save(s);
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
	@RequestMapping(value = "/datacatalog/cos-notification", method = RequestMethod.POST)
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
	 * @see IDataCatalogRest#updateDataCollection(com.gehc.ai
	 * .app.dc.entity.DataCollection)
	 */
	/*
	 * @Override
	 * 
	 * @RequestMapping(value = "/datacatalog", method = RequestMethod.PUT)
	 * public ApiResponse updateDataCollection(@RequestBody DataCollection
	 * dataCollection, HttpServletRequest request) {
	 * logger.info("+++ !!! In REST updateDataCollection, orgId = " +
	 * request.getAttribute("orgId")); ApiResponse apiResponse = null; try {
	 * logger.info(" *** In updateDataCollection rest"); if (null !=
	 * request.getAttribute("orgId")) { apiResponse = new
	 * ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
	 * ApplicationConstants.SUCCESS,
	 * dataCatalogService.updateDataCollection(dataCollection,
	 * request.getAttribute("orgId").toString())); } else { apiResponse = new
	 * ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
	 * ApplicationConstants.SUCCESS,
	 * dataCatalogService.updateDataCollection(dataCollection, null)); } } catch
	 * (Exception e) {
	 * logger.error("Exception occured while updating the data collection ", e);
	 * apiResponse = new ApiResponse(ApplicationConstants.FAILURE,
	 * ApplicationConstants.INTERNAL_SERVER_ERROR_CODE,
	 * "Failed to update the data collection ", dataCollection.getId()); }
	 * return apiResponse; }
	 */

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
	public DataSet saveDataSet(@RequestBody DataSet d, HttpServletRequest request) {
		if (null != request.getAttribute("orgId")) {
			d.setOrgId(request.getAttribute("orgId").toString());
			return dataSetRepository.save(d);
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#saveImageSeries(ImageSeries)
	 */
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/image-set", method = RequestMethod.POST)
	public ImageSeries saveImageSeries(@RequestBody ImageSeries i) {
		return imageSeriesRepository.save(i);
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
		logger.info("[Image Series] Get img series for DC id " + id);
		List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
		List<DataSet> dsLst = new ArrayList<DataSet>();
		if (null != id) {
			// dsLst = dataSetRepository.findByIdAndOrgId(id,
			// "61939267-d195-499f-bfd8-7d92875c7035");
			dsLst = request.getAttribute("orgId") == null ? new ArrayList<DataSet>()
					: dataSetRepository.findByIdAndOrgId(id, request.getAttribute("orgId").toString());
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
		return request.getAttribute("orgId") == null ? new ArrayList<DataSet>()
				: dataSetRepository.findByIdAndOrgId(id, request.getAttribute("orgId").toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IDataCatalogRest#getDataSetByType(java.lang.String,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/data-collection", method = RequestMethod.GET)
	public List<DataSet> getDataSetByType(@QueryParam("type") String type, HttpServletRequest request) {
		logger.info("Get DC for type " + type);
		if (null != type) {
			return request.getAttribute("orgId") == null ? new ArrayList<DataSet>()
					: dataSetRepository.findByTypeAndOrgId(type, request.getAttribute("orgId").toString());
		} else {
			return request.getAttribute("orgId") == null ? new ArrayList<DataSet>()
					: dataSetRepository.findByOrgId(request.getAttribute("orgId").toString());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IDataCatalogRest#getDataCollection()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/datacatalog/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeries(@RequestParam Map<String, String> params) {
		{
			Map<String, String> validParams = constructValidParams(params,
					Arrays.asList(ORG_ID, MODALITY, ANATOMY, ANNOTATIONS, SERIES_INS_UID));
			// List of img set based on filter criteria other than annotation
			List<ImageSeries> imageSeriesLst;// = new ArrayList<ImageSeries>();
			// List of img set with annotation
			List<ImageSeries> imgSetWithAnnotation = new ArrayList<ImageSeries>();
			// List of img set which does not have annotation
			List<ImageSeries> imgSetWithOutAnn = new ArrayList<ImageSeries>();

			try {
				if (null != validParams) {
					if (validParams.containsKey(SERIES_INS_UID)) {
						return imageSeriesRepository
								.findBySeriesInstanceUidIn(getListOfStringsFromParams(validParams.get(SERIES_INS_UID)));
					} else if (validParams.containsKey(ORG_ID)) {
						List<String> orgIdLst = getListOfStringsFromParams(validParams.get(ORG_ID));

						if (validParams.containsKey(MODALITY)) {
							List<String> modalityLst = getListOfStringsFromParams(validParams.get(MODALITY));
							if (validParams.containsKey(ANATOMY)) {
								List<String> anatomyLst = getListOfStringsFromParams(validParams.get(ANATOMY));
								imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(orgIdLst,
										anatomyLst, modalityLst);
							} else {
								imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityIn(orgIdLst,
										modalityLst);
							}
						} else if (validParams.containsKey(ANATOMY)) {
							List<String> anatomyLst = getListOfStringsFromParams(validParams.get(ANATOMY));
							imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyIn(orgIdLst, anatomyLst);
						} else {
							imageSeriesLst = imageSeriesRepository.findByOrgIdIn(orgIdLst);
						}

						// Get the data with annotation filter
						if (validParams.containsKey(ANNOTATIONS)) {
							List<String> typeLst = getListOfStringsFromParams(validParams.get(ANNOTATIONS));
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

	private List<String> getListOfStringsFromParams(String values) {
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

	private List<Long> getListOfLongsFromParams(String values) {
		List<Long> valueLst = new ArrayList<Long>();
		if (null != values && !values.isEmpty()) {
			String[] valueStrings = values.split(",");
			if (null != valueStrings && valueStrings.length > 0) {
				for (int i = 0; i < valueStrings.length; i++)
					valueLst.add(Long.valueOf(valueStrings[i]));
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
	@RequestMapping(value = "/datacatalog/image-set/{id}", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesById(@PathVariable Long id, HttpServletRequest request) {
		return request.getAttribute("orgId") == null ? new ArrayList<ImageSeries>()
				: imageSeriesRepository.findByIdAndOrgId(id, request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/study/{studyId}/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByStudyDbId(@PathVariable Long studyId, HttpServletRequest request) {
		return request.getAttribute("orgId") == null ? new ArrayList<ImageSeries>()
				: imageSeriesRepository.findByStudyDbIdAndOrgId(studyId, request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient/{patientDbid}/study", method = RequestMethod.GET)
	public List<Study> getStudiesByPatientDbid(@PathVariable String patientDbid, HttpServletRequest request) {
		logger.info("*** In REST getStudiesByPatientDbid, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Study>()
				: studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(patientDbid),
						request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient", method = RequestMethod.GET)
	public List<Patient> getAllPatients(HttpServletRequest request) {
		logger.info(" In REST getAllPatients, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Patient>()
				: patientRepository.findByOrgId(request.getAttribute("orgId").toString());
	}
}
