package com.gehc.ai.app.datacatalog.util.exportannotations;

import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.AnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ColumnHeader;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.LabelAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.DBResultToCsvBeanConverter;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

/**
 * {@code CsvConversionTemplatesTest} evaluates the behavior of {@link CsvConversionTemplates}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class CsvConversionTemplatesTest {

    @Test(expected = AssertionError.class)
    public void itShouldThrowAssertionErrorWhenAttemptingToInstantiate() throws Throwable {
        // ARRANGE
        Constructor<CsvConversionTemplates> constructor = CsvConversionTemplates.class.getDeclaredConstructor();
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

    @Test(expected = InvalidAnnotationException.class)
    public void itShouldThrowInvalidAnnotationExceptionWhenNotAssociatedWithDicomOrNonDicomImageSet() throws Exception {
        // ARRANGE
        Object[] mockResult = new Object[]{"INVALID_IMAGE_SET_FORMAT"};

        Map<String, Integer> mockResultIndexMap = new HashMap<>();
        mockResultIndexMap.put("imageSetFormat", 0);

        Map<String, Integer[]> mockResultIndicesMap = new HashMap<>();

        List<AnnotationCsv> mockCsvBeans = new ArrayList<>();
        mockCsvBeans.add(new LabelAnnotationCsv("series123", "label", "pneumothorax", null, null, null));

        DBResultToCsvBeanConverter mockConverter = (result, resultIndexMap, resultIndiciesMap) -> mockCsvBeans;

        Supplier<DBResultToCsvBeanConverter> mockSupplier = () -> mockConverter;

        // ACT
        CsvConversionTemplates.getColumnHeaders(mockResult, mockResultIndexMap, mockResultIndicesMap, mockSupplier);
    }

    @Test
    public void itShouldReturnEmptySetWhenNoBeansAreFound() throws Exception {
        // ARRANGE
        Object[] mockResult = new Object[]{"DCM"};

        Map<String, Integer> mockResultIndexMap = new HashMap<>();
        mockResultIndexMap.put("imageSetFormat", 0);

        Map<String, Integer[]> mockResultIndicesMap = new HashMap<>();

        DBResultToCsvBeanConverter mockConverter = (result, resultIndexMap, resultIndiciesMap) -> new ArrayList();

        Supplier<DBResultToCsvBeanConverter> mockSupplier = () -> mockConverter;

        // ACT
        Set<ColumnHeader> columnHeaders = CsvConversionTemplates.getColumnHeaders(mockResult, mockResultIndexMap, mockResultIndicesMap, mockSupplier);

        // ASSERT
        assertThat(columnHeaders.size(), is(equalTo(0)));
    }

    @Test
    public void itShouldReturnEmptyStringWhenNoBeansAreFound() throws Exception {
        // ARRANGE
        Object[] mockResult = new Object[]{"DCM"};

        Map<String, Integer> mockResultIndexMap = new HashMap<>();
        mockResultIndexMap.put("imageSetFormat", 0);

        Map<String, Integer[]> mockResultIndicesMap = new HashMap<>();

        List<AnnotationCsv> mockCsvBeans = new ArrayList<>();

        DBResultToCsvBeanConverter mockConverter = (result, resultIndexMap, resultIndiciesMap) -> mockCsvBeans;

        Supplier<DBResultToCsvBeanConverter> mockSupplier = () -> mockConverter;

        String[] mockColumnHeaders = new String[]{"seriesUID", "annotationType", "label"};

        // ACT
        String csv = CsvConversionTemplates.convertDBResultToCsv(mockResult, mockResultIndexMap, mockResultIndicesMap, mockColumnHeaders, mockSupplier, LabelAnnotationCsv.class);

        // ASSERT
        assertThat(csv, is(equalTo("")));
    }

}
