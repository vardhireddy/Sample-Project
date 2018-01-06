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

import static com.gehc.ai.app.common.constants.ValidationConstants.DATA_SET_TYPE;
import static com.gehc.ai.app.common.constants.ValidationConstants.DIGIT;
import static com.gehc.ai.app.common.constants.ValidationConstants.ENTITY_NAME;
import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.common.constants.ApplicationConstants;
import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationImgSetDataCol;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
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
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(DataCatalogRestImpl.class);

	public static final String ORGANIZATION_ID = "org-id";
	public static final String SERIESINSUID = "series-instance-uid";
	public static final String ORG_ID = "org_id";
	public static final String MODALITY = "modality";
	public static final String ANATOMY = "anatomy";
	public static final String ANNOTATIONS = "annotations";
	public static final String SERIES_INS_UID = "series_instance_uid";
	public static final String ABSENT = "absent";
	public static final String ANNOTATIONS_ABSENT = "annotation-absent";
	public static final String GE_CLASS = "ge-class";
	public static final String DATA_FORMAT = "data_format";
	public static final String INSTITUTION = "institution";
	public static final String EQUIPMENT = "equipment";
	public static final int ORG_ID_LENGTH = 255;
	public static final int DATA_COLLECTION_ID_LENGTH = 11;
	public static final int ANNOTATION_TYPE_LENGTH = 500;

	@Value("${coolidge.micro.inference.url}")
	private String coolidgeMInferenceUrl;
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

	@Autowired
	private IDataCatalogService dataCatalogService;

	private Set<Long> getUniqueImgSetIds(List<Annotation> annotationLst) {
		Set<Long> uniqueImgSetIds = new HashSet<Long>();
		if (null != annotationLst && !annotationLst.isEmpty()) {
			for (Iterator<Annotation> annotationItr = annotationLst.iterator(); annotationItr.hasNext();) {
				Annotation annotation = (Annotation) annotationItr.next();
				uniqueImgSetIds.add(annotation.getImageSet().getId());
			}
		}
		return uniqueImgSetIds;
	}

	Map<String, Object> constructValidParams(Map<String, Object> params, List<String> allowedParams) {
		Map<String, Object> validParams = new HashMap<>();
		for (String key : allowedParams) {
			if (params.containsKey(key)) {
				String value = params.get(key).toString();
				if (!value.isEmpty()) {
					validParams.put(key, value);
				}
			}
		}

		return validParams;
	}

	@Override
	@RequestMapping(value = "/datacatalog/healthcheck", method = RequestMethod.GET)
	public String healthcheck() {
		return ApplicationConstants.SUCCESS;
	}

	// TODOD:To fix the CFT why sometimes it uses the health check as all lower
	// case and sometimes as camel case
	// We will keep only one health check api
	@Override
	@RequestMapping(value = "/dataCatalog/healthCheck", method = RequestMethod.GET)
	public String healthCheck() {
		return ApplicationConstants.SUCCESS;
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient/{ids}", method = RequestMethod.GET)
	public List<Patient> getPatients(@PathVariable String ids, HttpServletRequest request) {
		logger.debug("*** In REST getPatients, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Patient>()
				: patientRepository.findByIdInAndOrgId(getListOfLongsFromParams(ids),
						request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.GET)
	public List<Study> getStudy(HttpServletRequest request) {
		logger.debug("*** In REST get all studies, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Study>()
				: studyRepository.findByOrgId(request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/study/{ids}", method = RequestMethod.GET)
	public List<Study> getStudiesById(@PathVariable String ids, HttpServletRequest request) {
		logger.debug("*** In REST getStudiesById, orgId = " + request.getAttribute("orgId"));
		List<Long> pids = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length; i++)
			pids.add(Long.valueOf(idStrings[i]));
		return studyRepository.findByIdInAndOrgId(pids, request.getAttribute("orgId").toString());

	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/patient", method = RequestMethod.POST)
	public Patient postPatient(@RequestBody Patient p) throws DataCatalogException {
		if (null != p && null != p.getPatientId() && null != p.getOrgId() && null == p.getId()) {
			logger.info(
					"[In REST, Saving patient data with id = " + p.getId() + " and org id = " + p.getOrgId() + " ]");
			List<Patient> patientLst = patientRepository.findByPatientIdAndOrgId(p.getPatientId(), p.getOrgId());
			if (patientLst != null && !patientLst.isEmpty()) {
				return patientLst.get(0);
			}
		} else if (null == p || null == p.getPatientId() || null == p.getOrgId()) {
			throw new DataCatalogException("Missing patient info");
		}
		p.setUploadDate(new Date(System.currentTimeMillis()));
		return patientRepository.save(p);
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.POST)
	public Study postStudy(@RequestBody Study s) throws DataCatalogException {
		if (null != s && null != s.getStudyInstanceUid() && null != s.getOrgId() && null == s.getId()) {
			logger.info("[In REST, Saving study with id = " + s.getId() + ", instance uid = " + s.getStudyInstanceUid()
					+ " and org id = " + s.getOrgId() + " ]");
			List<Study> studyLst = studyRepository.findByOrgIdAndStudyInstanceUid(s.getOrgId(),
					s.getStudyInstanceUid());
			if (studyLst != null && !studyLst.isEmpty()) {
				return studyLst.get(0);
			}
		} else if (null == s || null == s.getOrgId()) {
			throw new DataCatalogException("Missing study info");
		}

		if (null != s.getPatientDbId()) {
			s.setUploadDate(new Date(System.currentTimeMillis()));
			return studyRepository.save(s);
		} else {
			throw new DataCatalogException("Missing patientDb id");
		}
	}

	@Override
	@RequestMapping(value = "/annotation", method = RequestMethod.GET)
	public List<Annotation> getAnnotationsByImgSet(@QueryParam("imagesetid") Long imagesetid) {
		// Note: this is being used in C2M as well
		logger.debug("In REST getAnnotationsByImgSet , image set id = " + imagesetid);
		if (null != imagesetid) {
			return annotationRepository.findByImageSetId(Long.valueOf(imagesetid));
		} else {
			return new ArrayList<Annotation>();
		}
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/annotation", method = RequestMethod.POST)
	public ApiResponse saveAnnotation(@Valid @RequestBody Annotation annotation) {
		ApiResponse apiResponse = null;
		try {
			Annotation newAnnotation = annotationRepository.save(annotation);
			logger.info("[In REST, Saving new annotaion with id = " + newAnnotation.getId() + "]");
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
	public ApiResponse deleteAnnotation(@PathVariable String ids) {
		// C2M is using get annotation by ids so this has been removed from
		// interceptor
		ApiResponse apiResponse = null;
		Annotation ann = new Annotation();
		try {
			if (null != ids && ids.length() > 0) {
				String[] idStrings = ids.split(",");
				for (int i = 0; i < idStrings.length; i++) {
					ann.setId(Long.valueOf(idStrings[i]));
					logger.info("[-----Delete annotation " + Long.valueOf(idStrings[i]) + "]");
					// Get annotation object as somehow it was crying for org_id
					// is null
					List<Annotation> annLst = getAnnotationsById(idStrings[i], null);
					if (!annLst.isEmpty()) {
						logger.debug(" annLst.size() " + annLst.size());
						annotationRepository.delete(annLst.get(0));
					} else {
						annotationRepository.delete(ann);
					}
					apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
							ApplicationConstants.SUCCESS, ids);
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
	 * @see IDataCatalogRest#getAnnotationsById(java.lang. Long)
	 */
	@Override
	@RequestMapping(value = "/annotation/{ids}", method = RequestMethod.GET)
	public List<Annotation> getAnnotationsById(@PathVariable String ids, HttpServletRequest request) {
		logger.debug("In REST, getAnnotationsById");
		List<Long> idsLst = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length; i++)
			idsLst.add(Long.valueOf(idStrings[i]));
		return annotationRepository.findByIdIn(idsLst);
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/annotation-properties", method = RequestMethod.GET)
	public List<AnnotationProperties> getAnnotationProperties(@QueryParam("orgId") String orgId) {
		logger.debug("--- In REST, getAnnotationProperties, orgId = " + orgId);

		if (null != orgId) {
			String patternStr = UUID;
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(orgId);
			boolean matchFound = matcher.matches();
			if (!matchFound || orgId.length() > ORG_ID_LENGTH) {
				logger.debug("BAD REQUEST : org id is not valid");
				return new ArrayList<AnnotationProperties>();
			}
		}

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
		responseBuilder = Response.ok(annotationProperties);
		return (List<AnnotationProperties>) responseBuilder.build().getEntity();
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
	public DataSet saveDataSet(@Valid @RequestBody DataSet d, HttpServletRequest request) {
		logger.info("[In REST, Creating new data collection, orgId = " + request.getAttribute("orgId") + "]");
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
		if (null != i) {
			logger.debug("*** Now saving image series " + i.toString());
		}
		return imageSeriesRepository.save(i);
	}

	/*
	 * To get the image set based on the patient id like LIDC-IDRI-0286
	 *
	 * @see IDataCatalogRest#getImgSerByPatientId(java.lang. String)
	 */
	@Override
	@RequestMapping(value = "/datacatalog/patient/{ids}/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByPatientId(@PathVariable String ids, @QueryParam("orgId") String orgId) {
		logger.info("*** Get img series for patient id " + ids + " org id " + orgId);
		List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
		// TODO:Use ManyToOne mapping between Image Series and Patient
		if (null != ids && ids.length() > 0 && null != orgId && !orgId.isEmpty()) {
			logger.debug("Now getting patient db id based on patient id " + ids + " org id " + orgId);
			List<Patient> patLst = patientRepository.findByPatientIdAndOrgId(ids, orgId);
			if (null != patLst && !patLst.isEmpty()) {
				logger.info("[Image Series] Got patient DB id ");
				imgSerLst = imageSeriesRepository.findByPatientDbIdAndOrgId(((Patient) (patLst.get(0))).getId(), orgId);
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
	public List<ImageSeries> getImgSeriesByDSId(@PathVariable Long id) {
		// Note: Coolidge is using this as well
		logger.debug("In REST , Get img series for DC id " + id);
		List<DataSet> dsLst = new ArrayList<DataSet>();
		if (null != id) {
			dsLst = dataSetRepository.findById(id);
			if (null != dsLst && !dsLst.isEmpty()) {
				@SuppressWarnings("unchecked")
				List<Object> imgSeries = (ArrayList<Object>) ((DataSet) (dsLst.get(0))).getImageSets();
				List<Long> imgSerIdLst = new ArrayList<Long>();
				if (null != imgSeries && !imgSeries.isEmpty()) {
					for (int i = 0; i < imgSeries.size(); i++) {
						imgSerIdLst.add(Long.valueOf(imgSeries.get(i).toString()));
					}
					return getPatientForImgSeriesLst(imageSeriesRepository.findByIdIn(imgSerIdLst));
				}
			}
		}
		return new ArrayList<ImageSeries>();
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
		logger.debug("In REST, Get DC by Id " + id);
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
		logger.debug("In REST, Get DC for type " + type);
		if (null != type) {
			String patternStr = DATA_SET_TYPE;
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(type);
			boolean matchFound = matcher.matches();
			if (!matchFound) {
				logger.debug("BAD REQUEST : type is not valid");
				return new ArrayList<DataSet>();
			}
			return request.getAttribute("orgId") == null ? new ArrayList<DataSet>()
					: dataSetRepository.findByTypeAndOrgIdOrderByCreatedDateDesc(type,
							request.getAttribute("orgId").toString());
		} else {
			return request.getAttribute("orgId") == null ? new ArrayList<DataSet>()
					: dataSetRepository.findByOrgIdOrderByCreatedDateDesc(request.getAttribute("orgId").toString());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IDataCatalogRest#getDataCollection()
	 */
	@Override
	@RequestMapping(value = "/datacatalog/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeries(@RequestParam Map<String, Object> params) {
		{
			logger.debug("In REST, getImgSeries");
			Map<String, Object> validParams = constructValidParams(params, Arrays.asList(ORGANIZATION_ID, MODALITY,
					ANATOMY, ANNOTATIONS, SERIESINSUID, GE_CLASS, DATA_FORMAT, INSTITUTION, EQUIPMENT));
			// List of img set based on filter criteria other than annotation
			List<ImageSeries> imageSeriesLst;// = new ArrayList<ImageSeries>();
			// List of img set with annotation
			List<ImageSeries> imgSetWithAnnotation = new ArrayList<ImageSeries>();
			// List of img set which does not have annotation
			List<ImageSeries> imgSetWithOutAnn = new ArrayList<ImageSeries>();
			try {
				if (null != validParams) {
					if (validParams.containsKey(SERIESINSUID) && validParams.containsKey(ORGANIZATION_ID)) {
						return imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(
								getListOfStringsFromParams(validParams.get(SERIESINSUID).toString()),
								getListOfStringsFromParams(validParams.get(ORGANIZATION_ID).toString()));
					} else if (validParams.containsKey(ORGANIZATION_ID)) {
						imageSeriesLst = getImageSeriesList(validParams, imgSetWithAnnotation, imgSetWithOutAnn);
						if (!imageSeriesLst.isEmpty()) {
							if (validParams.containsKey(GE_CLASS) && validParams.containsKey(ANNOTATIONS)) {
								// Get the annotation type bcoz an image set can
								// have multiple anntations with different types
								List<String> typeLst = getListOfStringsFromParams(
										validParams.get(ANNOTATIONS).toString());
								return dataCatalogService.getImgSeries(params, imageSeriesLst, typeLst);
							} else {
								return imageSeriesLst;
							}
						}
					}
				}
			} catch (RuntimeException e) {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity("Operation failed while filtering image set data").build());
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity("Operation failed while filtering image set data").build());
			}
			return new ArrayList<ImageSeries>();
		}
	}

	private List<ImageSeries> getImageSeriesList(Map<String, Object> validParams,
			List<ImageSeries> imgSetWithAnnotation, List<ImageSeries> imgSetWithOutAnn) throws DataCatalogException {
		List<ImageSeries> imageSeriesLst;
		List<String> orgIdLst = getListOfStringsFromParams(validParams.get(ORGANIZATION_ID).toString());
		List<ImageSeries> patientImageSeriesLst = new ArrayList<ImageSeries>();
		imageSeriesLst = getImageSeriesListWithValidParamsAndOrgId(validParams, orgIdLst);

		logger.debug("In REST , get image series list ");
		// Get the data with annotation filter
		if (validParams.containsKey(ANNOTATIONS)) {
			List<String> typeLst = getListOfStringsFromParams(validParams.get(ANNOTATIONS).toString());
			if (!typeLst.isEmpty()) {
				// List of image series id based on criteria
				// other than annotation
				List<Long> imgSeriesIdLst = new ArrayList<Long>();
				for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
					ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
					imgSeriesIdLst.add(imageSeries.getId());
				}
				if (typeLst.contains(ABSENT)) {
					imgSetWithOutAnn = getImageSeriesWithOutAnnotations(imgSetWithOutAnn, imageSeriesLst,
							imgSeriesIdLst);
				} else {
					imgSetWithAnnotation = getImageSeriesWithAnnotations(imgSetWithAnnotation, imageSeriesLst, typeLst,
							imgSeriesIdLst, orgIdLst);
				}
			}
		}

		patientImageSeriesLst = getPatientForImageSeriesWithOrWithOutAnn(validParams, imgSetWithAnnotation,
				imgSetWithOutAnn, imageSeriesLst);
		return patientImageSeriesLst;
	}

	private List<ImageSeries> getPatientForImageSeriesWithOrWithOutAnn(Map<String, Object> validParams,
			List<ImageSeries> imgSetWithAnnotation, List<ImageSeries> imgSetWithOutAnn,
			List<ImageSeries> imageSeriesLst) {
		logger.debug("In REST , get patient for image series with Or without annotation");
		if (null != imgSetWithAnnotation && !imgSetWithAnnotation.isEmpty()) {
			// Data with Annotation
			return getPatientForImgSeriesLst(imgSetWithAnnotation);
		} else if (null != imgSetWithOutAnn && !imgSetWithOutAnn.isEmpty()) {
			// Data with no Annotation
			return getPatientForImgSeriesLst(imgSetWithOutAnn);
		} else if (!validParams.containsKey(ANNOTATIONS)) { // DC
			// without
			// Annotation
			// criteria
			return getPatientForImgSeriesLst(imageSeriesLst);
		}
		return new ArrayList<ImageSeries>();
	}

	private List<ImageSeries> getImageSeriesListWithValidParamsAndOrgId(Map<String, Object> validParams,
			List<String> orgIdLst) throws DataCatalogException {
		logger.debug("In REST , get image series list with valid params and orgId");
		List<ImageSeries> imageSeriesLst = null;
		if (validParams.containsKey(MODALITY)) {
			List<String> modalityLst = getListOfStringsFromParams(validParams.get(MODALITY).toString());
			if (validParams.containsKey(ANATOMY)) {
				imageSeriesLst = getImageSeriesWithModalityAnatomy(validParams, orgIdLst, modalityLst);
			} else if (validParams.containsKey(DATA_FORMAT)) {
				imageSeriesLst = getImageSeriesWithModalityDataFormat(validParams, orgIdLst, modalityLst);
			} else if (validParams.containsKey(INSTITUTION)) {
				imageSeriesLst = getImageSeriesWithModalityInstitution(validParams, orgIdLst, modalityLst);
			} else if (validParams.containsKey(EQUIPMENT)) {
				imageSeriesLst = getImageSeriesWithModalityEquiment(validParams, orgIdLst, modalityLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityIn(orgIdLst, modalityLst);
			}
		} else if (validParams.containsKey(ANATOMY)) {
			imageSeriesLst = getImageSeriesWithAnatomy(validParams, orgIdLst);
		} else if (validParams.containsKey(DATA_FORMAT)) {
			imageSeriesLst = getImageSeriesWithDataFormat(validParams, orgIdLst);
		} else if (validParams.containsKey(INSTITUTION)) {
			imageSeriesLst = getImageSeriesWithInstitution(validParams, orgIdLst);
		} else if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndEquipmentIn(orgIdLst, equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdIn(orgIdLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityEquiment(Map<String, Object> validParams, List<String> orgIdLst,
			List<String> modalityLst) throws DataCatalogException {
		logger.debug("In REST , get image series with modality ,equiment");
		List<ImageSeries> imageSeriesLst;
		List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
		imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityInAndEquipmentIn(orgIdLst, modalityLst,
				equipmentLst);
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithInstitution(Map<String, Object> validParams, List<String> orgIdLst)
			throws DataCatalogException {
		logger.debug("In REST , get image series with institution");
		List<ImageSeries> imageSeriesLst;
		List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
		if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndInstitutionInAndEquipmentIn(orgIdLst, institutionLst,
					equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndInstitutionIn(orgIdLst, institutionLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithDataFormat(Map<String, Object> validParams, List<String> orgIdLst)
			throws DataCatalogException {
		logger.debug("In REST , get image series with data format");
		List<ImageSeries> imageSeriesLst;
		List<String> dataFormatLst = getListOfStringsFromParams(validParams.get(DATA_FORMAT).toString());
		if (validParams.containsKey(INSTITUTION)) {
			List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
			if (validParams.containsKey(EQUIPMENT)) {
				List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndDataFormatInAndInstitutionInAndEquipmentIn(
						orgIdLst, dataFormatLst, institutionLst, equipmentLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndDataFormatInAndInstitutionIn(orgIdLst,
						dataFormatLst, institutionLst);
			}
		} else if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndDataFormatInAndEquipmentIn(orgIdLst, dataFormatLst,
					equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndDataFormatIn(orgIdLst, dataFormatLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithAnatomy(Map<String, Object> validParams, List<String> orgIdLst)
			throws DataCatalogException {
		logger.debug("In REST , get image series with anatomy");
		List<ImageSeries> imageSeriesLst;
		List<String> anatomyLst = getListOfStringsFromParams(validParams.get(ANATOMY).toString());
		if (validParams.containsKey(DATA_FORMAT)) {
			List<String> dataFormatLst = getListOfStringsFromParams(validParams.get(DATA_FORMAT).toString());
			if (validParams.containsKey(INSTITUTION)) {
				imageSeriesLst = getImageSeriesWithAnatomyDataFomatInstitutionAndOrEquipment(validParams, orgIdLst,
						anatomyLst, dataFormatLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndDataFormatIn(orgIdLst, anatomyLst,
						dataFormatLst);
			}
		} else if (validParams.containsKey(INSTITUTION)) {
			imageSeriesLst = getImageSeriesWithAnatomyInstitutionAndOrEquipment(validParams, orgIdLst, anatomyLst);
		} else if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndEquipmentIn(orgIdLst, anatomyLst,
					equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyIn(orgIdLst, anatomyLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithAnatomyInstitutionAndOrEquipment(Map<String, Object> validParams,
			List<String> orgIdLst, List<String> anatomyLst) throws DataCatalogException {
		logger.debug("In REST , get image series with anatomy, institution and/Or equipment");
		List<ImageSeries> imageSeriesLst;
		List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
		if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndInstitutionInAndEquipmentIn(orgIdLst,
					anatomyLst, institutionLst, equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndInstitutionIn(orgIdLst, anatomyLst,
					institutionLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithAnatomyDataFomatInstitutionAndOrEquipment(
			Map<String, Object> validParams, List<String> orgIdLst, List<String> anatomyLst, List<String> dataFormatLst)
			throws DataCatalogException {
		logger.debug("In REST , get Image series with anatomy, data format, institution and/Or equipment");
		List<ImageSeries> imageSeriesLst;
		List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
		if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository
					.findByOrgIdInAndAnatomyInAndDataFormatInAndInstitutionInAndEquipmentIn(orgIdLst, anatomyLst,
							dataFormatLst, institutionLst, equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndDataFormatInAndInstitutionIn(orgIdLst,
					anatomyLst, dataFormatLst, institutionLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityInstitution(Map<String, Object> validParams,
			List<String> orgIdLst, List<String> modalityLst) throws DataCatalogException {
		logger.debug("In REST , get Image series with modality, institution");
		List<ImageSeries> imageSeriesLst;
		List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
		if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityInAndInstitutionInAndEquipmentIn(orgIdLst,
					modalityLst, institutionLst, equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityInAndInstitutionIn(orgIdLst, modalityLst,
					institutionLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityDataFormat(Map<String, Object> validParams,
			List<String> orgIdLst, List<String> modalityLst) throws DataCatalogException {
		logger.debug("In REST , get Image series with modality, data format");
		List<ImageSeries> imageSeriesLst;
		List<String> dataFormatLst = getListOfStringsFromParams(validParams.get(DATA_FORMAT).toString());
		if (validParams.containsKey(INSTITUTION)) {
			List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
			if (validParams.containsKey(EQUIPMENT)) {
				List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
				imageSeriesLst = imageSeriesRepository
						.findByOrgIdInAndModalityInAndDataFormatInAndInstitutionInAndEquipmentIn(orgIdLst, modalityLst,
								dataFormatLst, institutionLst, equipmentLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityInAndDataFormatInAndInstitutionIn(
						orgIdLst, modalityLst, dataFormatLst, institutionLst);
			}
		} else {
			if (validParams.containsKey(EQUIPMENT)) {
				List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityInAndDataFormatInAndEquipmentIn(orgIdLst,
						modalityLst, dataFormatLst, equipmentLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityInAndDataFormatIn(orgIdLst, modalityLst,
						dataFormatLst);
			}
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityAnatomy(Map<String, Object> validParams, List<String> orgIdLst,
			List<String> modalityLst) throws DataCatalogException {
		logger.debug("In REST , get Image series with modality , anatomy");
		List<ImageSeries> imageSeriesLst;
		List<String> anatomyLst = getListOfStringsFromParams(validParams.get(ANATOMY).toString());
		if (validParams.containsKey(DATA_FORMAT)) {
			imageSeriesLst = getImageSeriesWithModalityAnatomyAndDataFormat(validParams, orgIdLst, modalityLst,
					anatomyLst);
		} else {
			if (validParams.containsKey(INSTITUTION)) {
				imageSeriesLst = getImageSeriesWithModalityAnatomyAndOrInstitution(validParams, orgIdLst, modalityLst,
						anatomyLst);
			} else {
				imageSeriesLst = getImageSeriesWithModalityAnatomyAndOrEquipment(validParams, orgIdLst, modalityLst,
						anatomyLst);
			}
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityAnatomyAndOrEquipment(Map<String, Object> validParams,
			List<String> orgIdLst, List<String> modalityLst, List<String> anatomyLst) throws DataCatalogException {
		logger.debug("In REST , get Image series with modality, anatomy and/Or equipment");
		List<ImageSeries> imageSeriesLst;
		if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityInAndEquipmentIn(orgIdLst,
					anatomyLst, modalityLst, equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(orgIdLst, anatomyLst,
					modalityLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityAnatomyAndOrInstitution(Map<String, Object> validParams,
			List<String> orgIdLst, List<String> modalityLst, List<String> anatomyLst) throws DataCatalogException {
		logger.debug("In REST , get Image series with modality, anatomy and/or institution");
		List<ImageSeries> imageSeriesLst;
		List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
		if (validParams.containsKey(EQUIPMENT)) {
			List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityInAndInstitutionInAndEquipmentIn(
					orgIdLst, anatomyLst, modalityLst, institutionLst, equipmentLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityInAndInstitutionIn(orgIdLst,
					anatomyLst, modalityLst, institutionLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithModalityAnatomyAndDataFormat(Map<String, Object> validParams,
			List<String> orgIdLst, List<String> modalityLst, List<String> anatomyLst) throws DataCatalogException {
		logger.debug("In REST , get Image series with modality, anatomy, data format");
		List<ImageSeries> imageSeriesLst;
		List<String> dataFormatLst = getListOfStringsFromParams(validParams.get(DATA_FORMAT).toString());
		if (validParams.containsKey(INSTITUTION)) {
			List<String> institutionLst = getListOfStringsFromParams(validParams.get(INSTITUTION).toString());
			if (validParams.containsKey(EQUIPMENT)) {
				List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
				imageSeriesLst = imageSeriesRepository
						.findByOrgIdInAndAnatomyInAndModalityInAndDataFormatInAndInstitutionInAndEquipmentIn(orgIdLst,
								anatomyLst, modalityLst, dataFormatLst, institutionLst, equipmentLst);
			} else {
				imageSeriesLst = imageSeriesRepository
						.findByOrgIdInAndAnatomyInAndModalityInAndDataFormatInAndInstitutionIn(orgIdLst, anatomyLst,
								modalityLst, dataFormatLst, institutionLst);
			}
		} else {
			if (validParams.containsKey(EQUIPMENT)) {
				List<String> equipmentLst = getListOfStringsFromParams(validParams.get(EQUIPMENT).toString());
				imageSeriesLst = imageSeriesRepository
						.findByOrgIdInAndAnatomyInAndModalityInAndDataFormatInAndEquipmentIn(orgIdLst, anatomyLst,
								modalityLst, dataFormatLst, equipmentLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityInAndDataFormatIn(orgIdLst,
						anatomyLst, modalityLst, dataFormatLst);
			}
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithAnnotations(List<ImageSeries> imgSetWithAnnotation,
			List<ImageSeries> imageSeriesLst, List<String> typeLst, List<Long> imgSeriesIdLst, List<String> orgIdLst) {
		logger.debug("In REST , get Image series with annotations");
		List<Annotation> annotationLst = new ArrayList<Annotation>();
		annotationLst = annotationRepository.findByImageSetIdInAndTypeInAndOrgId(imgSeriesIdLst, typeLst, orgIdLst);
		Set<Long> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
		if (!uniqueImgSetIds.isEmpty()) {
			for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
				ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
				if (uniqueImgSetIds.contains(imageSeries.getId())) {
					imgSetWithAnnotation.add(imageSeries);
				}
			}
		}
		logger.debug("Size of img set list with annotations "+ (imgSetWithAnnotation!=null?imgSetWithAnnotation.size():0));
		return imgSetWithAnnotation;
	}

	private List<ImageSeries> getImageSeriesWithOutAnnotations(List<ImageSeries> imgSetWithOutAnn,
			List<ImageSeries> imageSeriesLst, List<Long> imgSeriesIdLst) {
		logger.debug("In REST , get Image series without annotations");
		List<Annotation> annotationLst = new ArrayList<Annotation>();
		annotationLst = annotationRepository.findByImageSetIdIn(imgSeriesIdLst);
		Set<Long> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
		if (!uniqueImgSetIds.isEmpty()) {
			for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
				ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
				if (!uniqueImgSetIds.contains(imageSeries.getId())) {
					imgSetWithOutAnn.add(imageSeries);
				}
			}
		} else {
			return imageSeriesLst;
		}
		logger.debug("Size of img set list without annotations "+ (imgSetWithOutAnn!=null?imgSetWithOutAnn.size():0));
		return imgSetWithOutAnn;
	}

	private List<String> getListOfStringsFromParams(String values) throws DataCatalogException {
		List<String> valueLst = new ArrayList<String>();
		if (null != values && !values.isEmpty()) {
			String[] valueStrings = values.split(",");
			if (null != valueStrings && valueStrings.length > 0) {
				for (int i = 0; i < valueStrings.length; i++)
					try {
						valueLst.add(URLDecoder.decode(valueStrings[i], "UTF-8").toLowerCase());
					} catch (UnsupportedEncodingException e) {
						throw new DataCatalogException("Unable to decode request params");
					}
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
		logger.debug("In REST , patient for image series list");
		List<ImageSeries> imgSerWithPatientLst = new ArrayList<ImageSeries>();
		for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
			ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
			if (null != imageSeries && null != imageSeries.getPatientDbId()) {
				List<Patient> patientLst = patientRepository.findByIdAndOrgId(imageSeries.getPatientDbId(),
						imageSeries.getOrgId());
				if (null != patientLst && !patientLst.isEmpty()) {
					imageSeries.setPatient(patientLst.get(0));
				}
			}
			imgSerWithPatientLst.add(imageSeries);
		}
		return imgSerWithPatientLst;
	}

	@Override
	@RequestMapping(value = "/datacatalog/image-set/{id}", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesById(@PathVariable Long id) {
		logger.debug("*** In REST get image series by id " + id);
		return imageSeriesRepository.findById(id);
	}

	@Override
	@RequestMapping(value = "/datacatalog/study/{studyId}/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByStudyDbId(@PathVariable Long studyId, HttpServletRequest request) {
		logger.debug("*** In REST get image series by study id " + studyId);
		return request.getAttribute("orgId") == null ? new ArrayList<ImageSeries>()
				: imageSeriesRepository.findByStudyDbIdAndOrgId(studyId, request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient/{ids}/study", method = RequestMethod.GET)
	public List<Study> getStudiesByPatientDbid(@PathVariable String ids, HttpServletRequest request) {
		logger.debug("*** In REST getStudiesByPatientDbid, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Study>()
				: studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(ids),
						request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient", method = RequestMethod.GET)
	public List<Patient> getAllPatients(HttpServletRequest request) {
		logger.debug(" In REST getAllPatients, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Patient>()
				: patientRepository.findByOrgId(request.getAttribute("orgId").toString());
	}

	private void checkInvalidType(String id, String annotationType) {
		String patternStrId = DIGIT;
		Pattern patternId = Pattern.compile(patternStrId);
		Matcher matcherId = patternId.matcher(id);
		boolean matchFoundId = matcherId.matches();

		String patternStrAnnotationType = ENTITY_NAME;
		Pattern patternAnnotationType = Pattern.compile(patternStrAnnotationType);
		Matcher matcherAnnotationType = patternAnnotationType.matcher(annotationType);
		boolean matchFoundAnnotationType = matcherAnnotationType.matches();
		if (!matchFoundAnnotationType || !matchFoundId || id.length() > DATA_COLLECTION_ID_LENGTH
				|| annotationType.length() > ANNOTATION_TYPE_LENGTH) {
			logger.debug("Datacollection id or annotation type is not valid");
			throw new BadRequestException("Datacollection id or annotation type is not valid");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@RequestMapping(value = "/datacatalog/raw-target-data", method = RequestMethod.GET)
	public List getRawTargetData(@QueryParam("id") String id, @QueryParam("annotationType") String annotationType) {
		logger.info(" Entering method getRawTargetData --> id: " + id + " Type: " + annotationType);
		// Note: works fine with new DC which has image sets as Array of Longs
		if ((id == null) || (id.length() == 0) || annotationType == null) {
			logger.debug("Datacollection id and annotation type is required to get annotation for a data collection");
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Datacollection id and annotation type is required to get annotation for a data collection")
					.build());
		} else {
			checkInvalidType(id, annotationType);
		}
		ResponseBuilder responseBuilder;
		List<AnnotationImgSetDataCol> annImgSetDCLst = null;
		List<DataSet> dsLst = dataSetRepository.findById(Long.valueOf(id));
		if (null != dsLst && !dsLst.isEmpty()) {
			logger.debug("***** Data set Lst.size() = " + dsLst.size());
			if (null != dsLst.get(0).getImageSets()) {
				List<String> types = new ArrayList<String>();
				types.add(annotationType);
				/*
				 * List<ImageSeries> imgSeriesLst = imageSeriesRepository
				 * .findByIdIn((List<Long>) dsLst.get(0).getImageSets());
				 */
				List<Object> imgSeries = (ArrayList<Object>) ((DataSet) (dsLst.get(0))).getImageSets();
				List<Long> imgSerIdLst = getImgSerIdLst(imgSeries);
				/*
				 * if (null != imgSeries && !imgSeries.isEmpty()) { for (int i =
				 * 0; i < imgSeries.size(); i++) {
				 * imgSerIdLst.add(Long.valueOf(imgSeries.get(i).toString())); }
				 * }
				 */
				List<ImageSeries> imgSeriesLst = imageSeriesRepository.findByIdIn(imgSerIdLst);
				logger.debug("***** Got img series by id sucessfully");
				if (null != imgSeriesLst && !imgSeriesLst.isEmpty()) {
					logger.debug(" imgSeriesLst.size() = " + imgSeriesLst.size());
					Map<Long, ImageSeries> imgSeriesMap = new HashMap<Long, ImageSeries>();
					for (Iterator<ImageSeries> imgSeriesItr = imgSeriesLst.iterator(); imgSeriesItr.hasNext();) {
						ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
						imgSeriesMap.put(imageSeries.getId(), imageSeries);
					}
					/*
					 * List<Annotation> annotationLst =
					 * annotationRepository.findByImageSetIdInAndTypeIn((List<
					 * Long>) dsLst.get(0).getImageSets(), types);
					 */
					List<Annotation> annotationLst = annotationRepository.findByImageSetIdInAndTypeIn(imgSerIdLst,
							types);
					logger.info("***** Got annotationLst by img series id and type");
					if (null != annotationLst && !annotationLst.isEmpty()) {
						logger.debug(" annotationLst.size() = " + annotationLst.size());
						annImgSetDCLst = new ArrayList<AnnotationImgSetDataCol>();
						for (Iterator<Annotation> annotationItr = annotationLst.iterator(); annotationItr.hasNext();) {
							AnnotationImgSetDataCol annImgSetDataCol = new AnnotationImgSetDataCol();
							Annotation annotation = (Annotation) annotationItr.next();
							annImgSetDataCol.setDcId(id);
							annImgSetDataCol.setAnnotationDate(annotation.getAnnotationDate().toString());
							annImgSetDataCol.setAnnotationId(annotation.getId().toString());
							annImgSetDataCol.setAnnotationType(annotation.getType());
							annImgSetDataCol.setAnnotatorId(annotation.getAnnotatorId());
							ObjectMapper mapper = new ObjectMapper();
							TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
							};
							HashMap<String, Object> o = null;
							try {
								String jsonInString = mapper.writeValueAsString(annotation.getItem());
								o = mapper.readValue(jsonInString, typeRef);
							} catch (IOException e) { // NOSONAR
								e.printStackTrace();
								logger.error("Exception during getting raw target data ", e);
							}
							annImgSetDataCol.setAnnotationItem(o);
							annImgSetDataCol.setImId(annotation.getImageSet().getId().toString());
							ImageSeries imageSeries = imgSeriesMap.get(annotation.getImageSet().getId());
							annImgSetDataCol.setPatientDbid(imageSeries.getPatientDbId().toString());
							annImgSetDataCol.setUri(imageSeries.getUri());
							annImgSetDCLst.add(annImgSetDataCol);
						}
					}
				}
			}
		}

		if (annImgSetDCLst != null) {
			logger.debug(" annImgSetDCLst.size() = " + annImgSetDCLst.size());
			responseBuilder = Response.ok(annImgSetDCLst);
			return (List) responseBuilder.build().getEntity();
		}

		return (List) new ArrayList<AnnotationImgSetDataCol>();

	}

	private List<Long> getImgSerIdLst(List<Object> imgSeries) {
		List<Long> imgSerIdLst = new ArrayList<Long>();
		if (null != imgSeries && !imgSeries.isEmpty()) {
			for (int i = 0; i < imgSeries.size(); i++) {
				imgSerIdLst.add(Long.valueOf(imgSeries.get(i).toString()));
			}
		}
		return imgSerIdLst;
	}

	@Override
	@RequestMapping(value = "/datacatalog/data-summary", method = RequestMethod.GET)
	public Map<String, Object> dataSummary(@QueryParam("groupby") String groupby, HttpServletRequest request) {
		String orgId = request.getAttribute("orgId") == null ? null : request.getAttribute("orgId").toString();
		logger.debug("Get dataSummary for orgId = " + orgId + " group by " + groupby);
		Map<String, Object> filters = new HashMap<String, Object>();
		if (null != groupby && !groupby.isEmpty() && groupby.equalsIgnoreCase(ANNOTATIONS_ABSENT)) {
			logger.debug("Started for ANNOTATIONS_ABSENT"+ new Timestamp(System.currentTimeMillis()));
			filters.put(ANNOTATIONS_ABSENT, imageSeriesRepository.countImgWithNoAnn(orgId).get(0));
			logger.debug("END ANNOTATIONS_ABSENT"+ new Timestamp(System.currentTimeMillis()));
		} else {
			filters.putAll(getModalityAndAnatomyCount(orgId, filters));
			filters.putAll(getDataFormatAndInstitutionCount(orgId, filters));
			filters.putAll(getEquipmentAndAnnoatationTypeCount(orgId, filters));
		}
		return filters;
	}

	private Map<String, Object> getEquipmentAndAnnoatationTypeCount(String orgId, Map<String, Object> filters) {
		logger.debug("In REST, getEquipmentAndAnnoatationTypeCount, orgId = " + orgId);
		logger.debug("Started for getEquipmentAndAnnoatationTypeCount"+new Timestamp(System.currentTimeMillis()));
		List<Object[]> equipmentCount = imageSeriesRepository.countEquipment(orgId);
		if (null != equipmentCount && !equipmentCount.isEmpty()) {
			filters.putAll(getFiltersCount(equipmentCount, EQUIPMENT));
		}
		List<Object[]> annotationTypeCount = annotationRepository.countAnnotationType(orgId);
		if (null != annotationTypeCount && !annotationTypeCount.isEmpty()) {
			filters.putAll(getFiltersCount(annotationTypeCount, ANNOTATIONS));
		}
		logger.debug("END for getEquipmentAndAnnoatationTypeCount"+new Timestamp(System.currentTimeMillis()));
		return filters;
	}

	private Map<String, Object> getDataFormatAndInstitutionCount(String orgId, Map<String, Object> filters) {
		logger.debug("In REST, getDataFormatAndInstitutionCount, orgId = " + orgId);
		logger.debug("Started for getDataFormatAndInstitutionCount"+new Timestamp(System.currentTimeMillis()));
		List<Object[]> dataFormatCount = imageSeriesRepository.countDataFormat(orgId);
		if (null != dataFormatCount && !dataFormatCount.isEmpty()) {
			filters.putAll(getFiltersCount(dataFormatCount, DATA_FORMAT));
		}
		List<Object[]> institutionCount = imageSeriesRepository.countInstitution(orgId);
		if (null != institutionCount && !institutionCount.isEmpty()) {
			filters.putAll(getFiltersCount(institutionCount, INSTITUTION));
		}
		logger.debug("End for getDataFormatAndInstitutionCount"+new Timestamp(System.currentTimeMillis()));
		return filters;
	}

	private Map<String, Object> getModalityAndAnatomyCount(String orgId, Map<String, Object> filters) {
		logger.debug("In REST, getModalityAndAnatomyCount, orgId = " + orgId);
		logger.debug("Started for getModalityAndAnatomyCount"+new Timestamp(System.currentTimeMillis()));
		List<Object[]> modalityCount = imageSeriesRepository.countModality(orgId);
		if (null != modalityCount && !modalityCount.isEmpty()) {
			filters.putAll(getFiltersCount(modalityCount, MODALITY));
		}
		List<Object[]> anatomyCount = imageSeriesRepository.countAnatomy(orgId);
		if (null != anatomyCount && !anatomyCount.isEmpty()) {
			filters.putAll(getFiltersCount(anatomyCount, ANATOMY));
		}
		logger.debug("END for getModalityAndAnatomyCount"+new Timestamp(System.currentTimeMillis()));
		return filters;
	}

	private Map<String, Object> getFiltersCount(List<Object[]> objList, String filter) {
		Map<String, Object> filters = new HashMap<String, Object>();
		Map<String, Long> filterMap = new HashMap<String, Long>();
		objList.stream().forEach((record) -> {
			filterMap.put((String) record[0], (Long) record[1]);
		});
		filters.put(filter, filterMap);
		return filters;
	}

	@Override
	@RequestMapping(value = "/datacatalog/ge-class-data-summary", method = RequestMethod.GET)
	public Map<Object, Object> geClassDataSummary(@RequestParam Map<String, String> params,
			HttpServletRequest request) {
		logger.debug("Get ge class data summary, orgId = " + request.getAttribute("orgId"));

		if (null != request.getAttribute("orgId")) {
			String orgId = request.getAttribute("orgId").toString();
			if (null != orgId && !orgId.isEmpty() && null != params.get(ANNOTATIONS)) {
				String type = params.get("annotations");

				String patternStrAnnotation = ENTITY_NAME;
				Pattern patternAnnotation = Pattern.compile(patternStrAnnotation);
				Matcher matcherAnnotation = patternAnnotation.matcher(type);
				boolean matchFoundAnnotation = matcherAnnotation.matches();
				if (!matchFoundAnnotation) {
					logger.debug("BAD REQUEST : query parameter is not valid");
					return null;
				}

				params.remove(ANNOTATIONS);
				return dataCatalogService.geClassDataSummary(params, orgId, type);
			}
		}
		return null;
	}

	@Override
	@RequestMapping(value = "/datacatalog/data-collection/{id}", method = RequestMethod.DELETE)
	public ApiResponse deleteDataCollection(@PathVariable String id, HttpServletRequest request) {
		ApiResponse apiResponse = null;
		DataSet dataSet = new DataSet();
		try {
			if (null != id && id.length() > 0) {
				String[] idStrings = id.split(",");
				for (int i = 0; i < idStrings.length; i++) {
					dataSet.setId(Long.valueOf(idStrings[i]));
					logger.info("[-----Delete DC " + Long.valueOf(idStrings[i]) + "]");
					List<DataSet> dataSetLst = getDataSetById(Long.valueOf(idStrings[i]), request);
					if (!dataSetLst.isEmpty()) {
						logger.debug(" annLst.size() " + dataSetLst.size());
						dataSetRepository.delete(dataSetLst.get(0));
					} else {
						dataSetRepository.delete(dataSet);
					}
					apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
							ApplicationConstants.SUCCESS, id);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while calling delete data collection ", e);
			apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.BAD_REQUEST_CODE,
					"Id does not exist", id);
		}
		return apiResponse;
	}

	/*
	 *   * (non-Javadoc)   *   * @see
	 * com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/datacatalog/image-series", method = RequestMethod.GET)
	public List<ImageSeries> getImgSetByFilters(@RequestParam Map<String, Object> params) {
		Map<String, Object> validParams = constructValidParams(params, Arrays.asList(ORG_ID, MODALITY, ANATOMY,
				SERIES_INS_UID, DATA_FORMAT, INSTITUTION, EQUIPMENT, ANNOTATIONS, GE_CLASS));
		ResponseBuilder responseBuilder;
		List<ImageSeries> imageSeriesLst = new ArrayList<ImageSeries>();
		try {
			 if(null != validParams ){	
				if (validParams.containsKey(SERIES_INS_UID) && validParams.containsKey(ORG_ID)) {
					logger.debug("Getting img series based on series uid and org id");
					return imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(
							getListOfStringsFromParams(validParams.get(SERIES_INS_UID).toString()),
							getListOfStringsFromParams(validParams.get(ORG_ID).toString()));
				} else if (validParams.containsKey(ORG_ID) && validParams.containsKey(ANNOTATIONS)) {
					logger.debug("Getting img series based on all the filters");
					imageSeriesLst = dataCatalogService.getImgSetByFilters(validParams);
						return getImgSetByAnnotations(imageSeriesLst, params);
				}else if (validParams.containsKey(ORG_ID)) {
					logger.debug("Getting img series based on all the filters");
					return dataCatalogService.getImgSetByFilters(validParams);
				}
			 }
		} catch (ServiceException e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving image set by org id").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Operation failed while retrieving image set by org id").build());
		}
		responseBuilder = Response.ok(imageSeriesLst);
		return (List<ImageSeries>) responseBuilder.build().getEntity();
	}

	private List<ImageSeries> getImgSetByAnnotations(List<ImageSeries> imageSeriesLst,
			Map<String, Object> validParams) {
		List<ImageSeries> imgSetWithAnnotation = new ArrayList<ImageSeries>();
		List<ImageSeries> imgSetWithOutAnn = new ArrayList<ImageSeries>();
		List<String> typeLst;
		try {
			typeLst = getListOfStringsFromParams(validParams.get(ANNOTATIONS).toString());
			if (!typeLst.isEmpty()) {
				// List of image series id based on criteria
				// other than annotation
				List<Long> imgSeriesIdLst = new ArrayList<Long>();
				for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
					ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
					imgSeriesIdLst.add(imageSeries.getId());
				}
				if (typeLst.contains(ABSENT)) {
					return getImageSeriesWithOutAnnotations(imgSetWithOutAnn, imageSeriesLst,
							imgSeriesIdLst);
				} else if (validParams.containsKey(GE_CLASS)) {
					return dataCatalogService.getImgSeries(validParams, imageSeriesLst, typeLst);
				} else {
					List<String> orgIdLst = getListOfStringsFromParams(validParams.get(ORG_ID).toString());
					return getImageSeriesWithAnnotations(imgSetWithAnnotation, imageSeriesLst, typeLst,
							imgSeriesIdLst, orgIdLst);
				}
			}
		} catch (DataCatalogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<ImageSeries>();
	}
}