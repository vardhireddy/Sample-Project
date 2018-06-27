package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.UploadRepository;
import com.gehc.ai.app.datacatalog.rest.request.UpdateUploadRequest;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code UpdateUploadSteps} implements the test scenarios defined by
 * the {@code update_upload_run.feature} file.
 *
 * @author arunasindhugorantla
 */
@Component
public class UpdateUploadSteps {

    private final DataCatalogDaoImpl dataCatalogDao;
    private final UploadRepository uploadRepository;
    private final ContractRepository contractRepository;

    private MockMvc mockMvc;
    private ResultActions result;

    private UpdateUploadRequest updateUploadRequest = buildUpdateUploadRequest();

    /////////////////////////
    //
    // Test scenario setup //
    //
    /////////////////////////

    @Autowired
    public UpdateUploadSteps( MockMvc mockMvc, UploadRepository uploadRepository,
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

    @Given( "all required data are provided for update upload request" )
    public void AllDataIsProvided(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( any(Upload.class)) ).thenReturn( upload );
    }

    @Given( "upload ID provided  is invalid in the update upload request" )
    public void uploadIdIsNotProvided(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( any(Upload.class)) ).thenReturn( upload );
    }

    @Given( "summary and status both are not provided in the update upload request" )
    public void dataProvidedIsInComplete(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( any(Upload.class)) ).thenReturn( upload );
    }

    @Given( "lastModified date is not provided in the upload request" )
    public void lastModifiedDateInvalid(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( any(Upload.class)) ).thenReturn( upload );
    }

    @Given( "stale last modified date is provided in the upload request" )
    public void lastModifiedDateIsStale(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( any(Upload.class)) ).thenReturn( upload );
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////
    @When( "the API which updates a upload is invoked" )
    public void theAPIWhichUpdatesUploadIsInvoked() throws Exception{
        result = mockMvc.perform(put("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                                                                   .content(requestToJSON(this.updateUploadRequest)));
    }

    @When( "the API which updates a upload is invoked without upload ID" )
    public void theAPIWhichUpdatesUploadIsInvokedWithoutUploadId() throws Exception{
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,String> status = new HashMap<>();
        status.put("DICOM","99/100");
        status.put("NON-DICOM","1/1");
        UpdateUploadRequest updateUploadRequest =  new UpdateUploadRequest(null,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",summary,null,
                                                                           status,"user1",
                                                                           new Timestamp( 1313045029),new Timestamp( 1313045029));
        result = mockMvc.perform(put("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                                                                  .content(requestToJSON(updateUploadRequest)));
    }

    @When( "the API which updates a upload is invoked without summary and status" )
    public void theAPIWhichUpdatesUploadIsInvokedWithoutSummaryAndStatus() throws Exception{
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,String> status = new HashMap<>();
        status.put("DICOM","99/100");
        status.put("NON-DICOM","1/1");
        UpdateUploadRequest updateUploadRequest =  new UpdateUploadRequest(10L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",null,null,
                                                                           null,"user1",
                                                                           new Timestamp( 1313045029),new Timestamp( 1313045029));
        result = mockMvc.perform(put("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                                                                  .content(requestToJSON(updateUploadRequest)));
    }

    @When( "the API which updates a upload is invoked without lastModified date" )
    public void theAPIWhichUpdatesUploadIsInvokedWithInvalidLastModifiedDate() throws Exception{
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,String> status = new HashMap<>();
        status.put("DICOM","99/100");
        status.put("NON-DICOM","1/1");
        UpdateUploadRequest updateUploadRequest =  new UpdateUploadRequest(10L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",summary,null,
                                                                           status,"user1",
                                                                           new Timestamp( 1313045029),null);
        result = mockMvc.perform(put("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                                                                  .content(requestToJSON(updateUploadRequest)));
    }

    @When( "the API which updates a upload is invoked with old lastModified date" )
    public void theAPIWhichUpdatesUploadIsInvokedWithOldLastModifiedDate() throws Exception{
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,String> status = new HashMap<>();
        status.put("DICOM","99/100");
        status.put("NON-DICOM","1/1");
        UpdateUploadRequest updateUploadRequest =  new UpdateUploadRequest(10L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",summary,null,
                                                                           status,"user1",
                                                                           new Timestamp( 1313045029),new Timestamp( 1313045030));
        result = mockMvc.perform(put("/api/v1/datacatalog/upload").contentType(MediaType.APPLICATION_JSON)
                                                                  .content(requestToJSON(updateUploadRequest)));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////
    @Then( "a single upload should be save updated upload to the database" )
    public void callToSaveUpdatedUploadToDB() throws Exception{
        verify(dataCatalogDao, times(1)).saveUpload(any(Upload.class));
    }
    @Then("the update uploads API response status code should be 200")
    public void theupdateUploadResponseStatusCodeShouldBe201() throws Exception{
        result.andExpect(status().isOk());
    }

    @Then("the update uploads API response status code should be 400")
    public void theupdateUploadResponseStatusCodeShouldBe400() throws Exception{
        result.andExpect(status().isBadRequest());
    }

    @Then("the update uploads API response status code should be 409")
    public void theupdateUploadResponseStatusCodeShouldBe409() throws Exception{
        result.andExpect(status().isConflict());
    }

    @Then("the update uploads API response content type should be JSON")
    public void theCreateUploadResponseContentTypeShouldBeJson() throws Exception{
        MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        result.andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Then( "the update uploads API response body should contain an error message saying " +
           "the Id provided in update upload request is invalid." )
    public void updateUploadIdIsInvalid() throws Exception{
        result.andExpect(content().string(containsString("Id provided in update upload request is invalid.")));
    }

    @Then( "the update uploads API response body should contain an error message saying " +
           "either status or summary must be provided in update upload request" )
    public void mandatorydataIsMissing() throws Exception{
        result.andExpect(content().string(containsString("Either status or summary must be provided in update upload request")));
    }

    @Then( "the update uploads API response body should contain an error message saying " +
           "the Update Upload Request lastModifiedTime is missing" )
    public void lastModifiedTimeIsMissing() throws Exception{
        result.andExpect(content().string(containsString("Update Upload Request lastModifiedTime is missing")));
    }

    @Then( "the update uploads API response body should contain an error message saying " +
           "Upload update request last modified time does not match with the upload entity in db. " +
           "Please pull the latest instance and make an update" )
    public void OldlastModifiedTimeProvided() throws Exception{
        result.andExpect(content().string(containsString("Upload update request last modified time does not match with the upload entity in db." +
                                                         " Please pull the latest instance and make an update")));
    }


    /////////////
    //
    // Helpers //
    //
    /////////////

    private String requestToJSON(UpdateUploadRequest uploadRequest) throws JsonProcessingException{
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
        uploadRequest.setId(10L);
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

    private Contract buildContractEntity(){
        Contract contract = new Contract();
        
        contract.setId(10L);
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("perpetuity");
        contract.setActive("true");
        
        return contract;
    }

    private UpdateUploadRequest buildUpdateUploadRequest(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,String> status = new HashMap<>();
        status.put("DICOM","99/100");
        status.put("NON-DICOM","1/1");

        return  new UpdateUploadRequest(10L,"v1","orgId217wtysgs",
                                        dataType,1L,"space123",summary,tags,
                                        status,"user1",
                                        new Timestamp( 1313045029),new Timestamp( 1313045029));

    }
}

