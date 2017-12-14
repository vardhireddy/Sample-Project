package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Study;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/13/17.
 */
@Component
public class StudySteps {
    private final MockMvc mockMvc;
    private final StudyRepository studyRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private ResultActions retrieveResult;
    private String STUDY = "{\"id\":1,\"schemaVersion\":\"123\",\"patientDbId\":1,\"studyInstanceUid\":\"123\",\"studyDate\":\"\",\"studyTime\":\"\",\"studyId\":\"123\",\"studyDescription\":\"Test\",\"referringPhysician\":\"John Doe\",\"studyUrl\":\"http://test\",\"orgId\":\"123\",\"uploadDate\":\"2017-03-31\",\"uploadBy\":\"Test\",\"properties\":{}}";
    private String NEW_STUDY = "{\"id\":1,\"schemaVersion\":\"123\",\"patientDbId\":1,\"studyInstanceUid\":\"123\",\"studyDate\":\"\",\"studyTime\":\"\",\"studyId\":null,\"studyDescription\":\"Test\",\"referringPhysician\":\"John Doe\",\"studyUrl\":\"http://test\",\"orgId\":\"123\",\"uploadDate\":\"2017-03-31\",\"uploadBy\":\"Test\",\"properties\":{}}";
    private String STUDIES  = "{\"id\":1,\"schemaVersion\":\"123\",\"patientName\":\"Late Lucy\",\"patientId\":\"123\",\"birthDate\":\"09-09-1950\",\"gender\":\"M\",\"age\":\"90\",\"orgId\":\"123\",\"uploadDate\":\"2017-03-31\",\"uploadBy\":\"BDD\",\"properties\":\"any\"},{\"id\":2,\"schemaVersion\":\"123\",\"patientName\":\"Late Lucy\",\"patientId\":\"123\",\"birthDate\":\"09-09-1950\",\"gender\":\"M\",\"age\":\"90\",\"orgId\":\"123\",\"uploadDate\":\"2017-03-31\",\"uploadBy\":\"BDD\",\"properties\":\"any\"}";
    private Throwable throwable = null;
    @Autowired
    public StudySteps(MockMvc mockMvc, StudyRepository studyRepository, ImageSeriesRepository imageSeriesRepository,CommonSteps commonSteps,DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor =dataCatalogInterceptor;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
    }

    @Given("Get Imageset by study - DataSetUp Provided")
    public void givenImagesetByStudy() throws Exception {
        dataSetUpImagesetByStudy();

    }

    @When("Get Imageset by study")
    public void getImagesetByStudy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/study/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify Imageset by study")
    public void verifyImagesetByStudy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get all Studies - DataSetUp Provided")
    public void givenGetAllStudiesDataSetUpProvided() {
        dataStudyByOrgId();
    }

    @When("Get all Studies")
    public void whenGetAllStudies() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }
    @Then("verify Get all Studies")
    public void thenVerifyGetAllStudies() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("["+STUDY+"]")));
    }

    @Given("Save study - DataSetUp Provided")
    public void givenSaveStudyDataSetUpProvided() {
        when(studyRepository.findByOrgIdAndStudyInstanceUid(anyString(),anyString())).thenReturn(null);
        when(studyRepository.save(any(Study.class))).thenReturn(getStudy().get(0));
    }

    @When("Save study")
    public void whenSaveStudy() throws Exception {
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studyToJSON(getStudyWithOutStudyId().get(0)))
                        .requestAttr("orgId", "12")
        );
    }
    @Then("verify Save study")
    public void thenVerifySaveStudy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(STUDY)));
    }

    @Given("Save new study - DataSetUp Provided")
    public void givenSaveNewStudyDataSetUpProvided() {
        when(studyRepository.findByOrgIdAndStudyInstanceUid(anyString(),anyString())).thenReturn(new ArrayList());
        when(studyRepository.save(any(Study.class))).thenReturn(getStudy().get(0));
    }

    @When("Save new study")
    public void whenSaveNewStudy() throws Exception {
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studyToJSON(getStudyWithOutStudyId().get(0)))
                        .requestAttr("orgId", "12")
        );
    }
    @Then("verify Save new study")
    public void thenVerifySaveNewStudy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(STUDY)));
    }

    @Given("Get Multiple Studies - DataSetUp Provided")
    public void givenGetMultipleStudiesDataSetUpProvided() {
        dataStudyByStudyIds();
    }

    @When("Get Multiple Studies")
    public void whenGetMultipleStudies() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/study/1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }
    @Then("verify Get Multiple Studies")
    public void thenVerifyGetMultipleStudies() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("["+STUDY+"]")));

    }

    @Given("Get Multiple Studies with orgid null- DataSetUp Provided")
    public void givenGetMultipleStudiesWithOrgidNullDataSetUpProvided() {
        dataStudyByStudyIds();
        HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        when(mockedRequest.getAttribute("orgId")).thenReturn(null);

    }
    @When("Get Multiple Studies with orgid null")
    public void whenGetMultipleStudiesWithOrgidNull() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/study/1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", null)
        );
    }
    @Then("verify Get Multiple Studies  with orgid null")
    public void thenVerifyGetMultipleStudiesWithOrgidNull() throws Exception {
        retrieveResult.andExpect(content().string((containsString("{\"status\":\"FAILURE\""))));
    }

    @Given("Save existing study - DataSetUp Provided")
    public void givenSaveExistingStudyDataSetUpProvided() {
        List<Study> studyList = getStudy();
        when(studyRepository.findByOrgIdAndStudyInstanceUid(anyString(),anyString())).thenReturn(studyList);
        when(studyRepository.save(any(Study.class))).thenReturn(getStudy().get(0));
    }
    @When("Save existing study")
    public void whenSaveExistingStudy() throws Exception {
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studyToJSON(getStudyWithOutStudyId().get(0)))

        );
    }
    @Then("verify Save study should return existing study if it already exists")
    public void thenVerifySaveStudyShouldReturnExistingStudyIfItAlreadyExists() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(STUDY)));
    }

    @Given("Save Study with Null Study id - DataSetUp Provided")
    public void givenSaveStudyWithNullStudyIdDataSetUpProvided() {
        List<Study> studyList = getStudy();
        when(studyRepository.findByOrgIdAndStudyInstanceUid(anyString(),anyString())).thenReturn(studyList);
        when(studyRepository.save(any(Study.class))).thenReturn(getStudy().get(0));
    }

    @When("save Study with Null Study id")
    public void whenSaveStudyWithNullStudyId() throws Exception {
        try{
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studyToJSON(getNullStudy()))

        );}
        catch(Exception e){
            throwable = e;
        }
    }
    @Then("verify Saving Study with Null Study id")
    public void thenVerifySavingStudyWithNullStudyId() throws Exception {
        assert(throwable.toString().contains("Missing study info"));

    }

    private String studyToJSON(Study study) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(study);
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }



