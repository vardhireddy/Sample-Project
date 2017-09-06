package com.gehc.ai.app.datacatalog.dao.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.gehc.ai.app.datacatalog.entity.GEClass;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by sowjanyanaidu on 9/5/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
public class DataCatalogDaoImplTest {
    private GEClass[] returnValue;
    @Mock
    EntityManager entityManager;
    @Mock
    Query query;

    @InjectMocks
    DataCatalogDaoImpl dataCatalogDao;

    @Test
    public void testGetImageSeriesIdLst()  {
        List returnList = dataCatalogDao.getImgSeriesIdLst(getImageSeries());
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        assertEquals(expectedList,returnList);
    }

    @Test
    public void testGEClasses()  {
        Map geClass = getParamsMap();
        returnValue = dataCatalogDao.getGEClasses(geClass);
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        GEClass[] expectedValue = new GEClass[0];
        assertEquals(expectedValue.getClass(),returnValue.getClass());
    }

    @Test
    public void testgeClassDataSummary(){

    }

    private Map getParamsMap() {
        Map geClass = new HashMap<String, Object>();
        geClass.put( "org-id","4fac7976-e58b-472a-960b-42d7e3689f20");
        geClass.put( "modality", "CT,DX");

        geClass.put("value", "");
        geClass.put("patient_outcome",40);

        geClass.put("anatomy" ,"Chest,Lung");

        geClass.put("ge-class", "[{\"name\": \"Aorta Abnormalities\"}, {\"name\": \"Pediatric\", \"value\": \"\", \"patient_outcome\": \"40\"}]");// {"name": "Pediatric", "value": "", "patient_outcome": "40"}]
        return geClass;
    }

    @Test
    public void testGetImageSeries()  {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List returnList = dataCatalogDao.getImgSeries(getParamsMap(),getImageSeries(), null);
        List expectedList = new ArrayList();
        assertEquals(expectedList,returnList);
    }


   public void testGetImageSeriesThrowsJsonException()  {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        Map geClass = new HashMap<String, Object>();
       when(dataCatalogDao.getImgSeries(getParamsMap(), getImageSeries(), null)).thenThrow(new JsonGenerationException("Error"));
    }


    private List<ImageSeries> getImageSeries() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");;
        imageSeries.setUploadBy("BDD");
        imageSeries.setPatientDbId(1L);
        ImageSeries imageSeries1 = new ImageSeries();
        imageSeries1.setId(2L);
        imageSeries1.setDescription("test");
        imageSeries1.setAnatomy("Lung");
        imageSeries1.setModality("CT");;
        imageSeries1.setUploadBy("BDD");
        imageSeries1.setPatientDbId(2L);
        Map prop = new HashMap<String,String>();
        prop.put("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        imgSerLst.add(imageSeries1);
        return imgSerLst;
    }

}
