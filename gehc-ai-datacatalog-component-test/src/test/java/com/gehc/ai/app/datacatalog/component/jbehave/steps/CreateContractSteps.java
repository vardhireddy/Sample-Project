package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUsage;
import static com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUser;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    public CreateContractSteps(MockMvc mockMvc, DataCatalogDaoImpl dataCatalogDaoImpl, DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.dataCatalogDao = dataCatalogDaoImpl;
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

    @Given("all required legal meta data are provided")
    public void givenAllRequiredLegalMetaDataAreProvided() {
        this.contract = createMockContractRequest();
    }

    @Given("no internal errors occur")
    public void givenNoInternalErrorsOccur() {
        when(dataCatalogDao.saveContract(any(Contract.class))).thenReturn(createMockCompleteContract());
    }

    @Given("an internal error occurs")
    public void givenAnInternalErrorOccurs() {
        when(dataCatalogDao.saveContract(any(Contract.class))).thenThrow(Exception.class);
    }

    @Given("not all required legal meta data are provided")
    public void givenNotAllRequiredLegalMetaDataAreProvided() {
        this.contract = new Contract();
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

    @Then("a single contract should be saved to the database")
    public void thenASingleContractShouldBeSavedToTheDatabase()
            throws Exception {

        verify(dataCatalogDao, times(1)).saveContract(createMockCompleteContract());
    }

    @Then("the response's body should contain an error message saying not all required legal meta were provided")
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
        contract.setContractName("Test contract name");
        contract.setContactInfo("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setContractStartDate(null);
        contract.setUsageLength(365);
        contract.setDataOriginCountries(Arrays.asList(new String[]{"USA", "China"}));
        contract.setState("CA");
        contract.setTerritory("Test Territory");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT)}));
        contract.setDataResidence(Contract.DataResidence.GLOBAL);

        return contract;
    }

    private Contract createMockCompleteContract() {
        Contract contract = new Contract();
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setContractName("Test contract name");
        contract.setContactInfo("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setContractStartDate(null);
        contract.setUsageLength(365);
        contract.setDataOriginCountries(Arrays.asList(new String[]{"USA", "China"}));
        contract.setState("CA");
        contract.setTerritory("Test Territory");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT)}));
        contract.setDataResidence(Contract.DataResidence.GLOBAL);
        contract.setActive("Inactive");

        return contract;
    }

}
