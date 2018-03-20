/*
 *  MultiPointRoiAnnotation.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code MultiPointRoiAnnotation} is a bean representing an annotation that is a multi-point ROI (e.g. line, rectangle, ellipse, polygon or contour).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class MultiPointRoiAnnotation extends RoiAnnotation {

    private static final String[] REQUIRED_COLUMNS = new String[]{"data"};

    private List<List<Double>> data;

    /**
     * Creates a new {@code MultiPointRoiAnnotation} that is associated with a DICOM image set.
     *
     * @param seriesUID           (Required) The series instance UID of the DICOM data to which this {@code MultiPointRoiAnnotation} is associated.
     * @param annotationTypeAsStr (Required) The string representation of the multi-point ROI annotation type
     * @param coordSys            (Required) The coordinate system in which the data points should be represented
     * @param data                (Required) The array of data points that comprise the ROI.
     * @param localID             (Optional) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name                (Optional) A name to identify this ROI.  This name does not need to be unique.
     */
    public MultiPointRoiAnnotation(String seriesUID, String annotationTypeAsStr, String coordSys, List<List<Double>> data, String localID, String name) {
        super(seriesUID, annotationTypeAsStr, coordSys, localID, name);
        setUp(data);
    }

    /**
     * Creates a new {@code MultiPointRoiAnnotation} that is associated with a non-DICOM image set.
     *
     * @param fileName            (Required) The original file name of the non-DICOM image to which this {@code MultiPointRoiAnnotation} is associated
     * @param spaceID             (Required) The S3 space ID where the non-DICOM image is stored
     * @param annotationTypeAsStr (Required) The string representation of the multi-point ROI annotation type
     * @param coordSys            (Required) The coordinate system in which the data points should be represented
     * @param data                (Required) The array of data points that comprise the ROI.
     * @param localID             (Optional) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name                (Optional) A name to identify this ROI.  This name does not need to be unique.
     */
    public MultiPointRoiAnnotation(String fileName, String spaceID, String annotationTypeAsStr, String coordSys, List<List<Double>> data, String localID, String name) {
        super(fileName, spaceID, annotationTypeAsStr, coordSys, localID, name);
        setUp(data);
    }

    /**
     * Initializes instance fields that are specific to the {@code MultiPointRoiAnnotation}.
     *
     * @param data (Required) The array of data points that comprise the ROI.
     */
    private void setUp(List<List<Double>> data) {
        this.data = Objects.requireNonNull(data);
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public List<List<Double>> getData() {
        return data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    /////////////////////////////////////////////////
    //
    // APIs for defining CSV column set for an ROI //
    //
    /////////////////////////////////////////////////

    @Override
    public Set<String> getRequiredDicomColumns() {
        return getRequiredColumns(super.getRequiredDicomColumns());
    }

    @Override
    public Set<String> getRequiredNonDicomColumns() {
        return getRequiredColumns(super.getRequiredNonDicomColumns());
    }

    @Override
    public Set<String> getOptionalColumnsWithValues() {
        Map<String, String> optionalColumnValues = new HashMap<>();
        optionalColumnValues.put("name", getName());
        return optionalColumnValues.entrySet().stream().filter(entry -> entry.getValue() != null).map(nonNullEntry -> nonNullEntry.getKey()).collect(Collectors.toSet());
    }

    /**
     * Returns the set of columns required by this {@code MultiPointRoiAnnotation} and its parent.
     *
     * @param requiredParentColumns The columns required by this {@code MultiPointRoiAnnotation}'s parent.
     * @return a Set<String>
     */
    private static Set<String> getRequiredColumns(Set<String> requiredParentColumns) {
        Set<String> requiredColumns = new LinkedHashSet<>();
        requiredColumns.addAll(requiredParentColumns);
        Arrays.stream(REQUIRED_COLUMNS).forEach(requiredColumn -> requiredColumns.add(requiredColumn));
        return requiredColumns;
    }

}
