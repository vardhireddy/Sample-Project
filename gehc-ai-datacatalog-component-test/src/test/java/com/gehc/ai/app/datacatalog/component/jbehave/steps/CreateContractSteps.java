package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import gherkin.lexer.Da;
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
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUser;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUsage;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

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

    @Given("no internal errors occur when saving the contract")
    public void givenNoInternalErrorsOccurWhenSavingTheContract() {
        Contract c = createMockCompleteContract();
        when(contractRepository.save(any(Contract.class))).thenReturn(c);
    }

    @Given("an internal error occur when saving the contract")
    public void givenAnInternalErrorOccurWhenSavingTheContract() {
        when(contractRepository.save(any(Contract.class))).thenThrow(Exception.class);
    }

    @Given("required legal meta data - agreement name is not provided")
    public void givenRequiredLegalMetaDataAgreementNameNotProvided() {
        this.contract = createMockInCompleteContract(null,"john.doe@ge.com", Contract.DeidStatus.HIPAA_COMPLIANT.toString(), "2015-03-31","365", Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}),Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}), "USA");

    }

    @Given("required legal meta data - primary contact email is not provided")
    public void givenRequiredLegalMetaDataPrimaryContactEmailNotProvided() {
        this.contract = createMockInCompleteContract("agreeName",null, Contract.DeidStatus.HIPAA_COMPLIANT.toString(), "2015-03-31","365", Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}), Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}), "USA");

    }

    @Given("required legal meta data - de-identified status is not provided")
    public void givenRequiredLegalMetaDeIdentifiedStatusNotProvided() {
        this.contract = createMockInCompleteContract("agreeName","john.doe@ge.com", null, "2015-03-31","365", Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}), Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}), "USA");

    }

    @Given("required legal meta data - agreement begin date is not provided")
    public void givenRequiredLegalMetaDataAgreementBeginDateNotProvided() {
        this.contract = createMockInCompleteContract("agreeName","john.doe@ge.com", Contract.DeidStatus.HIPAA_COMPLIANT.toString(), null,"365", Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}), Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}), "USA");

    }

    @Given("required legal meta data - data usage period is not provided")
    public void givenRequiredLegalMetaDataUsagePeriodNotProvided() {
        this.contract = createMockInCompleteContract("agreeName","john.doe@ge.com", Contract.DeidStatus.HIPAA_COMPLIANT.toString(), "2015-03-31",null, Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}), Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}), "USA");

    }

    @Given("required legal meta data - data use cases are not provided")
    public void givenRequiredLegalMetaDataUseCasesNotProvided() {
        this.contract = createMockInCompleteContract("agreeName","john.doe@ge.com", Contract.DeidStatus.HIPAA_COMPLIANT.toString(), "2015-03-31","365", null,Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}),"USA");

    }

    @Given("required legal meta data - data origin country and state are not provided")
    public void givenRequiredLegalMetaDataOriginCountryStateNotProvided() {
        this.contract = createMockInCompleteContract("agreeName","john.doe@ge.com", Contract.DeidStatus.HIPAA_COMPLIANT.toString(), "2015-03-31","365", Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}), null, "USA");

    }

    @Given("required legal meta data - data allowed location is not provided")
    public void givenRequiredLegalMetaDataAllowedLocationNotProvided() {
        this.contract = createMockInCompleteContract("agreeName","john.doe@ge.com", Contract.DeidStatus.HIPAA_COMPLIANT.toString(), "2015-03-31","365", Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}), Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}), null);

    }



