/*
 * TargetData.java
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

import com.fasterxml.jackson.annotation.JsonProperty;

public class TargetData {
	@JsonProperty("gtMask")
	public String gtMask;
	
	@JsonProperty("img")
	public String img;
	
	@JsonProperty("patientId")
	public String patientId;
}
