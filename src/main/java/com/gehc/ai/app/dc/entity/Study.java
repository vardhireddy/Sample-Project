package com.gehc.ai.app.dc.entity;

import com.gehc.ai.app.dc.filters.JsonConverter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 200014175 on 10/28/2016.
 */
@Entity
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="schema_version")
    private String schemaVersion;
    public String getSchemaVersion() {
        return schemaVersion;
    }
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * DICOM Patient ID (0010,0020)
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
     * DICOM tag (0020,000D) - study instance UID, expected to be globally unique
     */
    @Column (name="study_instance_uid")
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
    @Column (name="stduy_url")
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
    private String org;
    public String getOrg() {
        return org;
    }
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="upload_date")
    private Date uploadDate;
    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
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

    @Convert(converter = JsonConverter.class)
    private Object properties;
    public Object getProperties() {
        return properties;
    }
    public void setProperties(Object properties) {
        this.properties = properties;
    }
}
