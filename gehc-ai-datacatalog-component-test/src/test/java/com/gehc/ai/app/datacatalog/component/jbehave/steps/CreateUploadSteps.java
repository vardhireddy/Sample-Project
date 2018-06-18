package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUsage;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUser;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.UploadRepository;
import com.gehc.ai.app.datacatalog.rest.request.UpdateContractRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
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

    @Given("dataType is not provided in the upload request")
    public void dataTypeIsNotProvidedInTheUploadRequest() throws Exception{
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

    @When("the API which creates a upload is invoked without dataType")
    public void theAPIWhichCreatesUploadIsInvokedWithoutDataType() throws Exception{
        this.uploadRequest.setDataType(null);
        result = mockMvc.perform(post("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                .content(requestToJSON(this.uploadRequest)));
    }

    @When("the API which creates a upload is invoked without tags")
    public void theAPIWhichCreatesUploadIsInvokedWithoutTags() throws Exception{
        this.uploadRequest.setTags(null);
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
        verify(dataCatalogDao, times(1)).saveUpload(upload);
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

    @Then("the response's body should contain an error message saying the request is Missing one/more required fields data.")
    public void theCreateUploadResponseMessageShouldBeMissingRequiredFieldsData() throws Exception{
        result.andExpect(content().string(containsString("Missing one/more required fields data.")));
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
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        Upload uploadRequest = new Upload();
        uploadRequest.setId(1L);
        uploadRequest.setSchemaVersion("v1");
        uploadRequest.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        uploadRequest.setContractId(100L);
        uploadRequest.setSpaceId("space123");
        uploadRequest.setUploadBy("user");
        uploadRequest.setDataType(dataType);
        uploadRequest.setTags(tags);
        uploadRequest.setUploadDate(new Timestamp(System.currentTimeMillis()));
        uploadRequest.setLastModified(new Timestamp(System.currentTimeMillis()));

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

