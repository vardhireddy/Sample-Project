/*
 * GeClass.java
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
 * This class holds the GeClass for Filters
 * 
 * This is an Auto-generated Class from http://www.jsonschema2pojo.org/
 * Make sure to add/generated this class again if any changes are required for the model
 *  
 * @author Madhu Y (305024964)
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "value" })
public class GeClass implements Serializable {

	private final static long serialVersionUID = -3123449080261133331L;

	@JsonProperty("name")
	private String name;
	@JsonProperty("value")
	private String value;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("value", value).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(value).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof GeClass) == false) {
			return false;
		}
		GeClass rhs = ((GeClass) other);
		return new EqualsBuilder().append(name, rhs.name).append(value, rhs.value).isEquals();
	}

}
