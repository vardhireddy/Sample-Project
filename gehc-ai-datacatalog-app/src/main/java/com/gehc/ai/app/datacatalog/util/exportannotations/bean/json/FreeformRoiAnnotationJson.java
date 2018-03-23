/*
 *  FreeformRoiAnnotationJson.java
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

import java.util.List;
import java.util.Locale;

/**
 * {@code FreeformRoiAnnotationJson} is a bean representing an annotation that is a freeform ROI (e.g. polygon or contour).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class FreeformRoiAnnotationJson extends RoiAnnotationJson {

    private List<List<Double>> data;

    /**
     * Creates a new {@code FreeformRoiAnnotationJson}.
     *
     * @param patientID      The ID of the patient to which this annotation is associated
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat The data format of the image set to which this annotation is associated
     * @param annotationType This annotation's type
     */
    public FreeformRoiAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, FreeformRoi annotationType, String coordSys, List<List<Double>> data, String localID, String name) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType.toString().toLowerCase(Locale.ENGLISH), coordSys, localID, name);
        this.data = data;
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

    @Override
    public boolean equals(Object o) {
        // Auto-generated
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreeformRoiAnnotationJson)) {
            return false;
        }

        FreeformRoiAnnotationJson that = (FreeformRoiAnnotationJson) o;

        return getData().equals(that.getData());
    }

    @Override
    public int hashCode() {
        // Auto-generated
        return getData().hashCode();
    }

}
