package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.rest.request.UpdateContractRequest;
import com.gehc.ai.app.datacatalog.service.impl.DataCatalogServiceImpl;
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

/**
 * {@code CreateContractSteps} implements the test scenarios defined by
 * the {@code create_contract_run.feature} file.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@Component
public class CreateContractSteps {

    private final DataCatalogInterceptor dataCatalogInterceptor;
    private final ContractRepository contractRepository;
    private final DataCatalogServiceImpl dataCatalogService;

    private MockMvc mockMvc;
    private ResultActions result;

    private Contract contract;

    private UpdateContractRequest updateRequest = buildContractEntityForUpdate();
    private UpdateContractRequest emptyUpdateRequest = new UpdateContractRequest();

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
                               DataCatalogInterceptor dataCatalogInterceptor,DataCatalogServiceImpl dataCatalogService) {
        this.mockMvc = mockMvc;
        this.contractRepository = contractRepository;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
        this.dataCatalogService = dataCatalogService;
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

    @Given("no internal errors occurs")
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

    //get contract API test cases
    @Given("a valid and active contract ID to retrieve")
    public void ValidActiveContractIdToRetrieve(){
        Contract contract = createMockCompleteContract();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
    }

    @Given("a valid and inactive contract ID to retrieve")
    public void ValidInActiveContractIdToRetrieve(){
        Contract contract = createMockCompleteContract();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
    }

    @Given("an invalid contract ID to retrieve")
    public void InValidContractIdToRetrieve(){
        Contract contract = new Contract();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
    }

    //update contract API test cases
    @Given("a valid and active contract ID, valid update request")
    public void validIdAndData(){
        Contract contract = createMockContractRequest();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid and active contract ID, invalid/empty update request")
    public void validIdAndInvalidData(){
        Contract contract = createMockContractRequest();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid and inactive contract ID to retrieve, valid request")
    public void inactiveIdAndValidData(){
        Contract contract = createMockContractRequest();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid and inactive contract ID to retrieve, invalid request (update request is validated first)")
    public void inactiveIdAndInValidData(){
        Contract contract = createMockContractRequest();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("an invalid contract ID to update, valid update request")
    public void invalidIdAndValidData(){
        Contract contract = new Contract();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("an invalid contract ID to update, invalid/empty update request")
    public void invalidIdAndInValidData(){
        Contract contract = new Contract();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid and active contract ID, valid status value only")
    public void validIdAndValidStatusOnly(){
        Contract contract = createMockContractRequest();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid and active contract ID, valid uri value only")
    public void validIdAndValidUriOnly(){
        Contract contract = createMockContractRequest();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid contract ID, valid data but exception in retrieving contract")
    public void validDataButExceptionRetrievingData(){
        Contract contract = new Contract();
        when(dataCatalogService.getContract(anyLong())).thenThrow(new RuntimeException(""));
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);
    }

    @Given("a valid contract ID, valid data but exception in saving updated contract")
    public void validDataButExceptionSavingData(){
        Contract contract = createMockContractRequest();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenThrow(new RuntimeException(""));
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

    //get contract API test cases
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

    //update contract API test cases
    @When("the API which updates a contract is invoked with a valid and active contract ID, valid update request")
    public void whenValidIdAndValidData() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid and active contract ID, invalid/empty update request")
    public void whenValidIdAndInValidData() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.emptyUpdateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid and inactive contract ID, valid request")
    public void whenInactiveIdAndValidData() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid and inactive contract ID, invalid request")
    public void whenInactiveIdAndInValidData() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.emptyUpdateRequest)));
    }

    @When("the API which updates a contract is invoked with an invalid contract ID, valid update request")
    public void whenInValidIdAndValidData() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    @When("the API which updates a contract is invoked with an invalid contract ID, invalid/empty update request")
    public void whenInValidIdAndInValidData() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.emptyUpdateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid and active contract ID, valid status value")
    public void whenValidIdAndValidStatusOnly() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid and active contract ID, valid uri value")
    public void whenValidIdAndValidUriOnly() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid ID, valid data but internal exception rises in retrieving")
    public void whenValidIdAndValidDataIntrenalExceptionRetrieving() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    @When("the API which updates a contract is invoked with a valid ID, valid data but internal exception rises in saving the contract")
    public void whenValidIdAndValidDataIntrenalExceptionSaving() throws Exception{
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/datacatalog/contract/1").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.updateRequest)));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("the response status code should be 200")
    public void thenResponseCodeShouldBe200() throws Exception {
        result.andExpect(status().isOk());
    }

    @Then("the response status code should be 201")
    public void thenResponseCodeShouldBe201() throws Exception {
        result.andExpect(status().isCreated());
    }

    @Then("the response status code should be 400")
    public void thenResponseCodeShouldBe400() throws Exception {
        result.andExpect(status().isBadRequest());
    }

    @Then("the response status code should be 500")
    public void thenResponseCodeShouldBe500() throws Exception {
        result.andExpect(status().isInternalServerError());
    }

    @Then("the response content type should be JSON")
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

    //get Contract then test cases
    @Then("verify Retrieve contract data")
    public void verifyGetContractData() throws Exception {
        result.andExpect(content().string(containsString("true")));
    }

    @Then("the response's body should contain a message saying the contract associated with given ID is inactive")
    public void verifyGetContract() throws Exception {
        result.andExpect(content().string(containsString("Contract associated with given Id is inactive")));
    }

    //update contract test cases
    @Then("response and database contract data should reflect the updated details")
    public void thenValidIdAndData() throws Exception{
        result.andExpect(content().string(containsString("updatedStatus")));
    }

    @Then("the response's body should contain a message saying the update request cannot be empty. Either status or uri must be provided")
    public void thenValidIdAndInvalidData() throws Exception{
        result.andExpect(content().string(containsString("Update request cannot be empty. Either status or uri must be provided.")));
    }

    @Then("the response's body should contain a message saying the contract associated with given Id is inactive and contract shall not be updated")
    public void thenInactiveIdAndValidData() throws Exception{
        result.andExpect(content().string(containsString("Contract associated with given Id is inactive. Contract shall not be updated.")));
    }

    @Then("the response's body should contain a message saying no contract exists with the given ID")
    public void thenInValidIdAndValidData() throws Exception{
        result.andExpect(content().string(containsString("{\"response\":\"No Contract Exists with the given Id.\"}")));
    }

    @Then("response and database contract data should reflect the updated details with change only in status")
    public void thenValidIdAndStatusOnlyInData() throws Exception{
        result.andExpect(content().string(containsString("updatedStatus")));
    }

    @Then("response and database contract data should reflect the updated details with change only in uri")
    public void thenValidIdAndUriOnlyInData() throws Exception{
        result.andExpect(content().string(containsString("{\"id\":1,\"orgId\":\"12345678-abcd-42ca-a317-4d408b98c500\"," +
                "\"uri\":\"[\\\"bla.pdf\\\"]\",\"deidStatus\":\"HIPAA Compliant\",\"dataOriginCountry\":\"USA\"," +
                "\"active\":\"true\",\"agreementName\":\"Test contract name\",\"primaryContactEmail\":\"john.doe@ge.com\"," +
                "\"dataUsagePeriod\":\"365\",\"dataOriginState\":\"CA\",\"status\":\"updatedStatus\"}"))); }

    @Then("response's body should contain a message exception retrieving the contract")
    public void thenValidIdAndDataExceptionRetrievingContract() throws Exception{
        result.andExpect(content().string(containsString("Exception retrieving the contract.")));
    }


    @Then("response's body should contain a message exception saving the updated contract")
    public void thenValidIdAndDataExceptionSavingUpdatedContract() throws Exception{
        result.andExpect(content().string(containsString("Exception saving the updated contract.")));
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

    private String requestToJSON(UpdateContractRequest updateRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(updateRequest);
        return str;
    }

    private Contract createMockContractRequest() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT.toString());
        contract.setDataUsagePeriod("365");
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
        contract.setUri("[\"bla.pdf\"]");
        contract.setActive("true");
        contract.setAgreementBeginDate(null);

    //    contract.setTerritory("Test Territory");
    //    contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT)}));
     //   contract.setDataResidence(Contract.DataResidence.GLOBAL);

        return contract;
    }

    private Contract createMockCompleteContract() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT.toString());
        contract.setAgreementBeginDate(null);
        contract.setDataUsagePeriod("365");
        contract.setDataOriginCountry("USA");
        contract.setDataOriginState("CA");
        contract.setUri("[\"bla.pdf\"]");
      //  contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT)}));
     //   contract.setDataResidence(Contract.DataResidence.GLOBAL);
        contract.setActive("true");

        return contract;
    }

    private UpdateContractRequest buildContractEntityForUpdate(){

        UpdateContractRequest updateContractRequest = new UpdateContractRequest("updatedStatus","[\"bla.pdf\"]");

        return updateContractRequest;
    }

    public Date getSqldate() throws Exception{
        String startDate="30-03-2013";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = sdf1.parse(startDate);
        java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
        System.out.print(sqlStartDate);
        return sqlStartDate;
    }
}
