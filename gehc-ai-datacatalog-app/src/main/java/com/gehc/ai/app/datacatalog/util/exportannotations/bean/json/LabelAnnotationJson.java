/*
 *  LabelAnnotationJson.java
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
import java.util.Objects;

/**
 * {@code LabelAnnotationJson} is a bean representing a label annotation as JSON.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelAnnotationJson extends AnnotationJson {

    private List<GEClass> geClasses;

    /**
     * Creates a new {@code LabelAnnotationJson}.
     *
     * @param patientID      The ID of the patient to which this annotation is associated
     * @param seriesUID      The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat The data format of the image set to which this annotation is associated
     * @param annotationID   The database assigned number that uniquely identifies this annotation
     * @param annotationType This annotation's type
     * @param geClasses      The GE classes that describe this label annotation
     */
    public LabelAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, String annotationType, List<GEClass> geClasses) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType);
        this.geClasses = Objects.requireNonNull(geClasses);
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public List<GEClass> getGeClasses() {
        return geClasses;
    }

    public void setGeClasses(List<GEClass> geClasses) {
        this.geClasses = geClasses;
    }

    @Override
    public boolean equals(Object o) {
        // Auto-generated
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabelAnnotationJson)) {
            return false;
        }

        LabelAnnotationJson that = (LabelAnnotationJson) o;

        return getGeClasses().equals(that.getGeClasses());
    }

    @Override
    public int hashCode() {
        // Auto-generated
        return getGeClasses().hashCode();
    }

}
