/*
 * Study.java
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

import static com.gehc.ai.app.common.constants.ValidationConstants.DESCRIPTION;
import static com.gehc.ai.app.common.constants.ValidationConstants.ENTITY_NAME;
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

/**
 * Created by 200014175 on 10/28/2016.
 */
@Entity
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) {this.id = id; }

    @Column(name="schema_version")
    @Size(max=50)
    @Pattern(regexp = ENTITY_NAME)
    private String schemaVersion;
    public String getSchemaVersion() {
        return schemaVersion;
    }
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * Patient table ID. Establishes a correlation with the patient table
     */
    @Column(name="patient_dbid")
    private Long patientDbId;
    public Long getPatientDbId() {
        return patientDbId;
    }
    public void setPatientDbId(Long patientDbId) {
        this.patientDbId = patientDbId;
    }
    
//    @ManyToOne
//    @JoinColumn(name = "patient_dbid", insertable =  false, updatable = false)
//    private Patient patient;
//    
//    public Patient getPatient() {
//		return patient;
//	}
//	public void setPatient(Patient patient) {
//		this.patient = patient;
//	}

	/**
     * DICOM tag (0020,000D) - study instance UID, expected to be globally unique
     */
    @Column (name="study_instance_uid")
    @Size(max=255)
    @Pattern(regexp = ENTITY_NAME)
    private String studyInstanceUid;
    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }
    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
    }

    /**
     * DICOM tag (0008,0020). Leaving date as string just as was obtained form DICOM, no attempt to parse.
     */
    @Column (name="study_date")
    @Size(max=255)
    @Pattern(regexp = ENTITY_NAME)
    private String studyDate;
    public String getStudyDate() {
        return studyDate;
    }
    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    /**
     * DICOM tag (0008,0030). Leaving time as string just as was obtained form DICOM, no attempt to parse.
     */
    @Column (name="study_time")
    @Size(max=255)
    @Pattern(regexp = ENTITY_NAME)
    private String studyTime;
    public String getStudyTime() {
        return studyTime;
    }
    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    /**
     * DICOM Study ID (0020,0010)
     */
    @Column (name="study_id")
    @Size(max=255)
    @Pattern(regexp = ENTITY_NAME)
    private String studyId;
    public String getStudyId() {
        return studyId;
    }
    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    /**
     * DICOM tag (0008,1030)
     */
    @Column (name="study_description")
    @Size(max=100)
    @Pattern(regexp = DESCRIPTION)
    private String studyDescription;
    public String getStudyDescription() {
        return studyDescription;
    }
    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    /**
     * DICOM tag (0008, 0090)
     */
    @Column (name="referring_physician")
    @Size(max=255)
    @Pattern(regexp = ENTITY_NAME)
    private String referringPhysician;
    public String getReferringPhysician() {
        return referringPhysician;
    }
    public void setReferringPhysician(String referringPhysician) {
        this.referringPhysician = referringPhysician;
    }

    /**
     * Obtained from the Cloud Object Store (COS)
     */
    @Column (name="study_url")
    @Size(max=500)
    private String studyUrl;
    public String getStudyUrl() {
        return studyUrl;
    }
    public void setStudyUrl(String studyUrl) {
        this.studyUrl = studyUrl;
    }

    /**
     * The organization who owns or uploads the data. This could be an entry in an org database
     */
    @Column(name="org_id")
    @Size(max=255)
    @Pattern(regexp = UUID)
    private String orgId;
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    //@Column(name="upload_date")
    //@JsonFormat(pattern="yyyyMMdd")
    @Column(name="upload_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date uploadDate;
    public Date getUploadDate()
    {
        return new Date(uploadDate.getTime());
    }
    public void setUploadDate(Date uploadDate)
    {
        this.uploadDate = new Date(uploadDate.getTime());
    }

    /**
    * An identifier for the one who uploaded the data. This allows to query for the data uploaded by a specific person.
    */
    @Column(name="upload_by")
    @Size(max=255)
    @Pattern(regexp = ENTITY_NAME)
    private String uploadBy;
    public String getUploadBy() {
        return uploadBy;
    }
    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    /**
     * Flexible JSON object to store any other parameter of interest
     */
    @Convert(converter = JsonConverter.class)
    private Object properties;
    public Object getProperties() {
        return properties;
    }
    public void setProperties(Object properties) {
        this.properties = properties;
    }
}
