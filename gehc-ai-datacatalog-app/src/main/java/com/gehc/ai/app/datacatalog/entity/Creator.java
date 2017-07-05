/*
 * Creator.java
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

import java.io.Serializable;

public class Creator implements Serializable {
	private static final long serialVersionUID = 1L;
    private String id;
    public String getId() {
		return (id==null)?id:id.replaceAll("^\"|\"$", "");
	}
	public void setId(String id) {
		this.id = id;
	}
	private String name;
	public String getName() {
		return (name==null)?name:name.replaceAll("^\"|\"$", "");
	}
	public void setName(String name) {
		this.name = name;
	}
	public Creator(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Creator() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Creator [id=" + id + ", name=" + name + "]";
	}	
}
