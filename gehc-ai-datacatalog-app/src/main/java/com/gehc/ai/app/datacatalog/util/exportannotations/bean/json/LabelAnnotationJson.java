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

import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code LabelAnnotationJson} is a bean representing a label annotation as JSON.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class LabelAnnotationJson extends AnnotationJson {

    private final List<GEClass> geClasses;

    private final String indication;

    private final String findings;

    /**
     * Creates a new {@code LabelAnnotationJson}.
     *
     * @param patientID      (Required) The ID of the patient to which this annotation is associated
     * @param seriesUID      (Required) The series instance UID of the DICOM data to which this annotation is associated
     * @param imageSetFormat (Required) The data format of the image set to which this annotation is associated
     * @param annotationID   (Required) The database assigned number that uniquely identifies this annotation
     * @param annotationType (Required) This annotation's type
     * @param geClasses      (Required) The GE classes that describe this label annotation
     * @param indication     (Optional) An indication associated with the provided GE classes
     * @param findings       (Optional) General notes associated with the provided GE classes
     */
    public LabelAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, String annotationType, List<GEClass> geClasses, String indication, String findings) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType);
        this.geClasses = Collections.unmodifiableList(Objects.requireNonNull(geClasses));
        this.indication = indication;
        this.findings = findings;
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public List<GEClass> getGeClasses() {
        return Collections.unmodifiableList(geClasses);
    }

    public String getIndication() {
        return indication;
    }

    public String getFindings() {
        return findings;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof LabelAnnotationJson)) return false;

        LabelAnnotationJson that = (LabelAnnotationJson) o;

        if (!that.canEqual(this)) return false;
        if (!super.equals(that)) return false;
        if (!getGeClasses().equals(that.getGeClasses())) return false;
        if ((getIndication() != null) ? (!getIndication().equals(that.getIndication())) : (that.getIndication() != null))
            return false;
        return (getFindings() != null) ? (getFindings().equals(that.getFindings())) : (that.getFindings() == null);
    }

    @Override
    public final int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getGeClasses().hashCode();
        result = 31 * result + (getIndication() != null ? getIndication().hashCode() : 0);
        result = 31 * result + (getFindings() != null ? getFindings().hashCode() : 0);
        return result;
    }

    @Override
    public final boolean canEqual(Object other) {
        return (other instanceof LabelAnnotationJson);
    }
}
