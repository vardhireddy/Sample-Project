package com.gehc.ai.app.dc.entity;

import com.gehc.ai.app.dc.filters.JsonConverter;
import org.springframework.data.convert.Jsr310Converters;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name="schema_version")
    private String schemaVersion;
    public String getSchemaVersion() {
        return schemaVersion;
    }
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    @Column(name="patient_name")
    private String patientName;
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @Column(name="patient_id")
    private String patientId;
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Birth date is intentionally left as a string so it can be whatever is in the DICOM tag
     */
    @Column(name="birth_date")
    private String birthDate;
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    private String gender;
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Age is intentionally left as a string so it can be whatever is in the DICOM tag
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
