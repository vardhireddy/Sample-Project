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
		private String name;
	    public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
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
	    private Object geClass5;
	    public Object getGeClass5() {
			return geClass5;
		}
		public void setGeClass5(Object geClass5) {
			this.geClass5 = geClass5;
		}
		public Object getGeClass6() {
			return geClass6;
		}
		public void setGeClass6(Object geClass6) {
			this.geClass6 = geClass6;
		}
		public Object getGeClass7() {
			return geClass7;
		}
		public void setGeClass7(Object geClass7) {
			this.geClass7 = geClass7;
		}
		public Object getGeClass8() {
			return geClass8;
		}
		public void setGeClass8(Object geClass8) {
			this.geClass8 = geClass8;
		}
		public Object getGeClass9() {
			return geClass9;
		}
		public void setGeClass9(Object geClass9) {
			this.geClass9 = geClass9;
		}
		public Object getGeClass10() {
			return geClass10;
		}
		public void setGeClass10(Object geClass10) {
			this.geClass10 = geClass10;
		}
		private Object geClass6;
	    private Object geClass7;
	    private Object geClass8;
	    private Object geClass9;
	    private Object geClass10;
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
			return "AnnotationDetails [patientId=" + patientId + ", seriesUID=" + seriesUID + ", annotationId="
					+ annotationId + ", annotationType=" + annotationType + ", name=" + name + ", coordSys=" + coordSys
					+ ", maskURI=" + maskURI + ", maskOrigin=" + maskOrigin + ", maskFormat=" + maskFormat
					+ ", indication=" + indication + ", findings=" + findings + ", data=" + data + ", instances="
					+ instances + ", geClass=" + geClass + ", geClass1=" + geClass1 + ", geClass2=" + geClass2
					+ ", geClass3=" + geClass3 + ", geClass4=" + geClass4 + ", geClass5=" + geClass5 + ", geClass6="
					+ geClass6 + ", geClass7=" + geClass7 + ", geClass8=" + geClass8 + ", geClass9=" + geClass9
					+ ", geClass10=" + geClass10 + "]";
		}
		public AnnotationDetails(String patientId, String seriesUID, Long annotationId, String annotationType,
				String name, String coordSys, String maskURI, Object maskOrigin, String maskFormat, String indication,
				String findings, Object data, Object instances, Object geClass, Object geClass1, Object geClass2,
				Object geClass3, Object geClass4, Object geClass5, Object geClass6, Object geClass7, Object geClass8,
				Object geClass9, Object geClass10) {
			super();
			this.patientId = patientId;
			this.seriesUID = seriesUID;
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
}
