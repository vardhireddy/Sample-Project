/*
 *  RoiAnnotation.java
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
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code RoiAnnotation} is a bean representing an annotation that is a ROI (e.g. rectangle, ellipse, polygon or contour).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class RoiAnnotation extends Annotation {

    private static final String[] REQUIRED_COLUMNS = new String[]{"coordSys", "data", "localID"};

    private String coordSys;

    private List<List<Double>> data;

    private String localID;

    private String name;

    /**
     * Creates a new {@code RoiAnnotation} that is associated with a DICOM image set.
     *
     * @param seriesUID           (Required) The series instance UID of the DICOM data to which this {@code RoiAnnotation} is associated.
     * @param annotationTypeAsStr (Required) The string representation of the ROI annotation type
     * @param coordSys            (Required) The coordinate system in which the data points should be represented
     * @param data                (Required) The array of data points that comprise the ROI.
     * @param localID             (Optional) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name                (Optional) A name to identify this ROI.  This name does not need to be unique.
     */
    public RoiAnnotation(String seriesUID, String annotationTypeAsStr, String coordSys, List<List<Double>> data, String localID, String name) {
        super(seriesUID, annotationTypeAsStr);
        setUp(coordSys, data, localID, name);
    }

    /**
     * Creates a new {@code RoiAnnotation} that is associated with a non-DICOM image set.
     *
     * @param fileName            (Required) The original file name of the non-DICOM image to which this {@code RoiAnnotation} is associated
     * @param spaceID             (Required) The S3 space ID where the non-DICOM image is stored
     * @param annotationTypeAsStr (Required) The string representation of the ROI annotation type
     * @param coordSys            (Required) The coordinate system in which the data points should be represented
     * @param data                (Required) The array of data points that comprise the ROI.
     * @param localID             (Optional) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name                (Optional) A name to identify this ROI.  This name does not need to be unique.
     */
    public RoiAnnotation(String fileName, String spaceID, String annotationTypeAsStr, String coordSys, List<List<Double>> data, String localID, String name) {
        super(fileName, spaceID, annotationTypeAsStr);
        setUp(coordSys, data, localID, name);
    }

    /**
     * Initializes instance fields that are specific to the {@code RoiAnnotation}.
     *
     * @param coordSys (Required) The coordinate system in which the data points should be represented
     * @param data     (Required) The array of data points that comprise the ROI.
     * @param localID  (Required) A identifier that is unique relative to other ROIs associated with the same image set.
     * @param name     (Optional) A name to identify this ROI.  This name does not need to be unique.
     */
    private void setUp(String coordSys, List<List<Double>> data, String localID, String name) {
        this.coordSys = Objects.requireNonNull(coordSys);
        data.forEach(datum -> System.out.println("HELLO " + datum));
        this.data = Objects.requireNonNull(data);
        this.localID = Objects.requireNonNull(localID);
        this.name = name;
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public String getCoordSys() {
        return coordSys;
    }

    public void setCoordSys(String coordSys) {
        this.coordSys = coordSys;
    }

    public List<List<Double>> getData() {
        return data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
     * Returns the set of columns required by this {@code RoiAnnotation} and its parent.
     *
     * @param requiredParentColumns The columns required by this {@code RoiAnnotation}'s parent.
     * @return a Set<String>
     */
    private static Set<String> getRequiredColumns(Set<String> requiredParentColumns) {
        Set<String> requiredColumns = new LinkedHashSet<>();
        requiredColumns.addAll(requiredParentColumns);
        Arrays.stream(REQUIRED_COLUMNS).forEach(requiredColumn -> requiredColumns.add(requiredColumn));
        return requiredColumns;
    }

}
