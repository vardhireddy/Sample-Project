package com.gehc.ai.app.dc.rest.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.common.responsegenerator.ResponseGenerator;
import com.gehc.ai.app.dc.entity.DataCollection;
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

    @Value ( "${experiment.targetData.gtMaskLocation}" )
    private String gtMaskLocation;

    @Value ( "${experiment.targetData.imgLocation}" )
    private String imgLocation;

    @Value ( "${experiment.targetData.locationType}" )
    private String locationType;

    private List<DataCollection> dataCollLst;
    private String dataCollId;

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
    }

    @Test
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
    }
}
