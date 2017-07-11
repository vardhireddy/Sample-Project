package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.ImageSet;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
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
    private final PatientRepository patientRepository;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;

    @Autowired
    public DataCatalogSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository,PatientRepository patientRepository) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.patientRepository = patientRepository;


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

    @When("Get data collection image-set details by its id")
    public void getdataSetByType() throws Exception {
        dataCollectionSetUpForImageSet();
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
                        .requestAttr("orgId", "123")
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
                get("/api/v1/datacatalog/data-collection/type/Annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection by Type -  Annotation")
    public void verifydataCollectionByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @Given("Retrieve image series by patient id - DataSetUp Provided")
    public void givenImageSeriesByPatientId() throws Exception {
        dataSetUpImageSeriesByPatientId();
    }

    @When("Get image series by patient id")
    public void getImageSeriesByPatientId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify image series by patient id")
    public void verifyImageSeriesByPatientId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        System.out.println(expectedImageSeries());
        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
    }

    @Given("Retrieve image series by id - DataSetUp Provided")
    public void givenImageSeriesById() throws Exception {
        dataSetUpImageSeriesById();
    }

    @When("Get image series by image series id")
    public void getImageSeriesById() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series/id/123")
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
                get("/api/v1/datacatalog/image-series/series-instance-uid/1/")
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
        List<ImageSeries> imageSeries= getImageSeries();
        when(imageSeriesRepository.save(any(ImageSeries.class))).thenReturn(imageSeries.get(0));
    }

    @When("Store an image set data")
    public void StoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries= getImageSeries();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/image-series")
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

    @Given("Get Imageset by study - DataSetUp Provided")
    public void givenImagesetByStudy() throws Exception {
        dataSetUpImagesetByStudy();

    }


    @When("Get Imageset by study")
    public void getImagesetByStudy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series/study-dbid/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify Imageset by study")
    public void verifyImagesetByStudy() throws Exception {
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
                get("/api/v1/datacatalog/image-series?orgId=61939267-d195-499f-bfd8-7d92875c7035&modality=CT&annotations=point&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify Image set based on filter criteria")
    public void verifyImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
    }


    private String defnToJSON(DataSet dataSet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dataSet);
    }

    private String imageSeriesToJSON(ImageSeries imageSeries) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageSeries);
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
        ImageSet imageSet = new ImageSet();
        imageSet.setAge("90");
        imageSet.setId("1");
        dataSet.setImageSets(imageSet);
        dataSets.add(dataSet);
        return dataSets;
    }

    private void dataSetUpImageSeriesByPatientId() {
        List<Patient> patLst = getPatients();
        List<ImageSeries> imgSerLst = getImageSeries();
        when(patientRepository.findByPatientId(anyString())).thenReturn(patLst);
        when(imageSeriesRepository.findByPatientDbId(anyLong())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesById() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findByIdAndOrgId(anyLong(),anyString())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesBySeriesInstanceId() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUid(anyString())).thenReturn(imgSerLst);
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(),anyString())).thenReturn(imgSerLst);
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
        prop.setProperty("test","bdd");
        imageSeries.setProperties( prop);
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }

    private String expectedImageSeries(){
        String imageSeries =  "{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}";
        return imageSeries;
    }

    private List<Patient> getPatients() {
        List<Patient> patLst = new ArrayList<Patient>();
        Patient patient = new Patient();
        patient.setAge("90");
        patient.setId(1L);
        patient.setOrgId("123");
        patLst.add(patient);
        return patLst;
    }

    private void dataCollectionSetUpByType() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByTypeAndOrgId(anyString(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForOrgId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByOrgId(anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForType() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByTypeAndOrgId(anyString(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForImageSet() {
//        List<DataSet> dataSets = new ArrayList<DataSet>();
//        DataSet dataSet = new DataSet();
//        dataSet.setId(1L);
//        dataSet.setCreatedBy("test");
//        ImageSet imageSet =new ImageSet();
//        imageSet.setAge("90");
//        imageSet.setAnatomy("Chest");
//        imageSet.setDataFormat("Test");
//        imageSet.setDescription("Test");
//        imageSet.setEquipment("Test");
//        imageSet.setGender( "Test");
//        imageSet.setId("Test");
//        imageSet.setInstanceCount(1);
//        imageSet.setInstitution("Test");
//        imageSet.setModality("Test");
//        imageSet.setOrgId("123");
//        imageSet.setPatientDbId(anyLong());
//        dataSet.setImageSets(imageSet);
//        ImageSet imageSet1 =new ImageSet();
//        imageSet1.setAge("90");
//        imageSet1.setAnatomy("Chest");
//        imageSet1.setDataFormat("Test");
        //dataSet.setImageSets(imageSet1);
        List<DataSet> dataSets = getDataSetsWithImageSet();

//        dataSets.add(dataSet);
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
        List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("TEST");
        imageSeriesList.add(imageSeries);
        when(imageSeriesRepository.findById(1L)).thenReturn(imageSeriesList);
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
}



