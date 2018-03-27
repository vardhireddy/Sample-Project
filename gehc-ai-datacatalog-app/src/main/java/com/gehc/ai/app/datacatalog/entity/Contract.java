package com.gehc.ai.app.datacatalog.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.JsonDateSerializer;
import com.gehc.ai.app.datacatalog.filters.LocalDateTimeAttributeConverter;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "contract")
public class Contract {
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

	public Object getUri() {
		return uri;
	}

	public void setUri(Object uri) {
		this.uri = uri;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getBusinessCase() {
		return businessCase;
	}

	public void setBusinessCase(String businessCase) {
		this.businessCase = businessCase;
	}

	public String getDeidStatus() {
		return deidStatus;
	}

	public void setDeidStatus(String deidStatus) {
		this.deidStatus = deidStatus;
	}

	public String getDataOriginCountry() {
		return dataOriginCountry;
	}

	public void setDataOriginCountry(String dataOriginCountry) {
		this.dataOriginCountry = dataOriginCountry;
	}

	public String getUsageRights() {
		return usageRights;
	}

	public void setUsageRights(String usageRights) {
		this.usageRights = usageRights;
	}

	public String getUsageNotes() {
		return usageNotes;
	}

	public void setUsageNotes(String usageNotes) {
		this.usageNotes = usageNotes;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Object getProperties() {
		return properties;
	}

	public void setProperties(Object properties) {
		this.properties = properties;
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
	private Object uri; // NOSONAR
	
	@Size(min=0, max=255)
	private String contractName;
	
	@Size(min=0, max=500)
	private String businessCase;
	
	@Size(min=0, max=255)
	private String deidStatus;
	
	@Size(min=0, max=255)
	private String dataOriginCountry;
	
	@Size(min=0, max=255)
	private String usageRights;
	
	@Size(min=0, max=500)
	private String usageNotes;
	
	@Size(min=0, max=255)
	private String contactInfo;
	
	@Size(min=0, max=50)
	private String active;
	
	@Convert(converter = JsonConverter.class)
	private Object properties; // NOSONAR
	
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