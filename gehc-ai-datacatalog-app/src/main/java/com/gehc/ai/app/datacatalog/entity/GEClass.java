/*
 * GEClass.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.entity;

public class GEClass {
	private String name;
	private String value;
	private String patient_outcome;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the patient_outcome
	 */
	public String getPatient_outcome() {
		return patient_outcome;
	}
	/**
	 * @param patient_outcome the patient_outcome to set
	 */
	public void setPatient_outcome(String patient_outcome) {
		this.patient_outcome = patient_outcome;
	}

}
