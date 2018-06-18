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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.JsonDateSerializer;
import com.gehc.ai.app.datacatalog.filters.ListOfStringConverter;
import com.gehc.ai.app.datacatalog.filters.LocalDateTimeAttributeConverter;
import lombok.Getter;
import lombok.Setter;
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
    //@NotNull
	private String schemaVersion;

	/**
	 * The organization who owns the data.
	 */
	@Column(name = "org_id")
	@Size(min=1, max=255)
    //@NotNull
	private String orgId;

    //@NotNull
    @Column(name = "data_type")
    @Convert(converter = ListOfStringConverter.class)
	private List<String> dataType;

	//@NotNull
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

        if (id != null ? !id.equals(upload.id) : upload.id != null) return false;
        if (!schemaVersion.equals(upload.schemaVersion)) return false;
        if (!orgId.equals(upload.orgId)) return false;
        if (!dataType.equals(upload.dataType)) return false;
        if (!contractId.equals(upload.contractId)) return false;
        if (!spaceId.equals(upload.spaceId)) return false;
        if (summary != null ? !summary.equals(upload.summary) : upload.summary != null) return false;
        if (!tags.equals(upload.tags)) return false;
        if (status != null ? !status.equals(upload.status) : upload.status != null) return false;
 //       if (!uploadBy.equals(upload.uploadBy)) return false;
//        if (!uploadDate.equals(upload.uploadDate)) return false;
//        return lastModified.equals(upload.lastModified);
        return   (uploadBy.equals(upload.uploadBy));
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + schemaVersion.hashCode();
        result = 31 * result + orgId.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + contractId.hashCode();
        result = 31 * result + spaceId.hashCode();
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + tags.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + uploadBy.hashCode();
//        result = 31 * result + uploadDate.hashCode();
//        result = 31 * result + lastModified.hashCode();
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