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

import java.util.Set;

/**
 * {@code AnnotationType} enumerates the types of annotations that can be produced and consumed by the Learning Factory.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public enum AnnotationType {
    LABEL() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return LabelConverter.convert(node, columnHeaders);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return LabelConverter.getColumnHeaders(node);
        }
    },
    POINT() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    },
    LINE() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    },
    RECT() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    },
    ELLIPSE() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    },
    CONTOUR() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    },
    BOX() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    },
    ELLIPSOID() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) {
            return null;
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) {
            return null;
        }
    };

    /**
     * Converts the provided {@link JsonNode} representation of an annotation to a CSV representation that is ingestable by the Learning Factory.
     *
     * @param node          The annotation node to convert to CSV
     * @param columnHeaders The column headers that will be used to organize the records of the CSV output
     * @return The converted CSV {@code String}
     * @throws InvalidAnnotationException If the provided {@code JsonNode} does not represent a well-formed annotation
     * @throws CsvConversionException If the provided {@code JsonNode} could not be successfully mapped to a corresponding CSV representation
     */
    public abstract String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException;

    /**
     * Returns the set of required columns and optional columns for this {@code AnnotationType} which have at least one non-null value.
     *
     * @param node The annotation node that will be evaluated to determine which optional columns have at least one non-null value
     * @return a {@code Set}
     * @throws InvalidAnnotationException If the provided {@code JsonNode} does not represent a well-formed annotation
     */
    public abstract Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException;

    /**
     * Returns the JSON property that can be used to get an annotation object's type.
     *
     * @return The JSON property as a {@code String}
     */
    public static String getJsonProperty() {
        return "annotationType";
    }
}