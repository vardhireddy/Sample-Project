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
		private String seriesInstanceUid;
		private Long annotationId;
	    private String type;
	    private String objectName;
	    private Object data;
	    private Object geClass;
		public String getPatientId() {
			return patientId;
		}
		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}
		public String getSeriesInstanceUid() {
			return seriesInstanceUid;
		}
		public void setSeriesInstanceUid(String seriesInstanceUid) {
			this.seriesInstanceUid = seriesInstanceUid;
		}
		public Long getAnnotationId() {
			return annotationId;
		}
		public void setAnnotationId(Long annotationId) {
			this.annotationId = annotationId;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
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
			this.seriesInstanceUid = seriesInstanceUid;
			this.annotationId = annotationId;
			this.type = type;
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
				", seriesInstanceUid='" + seriesInstanceUid + '\'' +
				", annotationId='" + annotationId + '\'' +
				", type='" + type + '\'' +
				", objectName='" + objectName + '\'' +
				", data='" + data + '\'' +
				", geClass='" + geClass + '\'' +
				'}';
	}
}
