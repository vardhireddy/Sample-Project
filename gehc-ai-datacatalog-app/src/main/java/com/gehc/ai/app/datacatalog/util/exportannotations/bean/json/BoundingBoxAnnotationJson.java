/*
 *  BoundingBoxAnnotationJson.java
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

import com.gehc.ai.app.datacatalog.util.exportannotations.bean.BoundingBox;

import java.util.Locale;

/**
 * {@code BoundingBox} is a bean representing an annotation that is a bounding box ROI (e.g. rectangle or ellipse).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingBoxAnnotationJson extends RoiAnnotationJson {

    private final BoundingBox data;

    /**
     * Creates a new {@code BoundingBoxJson} .
     *
     * @param patientID      The ID of the patient to which this annotation is associated
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat The data format of the image set to which this annotation is associated
     * @param annotationType This annotation's type
     */
    public BoundingBoxAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, BoundingBoxType annotationType, String coordSys, BoundingBox data, String localID, String name) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType.toString().toLowerCase(Locale.ENGLISH), coordSys, localID, name);
        this.data = data;
    }

    /////////////
    //
    // Getters //
    //
    /////////////

    public BoundingBox getData() {
        return data;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof BoundingBoxAnnotationJson)) return false;

        BoundingBoxAnnotationJson that = (BoundingBoxAnnotationJson) o;
        if (!that.canEqual(this)) return false;
        if (!super.equals(that)) return false;

        return getData().equals(that.getData());
    }

    @Override
    public final int hashCode() {
        // Auto-generated
        int result = super.hashCode();
        result = 31 * result + getData().hashCode();
        return result;
    }

    @Override
    public final boolean canEqual(Object other) {
        return (other instanceof BoundingBoxAnnotationJson);
    }

}
