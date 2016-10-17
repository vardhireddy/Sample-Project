package com.gehc.ai.app.dc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TargetData {
	@JsonProperty("gtMask")
	public String gtMask;
	
	public String getGtMask() {
    	return (gtMask!=null)?gtMask.replaceAll("^\"|\"$", ""):null;
	}

	public void setGtMask(String gtMask) {
		this.gtMask = gtMask;
	}

	public String getImg() {
    	return (img!=null)?img.replaceAll("^\"|\"$", ""):null;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@JsonProperty("img")
	public String img;
}
