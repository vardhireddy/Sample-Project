package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private ResultActions retrieveResult;
    private String ANNOTATIONS = "[{\"id\":1,\"schemaVersion\":\"1\",\"orgId\":\"Test\",\"annotatorId\":\"123\",\"annotationTool\":\"Tool\",\"annotationDate\":\"2017-03-31\",\"type\":\"type\",\"imageSetId\":1,\"item\":{}}]";

    @Autowired
    public AnnotationSteps(MockMvc mockMvc, StudyRepository studyRepository, PatientRepository patientRepository, ImageSeriesRepository imageSeriesRepository,AnnotationRepository annotationRepository,CommonSteps commonSteps,DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.patientRepository = patientRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.annotationRepository = annotationRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
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
                        .requestAttr("orgId", "12")
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
        when(annotationRepository.findByImageSetId(anyLong())).thenReturn(annotations);
    }

    @When("Get annotation set data by Imageset Id")
    public void getAnnotationSetByImageSetId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation?imagesetid=100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("Verify Get annotation set data by Imageset Id")
    public void verifyAnnotationSetByImageSetId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(ANNOTATIONS)));
    }

    @Given("Get annotation set data - DataSetUp Provided")
    public void givenAnnotationSet() throws Exception {
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetId(anyLong())).thenReturn(annotations);
    }

    @When("Get annotation set data")
    public void getAnnotationSet() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
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
        when(annotationRepository.findByIdIn(anyListOf(Long.class))).thenReturn(annotations);
    }

    @When("Get annotation set data for Ids")
    public void getAnnotationSetById() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("Verify Get annotation set data for Ids")
    public void verifyAnnotationSetById() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(ANNOTATIONS)));
    }

    @Given("Delete annotation set data for Ids - DataSetUp Provided")
    public void givenDeleteAnnotationSetById() throws Exception {
        Annotation annotation = new Annotation();
        doNothing().when(annotationRepository).delete(annotation);

    }

    @When("Delete annotation set data for Ids")
    public void deleteAnnotationSetById() throws Exception {
        retrieveResult = mockMvc.perform(
                delete("/api/v1/annotation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("Verify Delete annotation set data for Ids")
    public void verifyDeleteAnnotationSetById() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }

    @Given("Delete annotation set data for Ids with out org id - DataSetUp Provided")
    public void givenDeleteAnnotationSetByIdWithOutOrgid() throws Exception {
        Annotation annotation = new Annotation();
        doNothing().when(annotationRepository).delete(annotation);
    }

    @When("Delete annotation set data for Ids with out org id")
    public void deleteAnnotationSetByIdWithOutOrgid() throws Exception {
        retrieveResult = mockMvc.perform(
                delete("/api/v1/annotation/1")
                        .contentType(MediaType.APPLICATION_JSON)

        );

    }

    @Then("Verify Delete annotation set data for Ids with out org id")
    public void verifyDeleteAnnotationSetByIdWithOutOrgid() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }

    @Given("Store an annotation set data for throwing exception - DataSetUp Provided")
    public void givenStoreAnAnnotationSetDataForThrowingExceptionDataSetUpProvided() {

        when(annotationRepository.save(any(Annotation.class))).thenThrow(Exception.class);
    }

    @When("Store an annotation set data throws exception")
    public void whenStoreAnAnnotationSetDataThrowsException() throws Exception {
        Annotation annotation = commonSteps.getAnnotation();

        retrieveResult = mockMvc.perform(
                post("/api/v1/annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AnnotationToJSON(annotation))
                        .requestAttr("orgId", "12")
        );
    }

    @Then("Verify Store an annotation set data throws exception")
    public void thenVerifyStoreAnAnnotationSetDataThrowsException() throws Exception {
        retrieveResult.andExpect(content().string(containsString("{\"status\":\"FAILURE\",\"code\":\"500\",\"message\":\"FAILURE\",\"id\":null}")));

    }

    @Given("Delete annotation set data for Ids throws exception - DataSetUp Provided")
    public void givenDeleteAnnotationSetDataForIdsThrowsExceptionDataSetUpProvided() {
        Annotation annotation = new Annotation();
        doNothing().when(annotationRepository).delete(annotation);
    }
    @When("Delete annotation set data for Ids throws exception")
    public void whenDeleteAnnotationSetDataForIdsThrowsException() throws Exception {
        retrieveResult = mockMvc.perform(
                delete("/api/v1/annotation/ ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }
    @Then("Verify Delete annotation set data for Ids throws exception")
    public void thenVerifyDeleteAnnotationSetDataForIdsThrowsException() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Id does not exist")));
    }

    @Given("Get annotation set data for Ids null - DataSetUp Provided")
    public void givenGetAnnotationSetDataForIdsNullDataSetUpProvided() {
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByIdIn(anyListOf(Long.class))).thenReturn(annotations);

    }
    @When("Get annotation set data for Ids null")
    public void whenGetAnnotationSetDataForIdsNull() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation/,")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }
    @Then("Verify Get annotation set data for Ids null")
    public void thenVerifyGetAnnotationSetDataForIdsNull() throws Exception {
//        retrieveResult.andExpect(status().isOk());
//        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    private String AnnotationToJSON(Annotation annotation) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(annotation);
    }

}
