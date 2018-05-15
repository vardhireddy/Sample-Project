/*
 * DataCollectionsCreateRequest.java
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

/**
 * This class represents the Request for creating DataCollection for an
 * Experiment/Annotation/Inference
 * 
 * @author Madhu Y (305024964)
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "numDataCollections", "dataSet" })
public class DataCollectionsCreateRequest {

	@JsonProperty("numDataCollections")
	private Integer numDataCollections;

	@JsonProperty("dataSet")
	private DataSet dataSet;

	@JsonProperty("numDataCollections")
	public Integer getNumDataCollections() {
		return numDataCollections;
	}

	@JsonProperty("numDataCollections")
	public void setNumDataCollections(Integer numDataCollections) {
		this.numDataCollections = numDataCollections;
	}

	@JsonProperty("dataSet")
	public DataSet getDataSet() {
		return dataSet;
	}

	@JsonProperty("dataSet")
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
}
