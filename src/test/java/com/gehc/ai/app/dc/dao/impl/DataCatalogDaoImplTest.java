package com.gehc.ai.app.dc.dao.impl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DataCatalogDaoImplTest {

    DataCatalogDaoImpl classUnderTest = new DataCatalogDaoImpl();

    @Test
    public void testConstructQueryWithEmptyParams() {
        String params = classUnderTest.constructQuery(null);
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
        String result = classUnderTest.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

    @Test
    public void testConstructQueryWithSingleParamMultipleValue() {
        Map<String, String> input = constructQueryParam("modality", "CT,MR");
        String result = classUnderTest.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\", \"MR\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);
    }

    @Test
    public void testConstructQueryWithMultipleParamSingleValue() {
        Map<String, String> input = constructQueryParam("modality", "CT");
        input.putAll(constructQueryParam("anatomy", "LUNG"));
        String result = classUnderTest.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\") AND anatomy IN (\"LUNG\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }
    @Test
    public void testConstructQueryWithMultipleParamMultipleValue() {
        Map<String, String> input = constructQueryParam("modality", "CT,MR");
        input.putAll(constructQueryParam("anatomy", "LUNG,HEART"));
        String result = classUnderTest.constructQuery(input);
        String expectedResult = "WHERE modality IN (\"CT\", \"MR\") AND anatomy IN (\"LUNG\", \"HEART\")";

        assertEquals("Param constructed in incorrect ", expectedResult, result);

    }

}