package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.entity.CondensedDataCollection;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code RetrieveDataCollectionsSteps} implements the test scenarios defined by the
 * {@code retrieve_data_collections_run.feature} file.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@Component
public class RetrieveDataCollectionsSteps {

    private final DataSetRepository dataSetRepository;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private MockMvc mockMvc;
    private ResultActions result;

    private List<DataSet> dataSets;

    ///////////////
    //
    // Constants //
    //
    ///////////////

    private static final Long MOCK_ID = 1L;
    private static final String MOCK_NAME = "mock name";
    private static final String MOCK_DESCRIPTION = "mock description";
    private static final String MOCK_COLLECTION_TYPE = "Annotation";
    private static final String MOCK_CREATED_BY = "mock created by";
    private static final String MOCK_CREATED_DATE = "mock created date";
    private static final String MOCK_ORG_ID = "mock org ID";
    private static final List<Long> MOCK_IMAGE_SET_IDS = Arrays.asList(new Long[]{1L, 2L, 3L});

    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////

    @Autowired
    public RetrieveDataCollectionsSteps(MockMvc mockMvc, DataSetRepository dataSetRepository, DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.dataSetRepository = dataSetRepository;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
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

    @Given("an org has one or more data collections")
    public void givenAnOrgHasOneOrMoreDataCollections() {
        this.dataSets = Arrays.asList(new DataSet[]{createMockDataSet(), createMockDataSet(), createMockDataSet()});
    }

    @Given("no internal errors occur when retrieving the data collections for the target org ID")
    public void givenNoInternalErrorsOccurWhenRetrievingTheDataCollectionsForTheTargetOrgId() {
        when(dataSetRepository.findByOrgIdOrderByCreatedDateDesc(any(Pageable.class), any(String.class))).thenReturn(this.dataSets);
    }

    @Given("no internal errors occur when retrieving the data collections of a target collection type for the target org ID")
    public void givenNoInternalErrorsOccurWhenRetrievingTheDataCollectionsOfATargetCollectionTypeForTheTargetOrgId() {
        when(dataSetRepository.findByTypeAndOrgIdOrderByCreatedDateDesc(any(Pageable.class), any(String.class), any(String.class))).thenReturn(this.dataSets);
    }

    @SuppressWarnings("unchecked")
    @Given("an internal error occurs that causes the data collection retrieval for the target org ID to fail")
    public void givenAnInternalErrorOccursThatCausesTheDataCollectionRetrievalToFail() throws Exception {
        when(dataSetRepository.findByOrgIdOrderByCreatedDateDesc(any(Pageable.class), any(String.class))).thenThrow(Exception.class);
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    @When("the API which retrieves data collections is invoked with the target org ID")
    public void whenApiWhichRetrievesDataCollectionsIsInvokedWithTheTargetOrgId() throws Exception {
        result = mockMvc.perform(get("/api/v1/datacatalog/data-collection").contentType(MediaType.APPLICATION_JSON).requestAttr("orgId", MOCK_ORG_ID));
    }

    @When("the API which retrieves a data collection is invoked without an org ID")
    public void whenApiWhichRetrievesDataCollectionsIsInvokedWithoutTheTargetOrgId() throws Exception {
        result = mockMvc.perform(get("/api/v1/datacatalog/data-collection").contentType(MediaType.APPLICATION_JSON));
    }

    @When("the API which retrieves a data collection is invoked with the target org ID and collection type")
    public void whenApiWhichRetrievesDataCollectionsIsInvokedWithTheTargetOrgIdAndCollectionType() throws Exception {
        result = mockMvc.perform(get("/api/v1/datacatalog/data-collection?type=" + MOCK_COLLECTION_TYPE).contentType(MediaType.APPLICATION_JSON).requestAttr("orgId", MOCK_ORG_ID));
    }

    @When("the API which retrieves a data collection is invoked with the target org ID and invalid collection type")
    public void whenApiWhichRetrievesDataCollectionsIsInvokedWithTheTargetOrgIdAndInvalidCollectionType() throws Exception {
        result = mockMvc.perform(get("/api/v1/datacatalog/data-collection?type=foobar").contentType(MediaType.APPLICATION_JSON).requestAttr("orgId", MOCK_ORG_ID));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("the status code resulting from retrieving the data collections should be 200")
    public void thenTheStatusCodeResultingFromRetrievingTheDataCollectionsShouldBe200() throws Exception {
        result.andExpect(status().isOk());
    }

    @Then("the status code resulting from retrieving the data collections should be 400")
    public void thenTheStatusCodeResultingFromRetrievingTheDataCollectionsShouldBe400() throws Exception {
        result.andExpect(status().isBadRequest());
    }

    @Then("the content type resulting from  retrieving the data collections should be JSON")
    public void thenTheContentTypeResultingFromRetrievingTheDataCollectionsShouldBeJson() throws Exception {
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        result.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then("a single request to retrieve all data collections for the target org ID should be made")
    public void thenASingleRequestToRetrieveAllDataCollectionForTheTargetOrgIdShouldBeMade() throws Exception {
        verify(dataSetRepository, times(1)).findByOrgIdOrderByCreatedDateDesc(any(Pageable.class), eq(MOCK_ORG_ID));
    }

    @Then("a single request to retrieve all data collections of the target collection type for the target org ID should be made")
    public void thenASingleRequestToRetrieveAllDataCollectionOfTheTargetCollectionTypeForTheTargetOrgIdShouldBeMade() throws Exception {
        verify(dataSetRepository, times(1)).findByTypeAndOrgIdOrderByCreatedDateDesc(any(Pageable.class), eq(MOCK_COLLECTION_TYPE), eq(MOCK_ORG_ID));
    }

    @Then("the body resulting from retrieving the data collections should contain an error message saying an org ID needs to be provided")
    public void thenTheBodyResultingFromRetrievingTheDataCollectionsShouldContainAnErrorMessageSayingAnOrgIdNeedsToBeProvided() throws Exception {
        result.andExpect(content().string(containsString("An org ID must be provided in the request headers")));
    }

    @Then("the body resulting from retrieving the data collections should contain an error message saying an invalid collection type was provided")
    public void thenTheBodyResultingFromRetrievingTheDataCollectionsShouldContainAnErrorMessageSayingAnInvalidCollectionTypeWasProvided() throws Exception {
        result.andExpect(content().string(containsString("Invalid data collection type was provided")));
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private DataSet createMockDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.setId(MOCK_ID);
        dataSet.setName(MOCK_NAME);
        dataSet.setDescription(MOCK_DESCRIPTION);
        dataSet.setType(MOCK_COLLECTION_TYPE);
        dataSet.setCreatedBy(MOCK_CREATED_BY);
        dataSet.setCreatedDate(MOCK_CREATED_DATE);
        dataSet.setOrgId(MOCK_ORG_ID);
        dataSet.setImageSets(MOCK_IMAGE_SET_IDS);

        return dataSet;
    }

    private CondensedDataCollection createMockDataCollectionWithNumImageSets() {
        return new CondensedDataCollection(MOCK_ID, MOCK_NAME, MOCK_DESCRIPTION, MOCK_COLLECTION_TYPE, MOCK_CREATED_BY, MOCK_CREATED_DATE, MOCK_ORG_ID, MOCK_IMAGE_SET_IDS.size());
    }

}
