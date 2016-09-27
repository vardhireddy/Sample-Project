/*
 * DataCollection.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.entity;

import java.util.Arrays;


/**
 * @author 212071558
 *
 */
public class DataCollection {

    private String id;
    private String schemaVersion;
    private String name;
    private String description;
    private String[] imageSets;
    private String createdDate;
    public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	private Creator creator;
    public Creator getCreator() {
		return creator;
	}
	public void setCreator(Creator creator) {
		this.creator = creator;
	}
	/**
     * @return the id
     */
    public String getId() {
        return (id==null)?id:id.replaceAll("^\"|\"$", "");
    }
    /**
     * @param id the id to set
     */
    public void setId( String id ) {
        this.id = id;
    }
    /**
     * @return the schemaVersion
     */
    public String getSchemaVersion() {
        return (schemaVersion==null)?schemaVersion:schemaVersion.replaceAll("^\"|\"$", "");
    }
    /**
     * @param schemaVersion the schemaVersion to set
     */
    public void setSchemaVersion( String schemaVersion ) {
        this.schemaVersion = schemaVersion;
    }
    /**
     * @return the name
     */
    public String getName() {
        return (name==null)?name:name.replaceAll("^\"|\"$", "");
    }
    /**
     * @param name the name to set
     */
    public void setName( String name ) {
        this.name = name;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return  (description==null)?description:description.replaceAll("^\"|\"$", "");
    }
    /**
     * @param description the description to set
     */
    public void setDescription( String description ) {
        this.description = description;
    }
    /**
     * @return the imageSets
     */
    public String[] getImageSets() {
        return imageSets;
    }
    /**
     * @param imageSets the imageSets to set
     */
    public void setImageSets( String[] imageSets ) {
        this.imageSets = imageSets;
    }
 

    public DataCollection(String id, String schemaVersion, String name,
			String description, String[] imageSets, String createdDate,
			Creator creator) {
		super();
		this.id = id;
		this.schemaVersion = schemaVersion;
		this.name = name;
		this.description = description;
		this.imageSets = imageSets;
		this.createdDate = createdDate;
		this.creator = creator;
	}
	/**
     * 
     */
    public DataCollection() {
        super();
        // TODO Auto-generated constructor stub
    }
	@Override
	public String toString() {
		return "DataCollection [id=" + id + ", schemaVersion=" + schemaVersion
				+ ", name=" + name + ", description=" + description
				+ ", imageSets=" + Arrays.toString(imageSets)
				+ ", createdDate=" + createdDate + ", creator=" + creator + "]";
	}
}
