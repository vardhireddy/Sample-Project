package com.gehc.ai.app.datacatalog.rest.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUser;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase.DataUsage;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.ErrorCodes;
import com.gehc.ai.app.datacatalog.rest.request.UpdateContractRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

import com.gehc.ai.app.datacatalog.rest.request.UpdateUploadRequest;
import org.junit.Before;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;


import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.IDataCatalogRest;
import com.gehc.ai.app.datacatalog.rest.response.AnnotatorImageSetCount;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;

import javax.servlet.http.HttpServletRequest;


@RunWith ( MockitoJUnitRunner.class )
@ContextConfiguration
@TestPropertySource ( "classpath:application.yml" )
public class DataCatalogRestImplTest {

  /*  @Configuration
    static class DataCatalogRestImplTestContextConfig {
        @Bean
        public IDataCatalogRest dataCatalogRest() {
            return new DataCatalogRestImpl();
        }

        @Bean
        public IDataCatalogService dataCatalogService() {
            return mock( IDataCatalogService.class );
        }

        @Bean
        public ResponseGenerator responseGenerator() {
            return mock( ResponseGenerator.class );
        }

        @Bean
        public PatientRepository patientRepository() {
            return mock( PatientRepository.class );
        }

        @Bean
        public StudyRepository studyRepository() {
            return mock( StudyRepository.class );
        }

        @Bean
        public AnnotationRepository annotationRepository() {
            return mock( AnnotationRepository.class );
        }

        @Bean
        public COSNotificationRepository cosNotificationRepository() {
            return mock( COSNotificationRepository.class );
        }
        @Bean
        public RestTemplate restTemplate() {
            return mock( RestTemplate.class );
        }
    }*/

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Mock
    private IDataCatalogService dataCatalogService;
    @Mock
    private IDataCatalogRest dataCatalogRest;
  /*
    @Autowired
    private ResponseGenerator responseGenerator;*/

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private StudyRepository studyRepository;
    @Mock
    private AnnotationRepository annotationRepository;
    @Mock private ContractRepository contractRepository;
    @Mock
    private COSNotificationRepository cosNotificationRepository;
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DataSetRepository dataSetRepository;

    @InjectMocks
    private DataCatalogRestImpl controller;

    @Before
    public void setUp() throws Exception {

        httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute( "orgId", "1");

    }
    /*
    @Value ( "${experiment.targetData.gtMaskLocation}" )
    private String gtMaskLocation;

    @Value ( "${experiment.targetData.imgLocation}" )
    private String imgLocation;

    @Value ( "${experiment.targetData.locationType}" )
    private String locationType;

    private List<DataCollection> dataCollLst;
    private String dataCollId;
    private List<Annotation> annotationList;
    private List<ImageSet> imgSetLst;

    *//**
     * Set up.
     *
     * @throws Exception the exception
     *//*
    @Before
    public void setUp() throws Exception {
        if ( !Optional.ofNullable( dataCollLst ).isPresent() ) {
            try {
                dataCollLst = new ObjectMapper().readValue( getClass().getResourceAsStream( "/DataCollectionResponseJSON.json" ), new TypeReference<List<DataCollection>>() {
                } );
                dataCollId = dataCollLst.get( 0 ).getId();
            } catch ( IOException e ) {
                throw e;
            }
        }
        if ( !Optional.ofNullable( annotationList ).isPresent() ) {
            try {
                annotationList = new ObjectMapper().readValue( getClass().getResourceAsStream( "/AnnotationResponseJSON.json" ), new TypeReference<List<Annotation>>() {
                } );
              } catch ( IOException e ) {
                throw e;
            }
        }
        if ( !Optional.ofNullable( imgSetLst ).isPresent() ) {
            try {
                imgSetLst = new ObjectMapper().readValue( getClass().getResourceAsStream( "/ImgSetByOrgIdResponseJSON.json" ), new TypeReference<List<ImageSet>>() {
                } );
            } catch ( IOException e ) {
                throw e;
            }
        }
    }*/

