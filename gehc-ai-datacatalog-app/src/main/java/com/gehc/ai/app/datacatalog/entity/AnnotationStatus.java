/*
 * AnnotationStatus.java
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class holds the AnnotationStatus
 * 
 * This is an Auto-generated Class from http://www.jsonschema2pojo.org/
 * Make sure to add/generated this class again if any changes are required for the model
 *  
 * @author Madhu Y (305024964)
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "status", "imageSetId" })
public class AnnotationStatus implements Serializable {

	private final static long serialVersionUID = -8807390757151361523L;

	@JsonProperty("status")
	private String status;
	@JsonProperty("imageSetId")
	private Long imageSetId;

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("imageSetId")
	public Long getImageSetId() {
		return imageSetId;
	}

	@JsonProperty("imageSetId")
	public void setImageSetId(Long imageSetId) {
		this.imageSetId = imageSetId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("status", status).append("imageSetId", imageSetId).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(imageSetId).append(status).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof AnnotationStatus) == false) {
			return false;
		}
		AnnotationStatus rhs = ((AnnotationStatus) other);
		return new EqualsBuilder().append(imageSetId, rhs.imageSetId).append(status, rhs.status).isEquals();
	}

}
