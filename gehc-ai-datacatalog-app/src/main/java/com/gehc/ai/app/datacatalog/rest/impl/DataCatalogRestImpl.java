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
import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;
import static com.gehc.ai.app.common.constants.ValidationConstants.ANNOTATION_TYPES;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.gehc.ai.app.datacatalog.entity.AnnotationDetails;
import com.gehc.ai.app.datacatalog.entity.AnnotationImgSetDataCol;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.InstitutionSet;
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
@PropertySource({"classpath:application.yml"})
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
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";
    public static final int ORG_ID_LENGTH = 255;

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
            if (annotation.getId() != null) {
                Annotation newAnnotation = annotationRepository.save(annotation);
                logger.info("[In REST, updating annotation with id = " + newAnnotation.getId() + "]");
                apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
                        ApplicationConstants.SUCCESS, newAnnotation.getId().toString());
            } else {
                List<Integer> ids = dataCatalogService.getAnnotationsById(annotation);
                if (ids != null && !ids.isEmpty()) {
                    String idValue = ids.toArray()[0].toString();

                    logger.info("[In REST, Annotation exists so returning annotation with id = " + ids + "]");
                    apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
                            ApplicationConstants.SUCCESS,idValue);
                } else {
                    Annotation newAnnotation = annotationRepository.save(annotation);

                    logger.info("[In REST, Saving new annotation with id = " + newAnnotation.getId() + "]");
                    apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
                            ApplicationConstants.SUCCESS, newAnnotation.getId().toString());
                }
            }

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
    public DataSet saveDataSet(@RequestBody DataSet d, HttpServletRequest request) {
        logger.info("[In REST, Creating new data collection, orgId = " + request.getAttribute("orgId") + "]");
        if (null != request.getAttribute("orgId")) {
            d.setOrgId(request.getAttribute("orgId").toString());
            return dataSetRepository.save(d);
        } else
            return null;
    }


    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "/datacatalog/update-institution", method = RequestMethod.PUT)
    public ApiResponse updateInstitutionByImageSeriesList(@Valid @RequestBody InstitutionSet u, HttpServletRequest request) {
        ApiResponse apiResponse = null;
        logger.info("[In REST, update institution = " + u.getInstitution() + u.getSeriesUIds());
        try {
            if (u.getSeriesUIds().length>0) {
                imageSeriesRepository.updateInstitution(u.getInstitution(), u.getSeriesUIds());
                apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
                        ApplicationConstants.SUCCESS,convertStringArrayToString(u.getSeriesUIds(), ","));
            }
        } catch (Exception e) {
            logger.error("Exception occured while updating institution ", e);
            apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.BAD_REQUEST_CODE,
                    "Id does not exist", convertStringArrayToString(u.getSeriesUIds(), ","));

        }

        return apiResponse;
    }

    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
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
                    return dataCatalogService.getImgSeriesWithPatientByIds(imgSerIdLst);
                  //  return getPatientForImgSeriesLst(imageSeriesRepository.findByIdIn(imgSerIdLst));
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

    @SuppressWarnings({"unchecked", "rawtypes"})
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
        }
        
        ResponseBuilder responseBuilder;
        List<AnnotationImgSetDataCol> annImgSetDCLst = null;
        List<DataSet> dsLst = dataSetRepository.findById(Long.valueOf(id));
        if (null != dsLst && !dsLst.isEmpty()) {
            logger.debug("***** Data set Lst.size() = " + dsLst.size());
            if (null != dsLst.get(0).getImageSets()) {
                List<String> types = new ArrayList<String>();
                types.add(annotationType);
                List<Object> imgSeries = (ArrayList<Object>) ((DataSet) (dsLst.get(0))).getImageSets();
                List<Long> imgSerIdLst = getImgSerIdLst(imgSeries);
                List<ImageSeries> imgSeriesLst = imageSeriesRepository.findByIdIn(imgSerIdLst);
                logger.debug("***** Got img series by id sucessfully");
                if (null != imgSeriesLst && !imgSeriesLst.isEmpty()) {
                    logger.debug(" imgSeriesLst.size() = " + imgSeriesLst.size());
                    Map<Long, ImageSeries> imgSeriesMap = new HashMap<Long, ImageSeries>();
                    for (Iterator<ImageSeries> imgSeriesItr = imgSeriesLst.iterator(); imgSeriesItr.hasNext(); ) {
                        ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
                        imgSeriesMap.put(imageSeries.getId(), imageSeries);
                    }

                    List<Annotation> annotationLst = annotationRepository.findByImageSetIdInAndTypeIn(imgSerIdLst,
                            types);
                    logger.info("***** Got annotationLst by img series id and type");
                    if (null != annotationLst && !annotationLst.isEmpty()) {
                        logger.debug(" annotationLst.size() = " + annotationLst.size());
                        annImgSetDCLst = new ArrayList<AnnotationImgSetDataCol>();
                        for (Iterator<Annotation> annotationItr = annotationLst.iterator(); annotationItr.hasNext(); ) {
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
            logger.debug("Started for ANNOTATIONS_ABSENT" + new Timestamp(System.currentTimeMillis()));
            filters.put(ANNOTATIONS_ABSENT, imageSeriesRepository.countImgWithNoAnn(orgId).get(0));
            logger.debug("END ANNOTATIONS_ABSENT" + new Timestamp(System.currentTimeMillis()));
        } else {
            filters.putAll(getModalityAndAnatomyCount(orgId, filters));
            filters.putAll(getDataFormatAndInstitutionCount(orgId, filters));
            filters.putAll(getEquipmentAndAnnoatationTypeCount(orgId, filters));
        }
        return filters;
    }

    private Map<String, Object> getEquipmentAndAnnoatationTypeCount(String orgId, Map<String, Object> filters) {
        logger.debug("In REST, getEquipmentAndAnnoatationTypeCount, orgId = " + orgId);
        logger.debug("Started for getEquipmentAndAnnoatationTypeCount" + new Timestamp(System.currentTimeMillis()));
        List<Object[]> equipmentCount = imageSeriesRepository.countEquipment(orgId);
        if (null != equipmentCount && !equipmentCount.isEmpty()) {
            filters.putAll(getFiltersCount(equipmentCount, EQUIPMENT));
        }
        List<Object[]> annotationTypeCount = annotationRepository.countAnnotationType(orgId);
        if (null != annotationTypeCount && !annotationTypeCount.isEmpty()) {
            filters.putAll(getFiltersCount(annotationTypeCount, ANNOTATIONS));
        }
        logger.debug("END for getEquipmentAndAnnoatationTypeCount" + new Timestamp(System.currentTimeMillis()));
        return filters;
    }

    private Map<String, Object> getDataFormatAndInstitutionCount(String orgId, Map<String, Object> filters) {
        logger.debug("In REST, getDataFormatAndInstitutionCount, orgId = " + orgId);
        logger.debug("Started for getDataFormatAndInstitutionCount" + new Timestamp(System.currentTimeMillis()));
        List<Object[]> dataFormatCount = imageSeriesRepository.countDataFormat(orgId);
        if (null != dataFormatCount && !dataFormatCount.isEmpty()) {
            filters.putAll(getFiltersCount(dataFormatCount, DATA_FORMAT));
        }
        List<Object[]> institutionCount = imageSeriesRepository.countInstitution(orgId);
        if (null != institutionCount && !institutionCount.isEmpty()) {
            filters.putAll(getFiltersCount(institutionCount, INSTITUTION));
        }
        logger.debug("End for getDataFormatAndInstitutionCount" + new Timestamp(System.currentTimeMillis()));
        return filters;
    }

    private Map<String, Object> getModalityAndAnatomyCount(String orgId, Map<String, Object> filters) {
        logger.debug("In REST, getModalityAndAnatomyCount, orgId = " + orgId);
        logger.debug("Started for getModalityAndAnatomyCount" + new Timestamp(System.currentTimeMillis()));
        List<Object[]> modalityCount = imageSeriesRepository.countModality(orgId);
        if (null != modalityCount && !modalityCount.isEmpty()) {
            filters.putAll(getFiltersCount(modalityCount, MODALITY));
        }
        List<Object[]> anatomyCount = imageSeriesRepository.countAnatomy(orgId);
        if (null != anatomyCount && !anatomyCount.isEmpty()) {
            filters.putAll(getFiltersCount(anatomyCount, ANATOMY));
        }
        logger.debug("END for getModalityAndAnatomyCount" + new Timestamp(System.currentTimeMillis()));
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
                return dataCatalogService.geClassDataSummary(params, orgId);
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
    public List<ImageSeries> getImgSeriesByFilters(@RequestParam Map<String, Object> params) {
    	Map<String, Object> validParams = constructValidParams(params, Arrays.asList(ORG_ID, MODALITY, ANATOMY,
                SERIES_INS_UID, DATA_FORMAT, INSTITUTION, EQUIPMENT, ANNOTATIONS, GE_CLASS, FROM_DATE, TO_DATE));
        ResponseBuilder responseBuilder;
        List<ImageSeries> imageSeriesLst = new ArrayList<ImageSeries>();
        try {
            if (null != validParams) {
                if (validParams.containsKey(SERIES_INS_UID) && validParams.containsKey(ORG_ID)) {
                    logger.debug("Getting img series based on series uid and org id");
                    return imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(
                            getListOfStringsFromParams(validParams.get(SERIES_INS_UID).toString()),
                            getListOfStringsFromParams(validParams.get(ORG_ID).toString()));
                } else if (validParams.containsKey(ORG_ID)) {
                    logger.debug("Getting img series based on all filters");
                    return dataCatalogService.getImgSeriesByFilters(validParams);
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




    private List<Long> getImgSeriesIdsByDSId(@PathVariable Long id) {
        // Note: Coolidge is using this as well
        logger.debug("In REST , Get img series for DC id " + id);
        List<DataSet> dsLst = new ArrayList<DataSet>();
        if (null != id) {
            dsLst = dataSetRepository.findById(id);
            if (null != dsLst && !dsLst.isEmpty()) {
                @SuppressWarnings("unchecked")
                List<Object> imgSeries = (ArrayList<Object>) ((DataSet) (dsLst.get(0))).getImageSets();
                List<Long> imgSerIdLst = new ArrayList<Long>();
                if (!imgSeries.isEmpty()) {
                    for (int i = 0; i < imgSeries.size(); i++) {
                        imgSerIdLst.add(Long.valueOf(imgSeries.get(i).toString()));
                    }
                    return imgSerIdLst;
                }
            }
        }
        return new ArrayList<Long>();
    }

    @Override
    @RequestMapping(value = "/datacatalog/data-collection/{id}/annotation", method = RequestMethod.GET)
    public List<AnnotationDetails> getAnnotationsByDSId(@PathVariable Long id) {
        List<AnnotationDetails> annotationByDSList = new ArrayList<AnnotationDetails>();
        List<Long> imgSerIdLst = getImgSeriesIdsByDSId(id);
        if (!imgSerIdLst.isEmpty()) {
        	annotationByDSList = dataCatalogService.getAnnotationsByDSId(imgSerIdLst);
        }
        return annotationByDSList;
    }
    
    @Override
    @RequestMapping(value = "/datacatalog/image-set/{id}", method = RequestMethod.DELETE)
    public ApiResponse deleteImageSeries(@PathVariable String id) {
        ApiResponse apiResponse = null;
        ImageSeries imgSeries = new ImageSeries();
        try {
            if (null != id && id.length() > 0) {
                String[] idStrings = id.split(",");
                for (int i = 0; i < idStrings.length; i++) {
                    imgSeries.setId(Long.valueOf(idStrings[i]));
                    logger.debug("[-----Delete image series " + Long.valueOf(idStrings[i]) + "]");
                    List<ImageSeries> imgSeriesLst = imageSeriesRepository.findById(Long.valueOf(idStrings[i]));
                    if (!imgSeriesLst.isEmpty()) {
                        logger.debug(" image series size " + imgSeriesLst.size());
                        imageSeriesRepository.delete(imgSeriesLst.get(0));
                    } else {
                    	imageSeriesRepository.delete(imgSeries);
                    }
                    apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
                            ApplicationConstants.SUCCESS, id);
                }
            }
        } catch (Exception e) {
            logger.error("Exception occured while calling delete image series ", e);
            apiResponse = new ApiResponse(ApplicationConstants.FAILURE, ApplicationConstants.BAD_REQUEST_CODE,
                    "Id does not exist", id);
        }
        return apiResponse;
    }
}