    //TODO: Commented all tests as we are going to move to JPA and will update the test accordingly
/*    @Test
    public void testGetDataCollection() {
        try {
            when( dataCatalogService.getDataCollection( any(), any() ) ).thenReturn( dataCollLst );
            assertEquals( dataCollId, dataCatalogRest.getDataCollection( null, null ).get( 0 ).getId() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }

    @Test
    public void testGetEmptyDataCollection() {
        try {
            when( dataCatalogService.getDataCollection( any(), any() ) ).thenReturn( new ArrayList<DataCollection>() );
            assertEquals(0 , dataCatalogRest.getDataCollection( null, null ).size() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }*/

   /* @Test
    public void testSaveAnnotationNoException() {
        Annotation annotation = new Annotation();
        try {
            dataCatalogRest.saveAnnotation( annotation );
            assert (true);
        } catch ( Exception ex ) {
            fail();
        }
    }

    @Test
    public void testSaveAnnotationStatusSuccess() {
        Annotation annotation = new Annotation();
        when( annotationRepository.save(annotation)).thenReturn( annotationList.get( 0 ) );
        assertEquals( "SUCCESS", dataCatalogRest.saveAnnotation( annotation ).getUploadStatus() );
    }*/

/*    @Test
    public void testGetAnnotationsByImgSetAndTypeNoException() {
        String imageSets = new String();
        String types = new String();
        try {
            dataCatalogRest.getAnnotationsByImgSetAndType( imageSets, types );
            assert (true);
        } catch ( Exception ex ) {
            fail();
        }
    }*/

   /* @Test
    public void testFindByImageSetNoException() {
        String imageSets = new String();
        try {
            dataCatalogRest.getAnnotationsByImgSet( imageSets );
            assert (true);
        } catch ( Exception ex ) {
            fail();
        }
    }

    private Map<String, String> constructQueryParam(String key, String values) {
        Map<String, String> params = new HashMap<>();
        params.put(key, values);
        return params;
    }

    @Test
    public void testgetImgSet() {
        Map<String, String> input = constructQueryParam("orgId", "LIDC-1");
        try {
            when( dataCatalogService.getImgSet( any(), null ) ).thenReturn( imgSetLst );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input, null ).get( 0 ).getOrgId() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }

    @Test
    public void testgetImgSetWithAnn() {
        Map<String, String> input = constructQueryParam("orgId", "LIDC-1");
        input.putAll(constructQueryParam("annotations", "mask"));
        try {
            when( dataCatalogService.getImgSet( any(), null ) ).thenReturn( imgSetLst );
            when( annotationRepository.findByImageSetInAndTypeIn( any(), any())).thenReturn( annotationList );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input, null ).get( 0 ).getOrgId() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }

    @Test
    public void testgetImgSetWithAnnAbsent() {
        Map<String, String> input = constructQueryParam("orgId", "LIDC-1");
        input.putAll(constructQueryParam("annotations", "absent"));
        try {
            when( dataCatalogService.getImgSet( any(), null ) ).thenReturn( imgSetLst );
            when( annotationRepository.findByImageSet( any())).thenReturn( annotationList );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input, null ).get( 0 ).getOrgId() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    @Test
    public void testgetImgSetWithAnnAbsentMask() {
        Map<String, String> input = constructQueryParam("orgId", "LIDC-1");
        input.putAll(constructQueryParam("annotations", "absent,mask"));
        try {
            when( dataCatalogService.getImgSet( any(), null ) ).thenReturn( imgSetLst );
            when( annotationRepository.findByImageSetInAndTypeIn( any(), any())).thenReturn( annotationList );
            when( annotationRepository.findByImageSet( any())).thenReturn( annotationList );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input, null ).get( 0 ).getOrgId() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }*/


