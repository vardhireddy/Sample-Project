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

    private String patientID;
    private String annotationType;
    private String imageSetFormat;
    private String seriesUID;
    private Long annotationID;

    /**
     * Creates a new {@code AnnotationJson} that is associated with a DICOM image set.
     *
     * @param patientID      The ID of the patient to which this annotation is associated
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat The data format of the image set to which this annotation is associated
     * @param annotationID   The database assigned number that uniquely identifies this annotation
     * @param annotationType This annotation's type
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

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }

    public String getSeriesUID() {
        return seriesUID;
    }

    public void setSeriesUID(String seriesUID) {
        this.seriesUID = seriesUID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getImageSetFormat() {
        return imageSetFormat;
    }

    public void setImageSetFormat(String imageSetFormat) {
        this.imageSetFormat = imageSetFormat;
    }

    public Long getAnnotationID() {
        return annotationID;
    }

    public void setAnnotationID(Long annotationID) {
        this.annotationID = annotationID;
    }

    @Override
    public boolean equals(Object o) {
        // Auto-generated
        if (this == o) return true;
        if (!(o.getClass() == this.getClass())) return false;

        AnnotationJson that = (AnnotationJson) o;

        if (!getPatientID().equals(that.getPatientID())) return false;
        if (!getAnnotationType().equals(that.getAnnotationType())) return false;
        if (!getImageSetFormat().equals(that.getImageSetFormat())) return false;
        if (!getSeriesUID().equals(that.getSeriesUID())) return false;
        return getAnnotationID().equals(that.getAnnotationID());
    }

    @Override
    public int hashCode() {
        // Auto-generated
        int result = getPatientID().hashCode();
        result = 31 * result + getAnnotationType().hashCode();
        result = 31 * result + getImageSetFormat().hashCode();
        result = 31 * result + getSeriesUID().hashCode();
        result = 31 * result + getAnnotationID().hashCode();
        return result;
    }
}
