package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyLong;
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
public class ImageSetSteps {
    private final DataSetRepository dataSetRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final StudyRepository studyRepository;
    private final CommonSteps commonSteps;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;

    @Autowired
    public ImageSetSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository,CommonSteps commonSteps) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps =commonSteps;


    }

    @Given("Retrieve image series by id - DataSetUp Provided")
    public void givenImageSeriesById() throws Exception {
        dataSetUpImageSeriesById();
    }

    @When("Get image series by image series id")
    public void getImageSeriesById() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify image series by image series id")
    public void verifyImageSeriesById() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Retrieve image series by series instance uid - DataSetUp Provided")
    public void givenImageSeriesBySeriesInstanceId() throws Exception {
        dataSetUpImageSeriesBySeriesInstanceId();

    }

    @When("Get image series by series instance uid")
    public void getImageSeriesBySeriesInstanceId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?series-instance-uid=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify image series by series instance uid")
    public void verifyImageSeriesBySeriesInstanceId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Store an image set data - DataSetUp Provided")
    public void givenStoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.save(any(ImageSeries.class))).thenReturn(imageSeries.get(0));
    }

    @When("Store an image set data")
    public void StoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries = commonSteps.getImageSeries();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(imageSeriesToJSON(imageSeries.get(0)))
                        .requestAttr("orgId", "123")
        );

    }

    @Then("verify Store an image set data")
    public void verifyStoreAnImageSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());

        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }


    @Given("Get Image set based on filter criteria with SUID - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteria() throws Exception {
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(commonSteps.getImageSeries());
    }


    @When("Get Image set based on filter criteria with SUID")
    public void getImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?series-instance-uid=1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

    }

    @Then("verify Image set based on filter with SUID")
    public void verifyImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with ORG ID and other - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIn(anyListOf(String.class))).thenReturn(annList);
    }


    @When("Get Image set based on filter criteria with ORG ID and other")
    public void getImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267-d195-499f-bfd8-7d92875c7035&modality=CT&annotations=point&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
        );

    }

    @Then("verify Image set based on filter  with ORG ID and other")
    public void verifyImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }
    private String imageSeriesToJSON(ImageSeries imageSeries) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageSeries);
    }

    private void dataSetUpByFilter() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(imgSerLst);
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),
                anyListOf(String.class), anyListOf(String.class))).thenReturn(imgSerLst);
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIn(anyListOf(String.class))).thenReturn(annotations);

    }

    private void dataSetUpImageSeriesById() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesBySeriesInstanceId() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(imgSerLst);
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }


}
