package com.gehc.ai.app.dc.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.dc.dao.IDataCatalogDao;
import com.gehc.ai.app.dc.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.service.IDataCatalogService;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration
@TestPropertySource ( "classpath:application.yml" )
public class DataCatalogServiceImplTest {

        @Configuration
    static class DataCatalogServiceImplTestTestContextConfig {

        @Bean
        public IDataCatalogService dataCatalogService() {
            return new DataCatalogServiceImpl();
        }
        
        @Bean
        public IDataCatalogDao dataCatalogDao() {
            return mock( DataCatalogDaoImpl.class );
        }
        
        @Bean
        public JdbcTemplate jdbcTemplate() {
            return mock( JdbcTemplate.class );
        }
    }
        
    @Autowired
    private IDataCatalogDao dataCatalogDao;
    
    @Autowired
    private IDataCatalogService dataCatalogService;
    
    private List<DataCollection> dataCollLst;

    /**
     * Set up.
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        if ( !Optional.ofNullable( dataCollLst ).isPresent() ) {
            try {
                dataCollLst = new ObjectMapper().readValue( getClass().getResourceAsStream( "/DataCollectionResponseJSON.json" ), new TypeReference<List<DataCollection>>() {
                } );
            } catch ( IOException e ) {
                throw e;
            }
        }
    }
    
   /* @Test
    public void testGetDataCollection() {
        try {
              when(dataCatalogDao.getDataCollection(any(), any(), null)).thenReturn(dataCollLst);
              assertEquals( 1, dataCatalogService.getDataCollection(null, null, null).size() );
         } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }*/
    
    @SuppressWarnings ( "unchecked" )
    @Test ( expected = Exception.class )
    public void testGetDataCollectionException() throws Exception {        
            when(dataCatalogDao.getDataCollection(any(), any(), null)).thenThrow( Exception.class );
            dataCatalogService.getDataCollection( null, null, null );        
    }
}
