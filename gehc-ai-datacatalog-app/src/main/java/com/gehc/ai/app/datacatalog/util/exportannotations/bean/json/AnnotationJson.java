/*
 *  AnnotationJson.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import java.util.Objects;

/**
 * An {@code AnnotationJson} is a JSON representation of an annotation.  It stores clinically significant meta data pertaining
 * to a specific image set. There are many types of {@code AnnotationJson}s, each of which has its own kinds of meta data.  Therefore,
 * each {@code AnnotationJson} <b>must declare its annotation type</b> in order to establish the kinds of required and optional
 * meta data that it can store.  Additionally, an {@code AnnotationJson} can be associated with either a DICOM or non-DICOM image set.
 * Therefore, an {@code AnnotationJson} must specify its image set format.
 *
 * @see com.gehc.ai.app.datacatalog.util.exportannotations.AnnotationType
 */
public abstract class AnnotationJson {

    private final String patientID;
    private final String annotationType;
    private final String imageSetFormat;
    private final String seriesUID;
    private final Long annotationID;

    /**
     * Creates a new {@code AnnotationJson} that is associated with a DICOM image set.
     *
     * @param patientID      (Required) The ID of the patient to which this annotation is associated
     * @param seriesUID      (Required) The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat (Required) The data format of the image set to which this annotation is associated
     * @param annotationID   (Required) The database assigned number that uniquely identifies this annotation
     * @param annotationType (Required) This annotation's type
     * @return a new {@code AnnotationJson}
     */
    public AnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, String annotationType) {
        this.patientID = Objects.requireNonNull(patientID);
        this.seriesUID = Objects.requireNonNull(seriesUID);
        this.imageSetFormat = Objects.requireNonNull(imageSetFormat);
        this.annotationID = Objects.requireNonNull(annotationID);
        this.annotationType = Objects.requireNonNull(annotationType);
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public String getAnnotationType() {
        return annotationType;
    }

    public String getSeriesUID() {
        return seriesUID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getImageSetFormat() {
        return imageSetFormat;
    }

    public Long getAnnotationID() {
        return annotationID;
    }

    /**
     * Returns whether if it is possible for provided object to potentially equal an instance of {@code AnnotationJson}.
     *
     * @param other The object} to evaluate
     * @return {@code true} if it possible for the provided object to potentially equal an instance of {@code AnnotationJson}; otherwise, {@code false}
     */
    public boolean canEqual(Object other) {
        return (other instanceof AnnotationJson);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null && this != null) return false;
        if (!(o instanceof AnnotationJson)) return false;

        AnnotationJson that = (AnnotationJson) o;

        if (!that.canEqual(this)) return false;
        if (!getPatientID().equals(that.getPatientID())) return false;
        if (!getAnnotationType().equals(that.getAnnotationType())) return false;
        if (!getImageSetFormat().equals(that.getImageSetFormat())) return false;
        if (!getSeriesUID().equals(that.getSeriesUID())) return false;
        return getAnnotationID().equals(that.getAnnotationID());
    }

    @Override
    public int hashCode() {
        int result = getPatientID().hashCode();
        result = 31 * result + getAnnotationType().hashCode();
        result = 31 * result + getImageSetFormat().hashCode();
        result = 31 * result + getSeriesUID().hashCode();
        result = 31 * result + getAnnotationID().hashCode();
        return result;
    }
}
