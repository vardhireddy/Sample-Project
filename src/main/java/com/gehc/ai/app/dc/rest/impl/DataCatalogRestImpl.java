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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gehc.ai.app.common.responsegenerator.ResponseGenerator;
import com.gehc.ai.app.dc.entity.AnnotationSet;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.entity.Patient;
import com.gehc.ai.app.dc.entity.Study;
import com.gehc.ai.app.dc.entity.TargetData;
import com.gehc.ai.app.dc.repository.PatientRepository;
import com.gehc.ai.app.dc.repository.StudyRepository;
import com.gehc.ai.app.dc.rest.IDataCatalogRest;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping(value = "/api/v1/dataCatalog")
@PropertySource({"classpath:application.yml"})
public class DataCatalogRestImpl implements IDataCatalogRest {

    private static final String SUCCESS = "SUCCESS";
    public static final String ORG_ID = "orgId";
    public static final String MODALITY = "modality";
    public static final String ANATOMY = "anatomy";
    public static final String ANNOTATIONS = "annotations";

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
        Map<String,String> validParams = constructValidParams(params, Arrays.asList(ORG_ID, MODALITY, ANATOMY, ANNOTATIONS));
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
	public List<DataCollection> getDataCollection(@QueryParam("id") String id) {
		ResponseBuilder responseBuilder;
		List<DataCollection> dataCollection = new ArrayList<DataCollection>();
		try {
			dataCollection = dataCatalogService.getDataCollection(id);
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

	
	@Value("${experiment.targetData.gtMaskLocation}")
	private String gtMaskLocation;
	
	@Value("${experiment.targetData.imgLocation}")
	private String imgLocation;

	@Value("${experiment.targetData.locationType}")
	private String locationType;

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/data-collection-target", method = RequestMethod.GET)
	public Map getExperimentTargetData(@QueryParam("id") String id, @QueryParam("type") String type) {
		//Map m = new LinkedHashMap();
		Map tdmap = new HashMap();
		//System.out.println("gtmask location ==== " + gtMaskLocation);
		//System.out.println("img location ==== " + imgLocation);

		try {
			List<TargetData> l = dataCatalogService.getExperimentTargetData(id);
			if (l.size() > 0) {
				LinkedHashMap fileMap = new LinkedHashMap();
				for (int i = 0; i < l.size(); i++) {
					TargetData td = l.get(i);
					HashMap hm = new HashMap();
					String imgFullPath = td.img;
					
					hm.put("gtMask", td.gtMask.startsWith(gtMaskLocation) ? td.gtMask.substring(gtMaskLocation.length()) : td.gtMask);
					hm.put("img", td.img.startsWith(imgLocation)?td.img.substring(imgLocation.length()) : td.img);
					fileMap.put(td.patientId, hm);
				}

				tdmap.put("files", fileMap);
				tdmap.put("locationType", locationType);
				tdmap.put("gtMaskLocation", gtMaskLocation);
				tdmap.put("imgLocation", imgLocation);
				//m.put("targetData", tdmap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return tdmap;
	}
	
	
    @Autowired
	private PatientRepository patientRepository;
	@Autowired
	private StudyRepository studyRepository;

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/patient", method = RequestMethod.GET)
	public List<Patient> getPatient(@RequestParam Map<String, String> queryMap) {
		Set<String> keys = queryMap.keySet();
		Patient p = new Patient();
		for (Iterator<String> it = keys.iterator(); it.hasNext() ;) {
			String key = it.next();
			if ("patientName".equalsIgnoreCase(key))
				p.setPatientName(queryMap.get(key));
			else if ("patientId".equalsIgnoreCase(key))
				p.setPatientId(queryMap.get(key));
			else if ("birthdate".equalsIgnoreCase(key))
				p.setBirthDate(queryMap.get(key));
			else if ("gender".equalsIgnoreCase(key))
				p.setGender(queryMap.get(key));
			else if ("age".equalsIgnoreCase(key))
				p.setAge(queryMap.get(key));
			else if ("orgId".equalsIgnoreCase(key))
				p.setOrgId(queryMap.get(key));
			else if ("uploadBy".equalsIgnoreCase(key))
				p.setUploadBy(queryMap.get(key));
			else if ("uploadDate".equalsIgnoreCase(key)) {
				String dd = queryMap.get(key);
				int y = Integer.valueOf(dd.substring(0,4)) - 1900;
				int m = Integer.valueOf(dd.substring(4,6)) - 1;
				int day = Integer.valueOf(dd.substring(6));
				p.setUploadDate(new Date(y,m,day));
			}
		}
		return patientRepository.findAll(Example.of(p));
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/patient/{ids}", method = RequestMethod.GET)
	public List<Patient> getPatients(@PathVariable String ids) {
		List<Long> pids = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length;i++)
			pids.add(Long.valueOf(idStrings[i]));
		return patientRepository.findByIdIn(pids);
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/study", method = RequestMethod.GET)
	public List<Study> getStudy(@RequestParam Map<String, String> queryMap) {
//	     {
//	      "schemaVersion" : "v1",
//	      "patientDbId" : 456,
//	      "studyInstanceUid" : "DICOM tag (0020,000D)",
//	      "studyDate" : "DICOM tag (0008,0020)",
//	      "studyTime" : "DICOM tag (0008,0030)",
//	      "studyId" : "DICOM tag (0020,0010)",
//	      "studyDescription" : "DICOM tag (0008,1030)",
//	      "referringPhysician" : "DICOM tag (0008, 0090)",
//	      "studyUrl" : null,
//	      "orgId" : "Should come from UOM",
//	      "uploadDate" : null,
//	      "uploadBy" : "Should come from UOM",
//	      "properties" : {
//	        "anything" : "can go here"
//	      },
		Set<String> keys = queryMap.keySet();
		Study s = new Study();
		String sortCol = null;
		String sortdir = null;
		for (Iterator<String> it = keys.iterator(); it.hasNext() ;) {
			String key = it.next();
			if ("studyInstanceUid".equalsIgnoreCase(key))
				s.setStudyInstanceUid(queryMap.get(key));
			else if ("studyDate".equalsIgnoreCase(key))
				s.setStudyDate(queryMap.get(key));
			else if ("studyTime".equalsIgnoreCase(key))
				s.setStudyTime(queryMap.get(key));
			else if ("studyId".equalsIgnoreCase(key))
				s.setStudyId(queryMap.get(key));
			else if ("patientDbId".equalsIgnoreCase(key))
				s.setPatientDbId(Long.valueOf(queryMap.get(key)));
			else if ("studyDescription".equalsIgnoreCase(key))
				s.setStudyDescription(queryMap.get(key));
			else if ("referringPhysician".equalsIgnoreCase(key))
				s.setReferringPhysician(queryMap.get(key));
			else if ("orgId".equalsIgnoreCase(key))
				s.setOrgId(queryMap.get(key));
			else if ("studyUrl".equalsIgnoreCase(key))
				s.setStudyUrl(queryMap.get(key));
			else if ("uploadBy".equalsIgnoreCase(key))
				s.setUploadBy(queryMap.get(key));
			else if ("uploadDate".equalsIgnoreCase(key)) {
				String dd = queryMap.get(key);
				int y = Integer.valueOf(dd.substring(0,4)) - 1900;
				int m = Integer.valueOf(dd.substring(4,6)) - 1;
				int day = Integer.valueOf(dd.substring(6));
				s.setUploadDate(new Date(y,m,day));
			} else if (key.startsWith("_sort")) {
				sortCol = queryMap.get(key);
				sortdir = key.split(":")[1];
			}
			
		}
		return sortCol == null ? studyRepository.findAll(Example.of(s)) :
			studyRepository.findAll(Example.of(s), new Sort(Direction.fromString(sortdir), sortCol));
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/study/{ids}", method = RequestMethod.GET)
	public List<Study> getStudiesById(@PathVariable String ids) {
		List<Long> pids = new ArrayList<Long>();
		String[] idStrings = ids.split(",");
		for (int i = 0; i < idStrings.length;i++)
			pids.add(Long.valueOf(idStrings[i]));
		return studyRepository.findByIdIn(pids);
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/patient", method = RequestMethod.POST)
	public Patient postPatient(@RequestBody Patient p) {
		p.setUploadDate(new Date(System.currentTimeMillis()));
		patientRepository.save(p);
		return p;
	}

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/study", method = RequestMethod.POST)
	public Study postStudy(@RequestBody Study s) {
		s.setUploadDate(new Date(System.currentTimeMillis()));
		studyRepository.save(s);
		return s;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/patient/{patientId}/study", method = RequestMethod.GET)
	public List<Study> getStudies(@PathVariable String patientId) {
//		return patientRepository.findOne(Long.valueOf(patientId)).getStudies();
		return studyRepository.findByPatientDbId(Long.valueOf(patientId));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/study/{studyId}/image-set", method = RequestMethod.GET)
	public List<ImageSet> getImageSetByStudyId(@PathVariable String studyId) {
//		return patientRepository.findOne(Long.valueOf(patientId)).getStudies();
//		System.err.println("==========finding image sets for study " + studyId );
        ResponseBuilder responseBuilder;
		List<ImageSet> imageSet = new ArrayList<ImageSet>();
		try {

			imageSet = dataCatalogService.getImageSetByStudyId(studyId);
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
}
