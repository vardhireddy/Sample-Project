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
		private Long annotationId;
	    private String annotationType;
	    private String objectName;
	    private String coordSys;
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
		public String getSeriesInstanceUid() {
			return seriesUID;
		}
		public void setSeriesInstanceUid(String seriesInstanceUid) {
			this.seriesUID = seriesInstanceUid;
		}
		public Long getAnnotationId() {
			return annotationId;
		}
		public void setAnnotationId(Long annotationId) {
			this.annotationId = annotationId;
		}
		public String getType() {
			return annotationType;
		}
		public void setType(String type) {
			this.annotationType = type;
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
		public AnnotationDetails(String patientId, String seriesInstanceUid, Long annotationId, String type,
				String objectName, Object data, Object geClass) {
			super();
			this.patientId = patientId;
			this.seriesUID = seriesInstanceUid;
			this.annotationId = annotationId;
			this.annotationType = type;
			this.objectName = objectName;
			this.data = data;
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
}
