/*
 * AnnotationDetails.java
 *
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.entity;

public class AnnotationDetails {
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////
    //
    // Properties for enabling annotation-to-imageset association //
    //
    ////////////////////////////////////////////////////////////////

    private String patientId;
    private String seriesUID;

    ////////////////////////////////////////////////////////////////////////
    //
    // Properties for enabling identifying and classifying any annotation //
    //
    ////////////////////////////////////////////////////////////////////////

    private String imageSetFormat;
    private Long annotationId;
    private String annotationType;

    ///////////////////////////////////////////////////////////////////////
    //
    // Properties for describing the annotation-to-instances association //
    //
    ///////////////////////////////////////////////////////////////////////

    private Object instances;

    //////////////////////////////////////////////////
    //
    // Properties for describing a label annotation //
    //
    //////////////////////////////////////////////////

    private GEClass geClass;
    private GEClass geClass1;
    private GEClass geClass2;
    private GEClass geClass3;
    private GEClass geClass4;
    private GEClass geClass5;
    private GEClass geClass6;
    private GEClass geClass7;
    private GEClass geClass8;
    private GEClass geClass9;
    private GEClass geClass10;
    private String indication;
    private String findings;

    //////////////////////////////////////
    //
    // Properties for describing an ROI //
    //
    //////////////////////////////////////

    private String name;
    private Object data;
    private String coordSys;

    //////////////////////////////////////
    //
    // Properties for describing a mask //
    //
    //////////////////////////////////////

    private String maskURI;
    private Object maskOrigin;
    private String maskFormat;

    //////////////////
    //
    // Constructors //
    //
    //////////////////

    public AnnotationDetails() {
        super();
    }

    public AnnotationDetails(
            String patientId,
            String seriesUID,
            String imageSetFormat,
            Long annotationId,
            String annotationType,
            String name,
            String coordSys,
            String maskURI,
            Object maskOrigin,
            String maskFormat,
            String indication,
            String findings,
            Object data,
            Object instances,
            GEClass geClass,
            GEClass geClass1,
            GEClass geClass2,
            GEClass geClass3,
            GEClass geClass4,
            GEClass geClass5,
            GEClass geClass6,
            GEClass geClass7,
            GEClass geClass8,
            GEClass geClass9,
            GEClass geClass10
    ) {
        super();
        this.patientId = patientId;
        this.seriesUID = seriesUID;
        this.imageSetFormat = imageSetFormat;
        this.annotationId = annotationId;
        this.annotationType = annotationType;
        this.name = name;
        this.coordSys = coordSys;
        this.maskURI = maskURI;
        this.maskOrigin = maskOrigin;
        this.maskFormat = maskFormat;
        this.indication = indication;
        this.findings = findings;
        this.data = data;
        this.instances = instances;
        this.geClass = geClass;
        this.geClass1 = geClass1;
        this.geClass2 = geClass2;
        this.geClass3 = geClass3;
        this.geClass4 = geClass4;
        this.geClass5 = geClass5;
        this.geClass6 = geClass6;
        this.geClass7 = geClass7;
        this.geClass8 = geClass8;
        this.geClass9 = geClass9;
        this.geClass10 = geClass10;
    }

    /////////////////////////
    //
    // Getters and Setters //
    //
    /////////////////////////

    public String getSeriesUID() {
        return seriesUID;
    }

    public void setSeriesUID(String seriesUID) {
        this.seriesUID = seriesUID;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getImageSetFormat() {
        return this.imageSetFormat;
    }

    public void setImageSetFormat(String imageSetFormat) {
        this.imageSetFormat = imageSetFormat;
    }

    public Long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(Long annotationId) {
        this.annotationId = annotationId;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordSys() {
        return coordSys;
    }

    public void setCoordSys(String coordSys) {
        this.coordSys = coordSys;
    }

    public Object getMaskOrigin() {
        return maskOrigin;
    }

    public void setMaskOrigin(Object maskOrigin) {
        this.maskOrigin = maskOrigin;
    }

    public String getMaskURI() {
        return maskURI;
    }

    public void setMaskURI(String maskURI) {
        this.maskURI = maskURI;
    }

    public String getMaskFormat() {
        return maskFormat;
    }

    public void setMaskFormat(String maskFormat) {
        this.maskFormat = maskFormat;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public Object getGeClass1() {
        return geClass1;
    }

    public void setGeClass1(GEClass geClass1) {
        this.geClass1 = geClass1;
    }

    public Object getGeClass2() {
        return geClass2;
    }

    public void setGeClass2(GEClass geClass2) {
        this.geClass2 = geClass2;
    }

    public Object getGeClass3() {
        return geClass3;
    }

    public void setGeClass3(GEClass geClass3) {
        this.geClass3 = geClass3;
    }

    public Object getGeClass4() {
        return geClass4;
    }

    public void setGeClass4(GEClass geClass4) {
        this.geClass4 = geClass4;
    }

    public Object getGeClass5() {
        return geClass5;
    }

    public void setGeClass5(GEClass geClass5) {
        this.geClass5 = geClass5;
    }

    public Object getGeClass6() {
        return geClass6;
    }

    public void setGeClass6(GEClass geClass6) {
        this.geClass6 = geClass6;
    }

    public Object getGeClass7() {
        return geClass7;
    }

    public void setGeClass7(GEClass geClass7) {
        this.geClass7 = geClass7;
    }

    public Object getGeClass8() {
        return geClass8;
    }

    public void setGeClass8(GEClass geClass8) {
        this.geClass8 = geClass8;
    }

    public Object getGeClass9() {
        return geClass9;
    }

    public void setGeClass9(GEClass geClass9) {
        this.geClass9 = geClass9;
    }

    public Object getGeClass10() {
        return geClass10;
    }

    public void setGeClass10(GEClass geClass10) {
        this.geClass10 = geClass10;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getGeClass() {
        return geClass;
    }

    public void setGeClass(GEClass geClass) {
        this.geClass = geClass;
    }

    public Object getInstances() {
        return instances;
    }

    public void setInstances(Object instances) {
        this.instances = instances;
    }

    @Override
    public String toString() {
        return "AnnotationDetails [patientId=" + patientId + ", seriesUID=" + seriesUID + ", annotationId="
                + annotationId + ", annotationType=" + annotationType + ", name=" + name + ", coordSys=" + coordSys
                + ", maskURI=" + maskURI + ", maskOrigin=" + maskOrigin + ", maskFormat=" + maskFormat
                + ", indication=" + indication + ", findings=" + findings + ", data=" + data + ", instances="
                + instances + ", geClass=" + geClass + ", geClass1=" + geClass1 + ", geClass2=" + geClass2
                + ", geClass3=" + geClass3 + ", geClass4=" + geClass4 + ", geClass5=" + geClass5 + ", geClass6="
                + geClass6 + ", geClass7=" + geClass7 + ", geClass8=" + geClass8 + ", geClass9=" + geClass9
                + ", geClass10=" + geClass10 + "]";
    }


}
