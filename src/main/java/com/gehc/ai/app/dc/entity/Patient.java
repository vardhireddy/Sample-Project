/*
 * Patient.java
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gehc.ai.app.dc.filters.JsonConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by 200014175 on 10/27/2016.
 */
@Entity
public class Patient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    private static final long serialVersionUID = 1;

//    @OneToMany(mappedBy="patient")
//    private List<Study> studies;
//    
//    public List<Study> getStudies() {
//		return studies;
//	}
//	public void setStudies(List<Study> studies) {
//		this.studies = studies;
//	}

	@Column(name="schema_version")
    private String schemaVersion;
    public String getSchemaVersion() {
        return schemaVersion;
    }
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * Alphabetic component of Patient Name in DICOM tag (0010,0010). Phonetic and ideographic components are not included.
     */
    @Column(name="patient_name")
    private String patientName;
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * DICOM tag (0010,0020)
     */
    @Column(name="patient_id")
    private String patientId;
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Birth date is intentionally left as a string so it can be whatever is in the DICOM tag (0010,0030)
     */
    @Column(name="birth_date")
    private String birthDate;
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * DICOM tag (0010,0040)
     */
    private String gender;
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Age is intentionally left as a string so it can be whatever is in the DICOM tag (0010,1010)
     */
    private String age;
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * The organization who owns or uploads the data. This could be an entry in an org database
     */
    @Column(name="org_id")
    private String orgId;
    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="upload_date")
    @JsonFormat(pattern="yyyyMMdd")
    private Date uploadDate;
    public Date getUploadDate() {
        return new Date(uploadDate.getTime());
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = new Date(uploadDate.getTime());
    }

    /**
     * An identifier for the one who uploaded the data. This allows to query for the data uploaded by a specific person.
     */
    @Column(name="upload_by")
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
    private String properties;
    public String getProperties() {
        return properties;
    }
    public void setProperties(String properties) {
        this.properties = properties;
    }
}
