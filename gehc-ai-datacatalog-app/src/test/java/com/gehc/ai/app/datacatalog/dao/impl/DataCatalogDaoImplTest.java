package com.gehc.ai.app.datacatalog.dao.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gehc.ai.app.datacatalog.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

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

/*    @Test
    public void testGetImageSeriesIdLst() {
        List returnList = dataCatalogDao.getImgSeriesIdLst(getImageSeries());
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        assertEquals(expectedList, returnList);
    }*/

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
        Map result = dataCatalogDao.geClassDataSummary(getMapForGEClassDataSummary(), "123");
        assertEquals("{8082=CR, 121=DX}", result.toString());

    }

    public void dataSetUp() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        List expectedList = new ArrayList();
        Object[] newObj = new Object[]{BigInteger.valueOf(1), "4fac7976-e58b-472a-960b-42d7e3689f20", "DX", "CHEST", "PNG", "12345", "UCSF", 1, "GE XRAY", "test"};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
    }

    @Test
    public void testgetImageSeriesByFilters() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("ge_class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\"},{\"name\":\"Calcification\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesByFilters(input);
        assertEquals(getImageSeriesWithFilters().toString(), result.toString());
    }

    @Test
    public void testgetImageSeriesByFiltersWithAnnoationsAbsent() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "absent"));
        input.putAll(constructQueryParam("ge-class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\"},{\"name\":\"Calcification\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesByFilters(input);
        assertEquals(getImageSeriesWithFilters().toString(), result.toString());
    }

    @Test
    public void testgetImageSeriesByFiltersWithOutAnnoations() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        List result = dataCatalogDao.getImgSeriesByFilters(input);
        assertEquals(getImageSeriesWithFilters().toString(), result.toString());
    }


    @Test
    public void testgetDCByID() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        List expectedList = new ArrayList();
        Object[] newObj = new Object[]{"1", "SUID", 1, "test", "test", "{}", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\"},{\"name\":\"Calcification\",\"patient_outcome\":\"undefined.undefined\"}]","{}","{}","{}","{}"};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        List<Long> ids = new ArrayList<Long>();
        ids.add(0, 1L);
        ids.add(1, 2L);
        List result = dataCatalogDao.getAnnotationsByDSId(ids);
        assertEquals(getAnnotationDetails().size(), result.size());
        assertEquals(getAnnotationDetails().toArray()[0].getClass(), result.toArray()[0].getClass());
        assertEquals(getAnnotationDetails().toString(), result.toString());
    }
    //TODO: Need to review this test. It breaks when toString() method is added to Patient
//   @Test
//    public void testGetImageSeries() {
//        List expectedList = new ArrayList();
//        Object[] id = new Object[]{1L};
//        Patient patient = new Patient();
//        patient.setOrgId("123");
//        Object[] newObj = new Object[]{"1", "123", patient, "DX", "CHEST", 123, "test","test","test"};
//        Object[] annotations = new Object[]{
//                "label", 1L};
//
//        expectedList.add(newObj);
//        List typeList = new ArrayList();
//        typeList.add("label");
//        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
//        when(query.getResultList()).thenReturn(expectedList);
//        List returnList = dataCatalogDao.getImgSeriesByFilters(getParamsMap());
//        System.out.println("TTTTTT2" + expectedList + "MMMMM2" + returnList);
//        String expected = "";
//                //"id=1, schemaVersion=null, orgId=123, modality=DX, anatomy=CHEST, dataFormat=test, uri=null, seriesInstanceUid=null, description=null, institution=test, equipment=test, manufacturer=null, imageType=null, view=null, instanceCount=123, properties=null, uploadBy=null, uploadDate=null, patientDbId=null, studyDbId=null, patient=com.gehc.ai.app.datacatalog.entity.Patient";
//        assert (returnList.toString().contains(expected));
//        //assertEquals("{8082=CR, 121=DX}", "{8082=CR, 121=DX}");
//    }

    private List<ImageSeries> getImageSeriesWithFilters() {
        List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
        ImageSeries imgSeries = new ImageSeries();
        Patient p = new Patient();
        imgSeries.setId(1L);
        imgSeries.setOrgId("4fac7976-e58b-472a-960b-42d7e3689f20");
        imgSeries.setModality("DX");
        imgSeries.setAnatomy("CHEST");
        imgSeries.setDataFormat("PNG");
        imgSeries.setSeriesInstanceUid("12345");
        imgSeries.setInstitution("UCSF");
//        imgSeries.setManufacturer("test");
        imgSeries.setInstanceCount(1);
        imgSeries.setEquipment("GE XRAY");
//        imgSeries.setImageType("test");
//        imgSeries.setView("test");
        p.setPatientId("test");
//        p.setAge("12");
//        p.setGender("M");

        imgSeries.setPatient(p);
        imageSeriesList.add(imgSeries);
        return imageSeriesList;
    }

    private List<ImageSeries> getImageSeries() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setDataFormat("test");
        imageSeries.setInstitution("test");
        imageSeries.setManufacturer("test");
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


    private List<AnnotationDetails> getAnnotationDetails() {
        List<AnnotationDetails> annotationDetails = new ArrayList<AnnotationDetails>();
        AnnotationDetails annotation = new AnnotationDetails();
        annotation.setPatientId("1");
        annotation.setSeriesInstanceUid("SUID");
        annotation.setAnnotationId(1L);
        annotation.setObjectName("test");
        annotation.setType("test");
        annotation.setGeClass("[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\"},{\"name\":\"Calcification\",\"patient_outcome\":\"undefined.undefined\"}]");
        annotation.setData("{}");
        annotation.setGeClass1("{}");
        annotation.setGeClass2("{}");
        annotation.setGeClass3("{}");
        annotation.setGeClass4("{}");
        annotationDetails.add(annotation);
        return annotationDetails;
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
    

/*    @Test
    public void testConstructQueryWithEmptyParams() {
        String params = dataCatalogDao.constructQuery(null);
        assertEquals("Params should be empty when the passed params maps is null",params, "");
   } */

    private Map<String, Object> constructQueryParam(String key, Object values) {
        Map<String, Object> params = new HashMap<>();
        params.put(key, values);
        return params;
    }

    //  @Test
    public void testConstructQueryWithSingleParam() {
        Map<String, Object> input = constructQueryParam("modality", "CT");
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = "WHERE x.modality IN (\"CT\")";
        assertEquals("Param constructed in incorrect ", expectedResult, result);
    }

    //  @Test
    public void testConstructQueryWithSingleParamMultipleValue() {
        Map<String, Object> input = constructQueryParam("modality", "CT,MR");
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = "WHERE x.modality IN (\"CT\", \"MR\")";
        assertEquals("Param constructed in incorrect ", expectedResult, result);
    }

    //  @Test
    public void testConstructQueryWithMultipleParamSingleValue() {
        Map<String, Object> input = constructQueryParam("modality", "CT");
        input.putAll(constructQueryParam("anatomy", "LUNG"));
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = "WHERE x.modality IN (\"CT\") AND x.anatomy IN (\"LUNG\")";
        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

    // @Test
    public void testConstructQueryWithMultipleParamMultipleValue() {
        Map<String, Object> input = constructQueryParam("modality", "CT,MR");
        input.putAll(constructQueryParam("anatomy", "LUNG,HEART"));
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = "WHERE x.modality IN (\"CT\", \"MR\") AND x.anatomy IN (\"LUNG\", \"HEART\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }
}
