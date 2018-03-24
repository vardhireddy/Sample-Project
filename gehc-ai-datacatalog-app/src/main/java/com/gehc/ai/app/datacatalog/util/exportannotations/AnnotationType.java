/*
 *  AnnotationType.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.util.exportannotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.AnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ColumnHeader;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ImageSetAssociation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.LabelAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.MultiPointRoiAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.DBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.FreeformRoiDBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.LabelDBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.FreeformRoiDBResultToJsonBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.LabelDBResultToJsonBeanConverter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@code AnnotationType} enumerates the types of annotations that can be produced and consumed by the Learning Factory.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public enum AnnotationType {
    LABEL() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new LabelDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, LabelDBResultToCsvBeanConverter::new, LabelAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(result, resultIndexMap, resultIndicesMap, LabelDBResultToCsvBeanConverter::new);
        }
    },
    POLYGON() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new FreeformRoiDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, FreeformRoiDBResultToCsvBeanConverter::new, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(result, resultIndexMap, resultIndicesMap, FreeformRoiDBResultToCsvBeanConverter::new);
        }
    };

    /**
     * Converts the provided DB result record, which describes an annotation, into a bean representation that can be written as JSON.
     *
     * @param result           The DB result record, which describes an annotation, to convert
     * @param resultIndexMap   A mapping of a result meta data to its index in the result record.
     * @param resultIndicesMap A mapping of a result meta data to its indices in the result record.
     * @return an {@link AnnotationJson}
     * @throws InvalidAnnotationException if the provided result record does not represent a well-formed annotation
     */
    public abstract AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException;

    /**
     * Converts the provided DB result record, which describes an annotation, into a CSV representation that is ingestable by the Learning Factory.
     *
     * @param result           The DB result record, which describes an annotation, to convert to CSV
     * @param columnHeaders    The column headers that will be used to organize the records of the CSV output
     * @param resultIndexMap   A mapping of a result meta data to its index in the result record.
     * @param resultIndicesMap A mapping of a result meta data to its indices in the result record.
     * @return The converted CSV {@code String}
     * @throws InvalidAnnotationException if the provided result record does not represent a well-formed annotation
     * @throws CsvConversionException     if the provided result record could not be successfully mapped to a corresponding CSV representation
     */
    public abstract String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException;

    /**
     * Returns the set of required columns and optional columns for this {@code AnnotationType} which have at least one non-null value.
     *
     * @param result           The DB result record, which describes an annotation, to convert to CSV
     * @param resultIndexMap   A mapping of a result meta data to its index in the result record.
     * @param resultIndicesMap A mapping of a result meta data to its indices in the result record.
     * @return a {@code Set}
     * @throws InvalidAnnotationException If the provided {@code JsonNode} does not represent a well-formed annotation
     */
    public abstract Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException;

    ///////////////////////////////////////////////
    //
    // Helpers for converting annotations to CSV //
    //
    ///////////////////////////////////////////////

    /**
     * Converts the provided {@link JsonNode} representation of a annotation annotation to a CSV representation that is ingestable by the Learning Factory.
     *
     * @param result                The DB result record which describes an annotation
     * @param columnHeaders         The array of required column headers and optional column headers with aqt least one non-null value
     * @param beanConverterSupplier The supplier of the service which converts a {@code JsonNode} to a {@code List} of CSV beans
     * @param beanClass             The bean {@link Class} to which the {@code JsonNode}'s contents will be converted
     * @return A CSV {@code String}
     * @throws InvalidAnnotationException if the provided result record does not represent a well-formed annotation
     * @throws CsvConversionException     if the provided result record could not be successfully mapped to a corresponding CSV representation
     */
    private final static String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders, Supplier<DBResultToCsvBeanConverter> beanConverterSupplier, Class beanClass) throws InvalidAnnotationException, CsvConversionException {

        // Create a single CSV bean
        List<AnnotationCsv> annotationCsvBeans = beanConverterSupplier.get().getAnnotationBeans(result, resultIndexMap, resultIndicesMap);

        // Initialize the CSV writer with the appropriate CSV bean and column headers
        ColumnPositionMappingStrategy<AnnotationCsv> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(beanClass);
        strategy.setColumnMapping(columnHeaders);

        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<AnnotationCsv> beanWriter = new StatefulBeanToCsvBuilder(writer).withMappingStrategy(strategy).build();

        // Finally, write the beans to a CSV string
        try {
            beanWriter.write(annotationCsvBeans);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new CsvConversionException(e.getMessage());
        }

        return writer.toString();
    }

    /**
     * Returns the aggregation of required column headers and optional column headers that have values.
     *
     * @param result                The DB result record, which describes an annotation, that will be evaluated to see which optional columns have at least one non-null value
     * @param beanConverterSupplier The supplier of the service which converts a {@code JsonNode} to a {@code List} of CSV beans
     * @return The {@code Set} of aggregated column headers
     * @throws InvalidAnnotationException if the provided result record does not represent a well-formed annotation
     */
    private final static Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, Supplier<DBResultToCsvBeanConverter> beanConverterSupplier) throws InvalidAnnotationException {
        List<AnnotationCsv> annotationCsvBeans = beanConverterSupplier.get().getAnnotationBeans(result, resultIndexMap, resultIndicesMap);

        // First aggregate all optional columns that have at least one annotation with a non-null value
        Set<ColumnHeader> optionalColumnsWithValues = annotationCsvBeans.stream()
                .map(annotationBean -> annotationBean.getOptionalColumnsWithValues())
                .reduce(new LinkedHashSet<ColumnHeader>(), (aggregateSet, currentSet) -> {
                    // Take the union of all optional columns with values
                    aggregateSet.addAll(currentSet);
                    return aggregateSet;
                });

        // Next add those optional columns to the required columns
        Set<ColumnHeader> requiredColumns;
        int imageSetFormatIndex = resultIndexMap.get("imageSetFormat");
        if (ImageSetAssociation.isAssociatedWithDicom(result, imageSetFormatIndex)) {
            requiredColumns = annotationCsvBeans.get(0).getRequiredDicomColumns();
        } else if (ImageSetAssociation.isAssociatedWithNonDicom(result, imageSetFormatIndex)) {
            requiredColumns = annotationCsvBeans.get(0).getRequiredNonDicomColumns();
        } else {
            throw new IllegalArgumentException("The provided annotation node is not associated with either DICOM or non-DICOM data");
        }

        requiredColumns.addAll(optionalColumnsWithValues);

        // Finally return the aggregated set
        return requiredColumns;
    }
}