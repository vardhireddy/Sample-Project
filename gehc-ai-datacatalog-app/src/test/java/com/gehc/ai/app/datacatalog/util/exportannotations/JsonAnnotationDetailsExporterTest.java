package com.gehc.ai.app.datacatalog.util.exportannotations;

import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.PointRoiAnnotationJson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

/**
 * {@code JsonAnnotationDetailsExporterTest} evaluates the behavior of {@link JsonAnnotationDetailsExporter}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class JsonAnnotationDetailsExporterTest {

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

    /**
     * <b>GIVEN</b> that {@link JsonAnnotationDetailsExporter} is a utility class <br>
     * <b>WHEN</b> {@code JsonAnnotationDetailsExporter} is attempted to be constructed <br>
     * <b>THEN</b> an {@link AssertionError} should be thrown.
     *
     * @throws Throwable
     */
    @Test(expected = AssertionError.class)
    public void itShouldThrowAssertionErrorWhenAttemptingToInstantiate() throws Throwable {
        // ARRANGE
        Constructor<JsonAnnotationDetailsExporter> constructor = JsonAnnotationDetailsExporter.class.getDeclaredConstructor();
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
     * <b>WHEN</b> {@link JsonAnnotationDetailsExporter#exportAsJson(List, Map, Map)} is called with that DB  result <br>
     * <b>THEN</b> that DB result should be skipped and therefore not exported
     */
    @Test
    public void itShouldSkipUnsupportedAnnotationType() throws Exception {
        // ARRANGE
        List<AnnotationJson> expectedAnnotationBeans = new ArrayList<>();
        List<Double> pointCoords = DoubleStream.of(new double[]{-1.2345, 6.789, 10.1112}).boxed().collect(Collectors.toList());
        expectedAnnotationBeans.add(new PointRoiAnnotationJson("patient123", "series123", "DCM", 1L, "point", "IMAGE", pointCoords, "0", "ROI name",0));

        List<Object[]> dbResults = new ArrayList<>();
        // Add a DB result that contains a supported annotation type
        Object[] pointAnnotation = new Object[]{"patient123", "series123", "DCM", 1, "point", "\"ROI name\"", "\"0\"", "[-1.2345,6.789,10.1112]", null, null, null, null, null, null, null, null, null, null, null, "\"IMAGE\"", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null, 0};
        dbResults.add(pointAnnotation);

        // Add a DB result that contains an unsupported annotation type
        Object[] unsupportedAnnotation = new Object[]{"patient123", "series123", "DCM", 1, "contour2d", "\"ROI name\"", "\"0\"", "[-1.2345,6.789,10.1112]", null, null, null, null, null, null, null, null, null, null, null, "\"IMAGE\"", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null, 0};
        dbResults.add(unsupportedAnnotation);

        // ACT
        List<AnnotationJson> annotationBeans = JsonAnnotationDetailsExporter.exportAsJson(dbResults, resultIndexMap, resultIndicesMap);

        // ASSERT
        assertEquals(annotationBeans, expectedAnnotationBeans);
    }


}
