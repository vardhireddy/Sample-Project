/*
 *  CsvAnnotationDetailsExporter.java
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * {@code CsvAnnotationDetailsExporter} exports DB result records, which describe annotations, as a CSV string.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class CsvAnnotationDetailsExporter {

    /**
     * Prevents the {@code CsvAnnotationDetailsExporter} class from being instantiated.
     */
    private CsvAnnotationDetailsExporter() {
        throw new AssertionError("CsvAnnotationDetailsExporter is a utility class and should not be instantiated.");
    }

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Exports the provided DB result records, which describes annotations, as a CSV string.
     *
     * @param results          The DB result records which describe annotations
     * @param resultIndexMap   A mapping of single-valued result meta data to their index in the result record.
     * @param resultIndicesMap A mapping of multi-valued result meta data to their indices in the result record.
     * @return A CSV {@code String} representation of the input annotation(s)
     * @throws InvalidAnnotationException If the JSON representation of any of the annotations is not well-formed
     * @throws CsvConversionException     If the JSON representation of any of the annotations could not be successfully mapped to a corresponding CSV representation
     */
    public static final String exportAsCsv(List<Object[]> results, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException, CsvConversionException {
        StringBuilder csvBuilder = new StringBuilder();

        final int annotationTypeIndex = resultIndexMap.get("annotationType");

        // First, determine the set of columns headers to be used when producing the single CSV string.
        // The set will include, for all annotation types, the required column headers and optional column headers that have at least one non-null value
        Set<String> columnHeaders = new LinkedHashSet<>();
        for (final Object[] result : results) {
            AnnotationType annotationType = getAnnotationType(result, annotationTypeIndex);
            columnHeaders.addAll(annotationType.getColumnHeaders(result, resultIndexMap, resultIndicesMap));
        }
        // Convert the set of columns headers to an array to accomodate OpenCSV API
        String[] columnHeadersArr = columnHeaders.stream().toArray(String[]::new);

        // Then write the column headers as the first row of the CSV file
        csvBuilder.append(String.join(",", columnHeadersArr) + "\n");

        // Second, write out each annotation JSON node as CSV using the above set of column headers
        List<AnnotationCsv> annotationCsvBeans = new ArrayList<>();
        for (final Object[] result : results) {
            AnnotationType annotationType = getAnnotationType(result, annotationTypeIndex);
            String convertedCsv = annotationType.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeadersArr);
            csvBuilder.append(convertedCsv);
        }
        // Finally, return the aggregated CSV string
        return csvBuilder.toString();
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    /**
     * Returns the {@link AnnotationType} associated with the provided annotation {@link JsonNode}.
     *
     * @param result              The DB result to extract the annotation type from
     * @param annotationTypeIndex The index in the result record that defines the annotation type
     * @return the associated {@code AnnotationType} if it exists; otherwise, {@code null}.
     */
    private static AnnotationType getAnnotationType(Object[] result, int annotationTypeIndex) {
        String annotationTypeAsStr = (String) result[annotationTypeIndex];
        return AnnotationType.valueOf(annotationTypeAsStr.toUpperCase(Locale.ENGLISH));
    }

}