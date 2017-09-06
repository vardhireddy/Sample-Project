package com.gehc.ai.app.datacatalog.dao.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.GEClass;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sowjanyanaidu on 9/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DataCatalogDaoImplTest {
    private GEClass[] returnValue;
    @Mock
    EntityManager entityManager;
    @Mock
    Query query;
    @Mock
    ObjectMapper mapper;

    @InjectMocks
    DataCatalogDaoImpl dataCatalogDao;

    @Test
    public void testGetImageSeriesIdLst() {
        List returnList = dataCatalogDao.getImgSeriesIdLst(getImageSeries());
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        assertEquals(expectedList, returnList);
    }

    @Test
    public void testGEClasses() {
        Map geClass = getParamsMap();
        returnValue = dataCatalogDao.getGEClasses(geClass);
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        GEClass[] expectedValue = new GEClass[0];
        assertEquals(expectedValue.getClass(), returnValue.getClass());
    }

    @Test
    public void testgeClassDataSummary() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        when(query.getResultList()).thenReturn(getQueryList("modality"));
        Map result = dataCatalogDao.geClassDataSummary(getMapForGEClassDataSummary(), "123", "label");

        assertEquals("{8082=CR, 121=DX}", result.toString());

    }


    @Test
    public void testGetImageSeries() {
        List expectedList = new ArrayList();
        Object[] id = new Object[]{1L};
        Patient patient = new Patient();
        patient.setOrgId("123");
        Object[] newObj = new Object[]{"1", "123", patient, "DX", "CHEST", 123};
        Object[] annotations = new Object[]{
                "label", 1L};
        expectedList.add(newObj);
        List typeList = new ArrayList();
        typeList.add("label");
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedList);
        List returnList = dataCatalogDao.getImgSeries(getParamsMap(), getImageSeries(),typeList);
        System.out.println("TTTTTT" + expectedList + "MMMMM" + returnList);
        String expected = "id=1, schemaVersion=null, orgId=123, modality=DX, anatomy=CHEST, dataFormat=null, uri=null, seriesInstanceUid=null, description=null, institution=null, equipment=null, instanceCount=123, properties=null, uploadBy=null, uploadDate=null, patientDbId=null, studyDbId=null, patient=com.gehc.ai.app.datacatalog.entity.Patient";
        assert (returnList.toString().contains(expected));
    }

    private List<ImageSeries> getImageSeries() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setUploadBy("BDD");
        imageSeries.setPatientDbId(1L);
        ImageSeries imageSeries1 = new ImageSeries();
        imageSeries1.setId(2L);
        imageSeries1.setDescription("test");
        imageSeries1.setAnatomy("Lung");
        imageSeries1.setModality("CT");
        imageSeries1.setUploadBy("BDD");
        imageSeries1.setPatientDbId(2L);
        Map prop = new HashMap<String, String>();
        prop.put("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        imgSerLst.add(imageSeries1);
        return imgSerLst;
    }

    private Map getMapForGEClassDataSummary() {

        Map resultSetM = new HashMap();
        resultSetM.put("modality", "CR,DX");
        resultSetM.put("anatomy", "CHEST,LUNG");


        return resultSetM;
    }

    private Map getParamsMap() {
        Map geClass = new HashMap<String, Object>();
        geClass.put("org-id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        geClass.put("modality", "CT,DX");

        geClass.put("value", "");
        geClass.put("patient_outcome", 40);

        geClass.put("anatomy", "Chest,Lung");

        geClass.put("ge-class", "[{\"name\": \"Aorta Abnormalities\"}, {\"name\": \"Pediatric\", \"value\": \"\", \"patient_outcome\": \"40\"}]");// {"name": "Pediatric", "value": "", "patient_outcome": "40"}]
        return geClass;
    }

    private List<Object[]> getQueryList(String input) {
        List<Object[]> countM = new ArrayList<Object[]>();

        if (input.equals("modality")) {

            Object[] modality = new Object[]{
                    "DX", 121L};
            Object[] modality1 = new Object[]{
                    "CR", 8082L};
            countM.add(0, modality);
            countM.add(1, modality1);
        } else if (input.equals("anatomy")) {
            //anatomy
            Object[] anatomy = new Object[]{
                    "CHEST", 8203L};
            countM.add(0, anatomy);
        } else if (input.equals("annotations")) {
            Object[] annotations = new Object[]{
                    "test", 1L};
            countM.add(0, annotations);
        }
        return countM;
    }
}
