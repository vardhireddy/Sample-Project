/*
 * Upload.java
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

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.JsonDateSerializer;
import com.gehc.ai.app.datacatalog.filters.LocalDateTimeAttributeConverter;

public class Upload {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
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

	public Object getDataType() {
		return dataType;
	}

	public void setDataType(Object dataType) {
		this.dataType = dataType;
	}

	public String getDataUsage() {
		return dataUsage;
	}

	public void setDataUsage(String dataUsage) {
		this.dataUsage = dataUsage;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public Object getSummary() {
		return summary;
	}

	public void setSummary(Object summary) {
		this.summary = summary;
	}

	public Object getTags() {
		return tags;
	}

	public void setTags(Object tags) {
		this.tags = tags;
	}

	public Object getStatus() {
		return status;
	}

	public void setStatus(Object status) {
		this.status = status;
	}

	public String getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}

	public LocalDateTime getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}

	@Column(name = "schema_version")
	@Size(max=50)
	private String schemaVersion;
	/**
	 * The organization who owns the data.
	 */
	@Column(name = "org_id")
	@Size(min=0, max=255)
	private String orgId;
	
	@Convert(converter = JsonConverter.class)
	private Object dataType; // NOSONAR
	
	@Size(min=0, max=50)
	@NotNull
	private String dataUsage;
	
	private Long contractId;
	
	/**
	 * Space uuid in S3.
	 */
	@Column(name = "space_id")
	@Size(min=0, max=255)
	private String spaceId;
	
	@Convert(converter = JsonConverter.class)
	private Object summary; // NOSONAR
	
	@Convert(converter = JsonConverter.class)
	private Object tags; // NOSONAR
	
	@Convert(converter = JsonConverter.class)
	private Object status; // NOSONAR
	
	/**
	 * An identifier for the one who uploaded the data. This allows to query for
	 * the data uploaded by a specific person.
	 */
	@Column(name = "upload_by")
	@Size(min=0, max=255)
	private String uploadBy;
	
	/**
	 * Date data was uploaded into database. Should be left to database to
	 * provide.
	 */
	@Column(name = "upload_date")
	@JsonProperty(access = Access.READ_ONLY)
	@JsonSerialize(using=JsonDateSerializer.class)
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime uploadDate;
	

	

}
