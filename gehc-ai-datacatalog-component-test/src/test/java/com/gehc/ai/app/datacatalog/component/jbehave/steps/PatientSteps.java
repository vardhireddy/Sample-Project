package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.entity.Study;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/13/17.
 */
@Component
public class PatientSteps {
    private final MockMvc mockMvc;
    private final StudyRepository studyRepository;
    private final PatientRepository patientRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private ResultActions retrieveResult;
    private String PATIENTS = "[{\"id\":1,\"schemaVersion\":\"123\",\"patientName\":\"Late Lucy\",\"patientId\":\"123\",\"birthDate\":\"09-09-1950\",\"gender\":\"M\",\"age\":\"90\",\"orgId\":\"123\",\"uploadBy\":\"BDD\",\"properties\":\"any\"},{\"id\":2,\"schemaVersion\":\"123\",\"patientName\":\"Late Lucy\",\"patientId\":\"123\",\"birthDate\":\"09-09-1950\",\"gender\":\"M\",\"age\":\"90\",\"orgId\":\"123\",\"uploadBy\":\"BDD\",\"properties\":\"any\"}]";
    private String STUDY = "[{\"id\":1,\"schemaVersion\":\"123\",\"patientDbId\":1,\"studyInstanceUid\":\"123\",\"studyDate\":\"\",\"studyTime\":\"\",\"studyId\":\"123\",\"studyDescription\":\"Test\",\"referringPhysician\":\"John Doe\",\"studyUrl\":\"http://test\",\"orgId\":\"123\",\"uploadDate\":\"2017-03-31\",\"uploadBy\":\"Test\",\"properties\":{}}]";
    private String PATIENT = "[{\"id\":1,\"schemaVersion\":\"123\",\"patientName\":\"Late Lucy\",\"patientId\":\"123\",\"birthDate\":\"09-09-1950\",\"gender\":\"M\",\"age\":\"90\",\"orgId\":\"123\",\"uploadBy\":\"BDD\",\"properties\":\"any\"}]";
    private String PATIENTS_NULL_ID = "{\"schemaVersion\":\"123\",\"patientName\":\"Late Lucy\",\"patientId\":\"123\",\"birthDate\":\"09-09-1950\",\"gender\":\"M\",\"age\":\"90\",\"orgId\":\"123\",\"uploadBy\":\"BDD\",\"properties\":\"any\"}";
    private Throwable throwable = null;
    @Autowired
    public PatientSteps(MockMvc mockMvc, StudyRepository studyRepository, PatientRepository patientRepository, ImageSeriesRepository imageSeriesRepository,CommonSteps commonSteps,DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.patientRepository = patientRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
    }

    @Given("Retrieve study by patient Provided")
    public void givenStudyByPatient() throws Exception {
        dataStudyByPatient();
    }


