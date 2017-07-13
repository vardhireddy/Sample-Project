package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;

    @Autowired
    public ImageSetSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;


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
        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
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
        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
    }

    @Given("Store an image set data - DataSetUp Provided")
    public void givenStoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries = getImageSeries();
        when(imageSeriesRepository.save(any(ImageSeries.class))).thenReturn(imageSeries.get(0));
    }

    @When("Store an image set data")
    public void StoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries = getImageSeries();
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

        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
    }


    @Given("Get Image set based on filter criteria - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteria() throws Exception {
        //add needed mocks
    }


    @When("Get Image set based on filter criteria")
    public void getImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?orgId=61939267-d195-499f-bfd8-7d92875c7035&modality=CT&annotations=point&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify Image set based on filter criteria")
    public void verifyImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
    }

    private String imageSeriesToJSON(ImageSeries imageSeries) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageSeries);
    }

    private void dataSetUpImageSeriesById() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesBySeriesInstanceId() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(imgSerLst);
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }

    private List<ImageSeries> getImageSeries() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setDataFormat("dataFormat");
        imageSeries.setUri("tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10");
        imageSeries.setSeriesInstanceUid("1");
        imageSeries.setInstitution("UCSF");
        imageSeries.setEquipment("tem");
        imageSeries.setInstanceCount(1);
        imageSeries.setUploadBy("BDD");
        imageSeries.setPatientDbId(1L);
        Properties prop = new Properties();
        prop.setProperty("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }

    private String expectedImageSeries() {
        String imageSeries = "{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}";
        return imageSeries;
    }
}
