/*
 *  CsvAnnotationExporter.java
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.Annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * {@code CsvAnnotationExporter} exports a given JSON representation of annotation(s) as a CSV string.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class CsvAnnotationExporter {

    /**
     * Prevents the {@code CsvAnnotationExporter} class from being instantiated.
     */
    private CsvAnnotationExporter() {
        throw new AssertionError("CsvAnnotationExport is a utility class and should not be instantiated.");
    }

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Exports the provided JSON representation of one or more annotations as a CSV string.
     *
     * @param json The JSON representation of one or more annotations
     * @return A CSV {@code String} representation of the input annotation(s)
     * @throws InvalidAnnotationException If the JSON representation of any of the annotations is not well-formed
     * @throws CsvConversionException     If the JSON representation of any of the annotations could not be successfully mapped to a corresponding CSV representation
     */
    public static final String exportAsCsv(final String json) throws InvalidAnnotationException, CsvConversionException {
        final StringBuilder csvBuilder = new StringBuilder();

        // Parse the JSON string into a tree structure
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrNode;
        try {
            arrNode = new ObjectMapper().readTree(json);
        } catch (IOException e) {
            throw new InvalidAnnotationException(e.getMessage());
        }

        // We expect to have an array of annotation JSON objects
        if (arrNode.isArray()) {
            // First, determine the set of columns headers to be used when producing the single CSV string.
            // The set will include, for all annotation types, the required column headers and optional column headers that have at least one non-null value
            Set<String> columnHeaders = new LinkedHashSet<>();
            for (final JsonNode annotationNode : arrNode) {
                AnnotationType annotationType = getAnnotationType(annotationNode);
                columnHeaders.addAll(annotationType.getColumnHeaders(annotationNode));
            }
            // Convert the set of columns headers to an array to accomodate OpenCSV API
            String[] columnHeadersArr = columnHeaders.stream().toArray(String[]::new);

            // Second, write out each annotation JSON node as CSV using the above set of column headers
            List<Annotation> annotationBeans = new ArrayList<>();
            for (final JsonNode annotationNode : arrNode) {
                AnnotationType annotationType = getAnnotationType(annotationNode);
                String convertedCsv = annotationType.convertJsonToCsv(annotationNode, columnHeadersArr);
                csvBuilder.append(convertedCsv);
            }
        } else {
            throw new InvalidAnnotationException("The provided JSON representation of the annotation is not well-formed.");
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
     * @param annotationNode The {@code JsonNode} to evaluate
     * @return the associated {@code AnnotationType} if it exists; otherwise, {@code null}.
     */
    private static AnnotationType getAnnotationType(JsonNode annotationNode) {
        String annotationTypeAsStr = annotationNode.get(AnnotationType.getJsonProperty()).textValue().toUpperCase(Locale.ENGLISH);
        return AnnotationType.valueOf(annotationTypeAsStr);
    }

}