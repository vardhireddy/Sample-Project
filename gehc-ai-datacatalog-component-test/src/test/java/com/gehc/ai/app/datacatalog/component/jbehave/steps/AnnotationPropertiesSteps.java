package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import org.hibernate.service.spi.ServiceException;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/14/17.
 */
@Component
public class AnnotationPropertiesSteps {
    private final MockMvc mockMvc;

    private final PatientRepository patientRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final AnnotationRepository annotationRepository;
    private final CommonSteps commonSteps;
    private final AnnotationPropRepository annotationPropRepository;
    private ResultActions retrieveResult;
    private String AnnotationProp = "[{\"id\":1,\"schemaVersion\":\"123\",\"orgId\":\"123\",\"resourceName\":\"TEST\",\"classes\":null,\"createdDate\":\"2017-03-31\",\"createdBy\":\"test\"},{\"id\":1,\"schemaVersion\":\"123\",\"orgId\":\"123\",\"resourceName\":\"TEST\",\"classes\":null,\"createdDate\":\"2017-03-31\",\"createdBy\":\"test\"}]";

    @Autowired
    public AnnotationPropertiesSteps(MockMvc mockMvc, AnnotationPropRepository annotationPropRepository, PatientRepository patientRepository, ImageSeriesRepository imageSeriesRepository,AnnotationRepository annotationRepository,CommonSteps commonSteps) {
        this.mockMvc = mockMvc;
        this.annotationPropRepository = annotationPropRepository;
        this.patientRepository = patientRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.annotationRepository = annotationRepository;
        this.commonSteps = commonSteps;
    }

    @Given("Store an Annotation Properties set data - DataSetUp Provided")
    public void givenStoreAnAnnotationPropertiesSetDataDataSetUpProvided() {
       AnnotationProperties annotationProperties =  setAnnotationProp();
       when(annotationPropRepository.save(any(AnnotationProperties.class))).thenReturn(annotationProperties);

    }
    @When("Store an Annotation Properties set data")
    public void whenStoreAnAnnotationPropertiesSetData() throws Exception {
        AnnotationProperties annotationProperties =  setAnnotationProp();
        retrieveResult = mockMvc.perform(
                post("/api/v1/annotation-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AnnotationPropertiesToJSON(annotationProperties))
                        .param("org-id", "12")
        );
    }
    
    @Then("Verify Store an Annotation Properties set data")
    public void thenVerifyStoreAnAnnotationPropertiesSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }

    @Given("Get Annotation Properties set data - DataSetUp Provided")
    public void givenGetAnnotationPropertiesSetDataDataSetUpProvided() {
        AnnotationProperties annotationProperties =  setAnnotationProp();
        List<AnnotationProperties> annoPropList =  new ArrayList<AnnotationProperties>();
        annoPropList.add(annotationProperties);
        when(annotationPropRepository.findByOrgId(anyString())).thenReturn(annoPropList);
        //when(annotationPropRepository.findByOrgId(null)).thenReturn(null);
        //doThrow(new Exception()).when(annotationPropRepository).findByOrgId(anyString());
    }

    @When("Get Annotation Properties set data")
    public void whenGetAnnotationPropertiesSetData() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation-properties?orgId=12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );
    }

    @Then("Verify Get Annotation Properties set data")
    public void thenVerifyGetAnnotationPropertiesSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(AnnotationProp)));

    }


    private String AnnotationPropertiesToJSON(AnnotationProperties annotationProperties) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(annotationProperties);
    }

    private AnnotationProperties setAnnotationProp() {
        AnnotationProperties annotationProperties = new AnnotationProperties();
        annotationProperties.setClasses(null);
        annotationProperties.setCreatedBy("test");
        annotationProperties.setCreatedDate(commonSteps.getDate());
        annotationProperties.setId(1L);
        annotationProperties.setOrgId("123");
        annotationProperties.setResourceName("TEST");
        annotationProperties.setSchemaVersion("123");
        return annotationProperties;
    }
}
