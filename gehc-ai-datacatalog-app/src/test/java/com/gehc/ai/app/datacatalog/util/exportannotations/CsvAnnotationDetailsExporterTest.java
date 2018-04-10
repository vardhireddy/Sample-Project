package com.gehc.ai.app.datacatalog.util.exportannotations;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

/**
 * {@code CsvAnnotationDetailsExporterTest} evaluates the behavior of {@link CsvAnnotationDetailsExporter}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class CsvAnnotationDetailsExporterTest {

    private static final Map<String, Integer> resultIndexMap = new HashMap<>();
    private static final Map<String, Integer[]> resultIndicesMap = new HashMap<>();

    @BeforeClass
    public static void setUp() {
        resultIndexMap.put("patientID", 0);
        resultIndexMap.put("seriesUID", 1);
        resultIndexMap.put("imageSetFormat", 2);
        resultIndexMap.put("annotationID", 3);
        resultIndexMap.put("annotationType", 4);
        resultIndexMap.put("roiName", 5);
        resultIndexMap.put("roiLocalID", 6);
        resultIndexMap.put("roiData", 7);
        resultIndexMap.put("coordSys", 19);
        resultIndexMap.put("instances", 22);
        resultIndexMap.put("maskOrigin", 23);
        resultIndexMap.put("maskURI", 24);
        resultIndexMap.put("maskFormat", 25);
        resultIndexMap.put("roiIndex", 26);
    }

    @Test(expected = AssertionError.class)
    public void itShouldThrowAssertionErrorWhenAttemptingToInstantiate() throws Throwable {
        // ARRANGE
        Constructor<CsvAnnotationDetailsExporter> constructor = CsvAnnotationDetailsExporter.class.getDeclaredConstructor();
        assumeThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);

        // ACT
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            // We expect to get an InvocationTargetException that wraps the expected AssertionError
            throw e.getTargetException();
        }
    }

    /**
     * <b>GIVEN</b> a DB result describes an annotation of an unsupported type<br>
     * <b>WHEN</b> {@link CsvAnnotationDetailsExporter#exportAsCsv(List, Map, Map)} is called with that DB  result <br>
     * <b>THEN</b> that DB result should be skipped and therefore not exported
     */
    @Test
    public void itShouldSkipUnsupportedAnnotationType() throws Exception {
        // ARRANGE
        String expectedCsv =  "seriesUID,annotationType,instances,coordSys,data,localID,name,index\n\"series123\",\"point\",\"[1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695, 1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199]\",\"IMAGE\",\"[-1.2345, 6.789, 10.1112]\",\"0\",\"ROI name\",\"0\"\n";

        List<Object[]> dbResults = new ArrayList<>();
        // Add a DB result that contains a supported annotation type
        Object[] pointAnnotation = new Object[]{"patient123", "series123", "DCM", 1, "point", "\"ROI name\"", "\"0\"", "[-1.2345,6.789,10.1112]", null, null, null, null, null, null, null, null, null, null, null, "\"IMAGE\"", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null, "\"0\""};
        dbResults.add(pointAnnotation);

        // Add a DB result that contains an unsupported annotation type
        Object[] unsupportedAnnotation = new Object[]{"patient123", "series123", "DCM", 1, "contour2d", "\"ROI name\"", "\"0\"", "[-1.2345,6.789,10.1112]", null, null, null, null, null, null, null, null, null, null, null, "\"IMAGE\"", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null, "\"0\""};
        dbResults.add(unsupportedAnnotation);

        // ACT
        String csv = CsvAnnotationDetailsExporter.exportAsCsv(dbResults, resultIndexMap, resultIndicesMap);

        // ASSERT
        assertThat(csv,is(equalTo(expectedCsv)));
    }

}
