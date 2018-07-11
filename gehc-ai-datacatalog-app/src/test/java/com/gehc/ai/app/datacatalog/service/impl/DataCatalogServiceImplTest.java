package com.gehc.ai.app.datacatalog.service.impl;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import org.hibernate.DuplicateMappingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
public class DataCatalogServiceImplTest {

    @Mock
    private IDataCatalogDao dataCatalogDao;

    @InjectMocks
    DataCatalogServiceImpl service;

    //test cases for getContractsByDataSetID
    @Test
    public void itShouldReturnMapOfActiveAndInactiveContracts() {

        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);

        List<Contract> contractList = new ArrayList<>();
        Contract contract = buildContractEntity();
        contractList.add(contract);
        int expectedInactiveContracts = 1;

        Contract contract1 = buildContractEntity();
        contract1.setActive("false");
        contract1.setAgreementBeginDate("2017-06-08");
        contractList.add(contract1);
        int expectedActiveContracts = 1;

        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenReturn(contractList);

        //ACT
        Map<String, List<ContractByDataSetId>> result = service.getContractsByDataCollectionId(1L);

        //ASSERT
        assertEquals(expectedInactiveContracts, result.get("inactive").size());
        assertEquals(expectedActiveContracts, result.get("active").size());
    }

    @Test
    public void itShouldReturnAnEmptyHashMap() {
        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(null);

        int expectedInactiveContracts = 0;
        int expectedActiveContracts = 0;

        //ACT
        Map<String, List<ContractByDataSetId>> result = service.getContractsByDataCollectionId(1L);
        //ASSERT
        assertEquals(expectedInactiveContracts, result.get("inactive").size());
        assertEquals(expectedActiveContracts, result.get("active").size());
    }

    @Test
    public void itShouldAlsoReturnEmptyHashMap() {
        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);
        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenReturn(new ArrayList());
        int expectedInactiveContracts = 0;
        int expectedActiveContracts = 0;

        //ACT
        Map<String, List<ContractByDataSetId>> result = service.getContractsByDataCollectionId(1L);
        //ASSERT
        assertEquals(expectedInactiveContracts, result.get("inactive").size());
        assertEquals(expectedActiveContracts, result.get("active").size());
    }

    @Test(expected = RuntimeException.class)//ASSERT
    public void testGetContractsByDatasetIdForExceptionRetrievingImageSetList() {

        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenThrow(new RuntimeException());
        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenReturn(new ArrayList());

        //ACT
        service.getContractsByDataCollectionId(1L);
    }

    @Test(expected = RuntimeException.class)//ASSERT
    public void testGetContractsByDatasetIdForExceptionRetrievingContractList() {

        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);
        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenThrow(new RuntimeException());

        //ACT
        service.getContractsByDataCollectionId(1L);
    }

    //test cases for create Upload
    @Test
    public void  createUploadSuccessfully() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        Contract contract = buildContractEntity();
        when( dataCatalogDao.saveUpload( upload ) ).thenReturn( upload );
        when( dataCatalogDao.getContractDetails( anyLong() ) ).thenReturn( contract );
        //ACT
        Upload response = service.createUpload( upload );
        //ASSERT
        assertEquals( "f1341a2c-7a54-4d68-9f40-a8b2d14d3806",  response.getOrgId());

    }

    @Test(expected = DataCatalogException.class )
    public void  createUploadForInvalidRequest() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        upload.setSpaceId(""  );
        Contract contract = buildContractEntity();
        when( dataCatalogDao.saveUpload( upload ) ).thenReturn( upload );
        when( dataCatalogDao.getContractDetails( anyLong() ) ).thenReturn( contract );
        //ACT & ASSERT
         service.createUpload( upload );

    }

    @Test(expected = DataCatalogException.class )
    public void  createUploadForInvalidContractIdInRequest() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        Contract contract = buildContractEntity();
        when( dataCatalogDao.saveUpload( upload ) ).thenReturn( upload );
        when( dataCatalogDao.getContractDetails( anyLong() ) ).thenReturn( null );
        //ACT & ASSERT
        service.createUpload( upload );

    }

    @Test(expected = DataCatalogException.class )
    public void  createUploadForExpiredContractIdInRequest() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        Contract contract = buildContractEntity();
        when( dataCatalogDao.getUploadByQueryParameters( anyString(), anyString(), anyLong() ) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( upload ) ).thenReturn( upload );
        when( dataCatalogDao.getContractDetails( anyLong() ) ).thenReturn( contract );
        //ACT & ASSERT
        service.createUpload( upload );

    }

    @Test(expected = DataCatalogException.class )
    public void  createUploadForDuplicateDataInRequest() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        Contract contract = buildContractEntity();
        contract.setActive( "false" );
        when( dataCatalogDao.saveUpload( upload ) ).thenReturn( upload );
        when( dataCatalogDao.getContractDetails( anyLong() ) ).thenReturn( contract );
        //ACT & ASSERT
        service.createUpload( upload );

    }

    @Test(expected = RuntimeException.class )
    public void  createUploadForExceptionRetrievingContract() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        Contract contract = buildContractEntity();
        contract.setActive( "false" );
        when( dataCatalogDao.saveUpload( upload ) ).thenReturn( upload );
        when( dataCatalogDao.getContractDetails( anyLong() ) ).thenThrow( new RuntimeException( "" ) );
        //ACT & ASSERT
        service.createUpload( upload );

    }

    //test get upload by Id
    @Test
    public void  getAllUploadsSuccessfully(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        List<Upload> uploadList = new ArrayList<>(  );
        uploadList.add( upload );
        when( dataCatalogDao.getAllUploads( anyString() ) ).thenReturn( uploadList );
        //ACT
        List<Upload> result = service.getAllUploads( "orgId" );
        //ASSERT
        assertEquals( 1,  result.size());

    }

    @Test
    public void  getAllUploadForEmptyOrgId(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        List<Upload> uploadList = new ArrayList<>(  );
        uploadList.add( upload );
        when( dataCatalogDao.getAllUploads( anyString() ) ).thenReturn( uploadList );
        //ACT
        List<Upload> result = service.getAllUploads( "" );
        //ASSERT
        assertEquals( 0,  result.size());

    }


    //test get upload by Id
    @Test
    public void  getAllUploadByIdSuccessfully(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        upload.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        when( dataCatalogDao.getUploadById( anyLong() ) ).thenReturn( upload );
        //ACT
       Upload result = service.getUploadById( 1L );
        //ASSERT
        assertEquals( "f1341a2c-7a54-4d68-9f40-a8b2d14d3806",  result.getOrgId());

    }

    @Test
    public void  getAllUploadByIdForNUllID(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        upload.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        when( dataCatalogDao.getUploadById( anyLong() ) ).thenReturn( upload );
        //ACT
        Upload result = service.getUploadById( null );
        //ASSERT
        assertEquals( null,  result);

    }

    @Test
    public void  getAllUploadByIdWhenNoUploadExistsForGivenId(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        upload.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        when( dataCatalogDao.getUploadById( anyLong() ) ).thenReturn( null );
        //ACT
        Upload result = service.getUploadById( 1L );
        //ASSERT
        assertEquals( null,  result);

    }

    @Test(expected = DuplicateMappingException.class)
    public void  getAllUploadByIdWhenMultipleUploadsExistOnOneId(){
        //ARRANGE
        when( dataCatalogDao.getUploadById( anyLong() ) ).thenThrow( new DuplicateMappingException(DuplicateMappingException.Type.ENTITY,"") );
        //ACT && ASSERT
         service.getUploadById( 1L );

    }


    //updateUpload method test cases
    @Test
    public void updateUploadSuccessfully() throws Exception{

        //ARRANGE
        Upload updateUploadRequest = buildUpdateUploadRequest();
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        when( dataCatalogDao.saveUpload( any(Upload.class)) ).thenReturn( updateUploadRequest );
        //ACT
        Upload updateUpload = service.updateUploadEntity( updateUploadRequest );
        //ASSERT
        Assert.assertTrue(areUploadsEqual( updateUpload, updateUploadRequest) );
    }

    @Test(expected = DataCatalogException.class)
    public void updateUploadForInvalidIdException() throws Exception{

        //ARRANGE
        Upload updateUploadRequest =  new Upload(null,"v1","orgId217wtysgs",
                                                                                                                     null,1L,"space123",null,null,
                                                                                                                     null,"user1",
                                                                                                                     new Timestamp( 1313045029),new Timestamp( 1313045029));
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        //ACT //ASSERT
        service.updateUploadEntity( updateUploadRequest );

    }

    @Test(expected = DataCatalogException.class)
    public void updateUploadForUnSupportedIdException() throws Exception{

        //ARRANGE
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,Integer> status = new HashMap<>();
        status.put("failures",9);
        status.put("total",100);
        Upload updateUploadRequest =  new Upload(10L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",summary,null,
                                                                           status,"user1",
                                                                           new Timestamp( 1313045029),new Timestamp( 1313045029));
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( new Upload() );
        //ACT //ASSERT
        service.updateUploadEntity( updateUploadRequest );

    }

    @Test(expected = DataCatalogException.class)
    public void updateUploadForUnMatchedLastModifiedDatesException() throws Exception{

        //ARRANGE
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,Integer> status = new HashMap<>();
        status.put("failures",9);
        status.put("total",100);
        Upload updateUploadRequest =  new Upload(11L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",summary,null,
                                                                           status,"user1",
                                                                           new Timestamp( 1313045029),new Timestamp( 1313045030));
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload);
        //ACT //ASSERT
        service.updateUploadEntity( updateUploadRequest );

    }

    @Test(expected = DataCatalogException.class)
    public void updateUploadForLastModifiedDateInvalidInRequestException() throws Exception{

        //ARRANGE
        Upload updateUploadRequest =  new Upload(1L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",null,null,
                                                                           null,"user1",
                                                                           new Timestamp( 1313045029),null);
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        //ACT //ASSERT
        service.updateUploadEntity( updateUploadRequest );

    }

    @Test(expected = DataCatalogException.class)
    public void updateUploadForInvalidDataInRequestException() throws Exception{

        //ARRANGE
        Upload updateUploadRequest =  new Upload(1L,"v1","orgId217wtysgs",
                                                                           null,1L,"space123",null,null,
                                                                           null,"user1",
                                                                           new Timestamp( 1313045029),new Timestamp( 1313045029));
        Upload upload = buildUploadEntity();
        when( dataCatalogDao.getUploadById( anyLong()) ).thenReturn( upload );
        //ACT //ASSERT
        service.updateUploadEntity( updateUploadRequest );

    }

    ////////////////////
    //   Helpers      //
    ///////////////////

    public Contract buildContractEntity() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setSchemaVersion("v1");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("9999-06-08");
        contract.setDataUsagePeriod("12");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        contract.setActive("true");
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setUploadBy("user");
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);

        return contract;
    }

    private Upload buildUploadEntity(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        Upload uploadRequest = new Upload();
        uploadRequest.setId(4L);
        uploadRequest.setSchemaVersion("v1");
        uploadRequest.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        uploadRequest.setContractId(100L);
        uploadRequest.setSpaceId("space123");
        uploadRequest.setUploadBy("user");
        uploadRequest.setDataType(dataType);
        uploadRequest.setTags(tags);
        uploadRequest.setUploadDate(new Timestamp( 1313045029));
        uploadRequest.setLastModified(new Timestamp(1313045029));

        return uploadRequest;
    }

    private Upload buildUpdateUploadRequest(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,Integer> status = new HashMap<>();
        status.put("failures",9);
        status.put("total",100);

        return  new Upload(2L,"v1","orgId217wtysgs",
                                        dataType,1L,"space123",summary,tags,
                                        status,"user1",
                                        new Timestamp( 1313045029),new Timestamp( 1313045029));

    }

    private boolean areUploadsEqual(Upload entity, Upload request){

        if (!entity.getId().equals( request.getId() )
            || !entity.getContractId().equals( request.getContractId() )
            || !entity.getLastModified().equals( request.getLastModified() )
            || !entity.getUploadDate().equals( request.getUploadDate() )
            || !entity.getUploadBy().equals( request.getUploadBy() )
            || !entity.getTags().equals( request.getTags() )
            || !entity.getSpaceId().equals( request.getSpaceId() )
            || !entity.getOrgId().equals( request.getOrgId() )
            || !entity.getSchemaVersion().equals( request.getSchemaVersion() )
            || !entity.getDataType().equals( request.getDataType() ))
        {
            return false;
        }
        return true;

    }


}
