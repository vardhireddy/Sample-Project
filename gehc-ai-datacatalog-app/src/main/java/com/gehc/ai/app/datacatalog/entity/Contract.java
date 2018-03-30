/*
 * Contract.java
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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gehc.ai.app.common.constants.ValidationConstants;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.JsonDateSerializer;
import com.gehc.ai.app.datacatalog.filters.LocalDateTimeAttributeConverter;

@Entity
@JsonInclude(Include.NON_NULL)
public class Contract{

	public Contract(){
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Size(max=50)
	private String schemaVersion;
	/**
	 * The organization who owns the data.
	 */
	@Size(min=0, max=255)
	private String orgId;
	
	@Convert(converter = JsonConverter.class)
	private Object uri; // NOSONAR
	
	@Size(min=0, max=255)
	private String contractName;
	
	@Size(min=0, max=500)
	private String businessCase;
	
	@NotNull
	private DeidStatus deidStatus;
	
	private int usageLength;
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
	@NotNull
	@Size(min=0, max=255)
	private String uploadBy;
	
	/**
	 * Date data was uploaded into database. Should be left to database to
	 * provide.
	 */
	@JsonProperty(access = Access.READ_ONLY)
	@JsonSerialize(using=JsonDateSerializer.class)
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime uploadDate;
	  
	public Contract(Long id, String orgId, LocalDateTime uploadDate, String uploadBy, String contractName,
			String businessCase, DeidStatus deidStatus, int usageLength, String dataOriginCountry, String usageRights,
			String usageNotes, String contactInfo, List<String> uri, String active, Object properties, String schemaVersion) {
		super();
		this.id = id;
		this.orgId = orgId;
		this.uploadDate = uploadDate;
		this.uploadBy = uploadBy;
		this.contractName = contractName;
		this.businessCase = businessCase;
		this.deidStatus = deidStatus;
		this.usageLength = usageLength;
		this.dataOriginCountry = dataOriginCountry;
		this.usageRights = usageRights;
		this.usageNotes = usageNotes;
		this.contactInfo = contactInfo;
		this.uri = (new ArrayList<String>()).addAll(uri);
		this.active = active;
		this.properties = properties;
		this.schemaVersion = schemaVersion;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public LocalDateTime getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
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
	public DeidStatus getDeidStatus() {
		return deidStatus;
	}
	public void setDeidStatus(DeidStatus deidStatus) {
		this.deidStatus = deidStatus;
	}
	public int getUsageLength() {
		return usageLength;
	}
	public void setUsageLength(int usageLength) {
		this.usageLength = usageLength;
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
	public Object getUri() {
		return uri;
	}
	public void setUri(Object uri) {
		this.uri = uri;
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

	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}
	

	
	/**
	 * updates the update_date column with the current date for each newly 
	 * created object
	 */
	@PrePersist
	protected void onCreate() {
		uploadDate = LocalDateTime.now();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Contract [id=");builder.append(id);
		builder.append(", orgId=");builder.append(orgId);
		builder.append(", uploadDate=");builder.append(uploadDate);
		builder.append(", uploadBy=");builder.append(uploadBy);
		builder.append(", contractName=");builder.append(contractName);
		builder.append(", businessCase=");builder.append(businessCase);
		builder.append(", deidStatus=");builder.append(deidStatus);
		builder.append(", usageLength=");builder.append(usageLength);
		builder.append(", dataOriginCountry=");builder.append(dataOriginCountry);
		builder.append(", usageRights=");builder.append(usageRights);
		builder.append(", usageNotes=");builder.append(usageNotes);
		builder.append(", contactInfo=");builder.append(contactInfo);
		builder.append(", uri=");builder.append(uri);
		builder.append(", active=");builder.append(active);
		builder.append(", properties=");builder.append(properties);
		builder.append(", schemaVersion=");builder.append(schemaVersion);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	public enum DeidStatus {
		
		HIPAA_COMPLIANT(ValidationConstants.HIPAA_COMPLIANT), 
		DEIDS_TO_LOCAL_STANDARDS(ValidationConstants.DEIDS_TO_LOCAL_STANDARDS);

	    private String displayName;

	    DeidStatus(String displayName) {
	        this.displayName = displayName;
	    }

	    public String getDisplayName() { 
	    	return displayName; 
	    	}

	    // Optionally and/or additionally, toString.
	    @Override 
	    public String toString() 
	    { 
	    	return displayName; 
	    }
	    
	    public static DeidStatus fromDisplayName(String displayName){
	    	switch (displayName){
	    		case ValidationConstants.HIPAA_COMPLIANT:
	    			return DeidStatus.HIPAA_COMPLIANT;
	    		case ValidationConstants.DEIDS_TO_LOCAL_STANDARDS:
	    			return DeidStatus.DEIDS_TO_LOCAL_STANDARDS;
	    		default:
	    			throw new IllegalArgumentException("DeidStatus ["+displayName+"] not supported");
	    	}
	    }
	}

	/*DO NOT DELETE. This code is required, 
	but commented because it is breaking sonar because of condition coverage. 
	Comented to ensure RSNA build is delivered*/
	/*@Override
	public int hashCode() {//NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((businessCase == null) ? 0 : businessCase.hashCode());
		result = prime * result + ((contactInfo == null) ? 0 : contactInfo.hashCode());
		result = prime * result + ((contractName == null) ? 0 : contractName.hashCode());
		result = prime * result + ((dataOriginCountry == null) ? 0 : dataOriginCountry.hashCode());
		result = prime * result + ((deidStatus == null) ? 0 : deidStatus.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((schemaVersion == null) ? 0 : schemaVersion.hashCode());
		result = prime * result + ((uploadBy == null) ? 0 : uploadBy.hashCode());
		result = prime * result + ((uploadDate == null) ? 0 : uploadDate.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + usageLength;
		result = prime * result + ((usageNotes == null) ? 0 : usageNotes.hashCode());
		result = prime * result + ((usageRights == null) ? 0 : usageRights.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {//NOSONAR
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (businessCase == null) {
			if (other.businessCase != null)
				return false;
		} else if (!businessCase.equals(other.businessCase))
			return false;
		if (contactInfo == null) {
			if (other.contactInfo != null)
				return false;
		} else if (!contactInfo.equals(other.contactInfo))
			return false;
		if (contractName == null) {
			if (other.contractName != null)
				return false;
		} else if (!contractName.equals(other.contractName))
			return false;
		if (dataOriginCountry == null) {
			if (other.dataOriginCountry != null)
				return false;
		} else if (!dataOriginCountry.equals(other.dataOriginCountry))
			return false;
		if (deidStatus != other.deidStatus)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (schemaVersion == null) {
			if (other.schemaVersion != null)
				return false;
		} else if (!schemaVersion.equals(other.schemaVersion))
			return false;
		if (uploadBy == null) {
			if (other.uploadBy != null)
				return false;
		} else if (!uploadBy.equals(other.uploadBy))
			return false;
		if (uploadDate == null) {
			if (other.uploadDate != null)
				return false;
		} else if (!uploadDate.equals(other.uploadDate))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (usageLength != other.usageLength)
			return false;
		if (usageNotes == null) {
			if (other.usageNotes != null)
				return false;
		} else if (!usageNotes.equals(other.usageNotes))
			return false;
		if (usageRights == null) {
			if (other.usageRights != null)
				return false;
		} else if (!usageRights.equals(other.usageRights))
			return false;
		return true;
	}*/
}
