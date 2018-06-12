package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.*;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUser;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUsage;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HEAD;
import java.nio.charset.Charset;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class RetrieveContractsSteps {

    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private Contract contract;
    private final DataCatalogDaoImpl dataCatalogDao;
    private final ContractRepository contractRepository;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private List<Contract> contractLst;

    @Autowired
    public RetrieveContractsSteps(MockMvc mockMvc, DataCatalogDaoImpl dataCatalogDao, ContractRepository contractRepository, DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.dataCatalogDao = dataCatalogDao;
        this.contractRepository = contractRepository;
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

    @Given("there are one or more contracts in the database")
    public void thereAreOneOrMoreContractsInDb() throws Exception{
        contractLst = getAllContractMock();
    }

    @Given("there are no contracts in the database")
    public void thereAreNoContractsInDb() throws Exception{
        contractLst = new ArrayList<>();
    }

    @Given("no internal errors occur when retrieving the contracts")
    public void givenNoInternalErrorsOccurWhenRetrievingTheContracts(){
        when(dataCatalogDao.getAllContractsDetails(anyString())).thenReturn(contractLst);
    }

    @Given("an internal error occurs when retrieving the contracts")
    public void givenAnInternalErrorOccursWhenRetrievingTheContracts() {
        when(dataCatalogDao.getAllContractsDetails(anyString())).thenThrow(Exception.class);
    }

    //given test cases for getContractsForDataCollection api
    @Given("a data collection/set ID supported by LF")
    public void givenDataSetIdSupportedByLF(){

        List<Contract> contractByDataSetIdList = new ArrayList<>();
        Contract contractByDataSetId = buildContractByDataSetId();
        contractByDataSetIdList.add(contractByDataSetId);

        Contract contractByDataSetId2 = buildContractByDataSetId();
        contractByDataSetId2.setActive("true");
        contractByDataSetIdList.add(contractByDataSetId2);

        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);
        when(dataCatalogDao.getContractsByImageSetIds(imageSetIdList)).thenReturn(contractByDataSetIdList);
    }

    @Given("a data collection/set ID not supported by LF")
    public void givenDataSetIdNotSupportedByLF(){
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(new ArrayList<>());
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    @When("the API which retrieves the contracts is invoked with an org ID")
    public void whenTheAPIWhichCreatesAContractIsInvoked() throws Exception {
        retrieveResult = mockMvc.perform(get("/api/v1/datacatalog/contract")
                .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500"));
    }

    //when test cases for getContractsForDataCollection api
    @When("the api that gets contracts associated with the image sets of that data collection")
    public void whenApiReturnsDataForDatSetId() throws Exception{
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/contract/data-collection/1"));
    }

    @When("the api that gets contracts associated with the image sets of that data collection is hit")
    public void whenApiReturnsDataForDatSetIdIsHit() throws Exception{
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/contract/data-collection/12").contentType(MediaType.APPLICATION_JSON));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("all the contracts should be retrieved from the database")
    public void thenAllActiveContractsShouldBeRetrievedFromTheDatabase() throws Exception {
        verify(dataCatalogDao, times(1)).getAllContractsDetails("12345678-abcd-42ca-a317-4d408b98c500");
        retrieveResult.andExpect(content().string(containsString("expired")));
    }

    @Then("the get contracts response status code should be 200")
    public void thenGetContractsResponseCodeShouldBe200() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Then("the get contracts response status code should be 500")
    public void thenGetContractsResponseCodeShouldBe500() throws Exception {
        retrieveResult.andExpect(status().isInternalServerError());
    }

    @Then("the get contracts response's content type should be JSON")
    public void thenGetContractsResponseContentTypeShouldBeJson() throws Exception {
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        retrieveResult.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then("the response's body should contain a message saying could not get the contracts details due to an internal error")
    public void thenResponseBodyShouldContainErrorMessageSayingCouldNotGetContractsDueToInternalError() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Could not get the contracts due to an internal error")));
    }

    //then test cases for getContractsForDataCollection api
    @Then("the api must return a map of active and inactive contracts associated with the data collection")
    public void thenResultShouldBeMapOfContractLists() throws Exception{
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("false")));
    }

    @Then("the api must return error message saying no contracts exist for the given dataSet ID")
    public void thenResultShouldBeAnErrorMessage() throws Exception{
        retrieveResult.andExpect(status().isBadRequest());
        retrieveResult.andExpect(content().string(containsString("No contracts exist for the given dataSet ID.")));
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private String requestToJSON(Contract contract) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(contract);
        return str;
    }

    private Contract buildContractByDataSetId(){
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("9999-06-08");
        contract.setDataUsagePeriod("12");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setActive("false");
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setUploadBy("user");
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);

        return contract;
    }

    private List<Contract> getAllContractMock() throws Exception{

        List<Contract> result = new ArrayList<Contract>();

        Contract contract = new Contract();
        contract.setId(1L);
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);
        contract.setActive("true");
        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        contract.setUri(uriList);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setSchemaVersion("v1");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("365");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setUploadBy("user");
        contract.setExpired(false);

        result.add(contract);

        return result;
    }

}