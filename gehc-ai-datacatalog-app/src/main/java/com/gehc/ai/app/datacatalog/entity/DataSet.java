/*
 * DataSet.java
 *
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
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
import java.util.Map;
import javax.validation.constraints.NotNull;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;

import static com.gehc.ai.app.common.constants.ValidationConstants.DESCRIPTION;
import static com.gehc.ai.app.common.constants.ValidationConstants.ENTITY_NAME;
import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;
import static com.gehc.ai.app.common.constants.ValidationConstants.USER_NAME;
import static com.gehc.ai.app.common.constants.ValidationConstants.DATA_SET_TYPE;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "data_set")
public class DataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Column ( name = "schema_version" )
    @Size(max=50)
	@Pattern(regexp = ENTITY_NAME)
	private String schemaVersion;
    
	@Size(min=3, max=200)
	@Pattern(regexp = ENTITY_NAME)
	@NotNull
	private String name;

	//need to fix it to handle for null or empty string when pattern tag added
	@Size(min=0, max=500)
	@Pattern(regexp = DESCRIPTION)
	private String description;

	//@Column ( name = "image_sets" )
	//private String[] imageSets;
	@Column(name = "created_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private String createdDate;

	//private int imageSetsSize;
	@Size(min=0, max=50)
	@Pattern(regexp = DATA_SET_TYPE)
	@NotNull
    private String type;
	
    @Column ( name = "org_id" )
	@Size(min=0, max=255)
	@Pattern(regexp = UUID)
    private String orgId;

    @Column ( name = "created_by" )
    @Size(min=0, max=200)
	@Pattern(regexp = USER_NAME)
    private String createdBy;
    /**
     * Flexible JSON object to store properties of data collection
     */
    @Convert(converter = JsonConverter.class)
	@NotNull
    private Object properties; // NOSONAR
    
    public Object getProperties() {
		return properties;
	} // NOSONAR
	public void setProperties(Object properties) {
		this.properties = properties;
	} // NOSONAR

	public Object getImageSets() { // NOSONAR
		return imageSets; // NOSONAR
	}
	public void setImageSets(Object  imageSets) { // NOSONAR
		this.imageSets = imageSets; // NOSONAR
	}

	@Convert(converter = JsonConverter.class)
    @Column ( name = "image_sets" )
	@NotNull
    private Object  imageSets; // NOSONAR
	
    /**
     * Flexible JSON object to store properties of data collection
     */
    @Convert(converter = JsonConverter.class)
    private Object filters; // NOSONAR
    
	public Object getFilters() { // NOSONAR
		return filters; // NOSONAR
	}
	public void setFilters(Object filters) { // NOSONAR
		this.filters = filters; // NOSONAR
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSchemaVersion() {
		return schemaVersion;
	}
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
