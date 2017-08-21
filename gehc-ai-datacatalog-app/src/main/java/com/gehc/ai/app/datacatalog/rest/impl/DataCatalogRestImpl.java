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

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
//import COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.IDataCatalogRest;

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

	public static final String ORG_ID = "org-id";
	public static final String MODALITY = "modality";
	public static final String ANATOMY = "anatomy";
	public static final String ANNOTATIONS = "annotations";
	public static final String SERIES_INS_UID = "series-instance-uid";
	public static final String ABSENT = "absent";

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

	private Set<Long> getUniqueImgSetIds(List<Annotation> annotationLst) {
		Set<Long> uniqueImgSetIds = new HashSet<Long>();
		if (null != annotationLst && !annotationLst.isEmpty()) {
			logger.info("*** Annotations from list " + annotationLst.toString());
			for (Iterator<Annotation> annotationItr = annotationLst.iterator(); annotationItr.hasNext();) {
				Annotation annotation = (Annotation) annotationItr.next();
				uniqueImgSetIds.add(annotation.getImageSet().getId());
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
		logger.info("*** In REST getPatients, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Patient>()
				: patientRepository.findByIdInAndOrgId(getListOfLongsFromParams(ids),
						request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/study", method = RequestMethod.GET)
	public List<Study> getStudy(HttpServletRequest request) {
		logger.info("*** In REST get all studies, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Study>()
				: studyRepository.findByOrgId(request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/study/{ids}", method = RequestMethod.GET)
	public List<Study> getStudiesById(@PathVariable String ids, HttpServletRequest request) {
		logger.info("*** In REST getStudiesById, orgId = " + request.getAttribute("orgId"));
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
			List<Study> studyLst= studyRepository.findByOrgIdAndStudyInstanceUid(s.getOrgId(), s.getStudyInstanceUid());
			if (studyLst != null && !studyLst.isEmpty()) {
				return studyLst.get(0);
			}
		}else if (null == s || null == s.getOrgId()) {
			throw new DataCatalogException("Missing study info");
		}
		s.setUploadDate(new Date(System.currentTimeMillis()));
		return studyRepository.save(s);
	}

	@Override
	@RequestMapping(value = "/annotation", method = RequestMethod.GET)
	public List<Annotation> getAnnotationsByImgSet(@QueryParam("imagesetid") Long imagesetid) {
		// Note: this is being used in C2M as well
		if (null != imagesetid) {
			return annotationRepository.findByImageSetId(Long.valueOf(imagesetid));
		} else {
			return new ArrayList<Annotation>();
		}
	}

//	@Override
//	@RequestMapping(value = "/annotation/image-set", method = RequestMethod.POST)
//	public List<Annotation> getAnnotationsByImgSet(@RequestBody ImageSeries imageSet) {
//		logger.info("In getAnnotationsByImgSet");
//		// Note: this is being used in C2M as well
//		if (null != imageSet ) {
//			logger.info("------In getAnnotationsByImgSet");
//			return annotationRepository.findByImageSet(imageSet);
//		} else {
//			return new ArrayList<Annotation>();
//		}
//	}
	
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
	public ApiResponse deleteAnnotation(@PathVariable String ids) {
		// C2M is using get annotation by ids so this has been removed from interceptor
		ApiResponse apiResponse = null;
		Annotation ann = new Annotation();
		try {
			if (null != ids && ids.length() > 0) {
				String[] idStrings = ids.split(",");
				for (int i = 0; i < idStrings.length; i++) {
					ann.setId(Long.valueOf(idStrings[i]));
						logger.info(" -----Delete annotation " + Long.valueOf(idStrings[i]) );
						//Get annotation object as somehow it was crying for org_id is null
						List<Annotation> annLst = getAnnotationsById(idStrings[i], null);
						logger.info(" annLst.size() " + annLst.size() );
						if(null != annLst && !annLst.isEmpty()){
							annotationRepository.delete(annLst.get(0));
						}else{
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
			if (null != patLst && !patLst.isEmpty()) {
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
	public List<ImageSeries> getImgSeriesByDSId(@PathVariable Long id) {
		// Note: Coolidge is using this as well
		logger.info("Get img series for DC id " + id);
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
						imageSeriesLst = getImageSeriesList(validParams, imgSetWithAnnotation, imgSetWithOutAnn);
						if (!imageSeriesLst.isEmpty())
							return imageSeriesLst;
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

	private List<ImageSeries> getImageSeriesList(Map<String, String> validParams,
			List<ImageSeries> imgSetWithAnnotation, List<ImageSeries> imgSetWithOutAnn) {
		List<ImageSeries> imageSeriesLst;
		List<String> orgIdLst = getListOfStringsFromParams(validParams.get(ORG_ID));
		List<ImageSeries> patientImageSeriesLst = new ArrayList<ImageSeries>();
		imageSeriesLst = getImageSeriesListWithValidParamsAndOrgId(validParams, orgIdLst);

		// Get the data with annotation filter
		if (validParams.containsKey(ANNOTATIONS)) {
			List<String> typeLst = getListOfStringsFromParams(validParams.get(ANNOTATIONS));
			if (!typeLst.isEmpty()) {
				// List of image series id based on criteria
				// other than annotation
				//List<String> imgSeriesIdLst = new ArrayList<String>();
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
							imgSeriesIdLst);
				}
			}
		}

		patientImageSeriesLst = getPatientForImageSeriesWithOrWithOutAnn(validParams, imgSetWithAnnotation,
				imgSetWithOutAnn, imageSeriesLst);
		return patientImageSeriesLst;
	}

	private List<ImageSeries> getPatientForImageSeriesWithOrWithOutAnn(Map<String, String> validParams,
			List<ImageSeries> imgSetWithAnnotation, List<ImageSeries> imgSetWithOutAnn,
			List<ImageSeries> imageSeriesLst) {
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

	private List<ImageSeries> getImageSeriesListWithValidParamsAndOrgId(Map<String, String> validParams,
			List<String> orgIdLst) {
		List<ImageSeries> imageSeriesLst;
		if (validParams.containsKey(MODALITY)) {
			List<String> modalityLst = getListOfStringsFromParams(validParams.get(MODALITY));
			if (validParams.containsKey(ANATOMY)) {
				List<String> anatomyLst = getListOfStringsFromParams(validParams.get(ANATOMY));
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(orgIdLst, anatomyLst,
						modalityLst);
			} else {
				imageSeriesLst = imageSeriesRepository.findByOrgIdInAndModalityIn(orgIdLst, modalityLst);
			}
		} else if (validParams.containsKey(ANATOMY)) {
			List<String> anatomyLst = getListOfStringsFromParams(validParams.get(ANATOMY));
			imageSeriesLst = imageSeriesRepository.findByOrgIdInAndAnatomyIn(orgIdLst, anatomyLst);
		} else {
			imageSeriesLst = imageSeriesRepository.findByOrgIdIn(orgIdLst);
		}
		return imageSeriesLst;
	}

	private List<ImageSeries> getImageSeriesWithAnnotations(List<ImageSeries> imgSetWithAnnotation,
		List<ImageSeries> imageSeriesLst, List<String> typeLst, List<Long> imgSeriesIdLst) {
		List<Annotation> annotationLst = new ArrayList<Annotation>();
		annotationLst = annotationRepository.findByImageSetIdInAndTypeIn(imgSeriesIdLst, typeLst);
		Set<Long> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
		logger.info("Get uniqueImgSetIds.size() " + uniqueImgSetIds.size());
		if (!uniqueImgSetIds.isEmpty()) {
			for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
				ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
				if (uniqueImgSetIds.contains(imageSeries.getId())) {
					imgSetWithAnnotation.add(imageSeries);
				}
			}
		}
		return imgSetWithAnnotation;
	}

	private List<ImageSeries> getImageSeriesWithOutAnnotations(List<ImageSeries> imgSetWithOutAnn,
			List<ImageSeries> imageSeriesLst, List<Long> imgSeriesIdLst) {
		List<Annotation> annotationLst = new ArrayList<Annotation>();
		annotationLst = annotationRepository.findByImageSetIdIn(imgSeriesIdLst);
		Set<Long> uniqueImgSetIds = getUniqueImgSetIds(annotationLst);
		logger.info("# Get uniqueImgSetIds size() " + uniqueImgSetIds.size());
		if (!uniqueImgSetIds.isEmpty()) {
			for (Iterator<ImageSeries> imgSeriesItr = imageSeriesLst.iterator(); imgSeriesItr.hasNext();) {
				ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
				if (!uniqueImgSetIds.contains(imageSeries.getId())) {
					imgSetWithOutAnn.add(imageSeries);
				}
			}
		}
		return imgSetWithOutAnn;
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
			if (null != imageSeries && null != imageSeries.getPatientDbId()) {
				List<Patient> patientLst = patientRepository.findById(imageSeries.getPatientDbId());
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
		return imageSeriesRepository.findById(id);
	}

	@Override
	@RequestMapping(value = "/datacatalog/study/{studyId}/image-set", method = RequestMethod.GET)
	public List<ImageSeries> getImgSeriesByStudyDbId(@PathVariable Long studyId, HttpServletRequest request) {
		return request.getAttribute("orgId") == null ? new ArrayList<ImageSeries>()
				: imageSeriesRepository.findByStudyDbIdAndOrgId(studyId, request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient/{ids}/study", method = RequestMethod.GET)
	public List<Study> getStudiesByPatientDbid(@PathVariable String ids, HttpServletRequest request) {
		logger.info("*** In REST getStudiesByPatientDbid, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Study>()
				: studyRepository.findByPatientDbIdAndOrgId(Long.valueOf(ids),
						request.getAttribute("orgId").toString());
	}

	@Override
	@RequestMapping(value = "/datacatalog/patient", method = RequestMethod.GET)
	public List<Patient> getAllPatients(HttpServletRequest request) {
		logger.info(" In REST getAllPatients, orgId = " + request.getAttribute("orgId"));
		return request.getAttribute("orgId") == null ? new ArrayList<Patient>()
				: patientRepository.findByOrgId(request.getAttribute("orgId").toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/datacatalog/raw-target-data", method = RequestMethod.GET)
	public List getRawTargetData(@QueryParam("id") String id, @QueryParam("annotationType") String annotationType) {
		logger.info(" Entering method getRawTargetData --> id: " + id + " Type: " + annotationType);
		// Note: works fine with new DC which has image sets as Array of Longs
		if ((id == null) || (id.length() == 0)) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Datacollection id is required to get annotation for a data collection").build());
		}
		ResponseBuilder responseBuilder;
		List<AnnotationImgSetDataCol> annImgSetDCLst = null;
		List<DataSet> dsLst = dataSetRepository.findById(Long.valueOf(id));
		if (null != dsLst && !dsLst.isEmpty()) {
			logger.info("* dsLst.size() = " + dsLst.size());
			if (null != dsLst.get(0).getImageSets()) {
				List<String> types = new ArrayList<String>();
				types.add(annotationType);
				List<ImageSeries> imgSeriesLst = imageSeriesRepository
						.findByIdIn((List<Long>) dsLst.get(0).getImageSets());
				if (null != imgSeriesLst && !imgSeriesLst.isEmpty()) {
					logger.info(" imgSeriesLst.size() = " + imgSeriesLst.size());
					Map<Long, ImageSeries> imgSeriesMap = new HashMap<Long, ImageSeries>();
					for (Iterator<ImageSeries> imgSeriesItr = imgSeriesLst.iterator(); imgSeriesItr.hasNext();) {
						ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
						imgSeriesMap.put(imageSeries.getId(), imageSeries);
					}
//					List<String> imgSerIdStrLst = new ArrayList<String>();
//					for (Iterator<Long> imgSerIdItr = ((List<Long>) dsLst.get(0).getImageSets()).iterator(); imgSerIdItr
//							.hasNext();) {
//						imgSerIdStrLst.add(imgSerIdItr.next().toString());
//					}
//					List<Annotation> annotationLst = annotationRepository.findByImageSetInAndTypeIn(imgSerIdStrLst,
//							types);
					List<ImageSeries> imgSerIdStrLst = new ArrayList<ImageSeries>();
					for (Iterator<Long> imgSerIdItr = ((List<Long>) dsLst.get(0).getImageSets()).iterator(); imgSerIdItr
							.hasNext();) {
						ImageSeries imgSer = new ImageSeries();
						imgSer.setId(imgSerIdItr.next());
						imgSerIdStrLst.add(imgSer);
					}
					List<Annotation> annotationLst = annotationRepository.findByImageSetInAndTypeIn(imgSerIdStrLst,
							types);
					if (null != annotationLst && !annotationLst.isEmpty()) {
						logger.info(" annotationLst.size() = " + annotationLst.size());
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
			logger.info(" annImgSetDCLst.size() = " + annImgSetDCLst.size());
			responseBuilder = Response.ok(annImgSetDCLst);
			return (List) responseBuilder.build().getEntity();
		}

		return (List) new ArrayList<AnnotationImgSetDataCol>();

	}
	
	@Override
	@RequestMapping(value = "/datacatalog/query", method = RequestMethod.GET)
	public Map<String, Object> filters(@QueryParam("orgId") String orgId) {
		logger.info("Get filters for orgId = " + orgId);
		Map<String, Object> filters = new HashMap<String, Object>();
		List<Object[]> modalityCount = imageSeriesRepository.countModality(orgId);
		if (null != modalityCount && !modalityCount.isEmpty()) {
			filters.putAll(getFiltersCount(modalityCount, MODALITY));
		}
		List<Object[]> anatomyCount = imageSeriesRepository.countAnatomy(orgId);
		if (null != anatomyCount && !anatomyCount.isEmpty()) {
			filters.putAll(getFiltersCount(anatomyCount, ANATOMY));
		}
		List<Object[]> annotationTypeCount = annotationRepository.countAnnotationType(orgId);
		 /*Object[]  annotationAbsent = new Object[] {
	                "absent",12L};
		 annotationTypeCount.add(annotationAbsent);*/
		
		if (null != annotationTypeCount && !annotationTypeCount.isEmpty()) {
			filters.putAll(getFiltersCount(annotationTypeCount, ANNOTATIONS));
		}
		return filters;
	}
	
	private Map<String, Object> getFiltersCount(List<Object[]> objList, String filter){
		Map<String, Object> filters = new HashMap<String, Object>();
			Map<String, Long> filterMap = new HashMap<String, Long>();
			objList.stream().forEach((record) -> {
				filterMap.put((String) record[0], (Long) record[1]);
			});
			filters.put(filter, filterMap);
			return filters;
	}
	
	@Override
	@RequestMapping(value = "/datacatalog/img-set-with-no-ann", method = RequestMethod.GET)
	public List imgSetWithNoAnn(String orgId) {
		List<Long> countImgWithNoAnn = imageSeriesRepository.countImgWithNoAnn(orgId);
		logger.info("imgSetWithNoAnn count = " + countImgWithNoAnn.get(0));
		return countImgWithNoAnn;
	}
}
