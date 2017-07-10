package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.ImageSet;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class DataCatalogSteps {

    private final DataSetRepository dataSetRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;

    @Autowired
    public DataCatalogSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository,ImageSeriesRepository imageSeriesRepository) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;

    }


    @Given("datacatalog health check")
    public void datacatalogHealthCheck() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/dataCatalog/healthCheck")
        );
    }

    @Then("verify success")
    public void verifySuccess() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @When("Get data collection details by its id")
    public void getdataSet() throws Exception {
        dataSetSetUpForId();
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/1234567")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection details by its id")
    public void verifyGetDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }


    @When("Get data collection image-set details by its id")
    public void getdataSetByType() throws Exception {
        dataSetSetUpForImageSet();

        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection image-set details by its id")
    public void verifyGetDatSetByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @When("save DataSet")
    public void saveDataSet() throws Exception {

        DataSet dataSet = getSaveDataSet();
        when(dataSetRepository.save(any(DataSet.class))).thenReturn(dataSet);

        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/data-collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(defnToJSON(dataSet))
                .requestAttr("orgId", "123")

        );

    }

    private DataSet getSaveDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        dataSet.setDescription("test");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "22-01-2017 10:20:56";
        dataSet.setCreatedDate(dateInString);
        dataSet.setName("Test");
        dataSet.setProperties(new Properties());
        dataSet.setOrgId("123");
        dataSet.setSchemaVersion("123");
        dataSet.setType("Annotation");
        return dataSet;
    }

    @Then("verify Saving DataSet")
    public void verifySaveDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"123\",\"createdBy\":\"test\",\"properties\":{}}")));
    }

    private String defnToJSON(DataSet dataSet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dataSet);
    }

    private List<DataSet> getDataSets() {
        List<DataSet> dataSets = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        dataSets.add(dataSet);
        return dataSets;
    }

    private void dataSetSetUpForId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
    }

    private void dataSetSetUpForType() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByTypeAndOrgId(anyString(), anyString())).thenReturn(dataSets);
    }

    private void dataSetSetUpForImageSet() {
        List<DataSet> dataSets = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        ImageSet imageSet =new ImageSet();
        imageSet.setAge("90");
        imageSet.setAnatomy("Chest");
        imageSet.setDataFormat("Test");
        imageSet.setDescription("Test");
        imageSet.setEquipment("Test");
        imageSet.setGender( "Test");
        imageSet.setId("Test");
        imageSet.setInstanceCount(1);
        imageSet.setInstitution("Test");
        imageSet.setModality("Test");
        imageSet.setOrgId("123");
        imageSet.setPatientDbId(anyLong());
        dataSet.setImageSets(imageSet);
        ImageSet imageSet1 =new ImageSet();
        imageSet1.setAge("90");
        imageSet1.setAnatomy("Chest");
        imageSet1.setDataFormat("Test");
        dataSet.setImageSets(imageSet1);
        dataSets.add(dataSet);
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
        List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("TEST");
        imageSeriesList.add(imageSeries);
        when(imageSeriesRepository.findById(1L)).thenReturn(imageSeriesList);
    }
}



