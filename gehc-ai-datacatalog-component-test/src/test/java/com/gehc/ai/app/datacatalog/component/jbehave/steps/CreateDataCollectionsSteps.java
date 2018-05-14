package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

/**
 * {@code CreateDataCollectionsSteps} implements the test scenarios defined by the {@code create_data_collections_run.feature} file.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@Component
public class CreateDataCollectionsSteps {

    private final DataSetRepository dataSetRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final StudyRepository studyRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;
    private Throwable throwable = null;
    private DataCatalogDaoImpl dataCatalogDao;
    private DataSet dataSet;

    @Autowired
    public CreateDataCollectionsSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository, CommonSteps commonSteps, DataCatalogInterceptor dataCatalogInterceptor, DataCatalogDaoImpl dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
        this.dataCatalogDao = dataCatalogDao;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), anyObject())).thenReturn(true);
    }

    //////////////////////
    //
    // GIVEN statements //
    //
    //////////////////////

    @Given("a pool of unique image sets")
    public void givenPoolOfUniqueImageSets() throws Exception {
//        this.dataSet = getMockDataSet();
//        when(dataSetRepository.save(any(DataSet.class))).thenReturn(this.dataSet);
    }

    @Given("a pool of non-unique image sets")
    public void givenPoolOfNonUniqueImageSets() throws Exception {

    }

    @Given("an interal error that causes no data collections to be created")
    public void givenAnInternalErrorThatCausesNoDataCollectionsToBeCreated() throws Exception {

    }

    @Given("an interal error that causes at least one data collection to not be created")
    public void givenAnInternalErrorThatCausesAtLeastOneDataCollectionToNotBeCreated() throws Exception {

    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    @When("the API which creates a data collection is invoked to create <numCollections> data collections")
    public void whenExportAnnotationAsCsvAPICalled(@Named("numCollections") int numCollections) throws Exception {
//        retrieveResult = mockMvc.perform(
//                post("/api/v1/datacatalog/data-collection")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(defnToJSON(this.dataSet))
//                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500"));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("the response's status code should be 201")
    public void thenResponseCodeShouldBe201() throws Exception {
        //retrieveResult.andExpect(status().isOk());
    }

    @Then("the response's status code should be 206")
    public void thenResponseCodeShouldBe206() throws Exception {
        //retrieveResult.andExpect(status().isOk());
    }

    @Then("the response's status code should be 400")
    public void thenResponseCodeShouldBe400() throws Exception {

    }

    @Then("the response's status code should be 500")
    public void thenResponseCodeShouldBe500() throws Exception {

    }

    @Then("the response's content type should be JSON")
    public void thenResponseMediaTypeShouldBeJson() throws Exception {
        //retrieveResult.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the response's body should contain a JSON string that defines <numCollections> data collections")
    public void thenResponseBodyShouldContainJsonStringDefiningDataCollections(@Named("numCollections") int numCollections) throws Exception {
        //retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"12345678-abcd-42ca-a317-4d408b98c500\",\"createdBy\":\"test\",\"properties\":{},\"imageSets\":[]}")));
    }

    @Then("the response's body should contain an error message saying the provided image sets should be unique")
    public void thenResponseBodyShouldContainErrorMessageSayingProvidedImageSetsShouldBeUnique() throws Exception {

    }

    @Then("the response's body should contain an error message saying there was an internal error and no collections were created")
    public void thenResponseBodyShouldContainErrorMessageSayingThereWasInternalServerErrorAndNoCollectionsWereCreated() throws Exception {

    }

    @Then("the response's body should contain a message saying that some but not all data collections were created")
    public void thenResponseBodyShouldContainErrorMessageSayingSomeButNotAllDataCollectionsWereCreated() throws Exception {

    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private String defnToJSON(DataSet dataSet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dataSet);
    }

    private DataSet getMockDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        dataSet.setDescription("test");
        String dateInString = getDate();
        dataSet.setCreatedDate(dateInString);
        dataSet.setName("Test");
        dataSet.setProperties(new HashMap<String, String>());
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

}



