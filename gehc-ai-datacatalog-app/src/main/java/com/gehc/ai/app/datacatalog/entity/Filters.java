/*
 * Filters.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class holds the Filters that are applied on DataSet
 * 
 * This is an Auto-generated Class from http://www.jsonschema2pojo.org/
 * Make sure to add/generated this class again if any changes are required for the model
 *  
 * @author Madhu Y (305024964)
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "modality", "anatomy", "annotations", "dateFrom", "dateTo", "institution", "equipment",
		"dataFormat", "view", "ge-class" })
public class Filters implements Serializable {

	private final static long serialVersionUID = 1556079884821309586L;

	@JsonProperty("modality")
	private List<String> modality = null;
	@JsonProperty("anatomy")
	private List<String> anatomy = null;
	@JsonProperty("annotations")
	private List<String> annotations = null;
	@JsonProperty("dateFrom")
	private List<String> dateFrom = null;
	@JsonProperty("dateTo")
	private List<String> dateTo = null;
	@JsonProperty("institution")
	private List<String> institution = null;
	@JsonProperty("equipment")
	private List<String> equipment = null;
	@JsonProperty("dataFormat")
	private List<String> dataFormat = null;
	@JsonProperty("view")
	private List<String> view = null;
	@JsonProperty("ge-class")
	private List<GeClass> geClass = null;

	@JsonProperty("modality")
	public List<String> getModality() {
		return modality;
	}

	@JsonProperty("modality")
	public void setModality(List<String> modality) {
		this.modality = modality;
	}

	@JsonProperty("anatomy")
	public List<String> getAnatomy() {
		return anatomy;
	}

	@JsonProperty("anatomy")
	public void setAnatomy(List<String> anatomy) {
		this.anatomy = anatomy;
	}

	@JsonProperty("annotations")
	public List<String> getAnnotations() {
		return annotations;
	}

	@JsonProperty("annotations")
	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

	@JsonProperty("dateFrom")
	public List<String> getDateFrom() {
		return dateFrom;
	}

	@JsonProperty("dateFrom")
	public void setDateFrom(List<String> dateFrom) {
		this.dateFrom = dateFrom;
	}

	@JsonProperty("dateTo")
	public List<String> getDateTo() {
		return dateTo;
	}

	@JsonProperty("dateTo")
	public void setDateTo(List<String> dateTo) {
		this.dateTo = dateTo;
	}

	@JsonProperty("institution")
	public List<String> getInstitution() {
		return institution;
	}

	@JsonProperty("institution")
	public void setInstitution(List<String> institution) {
		this.institution = institution;
	}

	@JsonProperty("equipment")
	public List<String> getEquipment() {
		return equipment;
	}

	@JsonProperty("equipment")
	public void setEquipment(List<String> equipment) {
		this.equipment = equipment;
	}

	@JsonProperty("dataFormat")
	public List<String> getDataFormat() {
		return dataFormat;
	}

	@JsonProperty("dataFormat")
	public void setDataFormat(List<String> dataFormat) {
		this.dataFormat = dataFormat;
	}

	@JsonProperty("view")
	public List<String> getView() {
		return view;
	}

	@JsonProperty("view")
	public void setView(List<String> view) {
		this.view = view;
	}

	@JsonProperty("ge-class")
	public List<GeClass> getGeClass() {
		return geClass;
	}

	@JsonProperty("ge-class")
	public void setGeClass(List<GeClass> geClass) {
		this.geClass = geClass;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("modality", modality).append("anatomy", anatomy)
				.append("annotations", annotations).append("dateFrom", dateFrom).append("dateTo", dateTo)
				.append("institution", institution).append("equipment", equipment).append("dataFormat", dataFormat)
				.append("view", view).append("geClass", geClass).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(anatomy).append(dateTo).append(geClass).append(dataFormat).append(equipment)
				.append(view).append(annotations).append(modality).append(dateFrom).append(institution).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Filters) == false) {
			return false;
		}
		Filters rhs = ((Filters) other);
		return new EqualsBuilder().append(anatomy, rhs.anatomy).append(dateTo, rhs.dateTo).append(geClass, rhs.geClass)
				.append(dataFormat, rhs.dataFormat).append(equipment, rhs.equipment).append(view, rhs.view)
				.append(annotations, rhs.annotations).append(modality, rhs.modality).append(dateFrom, rhs.dateFrom)
				.append(institution, rhs.institution).isEquals();
	}

}