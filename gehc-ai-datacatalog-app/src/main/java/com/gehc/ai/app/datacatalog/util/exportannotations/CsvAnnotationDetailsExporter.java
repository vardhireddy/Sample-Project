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

import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * {@code CsvAnnotationDetailsExporter} exports DB result records, which describe annotations, as a CSV string.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class CsvAnnotationDetailsExporter {

    private static final Logger logger = Logger.getLogger(CsvAnnotationDetailsExporter.class);

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
     * @throws InvalidAnnotationException if at least one of the annotations described the DB result record is not a well-formed annotation
     * @throws CsvConversionException     if at least one of the annotations described the DB result record could not be successfully mapped to a corresponding CSV representation
     */
    public static final String exportAsCsv(List<Object[]> results, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException, CsvConversionException {
        // Toll-gate checks
        if (null == results) {
            throw new InvalidAnnotationException("There are no annotation details to export.");
        }

        // Toll gates passed! Begin exporting the annotation details as CSV

        StringBuilder csvBuilder = new StringBuilder();

        final int annotationTypeIndex = resultIndexMap.get("annotationType");

        // First, determine the set of column headers to be used when producing the single CSV string.
        // The set will include, for all annotation types, the required column headers and optional column headers that have at least one non-null value.
        // The column headers will be sorted by priority.
        String[] columHeaders = getOrderedColumnHeaders(results, annotationTypeIndex, resultIndexMap, resultIndicesMap);

        // Then write the column headers as the first row of the CSV file
        csvBuilder.append(String.join(",", columHeaders));
        csvBuilder.append("\n");

        // Then, write out each annotation as CSV using the above set of column headers
        for (final Object[] result : results) {
            String annotationTypeAsStr = (String) result[annotationTypeIndex];

            if (AnnotationType.contains(annotationTypeAsStr)) {
                AnnotationType annotationType = AnnotationType.valueOf(annotationTypeAsStr.toUpperCase(Locale.ENGLISH));
                String convertedCsv = annotationType.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columHeaders);
                csvBuilder.append(convertedCsv);
            } else {
                logger.debug("Skipping DB result since it describes an annotation of an unsupported type: " + annotationTypeAsStr);
            }
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
     * <p>Returns the column headers to be used when exporting the annotation details as CSV.  The column headers will be
     * ordered based on their defined priority.
     * </p>
     * <p>
     * (See {@link com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ColumnHeader} for more on header priority)
     * </p>
     *
     * @param results             The DB result records which describe annotations
     * @param annotationTypeIndex The index in the result record that defines the annotation type
     * @param resultIndexMap      A mapping of single-valued result meta data to their index in the result record.
     * @param resultIndicesMap    A mapping of multi-valued result meta data to their indices in the result record.
     * @return An array of column headers ordered by their priority.
     * @throws InvalidAnnotationException if at least one of the annotations described the DB result record is not a well-formed annotation
     */
    private static String[] getOrderedColumnHeaders(List<Object[]> results, int annotationTypeIndex, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
        // Group column headers by their priority.  Use a TreeMap to automatically sort headers by priority.
        Map<Integer, Set<String>> orderedColumnHeaders = new TreeMap<>();

        for (final Object[] result : results) {
            String annotationTypeAsStr = (String) result[annotationTypeIndex];

            if (AnnotationType.contains(annotationTypeAsStr)) {
                AnnotationType annotationType = AnnotationType.valueOf(annotationTypeAsStr.toUpperCase(Locale.ENGLISH));
                // Retrieve the necessary column headers for this annotation
                annotationType.getColumnHeaders(result, resultIndexMap, resultIndicesMap).stream()
                        .forEach(columnHeader -> {
                            // If a map already has a set defined for this header's priority, just add the header's name to the set
                            if (orderedColumnHeaders.containsKey(columnHeader.getPriority())) {
                                orderedColumnHeaders.get(columnHeader.getPriority()).add(columnHeader.getName());
                            } else {
                                // Otherwise, create a new set for this priority and then add the header's name to the newly created set
                                Set<String> columnNames = new LinkedHashSet<>();
                                columnNames.add(columnHeader.getName());
                                orderedColumnHeaders.put(columnHeader.getPriority(), columnNames);
                            }
                        });
            } else {
                logger.debug("Skipping DB result since it describes an annotation of an unsupported type: " + annotationTypeAsStr);
            }
        }

        // Create an ordered list of column headers simply by iterating through the TreeMap
        List<String> columnHeaders = new ArrayList<>();
        orderedColumnHeaders.entrySet().forEach(columnHeadersEntry -> columnHeaders.addAll(columnHeadersEntry.getValue()));

        // Convert the set of columns headers to an array to accomodate OpenCSV API
        return columnHeaders.stream().toArray(String[]::new);
    }

}