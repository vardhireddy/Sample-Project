package com.gehc.ai.app.dc.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.dc.entity.DataCollection;

@RunWith ( MockitoJUnitRunner.class )
@ContextConfiguration
public class DataCatalogDaoImplTest {

    private static final String GET_DC_PREFIX = "SELECT json_extract(a.data, '$.id') as id ,json_extract(a.data, '$.name') as name, "
            + " json_extract(a.data, '$.type') as type, "
            + " json_extract(a.data, '$.description') as description, "
            + " json_extract(a.data, '$.createdDate') as createdDate, "
            + " json_extract(a.data, '$.creator.name') as creatorName,"
            + " json_extract(a.data, '$.creator.id') as creatorId, "
            + " JSON_LENGTH(json_extract(a.data, '$.imageSets')) as imageSetsSize, a.properties as properties FROM data_collection a "
            + " where a.id = ? and json_extract(a.data, '$.type') = ? ";
            
    
    @InjectMocks
    private DataCatalogDaoImpl dataCatalogDaoImpl;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Spy
    private ObjectMapper mapper;
    
    private List<DataCollection> dataCollLst;
    private List<DataCollection> dataCollection;
    private List<DataCollection> expDataColl;
    
    /**
     * Set up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        if ( !Optional.ofNullable( dataCollLst ).isPresent() ) {
            try {
                dataCollLst = new ObjectMapper().readValue( getClass().getResourceAsStream( "/AllDataCollectionResponseJSON.json" ), new TypeReference<List<DataCollection>>() {} );
                dataCollection = new ObjectMapper().readValue( getClass().getResourceAsStream( "/DataCollectionResponseJSON.json" ), new TypeReference<List<DataCollection>>() {} );
                expDataColl = new ObjectMapper().readValue( getClass().getResourceAsStream( "/ExperimentDataCollectionResponseJSON.json" ), new TypeReference<List<DataCollection>>() {} );
            } catch ( IOException e ) {
                throw e;
            }
        }     
    }

    @SuppressWarnings ( "unchecked" )
    //@Test
    public void testGetDataCollectionById() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( dataCollection );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( "1", null, null );
            Assert.assertEquals( 1, dataCollection.size() );
         } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @SuppressWarnings ( "unchecked" )
    //@Test
    public void testGetAllDataCollection() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( dataCollLst );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( null, null, null);
            Assert.assertFalse( null==dataCollection );
         } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @SuppressWarnings ( "unchecked" )
   // @Test
    public void testDataCollectionByIdNExperimentType() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.contains( GET_DC_PREFIX), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( expDataColl );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( "1", "Experiment", null );
            Assert.assertEquals( "Experiment", dataCollection.get(0).getType());                
           } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @SuppressWarnings ( "unchecked" )
    //@Test
    public void testDataCollectionExperimentType() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( dataCollLst );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( null, "Experiment", null );
            Assert.assertEquals( "Experiment", dataCollection.get(0).getType());                
           } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @SuppressWarnings ( "unchecked" )
    //@Test
    public void testDataCollectionByIdNAnnotationType() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( dataCollLst );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( "1", "Annotation", null );
            Assert.assertEquals( "Annotation", dataCollection.get(1).getType()); 
           } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @SuppressWarnings ( "unchecked" )
    //@Test
    public void testDataCollectionAnnotationType() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( dataCollLst );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( null, "Annotation", null );
            Assert.assertEquals( "Annotation", dataCollection.get(1).getType()); 
           } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @SuppressWarnings ( "unchecked" )
    //@Test
    public void testDataCollectionNullType() {
        try {
            Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenReturn( dataCollLst );
            List<DataCollection> dataCollection = dataCatalogDaoImpl.getDataCollection( "1", null, null );
            Assert.assertEquals( null, dataCollection.get(2).getType());   
           } catch ( Exception e ) {
            e.printStackTrace();
            fail( "Method should not throw exception" );
        }
    }
    
    @Test
    public void testConstructQueryWithEmptyParams() {
        String params = dataCatalogDaoImpl.constructQuery(null);
        assertTrue("Params should be empty when the passed params maps is null",params.equals(""));
    }

    private Map<String, String> constructQueryParam(String key, String values) {
        Map<String, String> params = new HashMap<>();
        params.put(key, values);
        return params;
    }

    @Test
    public void testConstructQueryWithSingleParam() {
        Map<String, String> input = constructQueryParam("modality", "CT");
        String result = dataCatalogDaoImpl.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

    @Test
    public void testConstructQueryWithSingleParamMultipleValue() {
        Map<String, String> input = constructQueryParam("modality", "CT,MR");
        String result = dataCatalogDaoImpl.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\", \"MR\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);
    }

    @Test
    public void testConstructQueryWithMultipleParamSingleValue() {
        Map<String, String> input = constructQueryParam("modality", "CT");
        input.putAll(constructQueryParam("anatomy", "LUNG"));
        String result = dataCatalogDaoImpl.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\") AND anatomy IN (\"LUNG\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }
    @Test
    public void testConstructQueryWithMultipleParamMultipleValue() {
        Map<String, String> input = constructQueryParam("modality", "CT,MR");
        input.putAll(constructQueryParam("anatomy", "LUNG,HEART"));
        String result = dataCatalogDaoImpl.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\", \"MR\") AND anatomy IN (\"LUNG\", \"HEART\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

    @SuppressWarnings ( "unchecked" )
    //@Test ( expected = Exception.class )
    public void testGetDataCollectionException() throws Exception {        
        Mockito.when( this.jdbcTemplate.query( Matchers.anyString(), (PreparedStatementSetter)Matchers.anyObject(), (RowMapper<DataCollection>)Matchers.anyObject() ) ).thenThrow( Exception.class );
        dataCatalogDaoImpl.getDataCollection( "1", null, null );      
    }
}