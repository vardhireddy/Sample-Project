package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.Contract.DeidStatus;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by dipshah on 3/26/18.
 */
@Component
public class ContractSteps {
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private IDataCatalogDao dataCatalogDao;
    private ContractRepository contractRepository;

    @Autowired
    public ContractSteps(MockMvc mockMvc,
                         IDataCatalogDao dataCatalogDao, ContractRepository contractRepository) {
        this.mockMvc = mockMvc;
        this.dataCatalogDao = dataCatalogDao;
        this.contractRepository = contractRepository;
    }

    @Given("contract Id and Org Id")
    public void givenContractIdAndOrgId() throws Exception {
        Contract contract = getContract();
        when(contractRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(contract);

    }

    @When("the given parameters are existing in the repository")
    public void whenTheParametersExistInRepo() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1/validate?orgId=orgId"));
    }

    @Then("verify the api response status code is 200")
    public void verifyStatusCodeIs200() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Then("verify the api response body contains \"Contract exists\"")
    public void verifyResponseIsContractExists() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Contract exists")));
    }

    @Given("an inactive contract Id and an Org Id")
    public void givenInActiveContractIdAndOrgId() throws Exception {
        Contract contract = getContract();
        contract.setActive("false");
        when(contractRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(contract);

    }

    @When("the API to validate contract is invoked")
    public void whenTheContractIsInactive() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1/validate?orgId=orgId"));
    }

    @Then("a single request to retrieve the contract should be made to the repository")
    public void thenASingleRequestToRetrieveAllDataCollectionForTheTargetOrgIdShouldBeMade() throws Exception {
        verify(contractRepository, times(1)).findByIdAndOrgId(anyLong(), anyString());
    }

    @Then("the status code for the contract validation should be 403")
    public void thenTheStatusCodeForTheContractValidationShouldBe403() throws Exception {
        retrieveResult.andExpect(status().isForbidden());
    }

    @Then("verify the api response body contains \"Contract is inactive/expired\"")
    public void verifyResponseIsContractisInvalid() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Contract is inactive/expired")));
    }

    @Given("invalid contract Id or Org Id")
    public void givenInvalidContractIdAndOrgId() throws Exception {
        when(contractRepository.countByIdAndOrgId(anyLong(), anyString())).thenReturn(0);
    }

    @When("any of the given parameters are not existing in the repository")
    public void whenTheParametersDoesNotExistInRepo() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1/validate?orgId=invalidOrgId"));
    }

    @Then("verify that the api response status code is 200")
    public void verifyStatusCodeIsOk() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Then("verify the api response body contains \"Contract does not exist\"")
    public void verifyResponseIsContractDosNotExist() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Contract does not exist")));
    }

    @Given("a valid contract Id")
    public void givenValidAndActiveContractId() throws Exception {
        Contract contract = getContract();
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
    }

    @When("the contract id exists in repository and contract is active/ in true state")
    public void whenTheContractIsActive() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/datacatalog/contract/1").requestAttr("orgId","1"));
    }

    @Then("verify api response status code is 200")
    public void verifyStatusCodeIsOk200() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Then("verify the api response body contains \"Contract is inactivated successfully\"")
    public void verifyResponseIsContractDeletedSuccessfully() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Contract is inactivated successfully")));
    }

    @Given("a valid contract Id - contract inactive")
    public void givenValidAndInActiveContractId() throws Exception {
        Contract contract = getContract();
        contract.setActive("false");
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
    }

    @When("the contract id exists in repository but the contract is inactive/ in false state")
    public void whenTheContractIsInActive() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/datacatalog/contract/1").requestAttr("orgId","1"));
    }

    @Then("verify api response status code is 200 - ok")
    public void verifyStatusCodeOk200() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @Then("verify the api response body contains \"Contract with given id is already inactive\"")
    public void verifyResponseIsContractAlreadyDeleted() throws Exception {
        retrieveResult.andExpect(content().string(containsString("Contract with given id is already inactive")));
    }

    @Given("an invalid contract Id")
    public void givenInValidContractId() throws Exception {
        when(contractRepository.findById(anyLong())).thenReturn(null);
    }

    @When("the contract id does not exist in repository")
    public void whenTheContractDoesNotExistInRepo() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/datacatalog/contract/1").requestAttr("orgId","1"));
    }

    @Then("verify that the api response status code is 404")
    public void deleteResponseCodeIs404() throws Exception {
        retrieveResult.andExpect(status().isNotFound());
    }

    @Then("verify the api response body contains \"No contract exists with given id\"")
    public void verifyResponseIsContractDoesNotExist() throws Exception {
        retrieveResult.andExpect(content().string(containsString("No contract exists with given id")));
    }

    private Contract getContract(){
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setActive("true");
        //contract.setDataOriginCountry("Data Origin Country");
        contract.setOrgId("1");
        contract.setAgreementBeginDate("2018-06-08");
        contract.setDataUsagePeriod("perpetuity");

        return contract;
    }

}