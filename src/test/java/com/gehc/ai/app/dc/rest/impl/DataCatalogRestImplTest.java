package com.gehc.ai.app.dc.rest.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.common.responsegenerator.ResponseGenerator;
import com.gehc.ai.app.dc.entity.Annotation;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.repository.AnnotationRepository;
import com.gehc.ai.app.dc.repository.COSNotificationRepository;
import com.gehc.ai.app.dc.repository.PatientRepository;
import com.gehc.ai.app.dc.repository.StudyRepository;
import com.gehc.ai.app.dc.rest.IDataCatalogRest;
import com.gehc.ai.app.dc.service.IDataCatalogService;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration
@TestPropertySource ( "classpath:application.yml" )
public class DataCatalogRestImplTest {

    @Configuration
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
    }

    @Autowired
    private IDataCatalogService dataCatalogService;
    @Autowired
    private IDataCatalogRest dataCatalogRest;
    @Autowired
    private ResponseGenerator responseGenerator;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private AnnotationRepository annotationRepository;
    @Autowired
    private COSNotificationRepository cosNotificationRepository;
    @Autowired
    private RestTemplate restTemplate;
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

    /**
     * Set up.
     * 
     * @throws Exception the exception
     */
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
    }

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
        
    @Test
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
    }
    
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
    
    @Test
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
            when( dataCatalogService.getImgSet( any() ) ).thenReturn( imgSetLst );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input ).get( 0 ).getOrgId() );
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
            when( dataCatalogService.getImgSet( any() ) ).thenReturn( imgSetLst );
            when( annotationRepository.findByImageSetInAndTypeIn( any(), any())).thenReturn( annotationList );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input ).get( 0 ).getOrgId() );
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
            when( dataCatalogService.getImgSet( any() ) ).thenReturn( imgSetLst );
            when( annotationRepository.findByImageSet( any())).thenReturn( annotationList );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input ).get( 0 ).getOrgId() );
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
            when( dataCatalogService.getImgSet( any() ) ).thenReturn( imgSetLst );
            when( annotationRepository.findByImageSetInAndTypeIn( any(), any())).thenReturn( annotationList );
            when( annotationRepository.findByImageSet( any())).thenReturn( annotationList );
            assertEquals( "LIDC-1", dataCatalogRest.getImgSet( input ).get( 0 ).getOrgId() );
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }

}
