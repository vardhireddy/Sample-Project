package com.gehc.ai.app.datacatalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;

/**
 * Created by sowjanyanaidu on 9/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DataCatalogDaoImplTest {
    private GEClass[] returnValue;
    @Mock
    EntityManager entityManager;
    @Mock
    ContractRepository contractRepository;
    @Mock
    Query query;
    @Mock
    ObjectMapper mapper;

    @InjectMocks
    DataCatalogDaoImpl dataCatalogDao;

    public LocalDateTime getUploadDate() {
        String str = "2018-03-08 10:51:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localtDateAndTime = LocalDateTime.parse(str, formatter);

        return localtDateAndTime;
    }

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
    public void testGEClassesWithoutGEClass() {
        Map geClass = getParamsMap();
        geClass.remove("ge-class");
        returnValue = dataCatalogDao.getGEClasses(geClass);
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        GEClass[] expectedValue = new GEClass[0];
        assertEquals(expectedValue.getClass(), returnValue.getClass());
    }

    @Test
    public void testGEClassesThrowsException() {
        Map geClass = getParamsMap();
        geClass.put("ge-class", "[\"name\": \"Aorta Abnormalities\"}, {\"name\": \"Pediatric\", \"value\": \"\", \"patient_outcome\": \"40\"}]");// {"name": "Pediatric", "value": "", "patient_outcome": "40"}]
        returnValue = dataCatalogDao.getGEClasses(geClass);
        List expectedList = new ArrayList();
        expectedList.add(1L);
        expectedList.add(2L);
        GEClass[] expectedValue = new GEClass[0];
        assertEquals(expectedValue.getClass(), returnValue.getClass());
    }

    @Test
    public void testGEClassesThrowsException2() {
        Map geClass = getParamsMap();
        geClass.put("ge-class", "[{\"name\": }, {\"\": \"Pediatric\", \"value\": \"\", \"patient_outcome\": \"40\"}]");// {"name": "Pediatric", "value": "", "patient_outcome": "40"}]
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

    @Test
    public void testgeClassDataSummaryAnatomy() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        when(query.getResultList()).thenReturn(getQueryList("anatomy"));
        Map result = dataCatalogDao.geClassDataSummary(getMapForGEClassDataSummary(), "123");
        assertEquals("{8203=CHEST}", result.toString());

    }

    public void dataSetUp() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        List expectedList = new ArrayList();
        Object[] newObj = new Object[]{BigInteger.valueOf(1), "4fac7976-e58b-472a-960b-42d7e3689f20", "DX", "CHEST", "PNG", "12345", "UCSF", 1, "GE XRAY", "test", "{\"name\": \"PTX\"}", Timestamp.valueOf("2018-03-08 10:51:30"), "AP" };
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
    }

    @Test
    public void testgetImageSeriesByFilters() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("ge_class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\",\"upload_date\":\"2017-03-31 00:00:00\"},{\"name\":\"Calcification\",\"upload_date\":\"2017-03-31 00:00:00\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesByFilters(input, false, 1);
        assertEquals(getImageSeriesWithFilters().toString(), result.toString());
    }

    @Test(expected = Exception.class)
    public void testgetImageSeriesByFiltersThrowsException() {
        // dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("fromDate", "LABEL"));
        List result = dataCatalogDao.getImgSeriesByFilters(input,false,1);
    }

    @Test
    public void testgetImageSeriesByFiltersWithNullProperties() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        List expectedList = new ArrayList();
        Object[] newObj = new Object[]{BigInteger.valueOf(1), "4fac7976-e58b-472a-960b-42d7e3689f20", "DX", "CHEST", "PNG", "12345", "UCSF", 1, "GE XRAY", "test", "", Timestamp.valueOf("2018-03-08 10:51:30"), "AP" };
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);

        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("ge_class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\",\"upload_date\":\"2017-03-31 00:00:00\"},{\"name\":\"Calcification\",\"upload_date\":\"2017-03-31 00:00:00\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesByFilters(input,false,1);
        assertEquals(getImageSeriesWithFiltersAndNullProperties().toString(), result.toString());
    }

    @Test
    public void testgetImageSeriesByFiltersWithAnnoationsAbsent() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "absent"));
        input.putAll(constructQueryParam("ge-class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\"},{\"name\":\"Calcification\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesByFilters(input,false,1);
        assertEquals(getImageSeriesWithFilters().toString(), result.toString());
    }

    @Test
    public void testgetImageSeriesByFiltersWithOutAnnoations() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        List result = dataCatalogDao.getImgSeriesByFilters(input,false,1);
        assertEquals(getImageSeriesWithFilters().toString(), result.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSavePointAnnotation() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        resultList.add(1);
        Object[] newObj = new Object[]{1, "test", "[1105.8823529411766,616.3315508021391,2]", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        List result = dataCatalogDao.getAnnotationsIds(getForPointAnnotation());
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveLineAnnotation() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        resultList.add(1);
        Object[] newObj = new Object[]{1, "test", "[1105.8823529411766,616.3315508021391,2]", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation lineAnnotation = getForPointAnnotation();
        lineAnnotation.setType("line");
        List result = dataCatalogDao.getAnnotationsIds(lineAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveLineAnnotationthatDoesntMatch() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        Object[] newObj = new Object[]{1, "test", "[1105,616.3315508021391,2]", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation lineAnnotation = getForPointAnnotation();
        lineAnnotation.setType("line");
        List result = dataCatalogDao.getAnnotationsIds(lineAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }


    @Test
    public void testGetAnnotationIdsWhenTriedToSaveRectAnnotation() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        resultList.add(1);
        Object[] newObj = new Object[]{1, "test", "{\"xdir\":[511.7407407407409,0,0],\"ydir\":[511.7407407407409,0,0],\"origin\":[511.7407407407409,0,0]}", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation rectAnnotation = getForPointAnnotation();
        rectAnnotation.setType("rect");
        getRectangleEllipseItemInfo(rectAnnotation);
        List result = dataCatalogDao.getAnnotationsIds(rectAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveRectAnnotationCoordsDoesntMatch() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        Object[] newObj = new Object[]{1, "test", "{\"xdir\":[511.7407407407409,1,1],\"ydir\":[511.7407407407409,0,0],\"origin\":[511.7407407407409,0,0]}", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation rectAnnotation = getForPointAnnotation();
        rectAnnotation.setType("rect");
        getRectangleEllipseItemInfo(rectAnnotation);
        List result = dataCatalogDao.getAnnotationsIds(rectAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveEllipseAnnotation() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        resultList.add(1);
        Object[] newObj = new Object[]{1, "test", "{\"xdir\":[511.7407407407409,0,0],\"ydir\":[511.7407407407409,0,0],\"origin\":[511.7407407407409,0,0]}", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation ellipseAnnotation = getForPointAnnotation();
        ellipseAnnotation.setType("ellipse");
        getRectangleEllipseItemInfo(ellipseAnnotation);
        List result = dataCatalogDao.getAnnotationsIds(ellipseAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveLableAnnotation() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        resultList.add(1);
        Object[] newObj = new Object[]{1, null, null, "{\"name\": \"Pneumothorax\", \"value\": \"Small\"}", null, null, null, null, null, null, null, null, null, null, "IMAGE", null, "Medical Imaging - CONSULTATION Accession No: ACN Category/Procedure name: COMPUTED RADIOGRAPHY (RAD)/CHEST 2 VIEWS Portable chest AP upright and lateral Left chest tube is still present in the lower hemithorax. Moderate sized bilateral pleural effusions persist. There is partial atelectasis/consolidation of both lower lobes. There appears to be a tiny left pneumothorax. The upper lung zones are clear. **Signed 16/11/16 1332 Reported By: Osuszek Andrew MD FRCPC"};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation labelAnnotation = getForLabelAnnotation();
        List result = dataCatalogDao.getAnnotationsIds(labelAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveLableAnnotationGeCallsDoesntMatch() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        Object[] newObj = new Object[]{1, null, null, "{\"name\": \"Pneumothorax\", \"value\": \"large\"}", null, null, null, null, null, null, null, null, null, null, "IMAGE", null, "Medical Imaging - CONSULTATION Accession No: ACN Category/Procedure name: COMPUTED RADIOGRAPHY (RAD)/CHEST 2 VIEWS Portable chest AP upright and lateral Left chest tube is still present in the lower hemithorax. Moderate sized bilateral pleural effusions persist. There is partial atelectasis/consolidation of both lower lobes. There appears to be a tiny left pneumothorax. The upper lung zones are clear. **Signed 16/11/16 1332 Reported By: Osuszek Andrew MD FRCPC"};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation labelAnnotation = getForLabelAnnotation();
        List result = dataCatalogDao.getAnnotationsIds(labelAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }

    @Test
    public void testGetAnnotationIdsWhenTriedToSaveLableAnnotationFindingsDoesntMatch() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        Object[] newObj = new Object[]{1, null, null, "{\"name\": \"Pneumothorax\", \"value\": \"Small\"}", null, null, null, null, null, null, null, null, null, null, "IMAGE", null, "TEST"};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        Annotation labelAnnotation = getForLabelAnnotation();
        List result = dataCatalogDao.getAnnotationsIds(labelAnnotation);
        assertEquals(result.toString(), resultList.toString());
    }


    private Annotation getRectangleEllipseItemInfo(Annotation rectAnnotation) {
        LinkedHashMap item = new LinkedHashMap();
        Map<String, List<Long>> dataMap = new HashMap<String, List<Long>>();
        ArrayList list1 = new ArrayList();
        list1.add(511.7407407407409);
        list1.add(0);
        list1.add(0);
        dataMap.put("xdir", list1);
        dataMap.put("ydir", list1);
        dataMap.put("origin", list1);

        item.put("data", dataMap);
        item.put("properties", "{\"ge_class\":[]}");
        item.put("coord_sys", "IMAGE");
        item.put("object_name", "test");
        rectAnnotation.setItem(item);
        return rectAnnotation;
    }

    private Annotation getForPointAnnotation() {
        Annotation annotation = new Annotation();
        annotation.setOrgId("123");
        annotation.setAnnotationTool("");
        annotation.setAnnotatorId("123");
        Map<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        Map<String, List<String>> linkedHashMap1 = new LinkedHashMap<String, List<String>>();
        linkedHashMap.put("data", "[1105.8823529411766,616.3315508021391,2]");
        linkedHashMap.put("coord_sys", "IMAGE");
        ArrayList<String> list = new ArrayList<String>();
        linkedHashMap.put("properties", "{\"ge_class\": []}");
        linkedHashMap.put("object_name", "test");

        annotation.setItem(linkedHashMap);
        annotation.setImageSetId(1L);
        annotation.setType("point");

        return annotation;
    }

    private Annotation getForLabelAnnotation() {
        Annotation annotation = new Annotation();
        annotation.setOrgId("123");
        annotation.setAnnotationTool("");
        annotation.setAnnotatorId("123");

        LinkedHashMap geClassJson = new LinkedHashMap();

        ArrayList<Map> list = new ArrayList<Map>();
        Map map1 = new HashMap();

        map1.put("name", "Pneumothorax");
        map1.put("value", "Small");
        list.add(map1);
        Map map2 = new HashMap();
        map2.put("ge_class", list);
        map2.put("findings", "Medical Imaging - CONSULTATION Accession No: ACN Category/Procedure name: COMPUTED RADIOGRAPHY (RAD)/CHEST 2 VIEWS Portable chest AP upright and lateral Left chest tube is still present in the lower hemithorax. Moderate sized bilateral pleural effusions persist. There is partial atelectasis/consolidation of both lower lobes. There appears to be a tiny left pneumothorax. The upper lung zones are clear. **Signed 16/11/16 1332 Reported By: Osuszek Andrew MD FRCPC");
        geClassJson.put("properties", map2);

        annotation.setItem(geClassJson);
        annotation.setImageSetId(1L);
        annotation.setType("label");

        return annotation;
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
        LinkedHashMap prop = new LinkedHashMap();
        prop.put("name", "PTX");
        imgSeries.setProperties(prop);
//        imgSeries.setImageType("test");
//        imgSeries.setView("test");
        p.setPatientId("test");
//        p.setAge("12");
//        p.setGender("M");
        imgSeries.setUploadDate(getUploadDate());
        imgSeries.setView("AP");
        imgSeries.setPatient(p);
        imageSeriesList.add(imgSeries);
        return imageSeriesList;
    }

    private List<ImageSeries> getImageSeriesWithFiltersAndNullProperties() {
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
        LinkedHashMap prop = new LinkedHashMap();
        imgSeries.setProperties(null);
//        imgSeries.setImageType("test");
        imgSeries.setView("AP");
        p.setPatientId("test");
//        p.setAge("12");
//        p.setGender("M");
        imgSeries.setUploadDate(getUploadDate());
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

    @Test(expected = Exception.class)
    public void testConstructQueryThrowsException() {
        dataCatalogDao.constructQuery(null);
    }

    @Test
    public void testConstructQueryWithSingleParam() {
        Map<String, Object> input = constructQueryParam("modality", "CT");
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = " WHERE x.modality IN (\"CT\")";
        assertEquals("Param constructed in incorrect ", expectedResult, result);
    }

    @Test
    public void testConstructQueryWithSingleParamMultipleValue() {
        Map<String, Object> input = constructQueryParam("modality", "CT,MR");
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = " WHERE x.modality IN (\"CT\", \"MR\")";
        assertEquals("Param constructed in incorrect ", expectedResult, result);
    }

    @Test
    public void testConstructQueryWithMultipleParamSingleValue() {
        Map<String, Object> input = constructQueryParam("modality", "CT");
        input.putAll(constructQueryParam("anatomy", "LUNG"));
        input.put("dateFrom", "2017-12-14 19:00:00");
        input.put("dateTo", "2017-12-14 20:00:00");
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = " WHERE x.modality IN (\"CT\") AND x.anatomy IN (\"LUNG\") and x.upload_date between \"2017-12-14 19:00:00\" and \"2017-12-14 20:00:00\"";
        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

    @Test
    public void testConstructQueryWithMultipleParamMultipleValue() {
        Map<String, Object> input = constructQueryParam("modality", "CT,MR");
        input.putAll(constructQueryParam("anatomy", "LUNG,HEART"));
        String result = dataCatalogDao.constructQuery(input);
        String expectedResult = " WHERE x.modality IN (\"CT\", \"MR\") AND x.anatomy IN (\"LUNG\", \"HEART\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

    @Test
    public void testGetImgSeriesWithPatientByIds() throws DataCatalogException {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        List expectedList = new ArrayList();
        List resultList = new ArrayList();
        Object[] newObj = new Object[]{BigInteger.valueOf(1), "4fac7976-e58b-472a-960b-42d7e3689f20", "DX", "CHEST", "DCM", null, null, 1, null, null, null, null, null, "{\"name\": \"PTX\"}", Timestamp.valueOf("2018-03-08 10:51:30")};
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);
        List<Long> ids = new ArrayList<Long>();
        ids.add(0, 1L);
        List result = dataCatalogDao.getImgSeriesWithPatientByIds(ids);
        assertEquals(result.toString(), getImageSeriesWithPatient().toString());
    }

    @Test
    public void testingestContractDetails(){
    	Contract contract = new Contract();
    	contract.setOrgId("test_123");
    	contract.setId(1L);
    	when(contractRepository.save(contract)).thenReturn(contract);
    	Long contractId = dataCatalogDao.ingestContractDetails(contract);
    	assertEquals(contractId, contract.getId());
    }

    @Test
    public void testgetContractDetails(){
    	Contract contract = new Contract();
    	when(contractRepository.findOne(contract.getId())).thenReturn(contract);
    	Contract receivedContract = dataCatalogDao.getContractDetails(contract.getId());
    	assertEquals(contract, receivedContract);
    }

    private List<ImageSeries> getImageSeriesWithPatient() {
        List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
        ImageSeries imgSeries = new ImageSeries();
        Patient p = new Patient();
        imgSeries.setId(1L);
        imgSeries.setOrgId("4fac7976-e58b-472a-960b-42d7e3689f20");
        imgSeries.setModality("DX");
        imgSeries.setAnatomy("CHEST");
        imgSeries.setDataFormat("DCM");
        imgSeries.setSeriesInstanceUid(null);
        imgSeries.setInstanceCount(1);
        imgSeries.setEquipment(null);
        p.setPatientId(null);
        p.setAge(null);
        p.setGender(null);
        imgSeries.setPatient(p);
        imgSeries.setUri(null);
        LinkedHashMap prop = new LinkedHashMap();
        prop.put("name", "PTX");
        imgSeries.setProperties(prop);
        imgSeries.setUploadDate(getUploadDate());
        imageSeriesList.add(imgSeries);
        return imageSeriesList;
    }
    
    @Test
    public void testgetImageSeriesIdsByFilters() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("ge_class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\",\"upload_date\":\"2017-03-31 00:00:00\"},{\"name\":\"Calcification\",\"upload_date\":\"2017-03-31 00:00:00\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesIdsByFilters(input);
        assertEquals(getImageSeriesIdsWithFilters().toString(), result.toString());
    }

    @Test(expected = Exception.class)
    public void testgetImageSeriesIdsByFiltersThrowsException() {
        // dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("fromDate", "LABEL"));
        List result = dataCatalogDao.getImgSeriesIdsByFilters(input);
    }

    @Test
    public void testgetImageSeriesIdsByFiltersWithNullProperties() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        List expectedList = new ArrayList();
        Object[] newObj = new Object[]{BigInteger.valueOf(1), "4fac7976-e58b-472a-960b-42d7e3689f20", "DX", "CHEST", "PNG", "12345", "UCSF", 1, "GE XRAY", "test", "", Timestamp.valueOf("2018-03-08 10:51:30"), "AP" };
        expectedList.add(newObj);
        when(query.getResultList()).thenReturn(expectedList);

        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "LABEL"));
        input.putAll(constructQueryParam("ge_class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\",\"upload_date\":\"2017-03-31 00:00:00\"},{\"name\":\"Calcification\",\"upload_date\":\"2017-03-31 00:00:00\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesIdsByFilters(input);
        assertEquals(getImageSeriesIdsWithFilters().toString(), result.toString());
    }

    @Test
    public void testgetImageSeriesIdsByFiltersWithAnnoationsAbsent() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        input.putAll(constructQueryParam("annotations", "absent"));
        input.putAll(constructQueryParam("ge-class", "[{\"name\":\"Foreign Bodies\",\"value\":\"Absent\",\"patient_outcome\":\"5.1\"},{\"name\":\"Calcification\",\"patient_outcome\":\"undefined.undefined\"}]"));
        List result = dataCatalogDao.getImgSeriesIdsByFilters(input);
        assertEquals(getImageSeriesIdsWithFilters().toString(), result.toString());
    }

    @Test
    public void testgetImageSeriesIdsByFiltersWithOutAnnoations() {
        dataSetUp();
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        List result = dataCatalogDao.getImgSeriesIdsByFilters(input);
        assertEquals(getImageSeriesIdsWithFilters().toString(), result.toString());
    }
    
    private List<Long> getImageSeriesIdsWithFilters() {
        List<Long> imageSeriesIdsList = new ArrayList<Long>();
        imageSeriesIdsList.add(1L);
        return imageSeriesIdsList;
    }
    
    /**
     * this tests calls from the image set series retrieval using filter criteria
     */
    @Test
    public void testGetImageSeriesByFilterWithLimits() {
        Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        dataCatalogDao.getImgSeriesByFilters(input, false, 101);

        org.mockito.Mockito.verify(entityManager).createNativeQuery(argument.capture());
        assertTrue(argument.getValue().toLowerCase().endsWith("limit 101"));
    }
    
    @Test
    public void testGetImageSeriesByFilterWithLimitsAndRandmoization() {
    	Map<String, Object> input = constructQueryParam("org_id", "4fac7976-e58b-472a-960b-42d7e3689f20");
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        dataCatalogDao.getImgSeriesByFilters(input, true, 101);

        org.mockito.Mockito.verify(entityManager).createNativeQuery(argument.capture());
        assertTrue(argument.getValue().toLowerCase().endsWith("limit 101"));
        assertTrue(argument.getValue().toUpperCase().contains("ORDER BY RAND()"));
    }
}
