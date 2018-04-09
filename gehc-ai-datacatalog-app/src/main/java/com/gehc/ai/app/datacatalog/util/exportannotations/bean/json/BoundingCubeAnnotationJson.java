/*
 *  BoundingCubeAnnotationJson.java
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

import com.gehc.ai.app.datacatalog.util.exportannotations.bean.BoundingCube;

import java.util.Locale;

/**
 * {@code BoundingCubeAnnotationJson} is a bean representing an annotation that is a bounding cube ROI (e.g. box or ellipsoid).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingCubeAnnotationJson extends RoiAnnotationJson {

    private final BoundingCube data;

    /**
     * Creates a new {@code BoundingBoxJson} .
     *
     * @param patientID      The ID of the patient to which this annotation is associated
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat The data format of the image set to which this annotation is associated
     * @param annotationType This annotation's type
     * @param index          The frame index this ROI is associated with
     */
    public BoundingCubeAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, BoundingCubeType annotationType, String coordSys, BoundingCube data, String localID, String name, Integer index) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType.toString().toLowerCase(Locale.ENGLISH), coordSys, localID, name, index);
        this.data = data;
    }

    /////////////
    //
    // Getters //
    //
    /////////////

    public BoundingCube getData() {
        return data;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof BoundingCubeAnnotationJson)) return false;

        BoundingCubeAnnotationJson that = (BoundingCubeAnnotationJson) o;
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
        return (other instanceof BoundingCubeAnnotationJson);
    }

}
