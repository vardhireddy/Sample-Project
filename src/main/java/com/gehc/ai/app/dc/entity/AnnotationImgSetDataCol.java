/*
 * AnnotationImgSetDataCol.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.entity;


import java.util.HashMap;
import java.util.Map;

/**
 * @author 212367833
 *
 */

public class AnnotationImgSetDataCol {

	private String dcId;
	private String imId;
	private String annotationId;
	private String patientDbid;
	private String uri;
	private String annotationType;
	private Map<String, Object> annotationItem;
	private String annotatorId;
	private String annotationDate;

	public String getDcId() {
		return dcId;
	}

	public void setDcId(String dcId) {
		this.dcId = dcId;
	}

	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getAnnotationId() {
		return annotationId;
	}

	public void setAnnotationId(String annotationId) {
		this.annotationId = annotationId;
	}

	public String getPatientDbid() {
		return patientDbid;
	}

	public void setPatientDbid(String patientDbid) {
		this.patientDbid = patientDbid;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	public Map<String, Object> getAnnotationItem() {
		return this.annotationItem;
	}

	public void setAnnotationItem(HashMap<String, Object> annotationItem) {
		this.annotationItem = annotationItem;
	}

	public String getAnnotatorId() {
		return annotatorId;
	}

	public void setAnnotatorId(String annotatorId) {
		this.annotatorId = annotatorId;
	}

	public String getAnnotationDate() {
		return annotationDate;
	}

	public void setAnnotationDate(String annotationDate) {
		this.annotationDate = annotationDate;
	}

	@Override
	public String toString() {
		return "AnnotationImgSetDataCol{" +
				"dcId='" + dcId + '\'' +
				", imId='" + imId + '\'' +
				", annotationId='" + annotationId + '\'' +
				", patientDbid='" + patientDbid + '\'' +
				", annotationType='" + annotationType + '\'' +
				", annotationItem='" + annotationItem + '\'' +
				", annotatorId='" + annotatorId + '\'' +
				", annotationDate='" + annotationDate + '\'' +
				'}';
	}
}
