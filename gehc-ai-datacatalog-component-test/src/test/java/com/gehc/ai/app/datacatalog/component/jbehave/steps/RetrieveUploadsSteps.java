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
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code RetrieveUploadsSteps} implements the test scenarios defined by
 * the {@code retrieve_uploads_run.feature} file.
 *
 * @author arunasindhugorantla
 */
@Component
public class RetrieveUploadsSteps {

    private final DataCatalogDaoImpl dataCatalogDao;
    private final UploadRepository uploadRepository;

    private MockMvc mockMvc;
    private ResultActions result;

    private Upload uploadRequest = buildUploadEntity();

    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////

    @Autowired
    public RetrieveUploadsSteps(MockMvc mockMvc, UploadRepository uploadRepository, DataCatalogDaoImpl dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.uploadRepository = uploadRepository;
        this.dataCatalogDao = dataCatalogDao;

    }

    //////////////////////
    //
    // GIVEN statements //
    //
    //////////////////////

    @Given("a request to retrieve all uploads for an organization")
    public void aRequestToGetAllUploadEntitiesWithValidAuthenticationTokenToGetUploadsForAnOrganization(){
        Upload upload = buildUploadEntity();
        List<Upload> uploadsList = new ArrayList<>();
        uploadsList.add(upload);
        uploadsList.add(upload);
        uploadsList.add(upload);
        when(dataCatalogDao.getAllUploads(anyString())).thenReturn(uploadsList);
    }

    @Given("the request is authorized to read uploads for a specific organization")
    public void aRequestISAuthorisedToReadUploadsForAnOrganization(){

    }

    @Given("the request does not authorize read access to specific organization’s uploads")
    public void aRequestISNotAuthorisedToReadUploadsForAnOrganization(){

    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////
    @When("the API which retrieves uploads is invoked")
    public void theAPIWhichRetrievesUploadsIsInvoked() throws Exception{
        result = mockMvc.perform(get("/api/v1/datacatalog/upload")
                .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500"));
    }

    @When("the API which retrieves uploads is invoked - unauthorized")
    public void theAPIWhichRetrievesUploadsIsInvokedWithoutValidAuthenticationToken() throws Exception{
        result = mockMvc.perform(get("/api/v1/datacatalog/upload"));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////
    @Then("a single call to get all uploads for the target organization should be made to the database")
    public void aSingleCallToGetUploadsToTheDatabase() throws Exception{
        verify(dataCatalogDao, times(1)).getAllUploads(anyString());
    }

    @Then("the retrieve uploads API response content type should be JSON")
    public void theCreateUploadResponseContentTypeShouldBeJson() throws Exception{
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        result.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then("the retrieve uploads API response status code should be 200")
    public void theCreateUploadResponseStatusCodeShouldBe201() throws Exception{
        result.andExpect(status().isOk());
    }

    @Then("the retrieve uploads API response status code should be 403")
    public void theCreateUploadResponseStatusCodeShouldBe400() throws Exception{
        result.andExpect(status().isForbidden());
    }

    @Then("the retrieve uploads API response body should contain an error message saying he is not authorized to access a specific organization’s uploads")
    public void theGetAllUploadResponseMessageShouldBeRequestCannotBeValidatedBecauseOfMalformedAuthorizationToken() throws Exception{
        result.andExpect(content().string(containsString("Request cannot be validated because of malformed authorization token.")));
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
        uploadRequest.setId(2L);
        uploadRequest.setSchemaVersion("v1");
        uploadRequest.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        uploadRequest.setContractId(100L);
        uploadRequest.setSpaceId("space123");
        uploadRequest.setUploadBy("user");
        uploadRequest.setDataType(dataType);
        uploadRequest.setTags(tags);
        uploadRequest.setUploadDate(new Timestamp(1313045029));
        uploadRequest.setLastModified(new Timestamp(1313045029));

        return uploadRequest;
   }

}

