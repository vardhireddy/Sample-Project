package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Study;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private ResultActions retrieveResult;

    @Autowired
    public StudySteps(MockMvc mockMvc, StudyRepository studyRepository, ImageSeriesRepository imageSeriesRepository,CommonSteps commonSteps) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.commonSteps = commonSteps;
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
                        .param("org-id", "12")
        );

    }

    @Then("verify Imageset by study")
    public void verifyImagesetByStudy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get all Studies - DataSetUp Provided")
    public void givenGetMultipleStudiesDataSetUpProvided() {
        dataStudyByOrgId();
    }
    @When("Get all Studies")
    public void whenGetMultipleStudies() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );
    }
    @Then("verify Get all Studies")
    public void thenVerifyGetMultipleStudies() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(" ")));
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }



    private void dataStudyByStudyId() {
        List<Study> studyList = getStudy();
        when(studyRepository.findByIdIn(anyListOf(Long.class))).thenReturn(studyList);
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

    private Date getDate() {
        String str = "2017-03-31";
        return Date.valueOf(str);
    }

}
