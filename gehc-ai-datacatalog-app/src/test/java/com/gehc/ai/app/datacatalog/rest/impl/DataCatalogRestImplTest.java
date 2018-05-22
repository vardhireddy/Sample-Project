package com.gehc.ai.app.datacatalog.rest.impl;

import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.IDataCatalogRest;
import com.gehc.ai.app.datacatalog.rest.response.AnnotatorImageSetCount;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/*import com.gehc.ai.app.common.responsegenerator.ResponseGenerator;
import Annotation;
import DataCollection;
import ImageSet;
import AnnotationRepository;
import COSNotificationRepository;
import PatientRepository;
import StudyRepository;
import IDataCatalogRest;
import IDataCatalogService;*/


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

    @InjectMocks
    private DataCatalogRestImpl controller;
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
        assertEquals( "SUCCESS", dataCatalogRest.saveAnnotation( annotation ).getStatus() );
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
    public void testValidateContractIdAndOrgIdForValidData(){
        when(contractRepository.validateContractIdAndOrgId(anyLong(),anyString())).thenReturn(1);
        ResponseEntity<Map<String,String>> result = controller.validateContractIdAndOrgId(1L,"orgId");
        assertEquals("Contract exists", result.getBody().get("response"));
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testValidateContractIdAndOrgIdForInvalidData(){
        when(contractRepository.validateContractIdAndOrgId(anyLong(),anyString())).thenReturn(0);
        ResponseEntity<Map<String,String>> result = controller.validateContractIdAndOrgId(1L,"InvalidOrgId");
        assertEquals("Contract does not exist", result.getBody().get("response"));
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testValidateContractIdAndOrgIdForException(){
        when(contractRepository.validateContractIdAndOrgId(anyLong(),anyString())).thenThrow(new IllegalArgumentException());
        ResponseEntity<Map<String,String>> result = controller.validateContractIdAndOrgId(1L,"InvalidOrgId");
        assertEquals("Internal Server error. Please contact the corresponding service assitant.", result.getBody());
        assertEquals(500, result.getStatusCodeValue());
    }

}