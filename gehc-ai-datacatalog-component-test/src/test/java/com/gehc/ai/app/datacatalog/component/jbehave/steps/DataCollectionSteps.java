package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.*;
import com.gehc.ai.app.datacatalog.repository.*;
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
import java.sql.Date;
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
public class DataCollectionSteps {

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
    public DataCollectionSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository,CommonSteps commonSteps) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps = commonSteps;


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

    @Given("Retrieve DataCatalog with ID DataSetUp Provided")
    public void givenDataSetForId() throws Exception {
        dataCollectionSetUpForId();
    }

    @When("Get data collection details by its id")
    public void getdataSetForId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/1234567")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection details by its id")
    public void verifyGetDatSetForId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @Given("Retrieve DataCatalog with Org ID DataSetUp Provided")
    public void givenDataSetForOrgId() throws Exception {
        dataCollectionSetUpForOrgId();
    }

    @When("Get data collection by Org Id")
    public void getdataSet() throws Exception {

        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection by Org Id")
    public void verifyGetDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

   @Given("Retrieve Image Set with ID DataSetUp Provided when no imageset")
   public void givenGetImageSet() throws Exception {
       dataCollectionSetUpForImageSet();
   }

    @When("Get data collection image-set details by its id when no imageset")
    public void getdataSetByType() throws Exception {

        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection image-set details by its id when no imageset")
    public void verifyGetDatSetByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Save DataSet - DataSetUp Provided")
    public void givenSaveDataSet() throws Exception {
        DataSet dataSet = getSaveDataSet();
        when(dataSetRepository.save(any(DataSet.class))).thenReturn(dataSet);
    }

    @When("save DataSet")
    public void saveDataSet() throws Exception {
        DataSet dataSet = getSaveDataSet();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/data-collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(defnToJSON(dataSet))
                        .param("org-id", "12")
        );

    }

    @Then("verify Saving DataSet")
    public void verifySaveDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"123\",\"createdBy\":\"test\",\"properties\":{}}")));
    }

    @Given("Retrieve DataSet by Type DataSetUp Provided")
    public void givenDataSetByType() throws Exception {
        dataCollectionSetUpByType();
    }

    @When("Get data collection by Type -  Annotation")
    public void getDataCollectionByType() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection?type=Annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection by Type -  Annotation")
    public void verifydataCollectionByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @Given("DataCatalog Raw Target Data - DataSetUp Provided")
    public void givenDataCatalogRawTargetDataDataSetUpProvided() {
        List<DataSet> dataSets = getDataSetsWithImageSet();
        when(dataSetRepository.findById(anyLong())).thenReturn(dataSets);
        //List<ImageSeries> imageSeriesList =  new ArrayList<ImageSeries>();


        when(imageSeriesRepository.findByIdIn(anyListOf(Long.class))).thenReturn(commonSteps.getImageSeries());
        Annotation ann = commonSteps.getAnnotation();
        HashMap item = new HashMap();
        item.put("test","test");
        ann.setItem(item);
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(ann);
        when(annotationRepository
                .findByImageSetInAndTypeIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(annotations);

    }
    @When("get DataCatalog Raw Target Data")
    public void whenGetDataCatalogRawTargetData() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/raw-target-data?id=1&annotationType=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );
    }
    @Then("verify DataCatalog Raw Target Data")
    public void thenVerifyDataCatalogRawTargetData() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[{\"dcId\":\"1\",\"imId\":\"1\",\"annotationId\":\"1\",\"patientDbid\":\"1\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"annotationType\":\"type\",\"annotationItem\":{\"test\":\"test\"},\"annotatorId\":\"123\",\"annotationDate\":\"2017-03-31\"}]")));

    }

    @Given("Retrieve Image Set with ID DataSetUp Provided")
    public void givenRetrieveImageSetWithIDDataSetUpProvided() {
        dataCollectionSetUpForImageSetwithData();
    }

    @When("Get data collection image-set details by its id")
    public void whenGetDataCollectionImagesetDetailsByItsId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );
    }
    @Then("verify data collection image-set details by its id")
    public void thenVerifyDataCollectionImagesetDetailsByItsId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}]")));
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

    private List<DataSet> getDataSetsWithImageSet() {
        List<DataSet> dataSets = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        //ImageSeries imageSet = commonSteps.getImageSeries().get(0);
        List testList = new ArrayList();
        testList.add("1");
        dataSet.setImageSets(testList);
        dataSets.add(dataSet);
        return dataSets;
    }


    private void dataCollectionSetUpByType() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByTypeAndOrgIdOrderByCreatedDateDesc(anyString(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForOrgId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByOrgIdOrderByCreatedDateDesc(anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForType() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByTypeAndOrgIdOrderByCreatedDateDesc(anyString(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForImageSet() {
        List<DataSet> dataSets = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");

        List<ImageSeries> imageSeriesList =  new ArrayList<ImageSeries>();
        dataSet.setImageSets(imageSeriesList);
        dataSets.add(dataSet);
        when(dataSetRepository.findById(anyLong())).thenReturn(dataSets);
        when(imageSeriesRepository.findByIdIn(anyList())).thenReturn(imageSeriesList);
    }

    private void dataCollectionSetUpForImageSetwithData() {
        List<DataSet> dataSets = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        List testList = new ArrayList();
        testList.add("1");
        dataSet.setImageSets(testList);
        dataSets.add(dataSet);
        when(dataSetRepository.findById(anyLong())).thenReturn(dataSets);
        when(imageSeriesRepository.findByIdIn(anyList())).thenReturn(commonSteps.getImageSeries());
    }

    private DataSet getSaveDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        dataSet.setDescription("test");
        String dateInString = getDate();
        dataSet.setCreatedDate(dateInString);
        dataSet.setName("Test");
        dataSet.setProperties(new Properties());
        dataSet.setOrgId("123");
        dataSet.setSchemaVersion("123");
        dataSet.setType("Annotation");
        return dataSet;
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return "22-01-2017 10:20:56";
    }
}



