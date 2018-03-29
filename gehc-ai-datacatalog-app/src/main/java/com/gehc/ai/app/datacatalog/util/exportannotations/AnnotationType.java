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

import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ColumnHeader;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.LabelAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.MultiPointRoiAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.PointRoiAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.BoundingBoxDataConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.BoundingCubeDataConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.DBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.FreeformRoiDataConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.LabelDBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.MultiPointRoiDBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv.PointRoiDBResultToCsvBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.BoundingBoxDBResultToJsonBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.BoundingCubeDBResultToJsonBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.FreeformRoiDBResultToJsonBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.LabelDBResultToJsonBeanConverter;
import com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json.PointRoiDBResultToJsonBeanConverter;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@code AnnotationType} enumerates the types of annotations that can be produced and consumed by the Learning Factory.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
enum AnnotationType {
    /**
     * A textual label that describes a medical image.
     */
    LABEL() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new LabelDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, LabelDBResultToCsvBeanConverter::new, LabelAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, LabelDBResultToCsvBeanConverter::new);
        }
    },
    /**
     * A freeform ROI that is composed of multiple vertices whereby the vertices are connected by straight line segments.
     */
    POLYGON() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new FreeformRoiDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(FreeformRoiDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(FreeformRoiDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    },
    /**
     * A freeform ROI that is composed of multiple vertices whereby the vertices are connected by straight line segments.
     *
     * @deprecated This annotation type's name is a misnomer; it implies that two vertices are connected by a curve.  Therefore, use {@link #POLYGON} instead
     */
    @Deprecated
    CONTOUR() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new FreeformRoiDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(FreeformRoiDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(FreeformRoiDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    },
    /**
     * A rectangle ROI
     */
    RECT() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new BoundingBoxDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingBoxDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingBoxDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    },
    /**
     * An ellipse ROI
     */
    ELLIPSE() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new BoundingBoxDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingBoxDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingBoxDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    },
    /**
     * A point ROI
     */
    POINT() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new PointRoiDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, PointRoiDBResultToCsvBeanConverter::new, PointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, PointRoiDBResultToCsvBeanConverter::new);
        }
    },
    /**
     * A line ROI
     */
    LINE() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new FreeformRoiDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(FreeformRoiDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(FreeformRoiDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    },
    /**
     * A box ROI
     */
    BOX() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new BoundingCubeDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingCubeDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingCubeDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    },
    /**
     * An ellipsoid ROI
     */
    ELLIPSOID() {
        @Override
        public AnnotationJson convertDBResultToJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            return new BoundingCubeDBResultToJsonBeanConverter().getJson(result, resultIndexMap, resultIndicesMap);
        }

        @Override
        public String convertDBResultToCsv(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap, String[] columnHeaders) throws InvalidAnnotationException, CsvConversionException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingCubeDataConverter::new);
            return CsvConversionTemplates.convertDBResultToCsv(result, resultIndexMap, resultIndicesMap, columnHeaders, converterSupplier, MultiPointRoiAnnotationCsv.class);
        }

        @Override
        public Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
            Supplier<DBResultToCsvBeanConverter> converterSupplier = () -> new MultiPointRoiDBResultToCsvBeanConverter(BoundingCubeDataConverter::new);
            return CsvConversionTemplates.getColumnHeaders(result, resultIndexMap, resultIndicesMap, converterSupplier);
        }
    };

    /**
     * Returns whether provided string maps to an instance of {@code AnnotationType}.
     *
     * @param test The string to evaluate
     * @return {@code true} if the provided string has an associated {@code AnnotationType} instance; otherwise, false
     */
    public static boolean contains(String test) {
        for (AnnotationType annotType : AnnotationType.values()) {
            if (annotType.name().equalsIgnoreCase(test)) {
                return true;
            }
        }

        return false;
    }

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
     * @throws InvalidAnnotationException If the provided DB result record does not represent a well-formed annotation
     */
    public abstract Set<ColumnHeader> getColumnHeaders(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException;

}