    @Test
    public void testGetCountOfImagesSetPerAnnotatorByOrgId()
    {
        List<Object[]> mockdata = new ArrayList<>();
        Object[] record1 = {"annotator-1", 20};
        Object[] record2 = {"annotator-2", 25};

        mockdata.add(record1);
        mockdata.add(record2);

        when(annotationRepository.getCountOfImagesAnnotated(anyString())).thenReturn(mockdata);

        ResponseEntity result = controller.getCountOfImagesAnnotated("821u2e8u22iw9i2");
        assertEquals(200,result.getStatusCodeValue());
        List<AnnotatorImageSetCount> data = (List<AnnotatorImageSetCount>) result.getBody();
        assertEquals(20,data.get(0).getCountfImagesAnnotated());
    }

    @Test
    public void testGetCountOfImagesSetPerAnnotatorByOrgIdFor204()
    {
        List<Object[]> mockdata = new ArrayList<>();

        when(annotationRepository.getCountOfImagesAnnotated(anyString())).thenReturn(mockdata);

        ResponseEntity result = controller.getCountOfImagesAnnotated("821u2e8u22iw9i2");
        assertEquals(204,result.getStatusCodeValue());
    }


    @Test
    public void testGetCountOfImagesSetPerAnnotatorByOrgIdForException()
    {
        List<Object[]> mockdata = new ArrayList<>();
        Object[] record1 = {"annotator-1", 20};
        Object[] record2 = {"annotator-2", 25};

        mockdata.add(record1);
        mockdata.add(record2);

        when(annotationRepository.getCountOfImagesAnnotated(anyString())).thenThrow(new RuntimeException("internal error"));

        ResponseEntity result = controller.getCountOfImagesAnnotated("821u2e8u22iw9i2");
        assertEquals(500,result.getStatusCodeValue());
    }
    
    @Test
    public void testGetImgSeriesByDSId() {
    	List<DataSet> l = new ArrayList<DataSet>();
    	DataSet ds = new DataSet();
    	List<Long> imageSets = new ArrayList<Long>();
    	for (int k = 0; k < 10000; k++) {
    		imageSets.add((long) (Math.random() * 1000000));
    	}
    	l.add(ds);
    	ds.setImageSets(imageSets);
    	when(dataSetRepository.findById(anyLong())).thenReturn(l);
    	ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
    	final int limit = 1000;

    	controller.setMaxImageSeriesRows(limit);
    	controller.setRandomize(true);
     	controller.getImgSeriesByDSId(anyLong());
     	
 		org.mockito.Mockito.verify(dataCatalogService).getImgSeriesWithPatientByIds(argument.capture());
 		assertTrue(argument.getValue().size() == limit);
 		Set s = new HashSet<Long>();
 		for (int k = 0; k < argument.getValue().size(); k++) {
 			assertTrue(!s.contains((Long)argument.getValue().get(k)));
 		}
    }

    @Test
    public void testGetImgSeriesByDSIdWithoutRandomization() {
    	List<DataSet> l = new ArrayList<DataSet>();
    	DataSet ds = new DataSet();
    	List<Long> imageSets = new ArrayList<Long>();
    	for (int k = 0; k < 10000; k++) {
    		imageSets.add((long) (Math.random() * 1000000));
    	}
    	l.add(ds);
    	ds.setImageSets(imageSets);
    	when(dataSetRepository.findById(anyLong())).thenReturn(l);
    	ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
    	final int limit = 1000;

    	controller.setMaxImageSeriesRows(limit);
     	controller.getImgSeriesByDSId(anyLong());
        org.mockito.Mockito.verify(dataCatalogService).getImgSeriesWithPatientByIds(argument.capture());

        assertTrue(argument.getValue().size() == limit);
    }

