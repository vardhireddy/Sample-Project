package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.*;
import com.gehc.ai.app.datacatalog.entity.Properties;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.service.IRemoteService;
import com.gehc.ai.app.datacatalog.rest.impl.DataCatalogRestImpl;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import com.gehc.ai.app.datacatalog.service.impl.DataCatalogServiceImpl;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.LabelAnnotationJson;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class DataCollectionSteps {

    private final DataSetRepository dataSetRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final StudyRepository studyRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private final IRemoteService remoteServiceImpl;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;
    private Throwable throwable = null;
    private DataCatalogDaoImpl dataCatalogDao;
    private DataCatalogServiceImpl dataCatalogService;

    @Autowired
    public DataCollectionSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository,
                               ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository, CommonSteps commonSteps,
                               DataCatalogInterceptor dataCatalogInterceptor, DataCatalogDaoImpl dataCatalogDao, IRemoteService remoteServiceImpl, DataCatalogServiceImpl dataCatalogService) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
        this.dataCatalogDao = dataCatalogDao;
        this.remoteServiceImpl = remoteServiceImpl;
        this.dataCatalogService = dataCatalogService;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), anyObject())).thenReturn(true);
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
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify data collection details by its id")
    public void verifyGetDatSetForId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @When("Get data collection by Org Id")
    public void getdataSet() throws Exception {

        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
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
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify data collection image-set details by its id when no imageset")
    public void verifyGetDatSetByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Save DataSet - DataSetUp Provided")
    public void givenSaveDataSet() throws Exception {
    	// TODO: Update ALM test cases.  Have to comment out this test step since there is a more update to date version of this scenario
    	
		// DataSet dataSet = getSaveDataSet();
		// when(dataSetRepository.save(any(DataSet.class))).thenReturn(dataSet);
    }

	@When("save DataSet")
	public void saveDataSet() throws Exception {
		// TODO: Update ALM test cases. Have to comment out this test step since there is a more update to date version of this scenario
		
		// DataSet dataSet = getSaveDataSet();
		// retrieveResult = mockMvc.perform(
		// post("/api/v1/datacatalog/data-collection")
		// .contentType(MediaType.APPLICATION_JSON)
		// .content(defnToJSON(dataSet))
		// .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
		// );

	}

	@Then("verify Saving DataSet")
	public void verifySaveDatSet() throws Exception {
		// TODO: Update ALM test cases. Have to comment out this test step since there
		// is a more update to date version of this scenario

		// retrieveResult.andExpect(status().isOk());
		// retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017
		// 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"12345678-abcd-42ca-a317-4d408b98c500\",\"createdBy\":\"test\",\"properties\":{},\"imageSets\":[]}")));
	}

    @When("Get data collection by Type -  Annotation")
    public void getDataCollectionByType() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection?type=Annotation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );

    }

    @Then("verify data collection by Type -  Annotation")
    public void verifydataCollectionByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @When("Get data collection by Type is not valid")
    public void whenGetDataCollectionByTypeIsNotValid() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection?type=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );

    }


    @Then("verify data collection by Type is not valid")
    public void thenVerifyDataCollectionByTypeIsNotValid() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Retrieve Image Set with ID DataSetUp Provided")
    public void givenRetrieveImageSetWithIDDataSetUpProvided() {
        dataCollectionSetUpForImageSetwithPatientData();
    }

    @When("Get data collection image-set details by its id")
    public void whenGetDataCollectionImagesetDetailsByItsId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("verify data collection image-set details by its id")
    public void thenVerifyDataCollectionImagesetDetailsByItsId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        
        retrieveResult.andDo(MockMvcResultHandlers.print());
        retrieveResult.andExpect(content().string(containsString("[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31 00:00:00\",\"patientDbId\":1}]")));
    }

    @Given("Post DataCatalog with Org ID null DataSetUp Provided")
    public void givenRetrieveDataCatalogWithOrgIDNullDataSetUpProvided() {
        DataSet dataSet = getSaveDataSet();
        when(dataSetRepository.save(any(DataSet.class))).thenReturn(dataSet);
    }

    @When("Post data collection by Org Id null")
    public void whenGetDataCollectionByOrgIdNull() throws Exception {
        when(remoteServiceImpl.getOrgIdBasedOnSessionToken(anyString())).thenReturn(null);
        DataSet dataSet = getSaveDataSet();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/data-collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(defnToJSON(dataSet)));

    }

    @Then("verify data collection by Org Id null")
    public void thenVerifyDataCollectionByOrgIdNull() throws Exception {
        retrieveResult.andExpect(content().string(containsString("")));
    }

    @Given("datacatalog health check with lowercase")
    public void givenDatacatalogHealthCheckWithLowercase() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/healthcheck")
        );
    }

    @Then("verify success for with lowercase")
    public void thenVerifySuccessForWithLowercase() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Given("Retrieve DataSet for Filters by OrgId DataSetUp Provided")
    public void givenRetrieveDataSetForFiltersByOrgIdDataSetUpProvided() {
        //Modality
        List<Object[]> countM = getQueryList("modality");
        //anatomy
        List<Object[]> countA = getQueryList("anatomy");
        //annotation
        List<Object[]> countAnn = getQueryList("annotations");

        //data_format
        List<Object[]> countD = getQueryList("data_format");
        //institution
        List<Object[]> countI = getQueryList("institution");
        //equipment
        List<Object[]> countE = getQueryList("equipment");


        when(imageSeriesRepository.countAnatomy(anyString())).thenReturn(countA);
        when(imageSeriesRepository.countModality(anyString())).thenReturn(countM);
        when(imageSeriesRepository.countDataFormat(anyString())).thenReturn(countD);
        when(imageSeriesRepository.countInstitution(anyString())).thenReturn(countI);
        when(imageSeriesRepository.countEquipment(anyString())).thenReturn(countE);
        when(annotationRepository.countAnnotationType(anyString())).thenReturn(countAnn);
    }

    @When("Get DataSet for Filters by OrgId")
    public void whenGetDataSetForFiltersByOrgId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
                // .param("modality","['CT', 'MR', 'DX']&anatomy=['Lung', 'Chest']")

        );
    }

    @Then("verify DataSet for Filters by OrgId")
    public void thenVerifyDataSetForFiltersByOrgId() throws Exception {
        retrieveResult.andExpect(content().string(containsString("{\"data_format\":{\"dataFormat\":1},\"institution\":{\"UCSF\":1},\"equipment\":{\"CT\":1},\"annotations\":{\"test\":1},\"modality\":{\"DX\":121,\"CR\":8082},\"anatomy\":{\"CHEST\":8203}}")));
    }

    @Given("Retrieve DataSet for Filters by OrgId when Annotation Absent DataSetUp Provided")
    public void givenRetrieveDataSetForFiltersByOrgIdWhenAnnotationCountIsNullDataSetUpProvided() {
        List<Long> countAnn = new ArrayList<Long>();
        countAnn.add(0, 1L);
        countAnn.add(1, 2L);
        when(imageSeriesRepository.countImgWithNoAnn(anyString())).thenReturn(countAnn);
    }

    @When("Get DataSet for Filters by OrgId when Annotation Absent")
    public void whenGetDataSetForFiltersByOrgIdWhenAnnotationCountIsNull() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-summary")
                        .param("groupby", "annotation-absent")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")

        );
    }

    @Then("verify DataSet for Filters by OrgId when Annotation Absent")
    public void thenVerifyDataSetForFiltersByOrgIdWhenAnnotationCountIsNull() throws Exception {
        retrieveResult.andExpect(content().string(containsString("{\"annotation-absent\":1}")));

    }

    @Given("Retrieve DataSet for Filters by OrgId when Annotation count is empty DataSetUp Provided")
    public void givenRetrieveDataSetForFiltersByOrgIdWhenAnnotationCountIsEmptyDataSetUpProvided() {
        //Modality
        List<Object[]> countM = getQueryList("modality");
        //anatomy
        List<Object[]> countA = getQueryList("anatomy");

        when(imageSeriesRepository.countAnatomy(anyString())).thenReturn(countA);
        when(imageSeriesRepository.countModality(anyString())).thenReturn(countM);
        when(annotationRepository.countAnnotationType(anyString())).thenReturn(new ArrayList());
    }

    @When("Get DataSet for Filters by OrgId when Annotation count is empty")
    public void whenGetDataSetForFiltersByOrgIdWhenAnnotationCountIsEmpty() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-summary")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("verify DataSet for Filters by OrgId when Annotation count is empty")
    public void thenVerifyDataSetForFiltersByOrgIdWhenAnnotationCountIsEmpty() throws Exception {
        retrieveResult.andExpect(content().string(containsString("{}")));
    }

    @Given("Retrieve DataSet  group by ANNOTATIONS_ABSENT DataSetUp Provided")
    public void givenRetrieveDataSetgroupbyANNOTATIONS_ABSENTDataSetUpProvided() {
        List<Long> noAnn = new ArrayList<Long>();
        noAnn.add(0, 1L);
        when(imageSeriesRepository.countImgWithNoAnn(anyString())).thenReturn(noAnn);
    }

    @When("Get DataSet  group by ANNOTATIONS_ABSENT")
    public void whenGetDataSetgroupbyANNOTATIONS_ABSENT() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-summary?groupby=annotation-absent")
                        .contentType(MediaType.APPLICATION_JSON).requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("verify DataSet  group by ANNOTATIONS_ABSENT")
    public void thenVerifyDataSetgroupbyANNOTATIONS_ABSENT() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"annotation-absent\":1}")));
    }

    @Given("Retrieve DataSummary for GE-Class")
    public void givenRetrieveDataSummaryGEClassdDataSetUpProvided() {
        Map resultSet = getMapForGEClassDataSummary();
        when(dataCatalogDao.geClassDataSummary(anyMap(), anyString())).thenReturn(resultSet);
    }


    @When("Get DataSummary for GE-Class")
    public void whenGetDataSummaryGEClass() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/ge-class-data-summary?modality=CT,DX&anatomy=Chest,Lung&annotations=label")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("verify DataSummary for GE-Class")
    public void thenVerifyDataSummaryGEClass() throws Exception {
        retrieveResult.andExpect(content().string(containsString("{\"modality\":{\"CHEST\":8203},\"anatomy\":{\"DX\":121,\"CR\":8082},\"annotations\":{\"test\":1}}")));
    }

    @Given("Retrieve DataSummary for GE-Class with invalid annotation type")
    public void givenRetrieveDataSummaryForGEClassWithInvalidAnnotationType() {
        Map resultSet = getMapForGEClassDataSummary();
        when(dataCatalogDao.geClassDataSummary(anyMap(), anyString())).thenReturn(resultSet);
    }


    @When("Get DataSummary for GE-Class with invalid annotation type")
    public void whenGetDataSummaryForGEClassWithInvalidAnnotationType() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/ge-class-data-summary?modality=CT,DX&anatomy=Chest,Lung&annotations=label%^&&!")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }


    @Then("verify DataSummary for GE-Class with invalid annotation type")
    public void thenVerifyDataSummaryForGEClassWithInvalidAnnotationType() throws Exception {
        retrieveResult.andExpect(content().string(containsString("")));
    }


    @Given("Retrieve DataSummary for GE-Class without org id")
    public void givenRetrieveDataSummaryGEClassdNoOrgIdDataSetUpProvided() {
        Map resultSet = getMapForGEClassDataSummary();
        when(dataCatalogDao.geClassDataSummary(anyMap(), anyString())).thenReturn(resultSet);
    }


    @When("Get DataSummary for GE-Class without org id")
    public void whenGetDataSummaryGEClassNoOrgId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/ge-class-data-summary?modality=CT,DX&anatomy=Chest,Lung&annotations=label")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("verify DataSummary for GE-Class without org id")
    public void thenVerifyDataSummaryGEClassNoOrgId() throws Exception {
        retrieveResult.andExpect(content().string(containsString("")));
    }

    @Given("Multiple Data Collections by ids")
    public void givenMultipleDataCollectionsByIds() {
        DataSet dataSet = new DataSet();
        ArrayList dataSetArray = new ArrayList();
        dataSetArray.add(dataSet);
        when(dataSetRepository.findByIdAndOrgId(any(), any())).thenReturn(dataSetArray);
        doNothing().when(dataSetRepository).delete(dataSet);
    }

    @When("Delete Data collection by ids API is called")
    public void whenDeleteDataCollectionByIdsAPIIsCalled() throws Exception {
        retrieveResult = mockMvc.perform(
                delete("/api/v1/datacatalog/data-collection/1,2")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("verify Data Collection by ids has been deleted")
    public void thenVerifyDataCollectionByIdsHasBeenDeleted() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }

    @Given("Get Annotaition Ids by datacollectionId - Data Setup")
    public void givenGetAnnotaitionIdsByDatacollectionIdDataSetup() throws Exception {
        List<DataSet> dataSetList = getDataSetsWithImageSet();
        when(dataSetRepository.findById(anyLong())).thenReturn(Optional.of(dataSetList.get(0)));

        List<AnnotationJson> annotationDetails = new ArrayList<>();
        List<GEClass> geClasses = new ArrayList<>();
        geClasses.add(new GEClass("Foreign Bodies", "Absent", null));
        geClasses.add(new GEClass("Calcification", null, null));
        AnnotationJson annotation = new LabelAnnotationJson("1", "SUID", "DCM", 1L, "label", geClasses, "Test indication", "Test findings");

        annotationDetails.add(annotation);
        when(dataCatalogDao.getAnnotationDetailsByImageSetIDs(anyList())).thenReturn(annotationDetails);

    }

    @When("Get Annotaition Ids by datacollectionId is called")
    public void whenGetAnnotaitionIdsByDatacollectionIdIsCalled() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/1/annotation")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("verify Get Annotaition Ids by datacollectionId")
    public void thenVerifyGetAnnotaitionIdsByDatacollectionId() throws Exception {
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf8")
        );

        retrieveResult.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("[{\"patientID\":\"1\",\"seriesUID\":\"SUID\",\"imageSetFormat\":\"DCM\",\"annotationID\":1,\"annotationType\":\"label\",\"indication\":\"Test indication\",\"findings\":\"Test findings\", \"geClasses\":[{\"name\":\"Foreign Bodies\", \"value\":\"Absent\"},{\"name\":\"Calcification\", \"value\":null}]}]"
                ));

    }

    @Given("Get Annotaition Ids by datacollectionId When ImageSeriesNotFound - Data Setup")
    public void givenGetAnnotaitionIdsByDatacollectionIdWhenImageSeriesNotFoundDataSetup() throws Exception {
        List<DataSet> emptyListDataSet = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        emptyListDataSet.add(dataSet);
        when(dataSetRepository.findById(anyLong())).thenReturn(Optional.of(emptyListDataSet.get(0)));
        List<AnnotationJson> annotationJsonLst = new ArrayList<AnnotationJson>();
        when(dataCatalogDao.getAnnotationDetailsByImageSetIDs(anyList())).thenReturn(annotationJsonLst);
    }

    @When("Get Annotaition Ids by datacollectionId is called When ImageSeriesNotFound")
    public void whenGetAnnotaitionIdsByDatacollectionIdIsCalledWhenImageSeriesNotFound() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/1/annotation")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("verify Get Annotaition Ids by datacollectionId  When ImageSeriesNotFound")
    public void thenVerifyGetAnnotaitionIdsByDatacollectionIdWhenImageSeriesNotFound() throws Exception {
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Delete Data Collections by id")
    public void givenDeleteDataCollectionsById() {

        DataSet dataSet = new DataSet();
        doNothing().when(dataSetRepository).delete(dataSet);
    }

    @When("Delete Data collection by id API is called")
    public void whenDeleteDataCollectionByIdAPIIsCalled() throws Exception {
        retrieveResult = mockMvc.perform(
                delete("/api/v1/datacatalog/data-collection/1,2")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("verify Data Collection by id has been deleted")
    public void thenVerifyDataCollectionByIdHasBeenDeleted() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }

    ///////////////////////////////////////////////////
    //
    // Tests for exporting annotation details as CSV //
    //
    ///////////////////////////////////////////////////

    @Given("a data collection contains at least one image set and each image set contains at least one annotation")
    public void givenDataCollectionContainsOneImageSet() throws Exception {
        List<DataSet> mockDataSet = getDataSetsWithImageSet();
        when(dataSetRepository.findById(anyLong())).thenReturn(Optional.of(mockDataSet.get(0)));
        when(dataCatalogDao.getAnnotationDetailsAsCsvByImageSetIDs(anyList())).thenReturn("mockColumn\n\"mockValue\"");
    }

    @When("the API which exports a data collection's annotations as CSV is called")
    public void whenExportAnnotationAsCsvAPICalled() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/data-collection/1/annotation/csv")
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("the response's status code should be 200")
    public void thenResponseCodeShouldBeOK() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Then("the response's content type should be CSV")
    public void thenResponseMediaTypeShouldBeCSV() throws Exception {
        MediaType TEXT_CSV = new MediaType("text", "csv");
        retrieveResult.andExpect(content().contentType(TEXT_CSV));
    }

    @Then("the response's body should contain a string")
    public void thenResponseBodyShouldContainCSVForMultipleTypes() throws Exception {
        retrieveResult.andExpect(content().string("mockColumn\n\"mockValue\""));
    }

    private Map getMapForGEClassDataSummary() {
        Map resultSet = new HashMap();
        Map resultSetM = new HashMap();
        resultSetM.put("CHEST", 8203);
        Map resultSetA = new HashMap();
        resultSetA.put("test", 1);
        Map resultSetAN = new HashMap();
        resultSetAN.put("DX", 121);
        resultSetAN.put("CR", 8082);

        resultSet.put("modality", resultSetM);
        resultSet.put("annotations", resultSetA);
        resultSet.put("anatomy", resultSetAN);
        return resultSet;
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
        testList.add(1L);
        dataSet.setImageSets(testList);
        dataSets.add(dataSet);
        return dataSets;
    }

    private void dataCollectionSetUpForId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
    }

    private void dataCollectionSetUpForImageSet() {
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
        List<Long> testList = new ArrayList<Long>();
        testList.add(1L);
        dataSet.setImageSets(testList);
        when(dataSetRepository.findById(anyLong())).thenReturn(Optional.of(dataSet));
        when(imageSeriesRepository.findByIdIn(anyList())).thenReturn(imageSeriesList);
    }

    private void dataCollectionSetUpForImageSetwithData() {
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        List<Long> testList = new ArrayList<Long>();
        testList.add(1L);
        dataSet.setImageSets(testList);
        when(dataSetRepository.findById(anyLong())).thenReturn(Optional.of(dataSet));
        when(imageSeriesRepository.findByIdIn(anyList())).thenReturn(commonSteps.getImageSeriesWithFilterOneModality());
    }


    private void dataCollectionSetUpForImageSetwithPatientData() {
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        List<Long> testList = new ArrayList<Long>();
        testList.add(1L);
        dataSet.setImageSets(testList);
        when(dataSetRepository.findById(anyLong())).thenReturn(Optional.of(dataSet));
        when(dataCatalogDao.getImgSeriesWithPatientByIds(anyList())).thenReturn(commonSteps.getImageSeriesWithFilterOneModality());
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
        dataSet.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        dataSet.setSchemaVersion("123");
        dataSet.setType("Annotation");
        dataSet.setImageSets(new ArrayList());
        return dataSet;
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return "22-01-2017 10:20:56";
    }

    private List<Object[]> getQueryList(String input) {
        List<Object[]> countM = new ArrayList<Object[]>();

        if (input.equals("modality")) {

            Object[] modality = new Object[]{
                    "DX", 121L};
            Object[] modality1 = new Object[]{
                    "CR", 8082L};
            countM.add(0, modality);
            countM.add(1, modality1);
        } else if (input.equals("anatomy")) {
            //anatomy
            Object[] anatomy = new Object[]{
                    "CHEST", 8203L};
            countM.add(0, anatomy);
        } else if (input.equals("annotations")) {
            Object[] annotations = new Object[]{
                    "test", 1L};
            countM.add(0, annotations);
        } else if (input.equals("data_format")) {
            Object[] data_format = new Object[]{
                    "dataFormat", 1L};
            countM.add(0, data_format);

        } else if (input.equals("institution")) {
            Object[] institution = new Object[]{
                    "UCSF", 1L};
            countM.add(0, institution);

        } else if (input.equals("equipment")) {
            Object[] equipment = new Object[]{
                    "CT", 1L};
            countM.add(0, equipment);

        }
        return countM;
    }

}