//    @Given("an internal error occurs when saving the contract")
//    public void givenAnInternalErrorOccursWhenSavingTheContract() {
//        when(contractRepository.save(any(Contract.class))).thenThrow(Exception.class);
//    }

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
        result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/datacatalog/contract/1"));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("the validate contract response's status code should be 200")
    public void thenValidateContractResponseCodeShouldBe200() throws Exception {
        result.andExpect(status().isOk());
    }

    @Then("the create contract response's status code should be 201")
    public void thenCreateContractResponseCodeShouldBe201() throws Exception {
        result.andExpect(status().isCreated());
    }

    @Then("the create contract response's status code should be 400")
    public void thenCreateContractResponseCodeShouldBe400() throws Exception {
        result.andExpect(status().isBadRequest());
    }

    @Then("the validate contract response's status code should be 400")
    public void thenValidateContractResponseCodeShouldBe400() throws Exception {
        result.andExpect(status().isBadRequest());
    }

    @Then("the create contract response's status code should be 500")
    public void thenCreateContractResponseCodeShouldBe500() throws Exception {
        result.andExpect(status().isInternalServerError());
    }

    @Then("the create contract response's content type should be JSON")
    public void thenCreateContractResponseMediaTypeShouldBeJson() throws Exception {
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        result.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then("a single contract should be saved to the database")
    public void thenASingleContractShouldBeSavedToTheDatabase()
            throws Exception {
        Contract c = createMockCompleteContract();
        verify(contractRepository, times(1)).save(c);
    }

    @Then("the response's body should contain an error message saying not all required legal meta were provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAllLegalMetaDateNeedsToBeProvided() throws Exception {
        result.andExpect(content().string(containsString("All legal meta data must be provided")));
    }

    @Then("the response's body should contain an error message saying an agreement name must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAnAgreementNameMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("An agreement name must be provided")));
    }

    @Then("the response's body should contain an error message saying a primary contact email must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAPrimaryEmailMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("A primary contact email must be provided")));
    }

    @Then("the response's body should contain an error message saying the de-identified status must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingTheDeIdStatusMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("The de-identified status must be provided")));
    }

    @Then("the response's body should contain an error message saying an agreement begin date must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAnAgreementBeginDateMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("An agreement begin date must be provided")));
    }

    @Then("the response's body should contain an error message saying the data usage period must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingTheDataUsagePeriodMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("The data usage period must be provided")));
    }

    @Then("the response's body should contain an error message saying the data use cases must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingTheDataUseCasesMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("The data use cases must be provided")));
    }

    @Then("the response's body should contain an error message saying the data origin country and state must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingTheDataOriginCountryStateMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("The data origin country and state must be provided")));
    }

    @Then("the response's body should contain an error message saying the the data allowed location must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingTheDataAllowedLocationMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("The data allowed location must be provided")));
    }

    @Then("the response's body should contain an error message saying an org ID must be provided")
    public void thenResponseBodyShouldContainErrorMessageSayingAnOrgIdMustBeProvided() throws Exception {
        result.andExpect(content().string(containsString("An organization ID must be provided")));
    }

    @Then("the response's body should contain an error message saying could not save contract due to an internal error")
    public void thenResponseBodyShouldContainErrorMessageSayingCouldNotSaveContractDueToInternalError() throws Exception {
        result.andExpect(content().string(containsString("Could not save contract due to an internal error")));
    }

    @Then("verify Retrieve contract data")
    public void verifyGetContractData() throws Exception {
        result.andExpect(content().string(containsString("true")));
    }

    @Then("the response's body should contain a message saying the contract associated with given ID is inactive")
    public void verifyGetContract() throws Exception {
        result.andExpect(content().string(containsString("Contract associated with given Id is inactive")));
    }

    @Then("the response's body should contain a message saying no contract exists with the given ID")
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

    private Date getDate() {
        String str = "2017-03-02";
        return Date.valueOf(str);
    }

    private Date getDate2() {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate1 = dateFormat.parse("2018-03-30");
            date = new java.sql.Date(utilDate1.getTime());
        }catch (Exception e){}
        return date;
    }

    public Date getSqldate() throws Exception{
        String startDate="30-03-2013";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = sdf1.parse(startDate);
        java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
        System.out.print(sqlStartDate);
        return sqlStartDate;
    }

    private Contract createMockContractRequest() {
        Contract contract = new Contract();
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT.toString());
//        Date date = getDate();
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("365");
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
        //contract.setActive("true");

        //    contract.setTerritory("Test Territory");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        contract.setDataLocationAllowed("USA");
        //   contract.setDataResidence(Contract.DataResidence.GLOBAL);

        return contract;
    }

    private Contract createMockCompleteContract() {
        Contract contract = new Contract();
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT.toString());
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("365");
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        //   contract.setDataResidence(Contract.DataResidence.GLOBAL);
        contract.setActive("true");
        contract.setDataLocationAllowed("USA");

        return contract;
    }

    private Contract createMockInCompleteContract(String agreementName, String primaryContactEmail, String deidStatus, String agreementBeginDate, String dataUsagePeriod, List<ContractUseCase> useCases, List<ContractDataOriginCountriesStates> dataOriginCountriesStates, String dataLocationAllowed) {
        Contract contract = new Contract();
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName(agreementName);
        contract.setPrimaryContactEmail(primaryContactEmail);
        contract.setDeidStatus(deidStatus);
        contract.setAgreementBeginDate(agreementBeginDate);
        contract.setDataUsagePeriod(dataUsagePeriod);
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
        contract.setUseCases(useCases);
        contract.setDataOriginCountriesStates(dataOriginCountriesStates);
        //   contract.setDataResidence(Contract.DataResidence.GLOBAL);
        contract.setActive("true");
        contract.setDataLocationAllowed(dataLocationAllowed);

        return contract;
    }

}


/*
@test
Scenario: User shall not be able to retrieve contract data for valid and inactive contract Id
Meta: @automated
Given a valid and inactive contract ID to retrieve
When the API which retrieves a contract is invoked with a valid and inactive contract ID
Then the validate contract response's status code should be 200
Then the response's body should contain a message saying the contract associated with given ID is inactive

@test
Scenario: User shall not be able to retrieve contract data for invalid contract Id
Meta: @automated
Given an invalid contract ID to retrieve
When the API which retrieves a contract is invoked with an invalid contract ID
Then the validate contract response's status code should be 400
Then the response's body should contain a message saying no contract exists with the given ID
 */



