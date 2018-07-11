package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.DataCollectionsCreateRequest;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.Properties;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assume.assumeThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code CreateDataCollectionsSteps} implements the test scenarios defined by
 * the {@code create_data_collections_run.feature} file.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@Component
public class CreateDataCollectionsSteps {

    private final DataSetRepository dataSetRepository;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private MockMvc mockMvc;
    private ResultActions result;

    private Long[] imageSetIds;
    private DataCollectionsCreateRequest request;
    private DataSet dataCollection;

    ///////////////
    //
    // Constants //
    //
    ///////////////

    private static final int NUM_OF_TEST_IMAGE_SET_IDS = 7;

    private static final Long[] UNIQUE_IMAGE_SET_IDS = new Long[]{123L, 456L, 789L, 101112L, 131415L, 161718L, 192021L};

    private static final Long[] NON_UNIQUE_IMAGE_SET_IDS = new Long[]{123L, 456L, 789L, 101112L, 131415L, 161718L, 161718L};

    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////

    @Autowired
    public CreateDataCollectionsSteps(MockMvc mockMvc, DataSetRepository dataSetRepository,
                                      DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.dataSetRepository = dataSetRepository;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class),
                anyObject())).thenReturn(true);
    }

    //////////////////////
    //
    // GIVEN statements //
    //
    //////////////////////

    @Given("no data collection is provided")
    public void givenNoDataCollectionIsProvided() {
        this.request = new DataCollectionsCreateRequest();
    }

    @Given("no image sets are defined")
    public void giveNoImageSetsAreDefined() {
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = createMockDataSet(null, "Test", "Annotation");
        this.request.setDataSet(this.dataCollection);
    }

    @Given("an existing data collection")
    public void givenAnExistingDataCollection() {
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = createExistingMockDataSet(this.imageSetIds, "Test", "Annotation", 1L);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("a data collection that does not have a name defined")
    public void givenNoDataCollectionNameIsDefined() {
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = createExistingMockDataSet(this.imageSetIds, null, "Annotation", 1L);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("a data collection that does not have a type defined")
    public void givenNoDataCollectionTypeIsDefined() {
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = createExistingMockDataSet(this.imageSetIds, "Test", null, 1L);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("a data collection with unique image sets")
    public void givenPoolOfUniqueImageSets() {
        assumeThat(UNIQUE_IMAGE_SET_IDS.length, is(equalTo(NUM_OF_TEST_IMAGE_SET_IDS)));
        this.imageSetIds = UNIQUE_IMAGE_SET_IDS;
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = createMockDataSet(this.imageSetIds, "Test", "Annotation");
    }

    @Given("a data collection with non-unique image sets")
    public void givenPoolOfNonUniqueImageSets() {
        assumeThat(NON_UNIQUE_IMAGE_SET_IDS.length, is(equalTo(NUM_OF_TEST_IMAGE_SET_IDS)));
        this.imageSetIds = NON_UNIQUE_IMAGE_SET_IDS;
        this.request = new DataCollectionsCreateRequest();
        this.dataCollection = createMockDataSet(this.imageSetIds, "Test", "Annotation");
    }

    @Given("no data collection size is specified")
    public void givenNoDataCollectionSizeIsSpecified() {
        this.request.setDataCollectionSize(null);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the data collection size is 1")
    public void givenTheDataCollectionSizeIsOne() {
        this.request.setDataCollectionSize(1);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the data collection size is greater than 1 and less than the number of image sets")
    public void givenTheDataCollectionSizeIsGreaterThanOneAndLessThanTheNumberOfImageSets() {
        final int dataCollectionSize = 2;
        assumeThat(dataCollectionSize, is(greaterThan(1)));
        assumeThat(dataCollectionSize, is(lessThan(NUM_OF_TEST_IMAGE_SET_IDS)));
        this.request.setDataCollectionSize(2);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the data collection size is equal to the number of image sets")
    public void givenTheDataCollectionSizeIsEqualToTheNumberOfImageSets() {
        this.request.setDataCollectionSize(NUM_OF_TEST_IMAGE_SET_IDS);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the data collection size is less than 1")
    public void givenTheDataCollectionSizeIsLessThanOne() {
        this.request.setDataCollectionSize(-1);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the data collection size is greater than the number of image sets")
    public void givenTheDataCollectionSizeIsGreaterThanTheNumberOfImageSets() {
        this.request.setDataCollectionSize(NUM_OF_TEST_IMAGE_SET_IDS + 1);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the pool image of image sets is to be represented as <numCollections> data collections")
    public void givenPoolOfImageSetsIsToBeRepresentedAsSeveralCollections(@Named("numCollections") int numCollections) {
        this.request.setDataCollectionSize(numCollections);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("the specified data collection size doesn't matter")
    public void givenPoolOfImageSetsIsToBeRepresentedAs() {
        // Arbitrarily select a collection size
        this.request.setDataCollectionSize(1);
        this.request.setDataSet(this.dataCollection);
    }

    @Given("no internal errors occur")
    public void givenNoInternalErrorsOccur() {
        DataSet dummyDataSet = new DataSet();
        List<DataSet> dataSetsSaved = new ArrayList<>();
        dataSetsSaved.add(dummyDataSet);
        when(dataSetRepository.save(anyListOf(DataSet.class))).thenReturn(dataSetsSaved);
    }

    @SuppressWarnings("unchecked")
    @Given("an internal error that causes no data collections to be created")
    public void givenAnInternalErrorThatCausesNoDataCollectionsToBeCreated() throws Exception {
        when(dataSetRepository.save(anyListOf(DataSet.class))).thenThrow(DataRetrievalFailureException.class);
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    @When("the API which creates a data collection is invoked")
    public void whenApiWhichCreatesADataCollectionIsInvokedToCreateASingleDataCollection() throws Exception {
        result = mockMvc.perform(post("/api/v1/datacatalog/data-collection").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.request)).requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500"));
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
        result.andExpect(status().isBadRequest());
    }

    @Then("the response's status code should be 500")
    public void thenResponseCodeShouldBe500() throws Exception {
        result.andExpect(status().isInternalServerError());
    }

    @Then("the response's content type should be JSON")
    public void thenResponseMediaTypeShouldBeJson() throws Exception {
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        result.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then("the image sets should be split such that there are as many data collections saved to the database as there are image sets")
    public void thenTheImageSetsShouldBeSplitSuchThatThereAreAsManyDataCollectionsSavedToTheDatabaseAsThereAreImageSets()
            throws Exception {
        List<DataSet> expectedDataSetsToSave = Arrays.asList(new DataSet[]{createMockDataSetWithSuffix(new Long[]{123L}, "Test", "Annotation", 1),
                createMockDataSetWithSuffix(new Long[]{456L}, "Test", "Annotation", 2), createMockDataSetWithSuffix(new Long[]{789L}, "Test", "Annotation", 3),
                createMockDataSetWithSuffix(new Long[]{101112L}, "Test", "Annotation", 4), createMockDataSetWithSuffix(new Long[]{131415L}, "Test", "Annotation", 5),
                createMockDataSetWithSuffix(new Long[]{161718L}, "Test", "Annotation", 6), createMockDataSetWithSuffix(new Long[]{192021L}, "Test", "Annotation", 7)});

        verify(dataSetRepository, times(1)).save(expectedDataSetsToSave);
    }

    @Then("the number of data collections that have the target collection size should be quotient of the number of image sets divided by the target collection size and there should be one data collection saved that contains the remainder of the quotient")
    public void thenTheNumberOfDataColletionsThatHaveTheTargetCollectionSizeShouldBeTheQuotientOfTheNumberOfImageSetsDividedByTheTargetCollectionSizeAndThereShouldBeOneDataCollectionSavedThatContainsTheRemainderOfTheQuotient()
            throws Exception {
        List<DataSet> expectedDataSetsToSave = Arrays.asList(new DataSet[]{
                createMockDataSetWithSuffix(new Long[]{123L, 456L}, "Test", "Annotation",1), createMockDataSetWithSuffix(new Long[]{789L, 101112L}, "Test", "Annotation",2),
                createMockDataSetWithSuffix(new Long[]{131415L, 161718L}, "Test", "Annotation",3), createMockDataSetWithSuffix(new Long[]{192021L}, "Test", "Annotation",4)});

        verify(dataSetRepository, times(1)).save(expectedDataSetsToSave);
    }

    @Then("1 data collection should be saved to the database")
    public void thenOneDataCollectionShouldBeSavedToTheDatabase() throws Exception {
        List<DataSet> expectedDataSetsToSave = Arrays.asList(new DataSet[]{
                createMockDataSet(new Long[]{123L, 456L, 789L, 101112L, 131415L, 161718L, 192021L},"Test", "Annotation")});

        verify(dataSetRepository, times(1)).save(expectedDataSetsToSave);
    }

    @Then("1 data collection should be updated to the database")
    public void thenOneDataCollectionShouldBeUpdatedToTheDatabase() throws Exception {
        List<DataSet> expectedDataSetsToSave = Arrays.asList(new DataSet[]{
                createExistingMockDataSet(new Long[]{123L, 456L, 789L, 101112L, 131415L, 161718L, 192021L}, "Test", "Annotation",1L)});

        verify(dataSetRepository, times(1)).save(expectedDataSetsToSave);
    }

    @Then("the response's body should contain an error message saying a data collection needs to be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingADataCollectionNeedsToBeProvided() throws Exception {
        result.andExpect(content().string(containsString("A data collection must be provided")));
    }

    @Then("the response's body should contain an error message saying image sets must defined for the data collection")
    public void thenResponseBodyShouldContainErrorMessageSayingImageSetsMustBeDefinedForTheDataCollection() throws Exception {
        result.andExpect(content().string(containsString("The data collection must define a set of image set IDs")));
    }

    @Then("the response's body should contain an error message saying a name must defined for the data collection")
    public void thenResponseBodyShouldContainErrorMessageSayingANameMustBeDefinedForTheDataCollection() throws Exception {
        result.andExpect(content().string(containsString("The data collection must define a name")));
    }

    @Then("the response's body should contain an error message saying a type must defined for the data collection")
    public void thenResponseBodyShouldContainErrorMessageSayingATypeMustBeDefinedForTheDataCollection() throws Exception {
        result.andExpect(content().string(containsString("The data collection must define a type")));
    }

    @Then("the response's body should contain an error message saying the provided image sets should be unique")
    public void thenResponseBodyShouldContainErrorMessageSayingProvidedImageSetsShouldBeUnique() throws Exception {
        result.andExpect(content().string(containsString("The provided image sets are not unique")));
    }

    @Then("the response's body should contain an error message saying there was an internal error and no collections were created")
    public void thenResponseBodyShouldContainErrorMessageSayingThereWasInternalServerErrorAndNoCollectionsWereCreated() throws Exception {
        result.andExpect(content().string(containsString("Failed to save data collections")));
    }

    @Then("the response's body should contain an error message saying the image sets could be batched using the provided data collection size")
    public void thenResponseBodyShouldContainErrorMessageSayingTheNumberOfDataCollectionsToCreateShouldBeGreaterThanOrEqualToOne() throws Exception {
        result.andExpect(content().string(containsString("Failed to batch the provided image sets")));
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

    private DataSet createMockDataSet(Long[] imageSetIds, String name, String type) {
        DataSet dataSet = new DataSet();
        dataSet.setCreatedBy("test");
        dataSet.setDescription("test");
        String dateInString = getDate();
        dataSet.setCreatedDate(dateInString);
        dataSet.setName(name);
        dataSet.setProperties(new Properties());
        dataSet.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        dataSet.setSchemaVersion("123");
        dataSet.setType(type);
        if (Objects.nonNull(imageSetIds)) {
            dataSet.setImageSets(Arrays.asList(imageSetIds));
        } else {
            dataSet.setImageSets(null);
        }

        return dataSet;
    }

    private DataSet createMockDataSetWithSuffix(Long[] imageSetIds, String name, String type, int collectionSuffix) {
        DataSet dataSet = createMockDataSet(imageSetIds, name, type);
        dataSet.setName("Test - " + collectionSuffix);
        return dataSet;
    }

    private DataSet createExistingMockDataSet(Long[] imageSetIds, String name, String type, long collectionId) {
        DataSet dataSet = createMockDataSet(imageSetIds, name, type);
        dataSet.setId(collectionId);
        return dataSet;
    }

    private String getDate() {
        return "22-01-2017 10:20:56";
    }

}
