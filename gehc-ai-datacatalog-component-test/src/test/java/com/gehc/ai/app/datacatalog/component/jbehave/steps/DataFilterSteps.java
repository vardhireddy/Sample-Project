/*
 * DataFilterSteps.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.impl.DataCatalogRestImpl;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

/**
 * Created by Litao Yan
 */
@Component
public class DataFilterSteps {
    private final DataSetRepository dataSetRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final StudyRepository studyRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;
    private Throwable throwable = null;
    
    private DataCatalogRestImpl controller;

    private DataCatalogDaoImpl dataCatalogDao;   

    private String IMAGE_SERIES_WITH_INSTITUTIONS = "[{\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF, Institution,Montogmenry\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31 00:00:00\",\"patientDbId\":1}]";
    private String IMAGE_SERIES_WITH_EQUIPMENT = "[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\" \\\"\\\\\\\"Geode Platform\\\\\\\"\\\"\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31 00:00:00\",\"patientDbId\":1}]";
	
    private EntityManager entityManager;
	
    @Autowired
    public DataFilterSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository, CommonSteps commonSteps, DataCatalogInterceptor dataCatalogInterceptor, DataCatalogDaoImpl dataCatalogDao, DataCatalogRestImpl restImpl, EntityManager em) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps =commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
        this.dataCatalogDao =dataCatalogDao;
        this.controller = restImpl;
        this.entityManager = em;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
    }

	ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
	final int limit = 1000;

    @Given("data collection id")
    public void givenImageSetsFromDataCollectionId() throws Exception {
    	List<DataSet> l = new ArrayList<DataSet>();
    	DataSet ds = new DataSet();
    	List<Long> imageSets = new ArrayList<Long>();
    	for (int k = 0; k < 10000; k++) {
    		imageSets.add((long) (Math.random() * 1000000));
    	}
    	l.add(ds);
    	ds.setImageSets(imageSets);
    	when(dataSetRepository.findById(anyLong())).thenReturn(l);
    	controller.setMaxImageSeriesRows(limit);

    }


    @When("Get image series by data collection id")
    public void getImageSetsFromDataCollectionId() throws Exception {

        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/1/image-set")
        );
        org.mockito.Mockito.verify(dataCatalogDao).getImgSeriesWithPatientByIds(argument.capture());

    }

    @Then("verify image series by data collection id is capped")
    public void verifyImageSetsFromDataCollectionId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        assertTrue(argument.getValue().size() == limit);
    }
    
    ArgumentCaptor<Integer> queryArgument = ArgumentCaptor.forClass(Integer.class);
    @Given("image series filter criteria map")
    public void givenImageSeriesFilterCriteria() throws Exception {
    	controller.setMaxImageSeriesRows(limit);
    }
 
    @When("image series filter criteria map")
    public void getImageSeriesFilterCriteria() throws Exception {
    	retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20")
        );
    	System.out.println("llllll" + entityManager.getClass() + "...." + dataCatalogDao.getClass());
    	verify(dataCatalogDao).getImgSeriesByFilters(anyObject(), anyBoolean(), queryArgument.capture());

    }
    
    @Then("verify image series filter criteria map is capped with the limit argument")
    public void verifyImageSeriesFilterCriteria() throws Exception {
        retrieveResult.andExpect(status().isOk());
        
        assertTrue(queryArgument.getValue() == limit);

    }
}