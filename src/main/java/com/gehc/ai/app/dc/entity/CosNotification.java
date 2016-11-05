package com.gehc.ai.app.dc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 200014175 on 11/2/2016.
 */
@Entity
public class CosNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Time stamp from SNS message
     */
    @Column (name="time_stamp")
    private Date timeStamp;
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Actual message from COS notification, can be parsed
     */
    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Column (name="patient_status")
    private String patientStatus;
    public String getPatientStatus() {
        return patientStatus;
    }
    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    @Column (name="study_status")
    private String studyStatus;
    public String getStudyStatus() {
        return studyStatus;
    }
    public void setStudyStatus(String studyStatus) {
        this.studyStatus = studyStatus;
    }

    @Column (name="imageset_status")
    private String imagesetStatus;
    public String getImagesetStatus() {
        return imagesetStatus;
    }
    public void setImagesetStatus(String imagesetStatus) {
        this.imagesetStatus = imagesetStatus;
    }

    @Column(name="annotation_status")
    private String annotationStatus;
    public String getAnnotationStatus() {
        return annotationStatus;
    }
    public void setAnnotationStatus(String annotationStatus) {
        this.annotationStatus = annotationStatus;
    }
}
