/*
 * AnnotationProperties.java
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

import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gehc.ai.app.datacatalog.filters.JsonConverter;

@Entity
public class AnnotationProperties {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="schema_version")
    private String schemaVersion;
    /**
     * The organization who owns the data. 
     */
    @Column(name="org_id")
    @Size(max=255)
    @Pattern(regexp = UUID)
    private String orgId;
    
    @Column(name="resource_name")
    @Size(max=500)
    private String resourceName;
    public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
     * Flexible JSON object to store classes
     */
    @Convert(converter = JsonConverter.class)
    private Object classes;
    
    @Column(name="created_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    
    @Column(name="created_by")
    @Size(max=200)
    private String createdBy;

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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Object getClasses() {
		return classes;
	}

	public void setClasses(Object classes) {
		this.classes = classes;
	}

	public Date getCreatedDate() {
		return new Date(createdDate.getTime());
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = new Date(createdDate.getTime());
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public AnnotationProperties() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