    @When("Get study by patient")
    public void getStudyByPatient() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/123/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify study by patient")
    public void verifyStudyByPatient() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString
                (STUDY)));
    }

    @Given("Save Patient - DataSetUp Provided")
    public void givenSavePatient() throws Exception {
        getSavePatient();
    }

    @When("save Patient")
    public void savePatient() throws Exception {
        List<Patient> patients = getPatients();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientToJSON(patients.get(0)))
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify Saving Patient")
    public void verifySaveDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        //retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"123\",\"createdBy\":\"test\",\"properties\":{}}")));
    }

    @Given("Retrieve image series by patient id - DataSetUp Provided")
    public void givenImageSeriesByPatientId() throws Exception {
        dataSetUpImageSeriesByPatientId();
    }

    @When("Get image series by patient id")
    public void getImageSeriesByPatientId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/123/image-set?orgId=61939267")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify image series by patient id")
    public void verifyImageSeriesByPatientId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Retrieve image series by patient ids - DataSetUp Provided")
    public void givenImageSeriesByPatientIds() throws Exception {
        dataSetUpImageSeriesByPatientId();
    }

    @When("Get image series by patient ids")
    public void getImageSeriesByPatientIds() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/123,456/image-set?orgId=61939267")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify image series by patient ids")
    public void verifyImageSeriesByPatientIds() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get a Patient - DataSetUp Provided")
    public void givenGetAPatientDataSetUpProvided() {
        getPatient();
    }

    @When("Get a Patient")
    public void whenGetAPatient() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Get a Patient")
    public void thenVerifyGetAPatient() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(PATIENT)));
    }


    @Given("Get Patients - DataSetUp Provided")
    public void givenGetPatientsDataSetUpProvided() {
        setMultiplePatients();

    }

    @When("Get Patients")
    public void whenGetPatients() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Get Patients")
    public void thenVerifyGetPatients() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(PATIENTS)));
    }

    @Given("Get Patient Search by PatientId - DataSetUp Provided")
    public void givenGetPatientSearchByGenderDataSetUpProvided() {
        setPatientId();
    }

    @When("Get Patient Search by PatientId")
    public void whenGetPatientSearchByGender() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient?patientId=123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Patient Search by PatientId")
    public void thenVerifyPatientSearchByGender() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(PATIENT)));

    }

    @Given("Get All Patients - DataSetUp Provided")
    public void givenGetAllPatientsDataSetUpProvided() {
        setAllPatients();
    }

    @When("Get All Patients")
    public void whenGetAllPatients() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Get All Patients")
    public void thenVerifyGetAllPatients() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(PATIENTS)));

    }
    @Given("Save Patient with Null Patient id - DataSetUp Provided")
    public void givenSavePatientWithNullPtId() throws Exception {
        setAllPatients();

    }

    @When("save Patient with Null Patient id")
    public void savePatientWithNullPtId() throws Exception {
        List<Patient> patients = getPatientsWithNullPtID();
        try{
        mockMvc.perform(
                post("/api/v1/datacatalog/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientToJSON(patients.get(0)))
                        .requestAttr("orgId", "12")
        );}
        catch(Exception e){
             throwable = e;
        }

    }

    @Then("verify Saving Patient with Null Patient id")
    public void verifySaveDatSetWithNullPtId() throws Exception {
        assert(throwable.toString().contains("Missing patient info"));
    }

    @Given("Save Patient with Null ID - DataSetUp Provided")
    public void givenSavePatientWithNullIDDataSetUpProvided() {
        List<Patient> patients = new ArrayList<Patient>();
        patients.add(getPatientWithIdNull());
       when(patientRepository.findByPatientIdAndOrgId(anyString(),anyString())).thenReturn(patients);
    }
    @When("save Patient with Null ID")
    public void whenSavePatientWithNullID() throws Exception {
        Patient patient = getPatientWithIdNull();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientToJSON(patient))
                        .requestAttr("orgId", "12")
        );

    }
    
    @Then("verify Saving Patient with Null ID")
    public void thenVerifySavingPatientWithNullID() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(PATIENTS_NULL_ID)));
    }

    @Given("Save Existing Patient - DataSetUp Provided")
    public void givenSaveExistingPatient() throws Exception {
        List<Patient> patLst = getPatients();
        when(patientRepository.findByPatientIdAndOrgId(anyString(),anyString())).thenReturn(null);
    }

    @When("save  Existing Patient")
    public void saveExistingPatient() throws Exception {
        List<Patient> patients = getPatients();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientToJSON(patients.get(0)))
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify Saving Existing Patient")
    public void verifySaveExistingDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Given("Retrieve image series by patient when patient ids list returned null- DataSetUp Provided")
    public void givenImageSeriesByPatientIdsWhenNullPatientIdsInDB() throws Exception {
        dataSetUpImageSeriesByPatientIdWithNullPatientList();
    }

    @When("Get image series by patient ids when patient ids list returned null")
    public void getImageSeriesByPatientIdsWhenNullPatientIdsInDB() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/123,456/image-set?orgId=61939267")
                        .contentType(MediaType.APPLICATION_JSON)
        );

    }

    @Then("verify image series by patient ids when patient ids list returned null")
    public void verifyImageSeriesByPatientIdsWhenNullPatientIdsInDB() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Retrieve image series by patient when patient ids list returned empty- DataSetUp Provided")
    public void givenImageSeriesByPatientIdsWhenEmptyPatientIdsInDB() throws Exception {
        dataSetUpImageSeriesByPatientIdWithNullPatientList();
    }

    @When("Get image series by patient ids when patient ids list returned empty")
    public void getImageSeriesByPatientIdsWhenEmptyPatientIdsInDB() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/patient/123,456/image-set?orgId=61939267")
                        .contentType(MediaType.APPLICATION_JSON)
        );

    }

    @Then("verify image series by patient ids when patient ids list returned empty")
    public void verifyImageSeriesByPatientIdsWhenEmptyPatientIdsInDB() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }


    private Patient getPatientWithIdNull() {
        List<Patient> patients = getPatients();
        Patient patient =  patients.get(0);
        patient.setId(null);
        return patient;
    }


    private void dataSetUpImageSeriesByPatientId() {
        List<Patient> patLst = getPatients();
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(patientRepository.findByPatientIdAndOrgId(anyString(),anyString())).thenReturn(patLst);
        when(imageSeriesRepository.findByPatientDbIdAndOrgId(anyLong(),anyString())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesByPatientIdWithNullPatientList() {
        List<Patient> patLst = getPatients();
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(patientRepository.findByPatientIdAndOrgId(anyString(),anyString())).thenReturn(null);
        when(imageSeriesRepository.findByPatientDbIdAndOrgId(anyLong(),anyString())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesByPatientIdWithEmptyPatientList() {
        List<Patient> patLst = getPatients();
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(patientRepository.findByPatientIdAndOrgId(anyString(),anyString())).thenReturn(new ArrayList());
        when(imageSeriesRepository.findByPatientDbIdAndOrgId(anyLong(),anyString())).thenReturn(imgSerLst);
    }

    private void dataStudyByPatient() {
        List<Study> studyList = getStudy();
        when(studyRepository.findByPatientDbIdAndOrgId(anyLong(), anyString())).thenReturn(studyList);
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

    public void getSavePatient() {
        List<Patient> patLst = getPatients();
    }

    private void setMultiplePatients() {
        List<Patient> patients = getMultiplePatients();
        when(patientRepository.findByIdInAndOrgId(anyListOf(Long.class), anyString())).thenReturn(patients);
    }

    private void setAllPatients() {
        List<Patient> patients = getMultiplePatients();
        when(patientRepository.findByOrgId(anyString())).thenReturn(patients);
    }

    private void getPatient() {
        List<Patient> patients = getPatients();
        when(patientRepository.findByIdInAndOrgId(anyListOf(Long.class), anyString())).thenReturn(patients);
    }

    private void setPatientId() {
        List<Patient> patients = getPatients();
        when(patientRepository.findByPatientIdAndOrgId(anyString(),anyString())).thenReturn(patients);
    }

    private List<Patient> getMultiplePatients() {
        List<Patient> patLst = getPatients();
        Patient patient = new Patient();
        patient.setAge("90");
        patient.setId(2L);
        patient.setOrgId("123");
        patient.setAge("90");
        patient.setBirthDate("09-09-1950");
        patient.setGender("M");
        patient.setPatientId("123");
        patient.setPatientName("Late Lucy");
        patient.setProperties("any");
        patient.setSchemaVersion("123");
        patient.setUploadBy("BDD");
        patient.setUploadDate(getDate());
        patLst.add(patient);
        return patLst;
    }

    private List<Patient> getPatients() {
        List<Patient> patLst = new ArrayList<Patient>();
        Patient patient = new Patient();
        patient.setAge("90");
        patient.setId(1L);
        patient.setOrgId("123");
        patient.setAge("90");
        patient.setBirthDate("09-09-1950");
        patient.setGender("M");
        patient.setPatientId("123");
        patient.setPatientName("Late Lucy");
        patient.setProperties("any");
        patient.setSchemaVersion("123");
        patient.setUploadBy("BDD");
        patient.setUploadDate(getDate());
        patLst.add(patient);
        return patLst;
    }

    private List<Patient> getPatientsWithNullPtID() {
        List<Patient> patLst = new ArrayList<Patient>();
        Patient patient = new Patient();
        patient.setAge("90");
        patient.setId(null);
        patient.setOrgId(null);
        patient.setAge("90");
        patient.setBirthDate("09-09-1950");
        patient.setGender("M");
        patient.setPatientId(null);
        patient.setPatientName("Late Lucy");
        patient.setProperties("any");
        patient.setSchemaVersion("123");
        patient.setUploadBy("BDD");
        patient.setUploadDate(getDate());
        patLst.add(patient);
        return patLst;
    }

    private String patientToJSON(Patient patient) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(patient);
    }
}
