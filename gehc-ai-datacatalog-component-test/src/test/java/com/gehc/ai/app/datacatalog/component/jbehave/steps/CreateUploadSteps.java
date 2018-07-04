package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.UploadRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code CreateUploadSteps} implements the test scenarios defined by
 * the {@code create_upload_run.feature} file.
 *
 * @author arunasindhugorantla
 */
@Component
public class CreateUploadSteps {
    
    private final DataCatalogDaoImpl dataCatalogDao;
    private final UploadRepository uploadRepository;
    private final ContractRepository contractRepository;

    private MockMvc mockMvc;
    private ResultActions result;
    
    private Upload uploadRequest = buildUploadEntity();

    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////

    @Autowired
    public CreateUploadSteps(MockMvc mockMvc, UploadRepository uploadRepository,
                             ContractRepository contractRepository, DataCatalogDaoImpl dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.uploadRepository = uploadRepository;
        this.dataCatalogDao = dataCatalogDao;
        this.contractRepository = contractRepository;

    }

    //////////////////////
    //
    // GIVEN statements //
    //
    //////////////////////

    @Given("all required data are provided for create upload request")
    public void allRequiredDataAreProvidedForCreateUploadRequest()
    {
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    @Given("org ID is not provided in the upload request")
    public void orgIDIsNotProvidedInTheUploadRequest() throws Exception{
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    @Given("contractId is not provided in the upload request")
    public void contractIdIsNotProvidedInTheUploadRequest() throws Exception{
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    @Given("spaceId is not provided in the upload request")
    public void spaceIdIsNotProvidedInTheUploadRequest() throws Exception{
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    @Given("uploadBy status is not provided in the upload request")
    public void uploadByIsNotProvidedInTheUploadRequest() throws Exception{
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    @Given("tags is not provided in the upload request")
    public void tagsIsNotProvidedInTheUploadRequest() throws Exception{
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    @Given("spaceId, orgId, contractId is already associated with an existing upload entity")
    public void DuplicateDataInTheUploadRequest() throws Exception{
        Contract contract = buildContractEntity();
        Upload upload = buildUploadEntity();
        Upload uploadEntity = buildUploadEntity();
        when(dataCatalogDao.getContractDetails(anyLong())).thenReturn(contract);
        when(dataCatalogDao.getUploadByQueryParameters(anyString(),anyString(),anyLong())).thenReturn(uploadEntity);
        when(dataCatalogDao.saveUpload(any(Upload.class))).thenReturn(upload);
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////
    @When("the API which creates a upload is invoked")
    public void theAPIWhichCreatesUploadIsInvoked() throws Exception{
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked without an org ID")
    public void theAPIWhichCreatesUploadIsInvokedWithoutAnOrgID() throws Exception{
        this.uploadRequest.setOrgId("");
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked without contractId")
    public void theAPIWhichCreatesUploadIsInvokedWithoutContractId() throws Exception{
        this.uploadRequest.setContractId(null);
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked without spaceId")
    public void theAPIWhichCreatesUploadIsInvokedWithoutSpaceId() throws Exception{
        this.uploadRequest.setSpaceId("");
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked without uploadBy")
    public void theAPIWhichCreatesUploadIsInvokedWithoutUploadBy() throws Exception{
        this.uploadRequest.setUploadBy("");
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked without tags")
    public void theAPIWhichCreatesUploadIsInvokedWithoutTags() throws Exception{
        this.uploadRequest.setTags(null);
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked with duplicate data in spaceId, orgId, contractId")
    public void theAPIWhichCreatesUploadIsInvokedWithDuplicateDataInTheUploadRequest() throws Exception{
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        uploadRequest.setId(1L);
        uploadRequest.setSchemaVersion("v1");
        uploadRequest.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        uploadRequest.setContractId(100L);
        uploadRequest.setSpaceId("space123");
        uploadRequest.setUploadBy("user");
        uploadRequest.setTags(tags);
        uploadRequest.setUploadDate(new Timestamp(1313045029));
        uploadRequest.setLastModified(new Timestamp(1313045029));
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                                                                   .content(requestToJSON(this.uploadRequest)));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    @Then("a single upload should be saved to the database")
    public void aSingleUploadShouldBeSavedToTheDatabase() throws Exception{
        Upload upload = buildUploadEntity();
        verify(dataCatalogDao, times(1)).saveUpload(any(Upload.class));
    }

    @Then("the create upload response's status code should be 201")
    public void theCreateUploadResponseStatusCodeShouldBe201() throws Exception{
        result.andExpect(status().isCreated());
    }

    @Then("the create upload response's content type should be JSON")
    public void theCreateUploadResponseContentTypeShouldBeJson() throws Exception{
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        result.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then("the create upload response's status code should be 400")
    public void theCreateUploadResponseStatusCodeShouldBe400() throws Exception{
        result.andExpect(status().isBadRequest());
    }

    @Then("the create upload response's status code should be 409")
    public void theCreateUploadResponseStatusCodeShouldBe409() throws Exception{
        result.andExpect(status().isConflict());
    }

    @Then("the response's body should contain an error message saying the request is Missing one/more required fields data.")
    public void theCreateUploadResponseMessageShouldBeMissingRequiredFieldsData() throws Exception{
        result.andExpect(content().string(containsString("Missing one/more required fields data.")));
    }

    @Then("the response's body should contain an error message saying the an upload entity already exists with given spaceId, orgId and contractId.")
    public void theCreateUploadResponseMessageShouldBeDuplicationErrorMessage() throws Exception{
        result.andExpect(content().string(containsString("An Upload entity already exists with given spaceId, orgId and contractId.")));
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private String requestToJSON(Upload uploadRequest) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(uploadRequest);
        return str;
    }

    private Upload buildUploadEntity(){
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        Upload uploadRequest = new Upload();
        uploadRequest.setId(1L);
        uploadRequest.setSchemaVersion("v1");
        uploadRequest.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        uploadRequest.setContractId(100L);
        uploadRequest.setSpaceId("space123");
        uploadRequest.setUploadBy("user");
        uploadRequest.setTags(tags);
        uploadRequest.setUploadDate(new Timestamp(1313045029));
        uploadRequest.setLastModified(new Timestamp(1313045029));
        uploadRequest.setDataType(new ArrayList<>());

        return uploadRequest;
   }

    private Contract buildContractEntity(){
        Contract contract = new Contract();
        
        contract.setId(1L);
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("perpetuity");
        contract.setActive("true");
        
        return contract;
    }
}

