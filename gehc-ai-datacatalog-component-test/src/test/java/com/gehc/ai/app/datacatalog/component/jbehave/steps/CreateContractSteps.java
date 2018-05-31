package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

import static org.mockito.Matchers.anyLong;

/**
 * {@code CreateContractSteps} implements the test scenarios defined by
 * the {@code create_contract_run.feature} file.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@Component
public class CreateContractSteps {

    private final DataCatalogInterceptor dataCatalogInterceptor;
    private final DataCatalogDaoImpl dataCatalogDao;
    private final ContractRepository contractRepository;

    private MockMvc mockMvc;
    private ResultActions result;

    private Contract contract;

    ///////////////
    //
    // Constants //
    //
    ///////////////


    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////
/*
    @Autowired
    public CreateContractSteps(MockMvc mockMvc, DataCatalogDaoImpl dataCatalogDaoImpl, DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.dataCatalogDao = dataCatalogDaoImpl;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
    }
*/
    @Autowired
    public CreateContractSteps(MockMvc mockMvc, ContractRepository contractRepository,
                               DataCatalogInterceptor dataCatalogInterceptor, DataCatalogDaoImpl dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.contractRepository = contractRepository;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
        this.dataCatalogDao = dataCatalogDao;
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

    @Given("all required legal meta data are provided")
    public void givenAllRequiredLegalMetaDataAreProvided() {
        this.contract = createMockContractRequest();
    }

    @Given("no internal errors occur")
    public void givenNoInternalErrorsOccur() {
        when(contractRepository.save(any(Contract.class))).thenReturn(createMockCompleteContract());
    }

    @Given("an internal error occurs")
    public void givenAnInternalErrorOccurs() {
        when(contractRepository.save(any(Contract.class))).thenThrow(Exception.class);
    }

    @Given("not all required legal meta data are provided")
    public void givenNotAllRequiredLegalMetaDataAreProvided() {
        this.contract = new Contract();
    }

    @Given("a valid and active contract ID to retrieve")
    public void ValidActiveContractIdToRetrieve(){
        Contract contract = createMockCompleteContract();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
    }

    @Given("a valid and inactive contract ID to retrieve")
    public void ValidInActiveContractIdToRetrieve(){
        Contract contract = createMockCompleteContract();
        contract.setActive("false");
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
    }

    @Given("an invalid contract ID to retrieve")
    public void InValidContractIdToRetrieve(){
        Contract contract = new Contract();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    @When("the API which creates a contract is invoked with an org ID")
    public void whenTheAPIWhichCreatesAContractIsInvoked() throws Exception {
        result = mockMvc.perform(post("/api/v1/datacatalog/contract").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.contract)).requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500"));
    }

    @When("the API which creates a contract is invoked without an org ID")
    public void whenTheAPIWhichCreatesAContractIsInvokedWithoutAnOrgId() throws Exception {
        result = mockMvc.perform(post("/api/v1/datacatalog/contract").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.contract)));
    }

    @When("the API which retrieves a contract is invoked with a valid and active contract ID")
    public void whenTheAPIWhichRetrievesAContractIsInvoked() throws Exception {
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1"));
    }

    @When("the API which retrieves a contract is invoked with a valid and inactive contract ID")
    public void whenTheAPIWhichRetrievesAContractIsInvokedWithInactiveID() throws Exception {
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1"));
    }

    @When("the API which retrieves a contract is invoked with an invalid contract ID")
    public void whenTheAPIWhichRetrievesAContractIsInvokedWithInvalidID() throws Exception {
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1"));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("the response's status code should be 200")
    public void thenResponseCodeShouldBe200() throws Exception {
        result.andExpect(status().isOk());
    }

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

    @Then("a single contract should be saved to the database")
    public void thenASingleContractShouldBeSavedToTheDatabase()
            throws Exception {

        verify(contractRepository, times(1)).save(createMockCompleteContract());
    }

/*    @Then("the response's body should contain an error message saying not all required legal meta were provided")
    public void thenResponseBodyShouldContainErrorMessageSayingADataCollectionNeedsToBeProvided() throws Exception {
        result.andExpect(content().string(containsString("All legal meta data must be provided")));
    }

    @Then("the response's body should contain an error message saying an org ID must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingImageSetsMustBeDefinedForTheDataCollection() throws Exception {
        result.andExpect(content().string(containsString("An organization ID must be provided")));
    }

    @Then("the response's body should contain an error message saying an internal error occurred")
    public void thenResponseBodyShouldContainErrorMessageSayingANameMustBeDefinedForTheDataCollection() throws Exception {
        result.andExpect(content().string(containsString("Could not save contract due to an internal error")));
    }*/
    
    @Then("the response's body should contain an error message saying not all required legal meta were provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAllLegalMetaDateNeedsToBeProvided() throws Exception {
        result.andExpect(content().string(containsString("All legal meta data must be provided")));
    }

    @Then("the response's body should contain an error message saying an org ID must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAnOrgIdMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("An organization ID must be provided")));
    }

    @Then("the response's body should contain an error message saying an internal error occurred")
    public void thenResponseBodyShouldContainErrorMessageSayingAnInternalServerErrorOccured() throws Exception {
        result.andExpect(content().string(containsString("Could not save contract due to an internal error")));
    }

    @Then("verify Retrieve contract data")
    public void verifyGetContractData() throws Exception {
        result.andExpect(content().string(containsString("true")));
    }

    @Then("verify response is \"Contract associated with given Id is inactive\"")
    public void verifyGetContract() throws Exception {
        result.andExpect(content().string(containsString("Contract associated with given Id is inactive")));
    }

    @Then("verify response is \"No Contract Exists with the given Id\"")
    public void verifyGetContractBadRequest() throws Exception {
        result.andExpect(content().string(containsString("No Contract Exists with the given Id")));
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

    private Contract createMockContractRequest() {
        Contract contract = new Contract();
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT.toString());
        contract.setAgreementBeginDate(null);
        contract.setDataUsagePeriod("365");
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
        contract.setActive("true");
    //    contract.setTerritory("Test Territory");
    //    contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT)}));
     //   contract.setDataResidence(Contract.DataResidence.GLOBAL);

        return contract;
    }

    private Contract createMockCompleteContract() {
        Contract contract = new Contract();
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT.toString());
        contract.setAgreementBeginDate(null);
        contract.setDataUsagePeriod("365");
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
      //  contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT)}));
     //   contract.setDataResidence(Contract.DataResidence.GLOBAL);
        contract.setActive("true");

        return contract;
    }

}
