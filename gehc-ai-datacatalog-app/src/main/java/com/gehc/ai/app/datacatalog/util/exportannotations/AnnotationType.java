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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.Annotation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.ImageSetAssociation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.LabelAnnotation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.RoiAnnotation;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.JsonToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.LabelJsonToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.RoiJsonToCsvBeanConverter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
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
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, LabelJsonToCsvBeanConverter::new, LabelAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, LabelJsonToCsvBeanConverter::new);
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
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    },
    RECT() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    },
    ELLIPSE() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    },
    POLYGON() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    },
    CONTOUR() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    },
    BOX() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    },
    ELLIPSOID() {
        @Override
        public String convertJsonToCsv(JsonNode node, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return AnnotationType.convertJsonToCsv(node, columnHeaders, RoiJsonToCsvBeanConverter::new, RoiAnnotation.class);
        }

        @Override
        public Set<String> getColumnHeaders(JsonNode node) throws InvalidAnnotationException {
            return AnnotationType.getColumnHeaders(node, RoiJsonToCsvBeanConverter::new);
        }
    };

    /**
     * Converts the provided {@link JsonNode} representation of an annotation to a CSV representation that is ingestable by the Learning Factory.
     *
     * @param node          The annotation node to convert to CSV
     * @param columnHeaders The column headers that will be used to organize the records of the CSV output
     * @return The converted CSV {@code String}
     * @throws InvalidAnnotationException If the provided {@code JsonNode} does not represent a well-formed annotation
     * @throws CsvConversionException     If the provided {@code JsonNode} could not be successfully mapped to a corresponding CSV representation
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

    /**
     * Converts the provided {@link JsonNode} representation of a annotation annotation to a CSV representation that is ingestable by the Learning Factory.
     *
     * @param annotationNode        A {@code JsonNode} representing a annotation annotation
     * @param columnHeaders         The array of required column headers and optional column headers with aqt least one non-null value
     * @param beanConverterSupplier The supplier of the service which converts a {@code JsonNode} to a {@code List} of CSV beans
     * @return A CSV {@code String}
     * @throws CsvConversionException
     * @throws InvalidAnnotationException
     */
    private final static String convertJsonToCsv(JsonNode annotationNode, String[] columnHeaders, Supplier<JsonToCsvBeanConverter> beanConverterSupplier, Class beanClass) throws InvalidAnnotationException, CsvConversionException {
        // Create a single CSV bean
        List<Annotation> annotationBeans = beanConverterSupplier.get().getAnnotationBeans(annotationNode);

        // Initialize the CSV writer with the appropriate CSV bean and column headers
        ColumnPositionMappingStrategy<Annotation> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(beanClass);
        strategy.setColumnMapping(columnHeaders);

        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<Annotation> beanWriter = new StatefulBeanToCsvBuilder(writer).withMappingStrategy(strategy).build();

        // Finally, write the beans to a CSV string
        try {
            beanWriter.write(annotationBeans);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new CsvConversionException(e.getMessage());
        }

        return writer.toString();
    }

    /**
     * Returns the aggregation of required column headers and optional column headers that have values.
     *
     * @param annotationNode        The annotation {@link JsonNode} that will be evaluated to see which optional columns have at least one non-null value
     * @param beanConverterSupplier The supplier of the service which converts a {@code JsonNode} to a {@code List} of CSV beans
     * @return The {@code Set} of aggregated column headers
     * @throws InvalidAnnotationException
     */
    private final static Set<String> getColumnHeaders(JsonNode annotationNode, Supplier<JsonToCsvBeanConverter> beanConverterSupplier) throws InvalidAnnotationException {
        List<Annotation> annotationBeans = beanConverterSupplier.get().getAnnotationBeans(annotationNode);

        // First aggregate all optional columns that have at least one annotation with a non-null value
        Set<String> optionalColumnsWithValues = annotationBeans.stream()
                .map(annotationBean -> annotationBean.getOptionalColumnsWithValues())
                .reduce(new HashSet<String>(), (aggregateSet, currentSet) -> {
                    // Take the union of all optional columns with values
                    aggregateSet.addAll(currentSet);
                    return aggregateSet;
                });

        // Next add those optional columns to the required columns
        Set<String> requiredColumns;
        if (ImageSetAssociation.isAssociatedWithDicom(annotationNode)) {
            requiredColumns = annotationBeans.get(0).getRequiredDicomColumns();
        } else if (ImageSetAssociation.isAssociatedWithNonDicom(annotationNode)) {
            requiredColumns = annotationBeans.get(0).getRequiredNonDicomColumns();
        } else {
            throw new IllegalArgumentException("The provided annotation node is not associated with either DICOM or non-DICOM data");
        }

        requiredColumns.addAll(optionalColumnsWithValues);

        // Finally return the aggregated set
        return requiredColumns;
    }
}