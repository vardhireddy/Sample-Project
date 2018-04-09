/*
 *  PointRoiAnnotationCsv.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * {@code PointRoiAnnotationCsv} is a bean representing an annotation that is a single point ROI.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class PointRoiAnnotationCsv extends RoiAnnotationCsv {

    private static final ColumnHeader[] REQUIRED_COLUMNS = new ColumnHeader[]{new ColumnHeader("data", 1)};

    private final List<Double> data;

    /**
     * Creates a new {@code PointRoiAnnotationCsv} that is associated with a DICOM image set.
     *
     * @param seriesUID           (Required) The series instance UID of the DICOM data to which this {@code PointRoiAnnotationCsv} is associated.
     * @param annotationTypeAsStr (Required) The string representation of the multi-point ROI annotation type
     * @param instances           (Required) The image instances associated this annotation
     * @param coordSys            (Required) The coordinate system in which the data points should be represented
     * @param data                (Required) The array of data points that comprise the ROI.
     * @param localID             (Optional) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name                (Optional) A name to identify this ROI.  This name does not need to be unique.
     * @param index               (Optional) The frame index this ROI is associated with
     */
    public PointRoiAnnotationCsv(String seriesUID, String annotationTypeAsStr, List<String> instances, String coordSys, List<Double> data, String localID, String name, Integer index) {
        super(seriesUID, annotationTypeAsStr, instances, coordSys, localID, name, index);
        this.data = Collections.unmodifiableList(Objects.requireNonNull(data));
    }

    /**
     * Creates a new {@code PointRoiAnnotationCsv} that is associated with a non-DICOM image set.
     *
     * @param fileName            (Required) The original file name of the non-DICOM image to which this {@code PointRoiAnnotationCsv} is associated
     * @param spaceID             (Required) The S3 space ID where the non-DICOM image is stored
     * @param annotationTypeAsStr (Required) The string representation of the multi-point ROI annotation type
     * @param instances           (Required) The image instances associated this annotation
     * @param coordSys            (Required) The coordinate system in which the data points should be represented
     * @param data                (Required) The array of data points that comprise the ROI.
     * @param localID             (Optional) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name                (Optional) A name to identify this ROI.  This name does not need to be unique.
     * @param index               (Optional) The frame index this ROI is associated with
     */
    public PointRoiAnnotationCsv(String fileName, String spaceID, String annotationTypeAsStr, List<String> instances, String coordSys, List<Double> data, String localID, String name, Integer index) {
        super(fileName, spaceID, annotationTypeAsStr, instances, coordSys, localID, name, index);
        this.data = Collections.unmodifiableList(Objects.requireNonNull(data));
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public List<Double> getData() {
        return Collections.unmodifiableList(data);
    }

    /////////////////////////////////////////////////
    //
    // APIs for defining CSV column set for an ROI //
    //
    /////////////////////////////////////////////////

    @Override
    public Set<ColumnHeader> getRequiredDicomColumns() {
        return getRequiredColumns(super.getRequiredDicomColumns());
    }

    @Override
    public Set<ColumnHeader> getRequiredNonDicomColumns() {
        return getRequiredColumns(super.getRequiredNonDicomColumns());
    }

    /**
     * Returns the set of columns required by this {@code PointRoiAnnotationCsv} and its parent.
     *
     * @param requiredParentColumns The columns required by this {@code PointRoiAnnotationCsv}'s parent.
     * @return a Set<ColumnHeader>
     */
    private static Set<ColumnHeader> getRequiredColumns(Set<ColumnHeader> requiredParentColumns) {
        Set<ColumnHeader> requiredColumns = new LinkedHashSet<>();
        requiredColumns.addAll(requiredParentColumns);
        Arrays.stream(REQUIRED_COLUMNS).forEach(requiredColumn -> requiredColumns.add(requiredColumn));
        return requiredColumns;
    }

}