//    private void dataStudyByStudyId() {
//        List<Study> studyList = getStudy();
//        when(studyRepository.findByIdIn(anyListOf(Long.class))).thenReturn(studyList);
//    }

    private void dataStudyByStudyIds() {
        List<Study> studyList = getStudy();
//        Study study = new Study();
//        study.setId(2L);
//        study.setOrgId("124");
//        study.setPatientDbId(1L);
//        study.setProperties(new Properties());
//        study.setReferringPhysician("Early Emma");
//        studyList.add(study);
        when(studyRepository.findByIdInAndOrgId(anyListOf(Long.class), anyString())).thenReturn(studyList);
    }

    private void dataStudyByOrgId() {
        List<Study> studyList = getStudy();
        when(studyRepository.findByOrgId(anyString())).thenReturn(studyList);
    }

    private List<Study> getStudy() {
        List<Study> studyLst = new ArrayList<Study>();
        Study study = new Study();
        study.setId(1L);
        study.setOrgId("123");
        study.setPatientDbId(1L);
        study.setProperties(new Properties());
        study.setReferringPhysician("John Doe");
        study.setSchemaVersion("123");
        study.setStudyDate("");
        study.setStudyDescription("Test");
        study.setStudyInstanceUid("123");
        study.setStudyId("123");
        study.setStudyTime("");
        study.setStudyUrl("http://test");
        study.setUploadBy("BDD");
        Date date = getDate();
        study.setUploadDate(date);
        study.setUploadBy("Test");
        studyLst.add(study);
        return studyLst;
    }

    private List<Study> getStudyWithOutStudyId() {
        List<Study> studyLst = new ArrayList<Study>();
        Study study = new Study();
        study.setOrgId("123");
        study.setPatientDbId(1L);
        study.setProperties(new Properties());
        study.setReferringPhysician("John Doe");
        study.setSchemaVersion("123");
        study.setStudyDate("");
        study.setStudyDescription("Test");
        study.setStudyInstanceUid("123");
        study.setStudyTime("");
        study.setStudyUrl("http://test");
        study.setUploadBy("BDD");
        Date date = getDate();
        study.setUploadDate(date);
        study.setUploadBy("Test");
        studyLst.add(study);
        return studyLst;
    }

    private Study getNullStudy(){
        Study study = new Study();
        study.setId(null);
        study.setOrgId(null);
        study.setPatientDbId(1L);
        study.setProperties(new Properties());
        study.setReferringPhysician("John Doe");
        study.setSchemaVersion("123");
        study.setStudyDate("");
        study.setStudyDescription("Test");
        study.setStudyInstanceUid("123");
        study.setStudyId("123");
        study.setStudyTime("");
        study.setStudyUrl("http://test");
        study.setUploadBy("BDD");
        Date date = getDate();
        study.setUploadDate(date);
        study.setUploadBy("Test");
        study.setStudyInstanceUid(null);
        return study;
    }

    private Date getDate() {
        String str = "2017-03-31";
        return Date.valueOf(str);
    }

}
