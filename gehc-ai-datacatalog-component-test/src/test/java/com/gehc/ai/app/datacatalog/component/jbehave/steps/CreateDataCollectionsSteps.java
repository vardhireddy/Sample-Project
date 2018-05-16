package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.DataCollectionsCreateRequest;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code CreateDataCollectionsSteps} implements the test scenarios defined by the {@code create_data_collections_run.feature} file.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@Component
public class CreateDataCollectionsSteps {

    @Mock
    private final DataSetRepository dataSetRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private MockMvc mockMvc;
    private ResultActions result;
    private PreparedStatementSetter ps;
    private RowMapper rm;
    private Throwable throwable = null;
    private DataCatalogDaoImpl dataCatalogDao;
    private DataSet dataSet;

    private String[] imageSetIds;
    private DataCollectionsCreateRequest request;
    private DataSet dataCollection;

    ///////////////
    //
    // Constants //
    //
    ///////////////

    private static final String[] UNIQUE_IMAGE_SET_IDS = new String[]{"123", "456", "789", "101112", "131415", "161718", "192021"};

    private static final String[] NON_UNIQUE_IMAGE_SET_IDS = new String[]{"123", "456", "789", "101112", "131415", "161718", "161718"};

    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////

    @Autowired
    public CreateDataCollectionsSteps(MockMvc mockMvc, DataSetRepository dataSetRepository, CommonSteps commonSteps, DataCatalogInterceptor dataCatalogInterceptor, DataCatalogDaoImpl dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.dataSetRepository = dataSetRepository;
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
    public void givenPoolOfUniqueImageSets() {
        this.imageSetIds = UNIQUE_IMAGE_SET_IDS;
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = new DataSet();
    }

    @Given("a pool of non-unique image sets")
    public void givenPoolOfNonUniqueImageSets() {
        this.imageSetIds = NON_UNIQUE_IMAGE_SET_IDS;
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = new DataSet();
    }

    @Given("the pool image of image sets is to be represented as 1 data collection")
    public void givenPoolOfImageSetsIsToBeRepresentedAsOneCollection() {
        this.request.setNumDataCollections(1);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the pool image of image sets is to be represented as <numCollections> data collections")
    public void givenPoolOfImageSetsIsToBeRepresentedAsSeveralCollections(@Named("numCollections") int numCollections) {
        this.request.setNumDataCollections(numCollections);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the pool image of image sets is to be represented as <description>")
    public void givenPoolOfImageSetsIsToBeRepresentedAs(@Named("numCollections") int numCollections) {
        this.request.setNumDataCollections(numCollections);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("no internal errors occur")
    public void givenNoInternalErrorsOccur() {
        DataSet dummyDataSet = new DataSet();
        when(dataSetRepository.save(any(DataSet.class))).thenReturn(dummyDataSet);
    }

    @Given("an internal error that causes no data collections to be created")
    public void givenAnInternalErrorThatCausesNoDataCollectionsToBeCreated() throws Exception {

    }

    @Given("an internal error that causes at least one data collection to not be created")
    public void givenAnInternalErrorThatCausesAtLeastOneDataCollectionToNotBeCreated() throws Exception {

    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    @When("the API which creates a data collection is invoked")
    public void whenApiWhichCreatesADataCollectionIsInvokedToCreateASingleDataCollection() throws Exception {
        result = mockMvc.perform(
                post("/api/v1/datacatalog/data-collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestToJSON(this.request))
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500"));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("the response's status code should be 201")
    public void thenResponseCodeShouldBe201() throws Exception {
        result.andExpect(status().isCreated());
    }

    @Then("the response's status code should be 400")
    public void thenResponseCodeShouldBe400() throws Exception {

    }

    @Then("the response's status code should be 500")
    public void thenResponseCodeShouldBe500() throws Exception {

    }

    @Then("the response's content type should be JSON")
    public void thenResponseMediaTypeShouldBeJson() throws Exception {
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the image sets should saved as 1 data collection in the database")
    public void thenResponseBodyShouldContainJsonStringDefiningASingleDataCollection() throws Exception {
        final DataSet expectedDataSet = createMockDataSet(this.imageSetIds);
        verify(dataSetRepository, times(1)).save(expectedDataSet);
    }

    @Then("the image sets should saved as <numCollections> data collections in the database whereby each collection does not contain an image set in another collection")
    public void thenResponseBodyShouldContainJsonStringDefiningMultipleDistinctCollections(@Named("numCollections") int numCollections) throws Exception {
        //result.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"12345678-abcd-42ca-a317-4d408b98c500\",\"createdBy\":\"test\",\"properties\":{},\"imageSets\":[]}")));
    }

    @Then("the response's body should contain a JSON string that defines <description>")
    public void thenResponseBodyShouldContainJsonStringDefiningDataCollections(@Named("description") String description, @Named("numCollections") int numCollections) throws Exception {
        //result.andExpect(content().string(containsString("{\"id\":1,\"schemaVersion\":\"123\",\"name\":\"Test\",\"description\":\"test\",\"createdDate\":\"22-01-2017 10:20:56\",\"type\":\"Annotation\",\"orgId\":\"12345678-abcd-42ca-a317-4d408b98c500\",\"createdBy\":\"test\",\"properties\":{},\"imageSets\":[]}")));
    }

    @Then("the response's body should contain an error message saying the provided image sets should be unique")
    public void thenResponseBodyShouldContainErrorMessageSayingProvidedImageSetsShouldBeUnique() throws Exception {

    }

    @Then("the response's body should contain an error message saying there was an internal error and no collections were created")
    public void thenResponseBodyShouldContainErrorMessageSayingThereWasInternalServerErrorAndNoCollectionsWereCreated() throws Exception {

    }

    @Then("the response's body should contain an error message saying the number of data collections to create should be greater than or equal to 1")
    public void thenResponseBodyShouldContainErrorMessageSayingTheNumberOfDataCollectionsToCreateShouldBeGreaterThanOrEqualToOne() throws Exception {

    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private String requestToJSON(DataCollectionsCreateRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }

    private DataSet createMockDataSet(String[] imageSetIds) {
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
        dataSet.setImageSets(Arrays.asList(imageSetIds));
        return dataSet;
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return "22-01-2017 10:20:56";
    }

}



