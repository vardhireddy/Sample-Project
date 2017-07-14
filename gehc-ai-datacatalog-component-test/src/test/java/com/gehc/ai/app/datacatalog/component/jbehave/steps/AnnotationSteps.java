package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.common.responsegenerator.ApiResponse;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSet;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
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
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/13/17.
 */
@Component
public class AnnotationSteps {
    private final MockMvc mockMvc;
    private final StudyRepository studyRepository;
    private final PatientRepository patientRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final AnnotationRepository annotationRepository;
    private final CommonSteps commonSteps;
    private ResultActions retrieveResult;

    @Autowired
    public AnnotationSteps(MockMvc mockMvc, StudyRepository studyRepository, PatientRepository patientRepository, ImageSeriesRepository imageSeriesRepository,AnnotationRepository annotationRepository,CommonSteps commonSteps) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.patientRepository = patientRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.annotationRepository = annotationRepository;
        this.commonSteps = commonSteps;
    }

    @Given("Store an annotation set data - DataSetUp Provided")
    public void givenStoreAnAnnotationSetDataDataSetUpProvided() {
        when(annotationRepository.save(any(Annotation.class))).thenReturn(commonSteps.getAnnotation());
    }
    @When("Store an annotation set data")
    public void whenStoreAnAnnotationSetData() throws Exception {
        Annotation annotation = commonSteps.getAnnotation();

        retrieveResult = mockMvc.perform(
                post("/api/v1/annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AnnotationToJSON(annotation))
                        .param("org-id", "12")
        );
    }

    @Then("Verify Store an annotation set data")
    public void thenVerifyStoreAnAnnotationSetData() throws Exception{
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }
    @Given("Get annotation set data by Imageset Id - DataSetUp Provided")
    public void givenAnnotationSetByImageSetId() throws Exception {
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSet(anyString())).thenReturn(annotations);
    }

    @When("Get annotation set data by Imageset Id")
    public void getAnnotationSetByImageSetId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation?imagesetid=100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("Verify Get annotation set data by Imageset Id")
    public void verifyAnnotationSetByImageSetId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[{\"id\":1,\"schemaVersion\":\"1\",\"annotatorId\":\"123\",\"annotationDate\":\"2017-03-31\",\"type\":\"type\",\"imageSet\":\"imageSet\",\"item\":\"item\"}]")));
    }

    @Given("Get annotation set data - DataSetUp Provided")
    public void givenAnnotationSet() throws Exception {
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSet(anyString())).thenReturn(annotations);
    }

    @When("Get annotation set data")
    public void getAnnotationSet() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("Verify Get annotation set data")
    public void verifyAnnotationSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Get annotation set data for Ids - DataSetUp Provided")
    public void givenAnnotationSetById() throws Exception {
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        Annotation annotation = new Annotation();
        annotation.setId(2L);
        annotation.setAnnotatorId("1234");
        annotation.setImageSet("imageSet2");
        annotations.add(annotation);
        when(annotationRepository.findByIdIn(anyList())).thenReturn(annotations);
    }

    @When("Get annotation set data for Ids")
    public void getAnnotationSetById() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation/1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("Verify Get annotation set data for Ids")
    public void verifyAnnotationSetById() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    private String AnnotationToJSON(Annotation annotation) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(annotation);
    }

}
