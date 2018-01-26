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
		private String patientId;
		private String seriesUID;
		public String getSeriesUID() {
			return seriesUID;
		}
		public void setSeriesUID(String seriesUID) {
			this.seriesUID = seriesUID;
		}
		private Long annotationId;
	    private String annotationType;
	    public String getAnnotationType() {
			return annotationType;
		}
		public void setAnnotationType(String annotationType) {
			this.annotationType = annotationType;
		}
		private String objectName;
	    private String coordSys;
	    private String maskURI;
	    private Object maskOrigin;
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
		private String maskFormat;
	    public String getCoordSys() {
			return coordSys;
		}
		public void setCoordSys(String coordSys) {
			this.coordSys = coordSys;
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
		private String indication;
	    private String findings;
	    private Object data;
	    private Object instances;
	    public Object getInstances() {
			return instances;
		}
		public void setInstances(Object instances) {
			this.instances = instances;
		}
		private Object geClass;
	    private Object geClass1;
	    public Object getGeClass1() {
			return geClass1;
		}
		public void setGeClass1(Object geClass1) {
			this.geClass1 = geClass1;
		}
		public Object getGeClass2() {
			return geClass2;
		}
		public void setGeClass2(Object geClass2) {
			this.geClass2 = geClass2;
		}
		public Object getGeClass3() {
			return geClass3;
		}
		public void setGeClass3(Object geClass3) {
			this.geClass3 = geClass3;
		}
		public Object getGeClass4() {
			return geClass4;
		}
		public void setGeClass4(Object geClass4) {
			this.geClass4 = geClass4;
		}
		private Object geClass2;
	    private Object geClass3;
	    private Object geClass4;
		public String getPatientId() {
			return patientId;
		}
		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}
		public Long getAnnotationId() {
			return annotationId;
		}
		public void setAnnotationId(Long annotationId) {
			this.annotationId = annotationId;
		}
		public String getObjectName() {
			return objectName;
		}
		public void setObjectName(String objectName) {
			this.objectName = objectName;
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
		public void setGeClass(Object geClass) {
			this.geClass = geClass;
		}
		public AnnotationDetails() {
			super();
			// TODO Auto-generated constructor stub
		}
	@Override
	public String toString() {
		return "AnnotationDetails{" +
				", patientId='" + patientId + '\'' +
				", seriesInstanceUid='" + seriesUID + '\'' +
				", annotationId='" + annotationId + '\'' +
				", type='" + annotationType + '\'' +
				", objectName='" + objectName + '\'' +
				", data='" + data + '\'' +
				", geClass='" + geClass + '\'' +
				'}';
	}
	public AnnotationDetails(String patientId, String seriesUID, Long annotationId, String annotationType,
			String objectName, String coordSys, String maskURI, Object maskOrigin, String maskFormat, String indication,
			String findings, Object data, Object instances, Object geClass, Object geClass1, Object geClass2,
			Object geClass3, Object geClass4) {
		super();
		this.patientId = patientId;
		this.seriesUID = seriesUID;
		this.annotationId = annotationId;
		this.annotationType = annotationType;
		this.objectName = objectName;
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
	}
	
}
