package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.repository.*;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
       when(annotationPropRepository.save(annotationProperties)).thenReturn(annotationProperties);
        
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
        retrieveResult.andExpect(content().string(containsString("test")));
    }

    private String AnnotationPropertiesToJSON(AnnotationProperties annotationProperties) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(annotationProperties);
    }

    private AnnotationProperties setAnnotationProp() {
        AnnotationProperties annotationProperties = new AnnotationProperties();
        annotationProperties.setClasses(new Object());
        annotationProperties.setCreatedBy(" ");
        annotationProperties.setCreatedDate(commonSteps.getDate());
        annotationProperties.setId(1L);
        annotationProperties.setOrgId("123");
        annotationProperties.setResourceName("TEST");
        annotationProperties.setSchemaVersion("123");
        return annotationProperties;
    }
}
