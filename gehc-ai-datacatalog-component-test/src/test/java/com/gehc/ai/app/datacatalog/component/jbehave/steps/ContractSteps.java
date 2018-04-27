package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import javax.ws.rs.core.MediaType;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.Contract.DeidStatus;



/**
 * Created by dipshah on 3/26/18.
 */
@Component
public class ContractSteps {
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private IDataCatalogDao dataCatalogDao;
    
    @Autowired
    public ContractSteps(MockMvc mockMvc, 
    		IDataCatalogDao dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.dataCatalogDao = dataCatalogDao;
    }

    @Given("Store contract data - DataSetUp Provided")
    public void givenStoreContractData() throws Exception {
        Contract contract = getContract();
        when(dataCatalogDao.ingestContractDetails(any(Contract.class))).thenReturn(contract.getId());
    }

    @When("Store contract data")
    public void StoreContractData() throws Exception {
        
    	ClassLoader classLoader = getClass().getClassLoader();
    	
    	
        MockMultipartFile firstFile = new MockMultipartFile("contract", "contract.pdf", MediaType.MULTIPART_FORM_DATA, 
        									classLoader.getResourceAsStream("data/contract.pdf"));
        MockMultipartFile jsonFile = new MockMultipartFile("metadata", "metadata.json", MediaType.MULTIPART_FORM_DATA, 
        									classLoader.getResourceAsStream("data/metadata.json"));

        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/v1/datacatalog/contract")
                        .file(firstFile).file(jsonFile));

    }

    @Then("verify Store contract data")
    public void verifyStoreContractData() throws Exception {
    	retrieveResult.andExpect(content().json("{\"status\": \"SUCCESS\",\"responseObject\": 1}"));
        retrieveResult.andExpect(status().isCreated());
    }
    
    @Given("Retrieve contract data - DataSetUp Provided")
    public void givenRetrieveContractData() throws Exception {
        Contract contract = getContract();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
    }

    @When("Retrieve contract data")
    public void RetrieveContractData() throws Exception {
        retrieveResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/datacatalog/contract/1"));
    }

    @Then("verify Retrieve contract data")
    public void verifyRetrieveContractData() throws Exception {
    	//retrieveResult.andExpect(content().json("{\"status\": \"SUCCESS\",\"responseObject\": 1}"));
        retrieveResult.andExpect(status().isOk());
    }

    private Contract getContract(){
    	Contract contract = new Contract();
    	contract.setId(1L);
    	contract.setActive("true");
    	contract.setBusinessCase("Business Case");
    	contract.setContactInfo("Contact Info");
    	contract.setContractName("Contract Name");
    	contract.setDataOriginCountry("Data Origin Country");
    	contract.setDeidStatus(DeidStatus.DEIDS_TO_LOCAL_STANDARDS);
    	contract.setOrgId("orgId");
    	contract.setProperties("[\"CKGS USA - Passport _ How To Apply.pdf\",\"CKGS USA - Passport _ How To Apply.pdf\" ]");
    	contract.setSchemaVersion("Schema Version");
    	contract.setUploadBy("radiologist");
    	contract.setUploadDate(LocalDateTime.now());
    	contract.setUri("[\"CKGS USA - Passport _ How To Apply.pdf\",\"CKGS USA - Passport _ How To Apply.pdf\" ]");
    	contract.setUsageNotes("Usage Notes");
    	contract.setUsageRights("Usage Rights");
    	
    	return contract;
    }

}
