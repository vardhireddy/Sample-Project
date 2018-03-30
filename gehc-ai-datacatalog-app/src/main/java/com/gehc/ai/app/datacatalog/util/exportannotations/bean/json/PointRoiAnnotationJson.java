/*
 *  PointRoiAnnotationJson.java
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code PointRoiAnnotationJson} is a bean representing an annotation that is a point
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class PointRoiAnnotationJson extends RoiAnnotationJson {

    private final List<Double> data;

    /**
     * Creates a new {@code PointRoiAnnotationJson}.
     *
     * @param patientID      The ID of the patient to which this annotation is associated
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat The data format of the image set to which this annotation is associated
     * @param annotationType This annotation's type
     */
    public PointRoiAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, String annotationType, String coordSys, List<Double> data, String localID, String name) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType, coordSys, localID, name);
        this.data = Collections.unmodifiableList(Objects.requireNonNull(data));
    }

    /////////////
    //
    // Getters //
    //
    /////////////

    public List<Double> getData() {
        return Collections.unmodifiableList(data);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof PointRoiAnnotationJson)) return false;

        PointRoiAnnotationJson that = (PointRoiAnnotationJson) o;
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
        return (other instanceof PointRoiAnnotationJson);
    }

}
