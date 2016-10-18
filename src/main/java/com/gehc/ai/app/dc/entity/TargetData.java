package com.gehc.ai.app.dc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TargetData {
	@JsonProperty("gtMask")
	public String gtMask;
	
	@JsonProperty("img")
	public String img;
	
	@JsonProperty("patientId")
	public String patientId;
}
