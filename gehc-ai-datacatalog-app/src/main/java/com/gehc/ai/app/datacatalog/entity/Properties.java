/*
 * Properties.java
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * This class holds the DataCollection Properties
 * 
 * This is an Auto-generated Class from http://www.jsonschema2pojo.org/
 * Make sure to add/generated this class again if any changes are required for the model
 *  
 * @author Madhu Y (305024964)
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "annotationStatus", "annotationPropertiesTemplate" })
public class Properties implements Serializable {

	private final static long serialVersionUID = 4946996490881104109L;

	@JsonProperty("annotationStatus")
	private List<AnnotationStatus> annotationStatus;
	@JsonProperty("annotationPropertiesTemplate")
	private String annotationPropertiesTemplate;

	@JsonProperty("annotationStatus")
	public List<AnnotationStatus> getAnnotationStatus() {
		return annotationStatus;
	}

	@JsonProperty("annotationStatus")
	public void setAnnotationStatus(List<AnnotationStatus> annotationStatus) {
		this.annotationStatus = annotationStatus;
	}

	@JsonProperty("annotationPropertiesTemplate")
	public String getAnnotationPropertiesTemplate() {
		return annotationPropertiesTemplate;
	}

	@JsonProperty("annotationPropertiesTemplate")
	public void setAnnotationPropertiesTemplate(String annotationPropertiesTemplate) {
		this.annotationPropertiesTemplate = annotationPropertiesTemplate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("annotationStatus", annotationStatus)
				.append("annotationPropertiesTemplate", annotationPropertiesTemplate).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(annotationStatus).append(annotationPropertiesTemplate).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other == null){
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != this.getClass()) {
			return false;
		}
		Properties rhs = ((Properties) other);
		return new EqualsBuilder().append(annotationStatus, rhs.annotationStatus)
				.append(annotationPropertiesTemplate, rhs.annotationPropertiesTemplate).isEquals();
	}

}