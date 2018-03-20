/*
 *  Annotation.java
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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An {@code Annotation} stores clinically significant meta data pertaining to a specific image set.
 * There are many types of {@code Annotation}s, each of which has its own kinds of meta data.  Therefore,
 * each {@code Annotation} <b>must declare its annotation type</b> in order to establish the kinds of required and optional
 * meta data that it can store.  Additionally, an {@code Annotation} can be associated with either a DICOM or non-DICOM image set.
 * Therefore, an {@code Annotation} must be constructed with either a DICOM or non-DICOM image set association.
 *
 * @see com.gehc.ai.app.datacatalog.util.exportannotations.AnnotationType
 */
public abstract class Annotation {

    ///////////////////////////////////////////
    //
    // Instance variables for any annotation //
    //
    ///////////////////////////////////////////

    private String annotationType;

    ////////////////////////////////////////////////////////////////////////////
    //
    // Instance variables for an annotation associated with a DICOM image set //
    //
    ////////////////////////////////////////////////////////////////////////////

    private String seriesUID;

    ////////////////////////////////////////////////////////////////////////////////
    //
    // Instance variables for an annotation associated with a non-DICOM image set //
    //
    ////////////////////////////////////////////////////////////////////////////////

    private String fileName;

    private String spaceID;

    /**
     * Creates a new {@code Annotation} that is associated with a DICOM image set.
     *
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param annotationType This annotation's type. {@link com.gehc.ai.app.datacatalog.util.exportannotations.AnnotationType}
     * @return a new {@code Annotation}
     */
    public Annotation(String seriesUID, String annotationType) {
        this.seriesUID = seriesUID;
        this.annotationType = Objects.requireNonNull(annotationType);
    }

    /**
     * Creates a new {@code Annotation} that is associated with a non-DICOM image set.
     *
     * @param fileName       The original file name of the non-DICOM image to which this annotation is associated
     * @param spaceID        The S3 space ID where the non-DICOM is stored
     * @param annotationType This annotation's type
     */
    public Annotation(String fileName, String spaceID, String annotationType) {
        this.fileName = fileName;
        this.spaceID = spaceID;
        this.annotationType = annotationType;
    }

    /**
     * Returns the {@code Set} of columns required of an annotation that is associated with a DICOM image set.
     *
     * @return a {@code Set}
     */
    public Set<String> getRequiredDicomColumns() {
        Set<String> requiredDicomColumns = new LinkedHashSet<>();
        requiredDicomColumns.add("seriesUID");
        requiredDicomColumns.add("annotationType");
        return requiredDicomColumns;
    }

    /**
     * Returns the {@code Set} of columns required of an annotation that is associated with a non-DICOM image set.
     *
     * @return a {@code Set}
     */
    public Set<String> getRequiredNonDicomColumns() {
        Set<String> requiredDicomColumns = new LinkedHashSet<>();
        requiredDicomColumns.add("fileName");
        requiredDicomColumns.add("spaceID");
        requiredDicomColumns.add("annotationType");
        return requiredDicomColumns;
    }

    /**
     * Returns the {@code Set} of optional columns that can be used to describe an annotation.
     * The returned set will only consist of those optional columns that have a non-null value.
     *
     * @return a {@code Set}
     */
    public abstract Set<String> getOptionalColumnsWithValues();

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }

    public String getSeriesUID() {
        return seriesUID;
    }

    public void setSeriesUID(String seriesUID) {
        this.seriesUID = seriesUID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSpaceID() {
        return spaceID;
    }

    public void setSpaceID(String spaceID) {
        this.spaceID = spaceID;
    }

}
