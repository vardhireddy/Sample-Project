/*
 *  AnnotationCsv.java
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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An {@code AnnotationCsv} is a CSV representation of an annotation.  It stores clinically significant meta data pertaining
 * to a specific image set.  There are many types of {@code AnnotationCsv}s, each of which has its own kinds of meta data.  Therefore,
 * each {@code AnnotationCsv} <b>must declare its annotation type</b> in order to establish the kinds of required and optional
 * meta data that it can store.  Additionally, an {@code AnnotationCsv} can be associated with either a DICOM or non-DICOM image set.
 * Therefore, an {@code AnnotationCsv} must be constructed with either a DICOM or non-DICOM image set association.
 *
 * @see com.gehc.ai.app.datacatalog.util.exportannotations.AnnotationType
 */
public abstract class AnnotationCsv {

    ///////////////////////////////////////////
    //
    // Instance variables for any annotation //
    //
    ///////////////////////////////////////////

    private final String annotationType;

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
     * Creates a new {@code AnnotationCsv} that is associated with a DICOM image set.
     *
     * @param seriesUID      (Required) The series instance UID of the DICOM data to which this annotation is associated
     * @param annotationType (Required) This annotation's type. {@link com.gehc.ai.app.datacatalog.util.exportannotations.AnnotationType}
     * @return a new {@code AnnotationCsv}
     */
    public AnnotationCsv(String seriesUID, String annotationType) {
        this.seriesUID = Objects.requireNonNull(seriesUID);
        this.annotationType = Objects.requireNonNull(annotationType);
    }

    /**
     * Creates a new {@code AnnotationCsv} that is associated with a non-DICOM image set.
     *
     * @param fileName       (Required) The original file name of the non-DICOM image to which this annotation is associated
     * @param spaceID        (Required) The S3 space ID where the non-DICOM is stored
     * @param annotationType (Required) This annotation's type
     */
    public AnnotationCsv(String fileName, String spaceID, String annotationType) {
        this.fileName = Objects.requireNonNull(fileName);
        this.spaceID = Objects.requireNonNull(spaceID);
        this.annotationType = Objects.requireNonNull(annotationType);
    }

    /**
     * Returns the {@code Set} of columns required of an annotation that is associated with a DICOM image set.
     *
     * @return a {@code Set}
     */
    public Set<ColumnHeader> getRequiredDicomColumns() {
        Set<ColumnHeader> requiredDicomColumns = new LinkedHashSet<>();
        requiredDicomColumns.add(new ColumnHeader("seriesUID", 0));
        requiredDicomColumns.add(new ColumnHeader("annotationType", 1));
        return requiredDicomColumns;
    }

    /**
     * Returns the {@code Set} of columns required of an annotation that is associated with a non-DICOM image set.
     *
     * @return a {@code Set}
     */
    public Set<ColumnHeader> getRequiredNonDicomColumns() {
        Set<ColumnHeader> requiredDicomColumns = new LinkedHashSet<>();
        requiredDicomColumns.add(new ColumnHeader("fileName", 0));
        requiredDicomColumns.add(new ColumnHeader("spaceID", 0));
        requiredDicomColumns.add(new ColumnHeader("annotationType", 1));
        return requiredDicomColumns;
    }

    /**
     * Returns the {@code Set} of optional columns that can be used to describe an annotation.
     * The returned set will only consist of those optional columns that have a non-null value.
     *
     * @return a {@code Set}
     */
    public abstract Set<ColumnHeader> getOptionalColumnsWithValues();

    /////////////
    //
    // Getters //
    //
    /////////////

    public String getAnnotationType() {
        return annotationType;
    }

    public String getSeriesUID() {
        return seriesUID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSpaceID() {
        return spaceID;
    }

}
