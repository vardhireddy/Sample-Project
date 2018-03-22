/*
 *  RoiAnnotationJson.java
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

/**
 * {@code RoiAnnotationJson} is a bean representing an annotation that is a generic ROI (e.g. point, line, rectangle, ellipse, polygon or contour).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public abstract class RoiAnnotationJson extends AnnotationJson {

    private String coordSys;

    private String localID;

    private String name;

    public RoiAnnotationJson(String patientID, String seriesUID, String imageSetFormat, Long annotationID, String annotationType, String coordSys, String localID, String name) {
        super(patientID, seriesUID, imageSetFormat, annotationID, annotationType);
        this.coordSys = coordSys;
        this.localID = localID;
        this.name = name;
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public String getCoordSys() {
        return coordSys;
    }

    public void setCoordSys(String coordSys) {
        this.coordSys = coordSys;
    }

    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        // Auto-generated
        if (this == o) return true;
        if (!(o instanceof RoiAnnotationJson)) return false;
        if (!super.equals(o)) return false;

        RoiAnnotationJson that = (RoiAnnotationJson) o;

        if (!getCoordSys().equals(that.getCoordSys())) return false;
        if (!getLocalID().equals(that.getLocalID())) return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        // Auto-generated
        int result = super.hashCode();
        result = 31 * result + getCoordSys().hashCode();
        result = 31 * result + getLocalID().hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