    @Test
    public void testValidateContractIdAndOrgIdForValidData(){
        Contract contract = buildContractEntity();
        contract.setActive("true");
        when(contractRepository.findByIdAndOrgId(anyLong(),anyString())).thenReturn(contract);
        ResponseEntity<Map<String,String>> result = controller.validateContractByIdAndOrgId(1L,"orgId");
        assertEquals("Contract exists", result.getBody().get("response"));
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testValidateContractIdAndOrgIdForInvalidData(){
        Contract contract = buildContractEntity();
        contract.setActive("false");
        when(contractRepository.findByIdAndOrgId(anyLong(),anyString())).thenReturn(contract);
        ResponseEntity<Map<String,String>> result = controller.validateContractByIdAndOrgId(1L,"InvalidOrgId");
        assertEquals("Contract is inactive/expired", result.getBody().get("response"));
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    public void testValidateContractIdAndOrgIdForException(){
        when(contractRepository.findByIdAndOrgId(anyLong(),anyString())).thenThrow(new IllegalArgumentException());
        ResponseEntity<Map<String,String>> result = controller.validateContractByIdAndOrgId(1L,"InvalidOrgId");
        assertEquals("Internal Server error. Please contact the corresponding service assistant.", result.getBody());
        assertEquals(500, result.getStatusCodeValue());
    }

    //get contract unit test cases
    @Test
    public void testGetContractForValidActiveContractId(){
        Contract contract = buildContractEntity();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        ResponseEntity<Contract> result = controller.getContracts(1L);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("true",result.getBody().getActive());
    }

    @Test
    public void testGetContractForValidInActiveContractId(){
        Contract contract = buildContractEntity();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        ResponseEntity<Contract> result = controller.getContracts(1L);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response", "Contract associated with given Id is inactive"),result.getBody());
    }

    @Test
    public void testGetContractForInValidContractId(){
        Contract contract = new Contract();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        ResponseEntity<Contract> result = controller.getContracts(1L);
        assertEquals(400, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","No Contract Exists with the given Id."),result.getBody());
    }

    @Test
    public void testGetContractForExceptionRetriveingData(){
        when(dataCatalogService.getContract(anyLong())).thenThrow(new RuntimeException("internal error"));
        ResponseEntity<Contract> result = controller.getContracts(1L);
        assertEquals(500, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Exception retrieving the contract"),result.getBody());
    }

    @Test
    public void testGetContractForExceptionValidatingContractId(){
        ResponseEntity<Contract> result = controller.getContracts(null);
        assertEquals(400, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Please pass a valid contract ID"),result.getBody());
    }

    //update contract unit test cases
    @Test
    public void testUpdateContractWithAllData(){
        Contract contract = buildContractEntity();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        List<String> uriList = new ArrayList<>();
        uriList.add("blapu.pdf");
        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,uriList);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Contract.UploadStatus.UPLOAD_IN_PROGRESS, result.getBody().getUploadStatus());
    }

    @Test
    public void testUpdateContractWithStatus(){
        Contract contract = buildContractEntity();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,null);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Contract.UploadStatus.UPLOAD_IN_PROGRESS, result.getBody().getUploadStatus());
    }

    @Test
    public void testUpdateContractWithUri(){
        Contract contract = buildContractEntity();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        List<String> uriList = new ArrayList<>();
        uriList.add("blabla.pdf");
        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,uriList);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Contract.UploadStatus.UPLOAD_IN_PROGRESS, result.getBody().getUploadStatus());
    }

    @Test
    public void testUpdateContractWithNullRequest(){
        Contract contract = buildContractEntity();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        UpdateContractRequest updateRequest = new UpdateContractRequest();
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Update request cannot be empty. Either status or uri must be provided."), result.getBody());
    }


    @Test
    public void testUpdateContractWithInactiveContractID(){
        Contract contract = buildContractEntity();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,uriList);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Given contract ID does not exist or is inactive."), result.getBody());
    }

    @Test
    public void testUpdateContractWithInvalidContractID(){
        Contract contract = buildContractEntity();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenReturn(null);
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,uriList);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Given contract ID does not exist or is inactive."), result.getBody());
    }

    @Test
    public void testUpdateContractForExceptionInRetrievingFromDB(){
        Contract contract = buildContractEntity();
        contract.setActive("false");
        when(dataCatalogService.getContract(anyLong())).thenThrow(new RuntimeException(""));
        when(dataCatalogService.saveContract(any(Contract.class))).thenReturn(contract);

        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,uriList);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(500, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Exception retrieving the contract."), result.getBody());
    }

    @Test
    public void testUpdateContractForExceptionInSavingContractToDB(){
        Contract contract = buildContractEntity();
        when(dataCatalogService.getContract(anyLong())).thenReturn(contract);
        when(dataCatalogService.saveContract(any(Contract.class))).thenThrow(new RuntimeException(""));

        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        UpdateContractRequest updateRequest = new UpdateContractRequest(Contract.UploadStatus.UPLOAD_IN_PROGRESS,uriList);
        ResponseEntity<Contract> result = controller.updateContract(1L,updateRequest);

        assertEquals(500, result.getStatusCodeValue());
        assertEquals(Collections.singletonMap("response","Exception saving the updated contract."), result.getBody());
    }

    @Test
    public void testDeleteContractWhereStateIsActive()
    {
        //ARRANGE
        Contract contract = buildContractEntity();
        when(contractRepository.findOne(anyLong())).thenReturn(contract);
        httpServletRequest.setAttribute( "orgId", "12345678-abcd-42ca-a317-4d408b98c500");
        //ACT
        ResponseEntity<Map<String,String>> result = controller.deleteContract( 1L, httpServletRequest );
        //ASSERT
        assertEquals("Contract is inactivated successfully", result.getBody().get("response"));
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testDeleteContractWhereStateIsInactive()
    {
        //ARRANGE
        Contract contract = buildContractEntity();
        contract.setActive("false");

        when(contractRepository.findOne(anyLong())).thenReturn(contract);
        httpServletRequest.setAttribute( "orgId", "12345678-abcd-42ca-a317-4d408b98c500");
        //ACT
        ResponseEntity<Map<String,String>> result = controller.deleteContract( 1L, httpServletRequest );
        //ASSERT
        assertEquals("Contract with given id is already inactive", result.getBody().get("response"));
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testDeleteContractWhereContractDoesNotExist()
    {
        //ARRANGE
        when(contractRepository.findOne(anyLong())).thenReturn(null);
        //ACT
        ResponseEntity<Map<String,String>> result = controller.deleteContract( 1L, httpServletRequest );
        //ASSERT
        assertEquals("No contract exists with given id", result.getBody().get("response"));
        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testDeleteContractForExceptionInRetrievingContract()
    {
        //ARRANGE
        when(contractRepository.findOne(anyLong())).thenThrow(new IllegalArgumentException());
        //ACT
        ResponseEntity<Map<String,String>> result = controller.deleteContract( 1L, httpServletRequest );
        //ASSERT
        assertEquals("Error retrieving contract to delete. Please contact the corresponding service assistant.", result.getBody().get("response"));
        assertEquals(500, result.getStatusCodeValue());
    }

    @Test
    public void testDeleteContractForExceptionInUpdatingContract()
    {
        //ARRANGE
        Contract contract = buildContractEntity();
        when(contractRepository.findOne(anyLong())).thenReturn(contract);
        when(contractRepository.save(any(Contract.class))).thenThrow(new IllegalArgumentException());
        httpServletRequest.setAttribute( "orgId", "12345678-abcd-42ca-a317-4d408b98c500");
        //ACT
        ResponseEntity<Map<String,String>> result = controller.deleteContract( 1L, httpServletRequest );
        //ASSERT
        assertEquals("Error deleting the contract. Please contact the corresponding service assistant.", result.getBody().get("response"));
        assertEquals(500, result.getStatusCodeValue());
    }

    //test cases for getContractsForDataCollection
    @Test
    public void testGetContractsForDataCollection()
    {
        //ARRANGE
        Map<String, List<ContractByDataSetId>> data = new HashMap<>();
        List<ContractByDataSetId> contractByDataSetIdList = new ArrayList<>();
        ContractByDataSetId contractByDataSetId = buildContractByDataSetId();
        contractByDataSetIdList.add(contractByDataSetId);

        data.put("active",contractByDataSetIdList);
        data.put("inactive",contractByDataSetIdList);
        when(dataCatalogService.getContractsByDataCollectionId(anyLong())).thenReturn(data);

        //ACT
        ResponseEntity<?> result = controller.getContractsForDataCollection(1L);
        //ASSERT
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testGetContractsForDataCollectionForBadRequest()
    {
        //ARRANGE
        Map<String, List<ContractByDataSetId>> data = new HashMap<>();
        List<ContractByDataSetId> contractByDataSetIdList = new ArrayList<>();
        ContractByDataSetId contractByDataSetId = buildContractByDataSetId();
        contractByDataSetIdList.add(contractByDataSetId);

        data.put("active",new ArrayList<>());
        data.put("inactive",new ArrayList<>());
        when(dataCatalogService.getContractsByDataCollectionId(anyLong())).thenReturn(data);
        //ACT
        ResponseEntity<?> result = controller.getContractsForDataCollection(1L);
        //ASSERT
        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    public void testGetContractsByDataCollectionIdForInternalException()
    {//ARRANGE
        Map<String, List<ContractByDataSetId>> data = new HashMap<>();
        List<ContractByDataSetId> contractByDataSetIdList = new ArrayList<>();
        ContractByDataSetId contractByDataSetId = buildContractByDataSetId();
        contractByDataSetIdList.add(contractByDataSetId);

        data.put("active",new ArrayList<>());
        data.put("inactive",new ArrayList<>());
        when(dataCatalogService.getContractsByDataCollectionId(anyLong())).thenThrow(new RuntimeException());

        //ACT
        ResponseEntity<?> result = controller.getContractsForDataCollection(1L);
        //ASSERT
        assertEquals(500, result.getStatusCodeValue());
    }

    //test cases for create Upload
    @Test
    public void  createUploadSuccessfully() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.createUpload( upload ) ).thenReturn( upload );
        //ACT
        ResponseEntity response = controller.createUpload( upload );
        //ASSERT
        assertEquals( 201,  response.getStatusCodeValue());

    }

    @Test
    public void  createUploadFor400BadRequestData() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.createUpload( upload ) ).thenThrow( new DataCatalogException( "Missing one/more required fields data.", HttpStatus.BAD_REQUEST) );
        //ACT
        ResponseEntity response = controller.createUpload( upload );
        //ASSERT
        assertEquals( 400,  response.getStatusCodeValue());
        assertEquals( Collections.singletonMap("response","Missing one/more required fields data."),response.getBody() );

    }

    @Test
    public void  createUploadFor400OnInvalidOrExpiredContractId() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.createUpload( upload ) ).thenThrow( new DataCatalogException("Invalid/Expired contract ID provided.",HttpStatus.BAD_REQUEST) );
        //ACT
        ResponseEntity response = controller.createUpload( upload );
        //ASSERT
        assertEquals( 400,  response.getStatusCodeValue());
        assertEquals( Collections.singletonMap("response","Invalid/Expired contract ID provided."),response.getBody() );

    }

    @Test
    public void  createUploadFor400OnContractIdDoesNotExist() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.createUpload( upload ) ).thenThrow( new DataCatalogException("No contract exists with provided contract ID.",HttpStatus.BAD_REQUEST) );
        //ACT
        ResponseEntity response = controller.createUpload( upload );
        //ASSERT
        assertEquals( 400,  response.getStatusCodeValue());
        assertEquals( Collections.singletonMap("response","No contract exists with provided contract ID."),response.getBody() );

    }

    @Test
    public void  createUploadFor500Exception() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.createUpload( upload ) ).thenThrow( new RuntimeException("No contract exists with provided contract ID.") );
        //ACT
        ResponseEntity response = controller.createUpload( upload );
        //ASSERT
        assertEquals( 500,  response.getStatusCodeValue());
        assertEquals( Collections.singletonMap("response","Exception saving the upload entity. Please contact the corresponding service assistant."),response.getBody() );

    }

    @Test
    public void  createUploadFor409OnDuplicateDatEntry() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.createUpload( upload ) ).thenThrow( new DataCatalogException("An Upload entity already exists with given spaceId, orgId and contractId.",HttpStatus.CONFLICT) );
        //ACT
        ResponseEntity response = controller.createUpload( upload );
        //ASSERT
        assertEquals( 409,  response.getStatusCodeValue());
        assertEquals( Collections.singletonMap("response","An Upload entity already exists with given spaceId, orgId and contractId."),response.getBody() );

    }

    //test get all uploads
    @Test
    public void  getAllUploadsSuccessfully(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        List<Upload> uploadList = new ArrayList<>(  );
        uploadList.add( upload );
        uploadList.add( upload );
        when( dataCatalogService.getAllUploads( anyString() ) ).thenReturn( uploadList );
        //ACT
        ResponseEntity response = controller.getAllUploads( httpServletRequest );
        //ASSERT
        assertEquals( 200,  response.getStatusCodeValue());

    }

    @Test
    public void  getAllUploadsFor500Exception(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        List<Upload> uploadList = new ArrayList<>(  );
        uploadList.add( upload );
        uploadList.add( upload );
        when( dataCatalogService.getAllUploads( anyString() ) ).thenThrow( new RuntimeException("Exceptions")  );
        //ACT
        ResponseEntity response = controller.getAllUploads( httpServletRequest );
        //ASSERT
        assertEquals( 500,  response.getStatusCodeValue());

    }

    //test get upload by Id
    @Test
    public void  getAllUploadByIdSuccessfully(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadById( anyLong() ) ).thenReturn( upload );
        httpServletRequest.setAttribute( "orgId", "f1341a2c-7a54-4d68-9f40-a8b2d14d3806" );
        //ACT
        ResponseEntity response = controller.getUploadById( 1L , httpServletRequest);
        //ASSERT
        assertEquals( 200,  response.getStatusCodeValue());

    }

    @Test
    public void  getAllUploadByIdFor500Exception(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadById( anyLong() ) ).thenThrow( new RuntimeException("Exception") );
        httpServletRequest.setAttribute( "orgId", "f1341a2c-7a54-4d68-9f40-a8b2d14d3806" );
        //ACT
        ResponseEntity response = controller.getUploadById( 1L , httpServletRequest);
        //ASSERT
        assertEquals( 500,  response.getStatusCodeValue());

    }

    @Test
    public void  getAllUploadByIdFor404UploadNotFoundException(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadById( anyLong() ) ).thenReturn( new Upload() );
        httpServletRequest.setAttribute( "orgId", "f1341a2c-7a54-4d68-9f40-a8b2d14d3806" );
        //ACT
        ResponseEntity response = controller.getUploadById( 1L , httpServletRequest);
        //ASSERT
        assertEquals( 404,  response.getStatusCodeValue());

    }

    @Test
    public void  getAllUploadByIdFor403ForbiddenException(){
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadById( anyLong() ) ).thenReturn( upload );
        httpServletRequest.setAttribute( "orgId", "e687970ytklgtr6869o" );
        //ACT
        ResponseEntity response = controller.getUploadById( 1L , httpServletRequest);
        //ASSERT
        assertEquals( 403,  response.getStatusCodeValue());

    }

    // validate if upload exists API test cases
    @Test
    public void  getUploadByQueryParameters() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadByQueryParameters(anyString(), anyString(), anyLong() ) ).thenReturn( upload );
        Contract contract = buildContractEntity();
        when( dataCatalogService.getContract( anyLong() ) ).thenReturn( contract );
        //ACT
        ResponseEntity response = controller.getUploadByQueryParameters( "1" ,"1",1L );
        //ASSERT
        assertEquals( 200,  response.getStatusCodeValue());

    }

    @Test
    public void  getUploadByQueryParametersFor404NotFoundException() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadByQueryParameters(anyString(), anyString(), anyLong() ) ).thenReturn( null );
        Contract contract = buildContractEntity();
        when( dataCatalogService.getContract( anyLong() ) ).thenReturn( contract );
        //ACT
        ResponseEntity response = controller.getUploadByQueryParameters( "1" ,"1",1L );
        //ASSERT
        assertEquals( 200,  response.getStatusCodeValue());

    }

    @Test
    public void  getUploadByQueryParametersFor500Exception() throws Exception{
        //ARRANGE
        Upload upload = buildUploadEntity();
        when( dataCatalogService.getUploadByQueryParameters(anyString(), anyString(), anyLong() ) ).thenThrow( new RuntimeException( "" ) );
        Contract contract = buildContractEntity();
        when( dataCatalogService.getContract( anyLong() ) ).thenReturn( contract );
        //ACT
        ResponseEntity response = controller.getUploadByQueryParameters( "1" ,"1",1L );
        //ASSERT
        assertEquals( 500,  response.getStatusCodeValue());
    }

    //updateUpload method test cases
    @Test
    public void updateUploadSuccessfully() throws Exception{

        //ARRANGE
        UpdateUploadRequest updateUploadRequest = buildUpdateUploadRequest();
        Upload upload = buildUploadEntity();
        when( dataCatalogService.updateUploadEntity( any(UpdateUploadRequest.class) ) ).thenReturn( upload );
        //ACT
        ResponseEntity responseEntity = controller.updateUpload( updateUploadRequest );
        //ASSERT
        assertEquals( 200, responseEntity.getStatusCodeValue() );
    }

    @Test
    public void updateUploadForInvalidIdException() throws Exception{

        //ARRANGE
        UpdateUploadRequest updateUploadRequest = new UpdateUploadRequest(null,"v1","orgId217wtysgs",
                                                        null,1L,"space123",null,null,
                                                null,"user1",
                                                        new Timestamp( 1313045029),new Timestamp( 1313045029));

        when( dataCatalogService.updateUploadEntity( any(UpdateUploadRequest.class) ) ).thenThrow( new DataCatalogException( "",HttpStatus.BAD_REQUEST ) );
        //ACT
        ResponseEntity responseEntity = controller.updateUpload( updateUploadRequest );
        //ASSERT
        assertEquals( 400, responseEntity.getStatusCodeValue() );
    }

    @Test
    public void updateUploadFor500Exception() throws Exception{

        //ARRANGE
        UpdateUploadRequest updateUploadRequest = new UpdateUploadRequest(null,"v1","orgId217wtysgs",
                                                                          null,1L,"space123",null,null,
                                                                          null,"user1",
                                                                          new Timestamp( 1313045029),new Timestamp( 1313045029));

        when( dataCatalogService.updateUploadEntity( any(UpdateUploadRequest.class) ) )
                .thenThrow( new RuntimeException("" ) );
        //ACT
        ResponseEntity responseEntity = controller.updateUpload( updateUploadRequest );
        //ASSERT
        assertEquals( 500, responseEntity.getStatusCodeValue() );
    }

    /////////////////////
    //    HELPERS     //
    ////////////////////

    private Contract buildContractEntity(){
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setActive("true");
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);
        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        contract.setUri(uriList);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setSchemaVersion("v1");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("perpetuity");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(DataUser.GE_GLOBAL, DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setUploadBy("user");

        return contract;
    }

    private ContractByDataSetId buildContractByDataSetId(){
        return new ContractByDataSetId(2L,
                Contract.DeidStatus.HIPAA_COMPLIANT,
                "true",
                false,
                "user",
                Date.valueOf("2017-03-31"),
                "testAgreement",
                "joe@ge.com",
                "2018-06-08",
                "12",
                Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}),
                Contract.UploadStatus.UPLOAD_COMPLETED,
                Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}),
                Contract.DataLocationAllowed.GLOBAL);
    }

    private Upload buildUploadEntity(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        Upload uploadRequest = new Upload();
        uploadRequest.setId(3L);
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

    private UpdateUploadRequest buildUpdateUploadRequest(){
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

       return  new UpdateUploadRequest(3L,"v1","orgId217wtysgs",
                                    dataType,1L,"space123",summary,tags,
                                    status,"user1",
                                    new Timestamp( 1313045029),new Timestamp( 1313045029));

    }

}
