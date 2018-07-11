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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.entity.CondensedDataCollection;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataCollectionsCreateRequest;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.InstitutionSet;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.exceptions.InvalidContractException;
import com.gehc.ai.app.datacatalog.rest.request.UpdateUploadRequest;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gehc.ai.app.common.constants.ApplicationConstants;
import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.filters.RequestValidator;
import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.IDataCatalogRest;
import com.gehc.ai.app.datacatalog.rest.request.UpdateContractRequest;
import com.gehc.ai.app.datacatalog.rest.response.AnnotatorImageSetCount;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import com.gehc.ai.app.datacatalog.util.DataCatalogUtils;
import com.gehc.ai.app.datacatalog.util.exportannotations.Shuffle;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;

/**
 * @author 212071558
 */
@RestController
@Api(value = "DataCatalogRestController", description = "REST APIs related to DataCatalog Service")
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
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final int ORG_ID_LENGTH = 255;
    public static final String VIEW = "view";
    private static final String LABEL_SEPARATOR = "+";

    /**
     * this is used to limit the number of rows returned from the DB. we use this to limit the JSON output from the REST in order
     * to restrict the time and payload for the user to receive a response within reasonable amount of time and also avoiding
     * API gateway timeout. The value is specified in the configuration file
     */
    @Value("${spring.data.imageSeries.limit}")
    private int MAX_IMAGE_SERIES_ROWS;

    /**
     * this is used to randomize the output for the data scientist to spot check a subset of the images displayed in the webui
     */
    @Value("${spring.data.imageSeries.randomize}")
    private boolean randomize;

    /**
     * Setter for MAX_IMAGE_SERIES_ROWS property to facilitate unit test using Mockito
     *
     * @param r max # of rows returned
     */
    public void setMaxImageSeriesRows(int r) {
        this.MAX_IMAGE_SERIES_ROWS = r;
    }

    /**
     * Setter for randomize property to facilitate unit test using Mockito
     *
     * @param r true if randomize is called for
     */
    public void setRandomize(boolean r) {
        this.randomize = r;
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
    @Autowired
    private ContractRepository contractRepository;

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
                            ApplicationConstants.SUCCESS, idValue);
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

    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "/datacatalog/data-collection", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON} )
    @Transactional
    public ResponseEntity<?> saveDataSet(@RequestBody DataCollectionsCreateRequest dataCollectionsCreateRequest,
                                         HttpServletRequest request) {

        logger.info("[In REST, Creating new data collection, orgId = " + request.getAttribute("orgId") + "]");

		/* Toll gate checks */

        // Gate 1 - The org ID is required to be defined
        if (request.getAttribute("orgId") == null) {
            return new ResponseEntity<Object>(Collections.singletonMap("response", "An organization ID must be provided"), HttpStatus.BAD_REQUEST);
        }

        // Gate 2 - The data collection must be defined
        DataSet dataCollection = dataCollectionsCreateRequest.getDataSet();
        if (Objects.isNull(dataCollection)) {
            return new ResponseEntity<Object>(Collections.singletonMap("response", "A data collection must be provided"), HttpStatus.BAD_REQUEST);
        }

        // Gate 3 - The data collection's image set IDs must be defined
        List<Long> imageSetIds = dataCollection.getImageSets();
        if (Objects.isNull(imageSetIds)) {
            return new ResponseEntity<Object>(Collections.singletonMap("response", "The data collection must define a set of image set IDs"), HttpStatus.BAD_REQUEST);
        }

        // Gate 4 - The provided image set IDs are required to be unique
        if (imageSetIds.size() != imageSetIds.stream().distinct().count()) {
            return new ResponseEntity<Object>(Collections.singletonMap("response", "The provided image sets are not unique"), HttpStatus.BAD_REQUEST);
        }

        // Gate 5 - The data collection's name must be defined
        if (Objects.isNull(dataCollection.getName())) {
            return new ResponseEntity<Object>(Collections.singletonMap("response", "The data collection must define a name"), HttpStatus.BAD_REQUEST);
        }

        // Gate 6 - The data collection's type must be defined
        if (Objects.isNull(dataCollection.getType())) {
            return new ResponseEntity<Object>(Collections.singletonMap("response", "The data collection must define a type"), HttpStatus.BAD_REQUEST);
        }

		/* All gates passed! */

        dataCollection.setOrgId(request.getAttribute("orgId").toString());

        // Initially assume that no batching of collections is required
        List<DataSet> dataCollectionBatches = new ArrayList<>();
        dataCollectionBatches.add(dataCollection);

        // If a data collection size is specified such that it will produce multiple collections, then batch the image
        // sets based on the specified collection size
        final Integer dataCollectionSize = dataCollectionsCreateRequest.getDataCollectionSize();
        if (shouldBatchDataCollections(dataCollection, dataCollectionSize, imageSetIds.size())) {

            try {
                dataCollectionBatches = DataCatalogUtils.getDataCollectionBatches(dataCollection, dataCollectionsCreateRequest.getDataCollectionSize());
            } catch (DataCatalogException e) {
                logger.error(e.getMessage());
                return new ResponseEntity<Object>(Collections.singletonMap("response", "Failed to batch the provided image sets"), HttpStatus.BAD_REQUEST);
            }

        }

        // Try saving the data collections
        List<DataSet> savedDataCollections;
        try {
            savedDataCollections = dataSetRepository.save(dataCollectionBatches);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(Collections.singletonMap("response", "Failed to save data collections"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return the database-assigned IDs of the saved data collections
        return new ResponseEntity<List<Long>>(aggregateSavedDataCollectionsIds(savedDataCollections), HttpStatus.CREATED);
    }

    private boolean shouldBatchDataCollections(DataSet dataCollection, Integer dataCollectionSize, int numImageSets) {
        final Predicate<DataSet> collectionDoesNotAlreadyExist = dc -> Objects.isNull(dc.getId());
        final Predicate<Integer> collectionIsToBeSplitIntoMultipleCollections = collectionSize -> Objects.nonNull(collectionSize) && collectionSize != numImageSets;

        return collectionDoesNotAlreadyExist.test(dataCollection) && collectionIsToBeSplitIntoMultipleCollections.test(dataCollectionSize);
    }

    private List<Long> aggregateSavedDataCollectionsIds(List<DataSet> savedDataCollections) {
        return savedDataCollections.stream().map(savedDataSet -> savedDataSet.getId())
                .collect(Collectors.toList());
    }

    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "/datacatalog/update-institution", method = RequestMethod.PUT)
    public ApiResponse updateInstitutionByImageSeriesList(@Valid @RequestBody InstitutionSet u, HttpServletRequest request) {
        ApiResponse apiResponse = null;
        logger.info("[In REST, update institution = " + u.getInstitution() + u.getSeriesUIds());
        try {
            if (u.getSeriesUIds().length > 0) {
                imageSeriesRepository.updateInstitution(u.getInstitution(), u.getSeriesUIds());
                apiResponse = new ApiResponse(ApplicationConstants.SUCCESS, Status.OK.toString(),
                        ApplicationConstants.SUCCESS, convertStringArrayToString(u.getSeriesUIds(), ","));
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

    /**
     * given a list of image set id's, return a subset of those id's in a new list. The size and order of the image
     * set ids will be determined by the limit on max # of rows and randomization flag
     *
     * @param imgSeries list of image ids
     * @return list of image series ids given contraints on max number of rows and whether randomization flag is set
     */
    private List<Long> getImageSeriesIdList(List<Long> imgSeries) {
        List<Long> imgSerIdLst = new ArrayList<Long>();

        int size = MAX_IMAGE_SERIES_ROWS > 0 ? Math.min(MAX_IMAGE_SERIES_ROWS, imgSeries.size()) : imgSeries.size();
        int[] shuffled = null;
        if (this.randomize) {
            shuffled = Shuffle.shuffle(imgSeries.size(), size, null);
            for (int i = 0; i < size; i++) {
                imgSerIdLst.add(Long.valueOf(imgSeries.get(shuffled[i]).toString()));
            }
        } else {
            for (int i = 0; i < size; i++) {
                imgSerIdLst.add(Long.valueOf(imgSeries.get(i).toString()));
            }
        }

        return imgSerIdLst;
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
        logger.debug(">>>>>>>>>>>In REST , Get img series for DC id " + id);
        List<DataSet> dsLst = null;
        if (null != id) {
            dsLst = dataSetRepository.findById(id);
            if (null != dsLst && !dsLst.isEmpty()) {
                @SuppressWarnings("unchecked")
                List<Long> imgSeries = ((DataSet) (dsLst.get(0))).getImageSets();


                if (null != imgSeries && !imgSeries.isEmpty()) {
                    List<Long> imgSerIdLst = getImageSeriesIdList(imgSeries);
                    return dataCatalogService.getImgSeriesWithPatientByIds(imgSerIdLst);
                }
            }
        }
        // returns an empty list
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
        Object orgId = request.getAttribute("orgId");
        logger.debug("In REST, Get DC by Id " + id + ", orgId passed = " + orgId);
        return orgId == null ? new ArrayList<DataSet>()
                : dataSetRepository.findByIdAndOrgId(id, orgId.toString());
    }

    @Override
    @RequestMapping(value = "/datacatalog/data-collection", method = RequestMethod.GET)
    public ResponseEntity<?> getDataSetByType(@QueryParam("type") String type, Pageable page, HttpServletRequest request) {
        logger.debug("In REST, Get DC for type " + type);

        /* Common toll gate checks */

        if (Objects.isNull(request.getAttribute("orgId"))) {
            logger.error("BAD REQUEST : data collection type is not valid");
            return new ResponseEntity<Object>(Collections.singletonMap("response", "An org ID must be provided in the request headers"), HttpStatus.BAD_REQUEST);
        }

        /* All common gates passed */

        List<DataSet> matchingDataCollections;

        if (Objects.nonNull(type)) {
            String patternStr = DATA_SET_TYPE;
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(type);
            boolean matchFound = matcher.matches();

            if (!matchFound) {
                logger.error("BAD REQUEST : data collection type is not valid");
                return new ResponseEntity<Object>(Collections.singletonMap("response", "Invalid data collection type was provided"), HttpStatus.BAD_REQUEST);
            }

            matchingDataCollections = dataSetRepository.findByTypeAndOrgIdOrderByCreatedDateDesc(page, type, request.getAttribute("orgId").toString());
        } else {
            matchingDataCollections = dataSetRepository.findByOrgIdOrderByCreatedDateDesc(page, request.getAttribute("orgId").toString());
        }

        return new ResponseEntity<List<CondensedDataCollection>>(CondensedDataCollection.fromDataSetEntities(matchingDataCollections), HttpStatus.OK);
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

    /**
     * @param id
     * @param annotationType
     * @throws WebApplicationException
     */
    public void nonNullCheckForInputParameters(String id, String annotationType) throws WebApplicationException {
        if ((id == null) || (id.length() == 0) || annotationType == null) {
            logger.debug("Datacollection id and annotation type is required to get annotation for a data collection");
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Datacollection id and annotation type is required to get annotation for a data collection")
                    .build());
        }
    }

    /**
     * @param annotationType
     * @param types
     */
    public void setAnnotationTypes(String annotationType, List<String> types) {
        if (annotationType.contains(LABEL_SEPARATOR)) {
            String[] annotations = annotationType.split("\\" + LABEL_SEPARATOR);
            types.addAll(Arrays.asList(annotations));
        } else {
            types.add(annotationType);
        }
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
            filters.putAll(getViewCount(orgId, filters));
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

    private Map<String, Object> getViewCount(String orgId, Map<String, Object> filters) {
        logger.debug("In REST, getViewCount, orgId = " + orgId);
        logger.debug("Started for getViewCount" + new Timestamp(System.currentTimeMillis()));
        List<Object[]> viewCount = imageSeriesRepository.countView(orgId);
        if (null != viewCount && !viewCount.isEmpty()) {
            filters.putAll(getFiltersCount(viewCount, VIEW));
        }
        logger.debug("END for getViewCount" + new Timestamp(System.currentTimeMillis()));
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
     *   * (non-Javadoc)   *   * @see
     * com.gehc.ai.app.dc.rest.IDataCatalogRest#getDataCollection()
     */
    @SuppressWarnings("unchecked")
    @Override
    @RequestMapping(value = "/datacatalog/image-series", method = RequestMethod.GET)
    public List<ImageSeries> getImgSeriesByFilters(@RequestParam Map<String, Object> params) {

        try {
            RequestValidator.validateImageSeriesFilterParamMap(params);
        } catch (DataCatalogException exception) {
            throw new WebApplicationException(exception.getLocalizedMessage());
        }
        Map<String, Object> validParams = constructValidParams(params, Arrays.asList(ORG_ID, MODALITY, ANATOMY,
                SERIES_INS_UID, DATA_FORMAT, INSTITUTION, EQUIPMENT, VIEW, ANNOTATIONS, GE_CLASS, DATE_FROM, DATE_TO));

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
                    return dataCatalogService.getImgSeriesByFilters(validParams, randomize, MAX_IMAGE_SERIES_ROWS);
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
                return dsLst.get(0).getImageSets();
            }
        }
        return new ArrayList<Long>();
    }

    @Override
    @RequestMapping(value = "/datacatalog/data-collection/{id}/annotation", method = RequestMethod.GET)
    public List<AnnotationJson> getAnnotationsByDSId(@PathVariable Long id) throws InvalidAnnotationException {
        List<AnnotationJson> annotationByDSList = new ArrayList<>();
        List<Long> imgSerIdLst = getImgSeriesIdsByDSId(id);

        annotationByDSList = dataCatalogService.getAnnotationDetailsByImageSetIDs(imgSerIdLst);

        return annotationByDSList;
    }

    @Override
    @RequestMapping(value = "/datacatalog/data-collection/{id}/annotation/csv", method = RequestMethod.GET)
    public ResponseEntity<String> exportAnnotationsAsCsv(final HttpServletResponse response, @PathVariable Long id) {
        logger.debug("Exporting annotations as CSV for data collection " + id);
        response.setHeader("Content-Disposition", "attachment; filename=filename.csv");
        response.setContentType("text/csv");

        try {
            // Get all image set IDs for the provided data collection ID
            List<Long> imgSerIdLst = getImgSeriesIdsByDSId(id);

            // Get the annotation details as CSV for all retrieved image set IDs
            String csvResponse = csvResponse = dataCatalogService.getAnnotationDetailsAsCsvByImageSetIDs(imgSerIdLst);

            // Finally return the response containing the CSV
            return new ResponseEntity<String>(csvResponse, HttpStatus.OK);
        } catch (InvalidAnnotationException | CsvConversionException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all Contracts", httpMethod = "GET", response = List.class, tags = "Get all Contracts")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = List.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract", method = RequestMethod.GET)
    public ResponseEntity<List<Contract>> getAllContracts(HttpServletRequest request) {

        logger.debug("Get all contracts");

        List<Contract> contracts = new ArrayList<Contract>();

        /* Toll gate checks */

        // Gate 1 - The HttpServletRequest object must be accessible.  Otherwise, we can't extract the org ID
        if (Objects.isNull(request)) {
            return new ResponseEntity(Collections.singletonMap("response", "HTTP request object was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Gate 2 - The org ID is required to be defined
        if (Objects.isNull(request.getAttribute("orgId"))) {
            return new ResponseEntity(Collections.singletonMap("response", "An organization ID must be provided"), HttpStatus.BAD_REQUEST);
        }

        try {
            String orgId = request.getAttribute("orgId").toString();

            logger.debug("Get all contracts -  org Id = "+orgId);

            contracts = dataCatalogService.getAllContracts(orgId);
        } catch (InvalidContractException ice) {
            logger.error("Could not get the contracts due to an internal error ", ice.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", "Internal error occured : " + ice.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            logger.error("Could not get the contracts due to an internal error ", e.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", "Could not get the contracts due to an internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(contracts, HttpStatus.OK);
    }

    /**
     * API to fetch contract
     *
     * @param
     * @return
     */
    @Override
    @ApiOperation(value = "Get Contract By Id ", httpMethod = "GET", response = Contract.class, tags = "Retrieve Contract")
    @ApiResponses(value = {
    		@io.swagger.annotations.ApiResponse(code = 200, message = "Success|OK", response = Contract.class),
    		@io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
    		@io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
    		@io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
    		@io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
    		@io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
    		@io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
    		@io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
    		@io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
    		@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
    		@io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract/{contractId}", method = RequestMethod.GET)
    public ResponseEntity<Contract> getContracts(@ApiParam(value = "Id of Contract") @PathVariable(value = "contractId") Long contractId) {
        Contract contract;
        try {
            RequestValidator.validateContractId(contractId);
        } catch (DataCatalogException exception) {
            logger.error("Exception occured while validating the contract ", exception.getMessage());
            return new ResponseEntity(Collections.singletonMap("response","Please pass a valid contract ID"), HttpStatus.BAD_REQUEST);
        }

        try {
            contract = dataCatalogService.getContract(contractId);
        } catch (Exception e) {
            logger.error("Exception retrieving the contract ", e.getMessage());
            return new ResponseEntity(Collections.singletonMap("response","Exception retrieving the contract"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (contract == null || contract.getActive() == null){
            return new ResponseEntity(Collections.singletonMap("response","No Contract Exists with the given Id."), HttpStatus.BAD_REQUEST);
        }else if (contract.getActive().equalsIgnoreCase("false")) {
            return new ResponseEntity(Collections.singletonMap("response", "Contract associated with given Id is inactive"), HttpStatus.OK);
        }else return new ResponseEntity<>(contract, HttpStatus.OK);

    }

    @Override
    @RequestMapping(value = "/datacatalog/image-set/annotated-image-set-count-by-user", method = RequestMethod.GET)
    public ResponseEntity<List<AnnotatorImageSetCount>> getCountOfImagesAnnotated(
            @RequestParam(value = "orgId") String orgId) {
        logger.info("Passing the org Id to get count of images annotated : {}", orgId);
        List<AnnotatorImageSetCount> responseList = new ArrayList<>();

        List<Object[]> resultSet;

        try {
            resultSet = annotationRepository.getCountOfImagesAnnotated(orgId);
        } catch (Exception e) {
            logger.error("Exception retrieving data in getCountOfImagesAnnotated : {}", e.getMessage());
            return new ResponseEntity("Internal Server error. Please contact the corresponding service assistant.",
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

        resultSet.stream().forEach(record -> {

            AnnotatorImageSetCount annotatorImageSetCount = new AnnotatorImageSetCount(record[0].toString(),
                    Integer.valueOf(record[1].toString()));
            responseList.add(annotatorImageSetCount);

        });

        if (responseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Verify if contract is valid", httpMethod = "GET", response = Contract.class, tags = "Verify if contract is valid")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Contract.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract/{contractId}/validate", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> validateContractByIdAndOrgId(@PathVariable("contractId") Long contractId,
                                                                            @RequestParam("orgId") String orgId) {

        logger.info("Passing in contract Id and Org Id for validation.");

        Contract contract;
        try {
            contract = contractRepository.findByIdAndOrgId(contractId, orgId);
        } catch (Exception e) {
            logger.error("Error validating given parameters : {}", e.getMessage());
            return new ResponseEntity("Internal Server error. Please contact the corresponding service assistant.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (contract == null) {
            return new ResponseEntity<>(Collections.singletonMap("response", "Contract does not exist"), HttpStatus.NOT_FOUND);
        }
            boolean isContractExpired = ContractByDataSetId.isContractExpired(contract.getAgreementBeginDate(),contract.getDataUsagePeriod());

        if (contract.getActive().equalsIgnoreCase("false") || isContractExpired)
        {
            return new ResponseEntity<>(Collections.singletonMap("response", "Contract is inactive/expired"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(Collections.singletonMap("response", "Contract exists"), HttpStatus.OK);
    }

	/*
     * (non-Javadoc)
	 *
	 * @see com.gehc.ai.app.datacatalog.rest.IDataCatalogRest#
	 * getImgSeriesIdsByFilters(java.util.Map)
	 */

    @SuppressWarnings("unchecked")
    @Override
    @RequestMapping(value = "/datacatalog/image-series/ids", method = RequestMethod.GET)
    public List<Long> getImgSeriesIdsByFilters(@RequestParam Map<String, Object> params) {
        logger.info("[getImgSeriesIdsByFilters] Getting img series ids based on all filters");
        try {
            RequestValidator.validateImageSeriesFilterParamMap(params);
        } catch (DataCatalogException exception) {
            throw new WebApplicationException(exception.getLocalizedMessage());
        }
        Map<String, Object> validParams = constructValidParams(params, Arrays.asList(ORG_ID, MODALITY, ANATOMY,
                SERIES_INS_UID, DATA_FORMAT, INSTITUTION, EQUIPMENT, VIEW, ANNOTATIONS, GE_CLASS, DATE_FROM, DATE_TO));
        try {
            if (null != validParams) {
                logger.info("[getImgSeriesIdsByFilters] validParams is not null");
                if (validParams.containsKey(ORG_ID)) {
                    logger.info("Getting img series ids based on all filters");
                    return dataCatalogService.getImgSeriesIdsByFilters(validParams);
                }
            }
        } catch (ServiceException e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Operation failed while retrieving image set ids by org id").build());
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Operation failed while retrieving image set ids by org id").build());
        }
        return new ArrayList<Long>();
    }

	@Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Contract", httpMethod = "POST", response = Contract.class, tags = "Create Contract")
    @ApiResponses(value = {
    		@io.swagger.annotations.ApiResponse(code = 201, message = "Created", response = Contract.class),
    		@io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
    		@io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
    		@io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
    		@io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
    		@io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
    		@io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
    		@io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
    		@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
    		@io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
	public ResponseEntity<?> saveContract(@RequestBody Contract contract, HttpServletRequest request) {
        logger.debug("Creating a new contract.");

			/* Toll gate checks */

            // Gate 1 - The HttpServletRequest object must be accessible.  Otherwise, we can't extract the org ID
			if(Objects.isNull(request)){
                return new ResponseEntity<Object>(Collections.singletonMap("response", "HTTP request object was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
			}

            // Gate 2 - The org ID is required to be defined
            if (Objects.isNull(request.getAttribute("orgId"))) {
                return new ResponseEntity<Object>(Collections.singletonMap("response", "An organization ID must be provided"), HttpStatus.BAD_REQUEST);
            }

	        // Gate 3 - The contract must be defined
	        if(Objects.isNull(contract)){
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "A contract must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 4 - An agreement name must be provided
	        if (Objects.isNull(contract.getAgreementName()) || contract.getAgreementName().isEmpty()) {
	        	return new ResponseEntity<Object>(Collections.singletonMap("response", "An agreement name must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 5 - The primary contact email must be provided
	        if (Objects.isNull(contract.getPrimaryContactEmail()) || contract.getPrimaryContactEmail().isEmpty()) {
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "A primary contact email must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 6 - The de-identified status must be provided
	        if (Objects.isNull(contract.getDeidStatus())) {
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "The de-identified status must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 7 - An agreement begin date must be provided
	        if (Objects.isNull(contract.getAgreementBeginDate()) || contract.getAgreementBeginDate().isEmpty()) {
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "An agreement begin date must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 8 - The data usage period must be provided
	        if (Objects.isNull(contract.getDataUsagePeriod()) || contract.getDataUsagePeriod().isEmpty()) {
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "The data usage period must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 9 - The data origin country and state must be provided
	        if (Objects.isNull(contract.getDataOriginCountriesStates()) || contract.getDataOriginCountriesStates().isEmpty()) {
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "The data origin country and state must be provided"), HttpStatus.BAD_REQUEST);
	        }

	        // Gate 10 - The data use cases must be provided
	        if (Objects.isNull(contract.getUseCases()) || contract.getUseCases().isEmpty()) {
	            return new ResponseEntity<Object>(Collections.singletonMap("response", "The data use cases must be provided"), HttpStatus.BAD_REQUEST);
	        }

            // Gate 11 - The data allowed location must be provided
            if (Objects.isNull(contract.getDataLocationAllowed())) {
                return new ResponseEntity<Object>(Collections.singletonMap("response", "The data allowed location must be provided"), HttpStatus.BAD_REQUEST);
            }

	        /* All gates passed! */
	        // Now save the contract
	        Contract contractObj;
			try {
			    contract.setOrgId(request.getAttribute("orgId").toString());
			    contract.setActive("true");
			    contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);
				contractObj = contractRepository.save(contract);
			} catch (Exception e1) {
				logger.error(e1.getMessage());
                return new ResponseEntity<Object>(Collections.singletonMap("response", "Could not save contract due to an internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(null != contractObj){
				logger.debug("Contract id " + contractObj.getId());
			}
            // Return the recently saved contract
            return new ResponseEntity<Contract>(contractObj, HttpStatus.CREATED);
	}

    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update Contract", httpMethod = "PUT", response = Contract.class, tags = "Update Contract")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Contract.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract/{contractId}", method = RequestMethod.PUT)
    public ResponseEntity<Contract> updateContract(@PathVariable Long contractId,
                                                   @RequestBody UpdateContractRequest updateRequest) {

        logger.info("Validating contract update request body.");

        if (validateUpdateRequest(updateRequest) == false) {
            return new ResponseEntity(Collections.singletonMap("response", "Update request cannot be empty. Either status or uri must be provided."), HttpStatus.BAD_REQUEST);
        }

        Contract contractToBeUpdated;
        try {
            contractToBeUpdated = dataCatalogService.getContract(contractId);
        } catch (Exception e) {
            logger.error("Exception retrieving the contract ", e.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", "Exception retrieving the contract."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (validateContractTobeUpdated(contractToBeUpdated) == false) {
            logger.error("Invalid/Inactive contract ID passed in request : {}", contractId);
            return new ResponseEntity(Collections.singletonMap("response", "Given contract ID does not exist or is inactive."), HttpStatus.BAD_REQUEST);
        } else

        {
            contractToBeUpdated = updateContractHelper(contractToBeUpdated, updateRequest);
        }

        try {
            contractToBeUpdated = dataCatalogService.saveContract(contractToBeUpdated);
        } catch (Exception e1) {
            logger.error("Exception saving the contract object", e1.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", "Exception saving the updated contract."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(contractToBeUpdated, HttpStatus.OK);
    }

    private boolean validateUpdateRequest(UpdateContractRequest updateRequest) {
        if (updateRequest == null
                || ((updateRequest.getUploadStatus() == null)
                        && (updateRequest.getUri() == null || updateRequest.getUri().isEmpty()))) {
            return false;
        }
        return true;
    }

    private boolean validateContractTobeUpdated(Contract contractToBeUpdated) {

        if (contractToBeUpdated == null
                || contractToBeUpdated.getActive() == null
                || (contractToBeUpdated.getActive().equalsIgnoreCase("false"))) {
            return false;
        } else {
            return true;
        }
    }

    private Contract updateContractHelper(Contract contractToBeUpdated, UpdateContractRequest updateRequest) {

        contractToBeUpdated.setUploadStatus((updateRequest.getUploadStatus() == null
                ? contractToBeUpdated.getUploadStatus()
                : updateRequest.getUploadStatus()));
        contractToBeUpdated.setUri((updateRequest.getUri() == null || updateRequest.getUri().isEmpty())
                ? contractToBeUpdated.getUri()
                : updateRequest.getUri());

        return contractToBeUpdated;
    }


    @SuppressWarnings("unchecked")
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete Contract by Id", httpMethod = "DELETE", response = Contract.class, tags = "Delete Contract by Id")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Contract.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract/{contractId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> deleteContract(@PathVariable("contractId") Long contractId,
                                                              HttpServletRequest httpServletRequest) {

        logger.info("Passing contract id to delete contract :", contractId);
        String orgId = "";
        try {
            orgId = RequestValidator.getOrgIdFromAuth(httpServletRequest);
        }catch (DataCatalogException e)
        {
            logger.error("Error validating servlet request : {}",e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("response", "Request cannot be validated because of malformed authorization token."),e.getHttpStatusCode());
        }

        String status = "false";

        Contract contractToBeDeleted;
        try {
            contractToBeDeleted = contractRepository.findOne(contractId);
        } catch (Exception e) {
            logger.error("Error retrieving contract to delete: {}", e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("response", "Error retrieving contract to delete. Please contact the corresponding service assistant."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            RequestValidator.validateContractToBeDeleted(contractToBeDeleted,contractId,orgId);
        }catch (DataCatalogException e)
        {
            logger.error("Error validating contract : {}",e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("response", e.getMessage()),e.getHttpStatusCode());
        }

        try {
            contractToBeDeleted.setActive(status);
            contractRepository.save(contractToBeDeleted);
        } catch (Exception e) {
            logger.error("Error deleting the contract : {}", e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("response", "Error deleting the contract. Please contact the corresponding service assistant."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Collections.singletonMap("response", "Contract is inactivated successfully"), HttpStatus.OK);

    }

    @SuppressWarnings("unchecked")
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get Contracts for given data-collection Id", httpMethod = "GET", response = HashMap.class, tags = "Get Contracts for given data-collection Id")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = HashMap.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/contract/data-collection/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getContractsForDataCollection(@PathVariable("id") Long dataCollectionId) {

        logger.info("Data collection/set ID to get contracts id :{}", dataCollectionId);

        Map<String,List<ContractByDataSetId>> resultListOfContracts;

        try {
            resultListOfContracts = dataCatalogService.getContractsByDataCollectionId(dataCollectionId);
        }catch (Exception e){
            logger.error("Error retrieving contracts associated with the data collection : {}", e.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", "Error retrieving contracts associated with the data collection." +
                    " Please contact the corresponding service assistant."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultListOfContracts.get("active").isEmpty() && resultListOfContracts.get("inactive").isEmpty())
        {
            return new ResponseEntity(Collections.singletonMap("response", "No contracts exist for the given data collection ID."), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(resultListOfContracts, HttpStatus.OK);
    }

    @Override
    @ApiOperation(value = "Create Upload", httpMethod = "POST", response = Upload.class, tags = "Create Upload")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Created", response = Upload.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/upload", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON}, consumes = {MediaType.APPLICATION_JSON})
    public ResponseEntity<?> createUpload(@RequestBody Upload uploadRequest){

        logger.info("Passing upload request to create upload entity.");

        Upload uploadResponse;
        try {
            uploadResponse = dataCatalogService.createUpload(uploadRequest);
        }catch (DataCatalogException e)
        {
            logger.error(e.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", e.getMessage()),e.getHttpStatusCode());
        }catch (Exception e)
        {
            logger.error("Exception saving the upload entity : {}", e.getMessage());
            return new ResponseEntity(Collections.singletonMap("response", "Exception saving the upload entity." +
                    " Please contact the corresponding service assistant."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(uploadResponse,HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all upload entities", httpMethod = "GET", response = List.class, tags = "Retrieve Upload entities")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success|OK", response = List.class),
            @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/upload", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    @Override
    public ResponseEntity<?> getAllUploads(HttpServletRequest httpServletRequest){

        logger.info("Passing http request for validation.");

        String orgId = "";

        List<Upload> listOfUploadEntities;
        try {
            orgId = RequestValidator.getOrgIdFromAuth( httpServletRequest);
            listOfUploadEntities = dataCatalogService.getAllUploads(orgId);

        } catch ( DataCatalogException e )
        {
                logger.error( "Error validating servlet request : {}", e.getMessage() );
                return new ResponseEntity<>( Collections.singletonMap( "response", "Request cannot be validated because of malformed authorization token." ), HttpStatus.FORBIDDEN );

        } catch ( Exception e ) {
                logger.error( "Error retrieving upload entities : {}", e.getMessage() );
                e.printStackTrace();
                return new ResponseEntity<>( Collections.singletonMap( "response", "Error retrieving upload entities." + " Please contact the corresponding service assistant." ),
                                             HttpStatus.INTERNAL_SERVER_ERROR );
        }

        return new ResponseEntity<>(listOfUploadEntities,HttpStatus.OK);
    }

    @Override
    @ApiOperation(value = "Get Upload By Id ", httpMethod = "GET", response = Upload.class, tags = "Retrieve Upload")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success|OK", response = Upload.class),
            @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/upload/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    public ResponseEntity<?> getUploadById(@ApiParam(value = "Id of Upload") @PathVariable(value = "id") Long uploadId,
                                                HttpServletRequest httpServletRequest) {

        logger.info( "Passing upload ID to retrieve upload details." );

        String orgId = "";
        Upload upload;
        try {
            orgId = RequestValidator.getOrgIdFromAuth( httpServletRequest );
            RequestValidator.validateUploadId( uploadId );
            upload = dataCatalogService.getUploadById( uploadId );
        } catch ( DataCatalogException exception ) {
            logger.error( "Exception validating the request authorisation.", exception.getMessage() );
            return new ResponseEntity( Collections.singletonMap( "response",
                                                                 exception.getMessage()), exception.getHttpStatusCode() );
        }catch ( Exception e ) {
            logger.error( "Exception retrieving the upload entity ", e.getMessage() );
            e.printStackTrace();
            return new ResponseEntity( Collections.singletonMap( "response", "Exception retrieving the upload entity." ), HttpStatus.INTERNAL_SERVER_ERROR );
        }


        if (upload == null || upload.getId() == null){
            return new ResponseEntity(Collections.singletonMap("response","No Upload Exists with the given Id."), HttpStatus.NOT_FOUND);
        }else if (!upload.getOrgId().equals(orgId))
        {
            return new ResponseEntity( Collections.singletonMap( "response", "User does not have access to the requested upload data."), HttpStatus.FORBIDDEN );
        }
        else
        {
            return new ResponseEntity<>(upload, HttpStatus.OK);
        }

    }

    @Override
    @ApiOperation(value = "Validate if Upload exists", httpMethod = "GET", response = Upload.class, tags = "Retrieve Upload")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success|OK", response = Upload.class),
            @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/upload/validate", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    public ResponseEntity<?> getUploadByQueryParameters(@RequestParam("spaceId") String spaceId,
                                                        @RequestParam("orgId" ) String orgId,
                                                        @RequestParam(value = "contractId", required = false) Long contractId ){

            logger.info( "Passing query parameters to retrieve upload details." );

            Upload upload;
            try {
                    upload = dataCatalogService.getUploadByQueryParameters(spaceId, orgId, contractId);
            } catch ( DataCatalogException e ) {
                    logger.error( "Exception validating the request.", e.getMessage() );
                    return new ResponseEntity( Collections.singletonMap( "response", e.getMessage()), e.getHttpStatusCode() );
            }catch ( Exception e ) {
                    logger.error( "Exception retrieving the upload entity ", e.getMessage() );
                    e.printStackTrace();
                    return new ResponseEntity( Collections.singletonMap( "response", "Exception retrieving the upload entity." ), HttpStatus.INTERNAL_SERVER_ERROR );
            }

            return new ResponseEntity<>(upload, HttpStatus.OK);
        }


    @Override
    @ApiOperation(value = "Update Upload Entity ", httpMethod = "PUT", response = Upload.class, tags = "Update Upload")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success|OK", response = Upload.class),
            @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "UnAuthorized"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 405, message = "Method Not Allowed"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable"),
            @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),
            @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported Media Type"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad Gateway") })
    @RequestMapping(value = "/datacatalog/upload", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON}, consumes = {MediaType.APPLICATION_JSON})
    public ResponseEntity<?> updateUpload(@RequestBody UpdateUploadRequest updateRequest){

        logger.info( "Passing update upload request parameters." );

        Upload upload;
        try {
            upload = dataCatalogService.updateUploadEntity(updateRequest);
        } catch ( DataCatalogException e ) {
            logger.error( "Exception validating the update upload request.", e.getMessage() );
            return new ResponseEntity( Collections.singletonMap( "response", e.getMessage()), e.getHttpStatusCode() );
        }catch ( Exception e ) {
            logger.error( "Exception updating the upload entity ", e.getMessage() );
            e.printStackTrace();
            return new ResponseEntity( Collections.singletonMap( "response", "Exception updating the upload entity." ), HttpStatus.INTERNAL_SERVER_ERROR );
        }

        return new ResponseEntity<>(upload, HttpStatus.OK);
    }

}
