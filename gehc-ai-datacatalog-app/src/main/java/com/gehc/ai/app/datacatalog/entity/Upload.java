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

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.ListOfStringConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public final class Upload {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "schema_version")
	@Size(min=2, max=50)
    @NotNull
	private String schemaVersion;

	/**
	 * The organization who owns the data.
	 */
	@Column(name = "org_id")
	@Size(min=1, max=255)
    @NotNull
	private String orgId;

    @NotNull
    @Column(name = "data_type")
    @Convert(converter = ListOfStringConverter.class)
	private List<String> dataType;

	@NotNull
    @Column(name = "contract_id")
	private Long contractId;
	
	/**
	 * Space uuid in S3.
	 */
	@Column(name = "space_id")
	@Size(min=1, max=255)
	private String spaceId;

    @Convert(converter = ListOfStringConverter.class)
	private List<String> summary;
	
	@Convert(converter = JsonConverter.class)
	private Map<String,String> tags;
	
	@Convert(converter = JsonConverter.class)
	private Map<String,String> status;
	
	/**
	 * An identifier for the one who uploaded the data. This allows to query for
	 * the data uploaded by a specific person.
	 */
	@Column(name = "upload_by")
	@Size(min=0, max=255)
	private String uploadBy;

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="upload_date")
    @CreationTimestamp
	private Timestamp uploadDate;

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="last_modified")
    @UpdateTimestamp
    private Timestamp lastModified;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Upload upload = (Upload) o;

        if (getId() != null ? !getId().equals(upload.getId()) : upload.getId() != null) return false;
        if (!getSchemaVersion().equals(upload.getSchemaVersion())) return false;
        if (!getOrgId().equals(upload.getOrgId())) return false;
        if (!getDataType().equals(upload.getDataType())) return false;
        if (!getContractId().equals(upload.getContractId())) return false;
        if (!getSpaceId().equals(upload.getSpaceId())) return false;
        if (getSummary() != null ? !getSummary().equals(upload.getSummary()) : upload.getSummary() != null)
            return false;
        if (!getTags().equals(upload.getTags())) return false;
        if (getStatus() != null ? !getStatus().equals(upload.getStatus()) : upload.getStatus() != null) return false;
        if (!getUploadBy().equals(upload.getUploadBy())) return false;
        if (getUploadDate() != null ? !getUploadDate().equals(upload.getUploadDate()) : upload.getUploadDate() != null)
            return false;
        return getLastModified() != null ? getLastModified().equals(upload.getLastModified()) : upload.getLastModified() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getSchemaVersion().hashCode();
        result = 31 * result + getOrgId().hashCode();
        result = 31 * result + getDataType().hashCode();
        result = 31 * result + getContractId().hashCode();
        result = 31 * result + getSpaceId().hashCode();
        result = 31 * result + (getSummary() != null ? getSummary().hashCode() : 0);
        result = 31 * result + getTags().hashCode();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + getUploadBy().hashCode();
        result = 31 * result + (getUploadDate() != null ? getUploadDate().hashCode() : 0);
        result = 31 * result + (getLastModified() != null ? getLastModified().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "id=" + id +
                ", schemaVersion='" + schemaVersion + '\'' +
                ", orgId='" + orgId + '\'' +
                ", dataType=" + dataType +
                ", contractId=" + contractId +
                ", spaceId='" + spaceId + '\'' +
                ", summary=" + summary +
                ", tags=" + tags +
                ", status=" + status +
                ", uploadBy='" + uploadBy + '\'' +
                ", uploadDate=" + uploadDate +
                ", lastModified=" + lastModified +
                '}';
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<String> getDataType() {
        return dataType;
    }

    public void setDataType(List<String> dataType) {
        this.dataType = dataType;
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

    public List<String> getSummary() {
        return summary;
    }

    public void setSummary(List<String> summary) {
        this.summary = summary;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }
}