/*
 * ImageSet.java
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

/**
 * @author 212071558
 *
 */
public class ImageSet {

    private String id;
    /**
     * @return the id
     */
    public String getId() {
    	return (id==null)?id:id.replaceAll("^\"|\"$", "");
    }
    /**
     * @param id the id to set
     */
    public void setId( String id ) {
        this.id = id;
    }
    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }
    /**
     * @param orgId the orgId to set
     */
    public void setOrgId( String orgId ) {
        this.orgId = orgId;
    }
    private String schemaVersion;
    private String seriesId;
    private String studyId;
    private String patientId;
    private String orgId;
    private String orgName;
    private String permissionId;
    private String modality;
    private String anatomy;
    private String diseaseType;
    private String dataFormat;
    private long age;
    private String gender;
    private String uri;

    /**
     * @return the schemaVersion
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }
    /**
     * @param schemaVersion the schemaVersion to set
     */
    public void setSchemaVersion( String schemaVersion ) {
        this.schemaVersion = schemaVersion;
    }
    /**
     * @return the seriesId
     */
    public String getSeriesId() {
        return seriesId;
    }
    /**
     * @param seriesId the seriesId to set
     */
    public void setSeriesId( String seriesId ) {
        this.seriesId = seriesId;
    }
    /**
     * @return the studyId
     */
    public String getStudyId() {
        return studyId;
    }
    /**
     * @param studyId the studyId to set
     */
    public void setStudyId( String studyId ) {
        this.studyId = studyId;
    }
    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }
    /**
     * @param patientId the patientId to set
     */
    public void setPatientId( String patientId ) {
        this.patientId = patientId;
    }
    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }
    /**
     * @param orgName the orgName to set
     */
    public void setOrgName( String orgName ) {
        this.orgName = orgName;
    }
    /**
     * @return the permissionId
     */
    public String getPermissionId() {
        return permissionId;
    }
    /**
     * @param permissionId the permissionId to set
     */
    public void setPermissionId( String permissionId ) {
        this.permissionId = permissionId;
    }
    /**
     * @return the modality
     */
    public String getModality() {
        return modality;
    }
    /**
     * @param modality the modality to set
     */
    public void setModality( String modality ) {
        this.modality = modality;
    }
    /**
     * @return the anatomy
     */
    public String getAnatomy() {
        return anatomy;
    }
    /**
     * @param anatomy the anatomy to set
     */
    public void setAnatomy( String anatomy ) {
        this.anatomy = anatomy;
    }
    /**
     * @return the diseaseType
     */
    public String getDiseaseType() {
        return diseaseType;
    }
    /**
     * @param diseaseType the diseaseType to set
     */
    public void setDiseaseType( String diseaseType ) {
        this.diseaseType = diseaseType;
    }
    /**
     * @return the dataFormat
     */
    public String getDataFormat() {
        return dataFormat;
    }
    /**
     * @param dataFormat the dataFormat to set
     */
    public void setDataFormat( String dataFormat ) {
        this.dataFormat = dataFormat;
    }
    /**
     * @return the age
     */
    public long getAge() {
        return age;
    }
    /**
     * @param age the age to set
     */
    public void setAge( long age ) {
        this.age = age;
    }
    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }
    /**
     * @param gender the gender to set
     */
    public void setGender( String gender ) {
        this.gender = gender;
    }
    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }
    /**
     * @param uri the uri to set
     */
    public void setUri( String uri ) {
        this.uri = uri;
    }

    /**
     * 
     */
    public ImageSet() {
        super();
        // TODO Auto-generated constructor stub
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ImageSet [id=" + id + ", schemaVersion=" + schemaVersion + ", seriesId=" + seriesId + ", studyId=" + studyId + ", patientId=" + patientId + ", orgId=" + orgId + ", orgName=" + orgName + ", permissionId=" + permissionId + ", modality=" + modality + ", anatomy=" + anatomy + ", diseaseType=" + diseaseType + ", dataFormat=" + dataFormat + ", age=" + age + ", gender=" + gender + ", uri=" + uri + "]";
    }
    /**
     * @param id
     * @param schemaVersion
     * @param seriesId
     * @param studyId
     * @param patientId
     * @param orgId
     * @param orgName
     * @param permissionId
     * @param modality
     * @param anatomy
     * @param diseaseType
     * @param dataFormat
     * @param age
     * @param gender
     * @param uri
     */
    public ImageSet( String id, String schemaVersion, String seriesId, String studyId, String patientId, String orgId, String orgName, String permissionId, String modality, String anatomy,
                     String diseaseType, String dataFormat, long age, String gender, String uri ) {
        super();
        this.id = id;
        this.schemaVersion = schemaVersion;
        this.seriesId = seriesId;
        this.studyId = studyId;
        this.patientId = patientId;
        this.orgId = orgId;
        this.orgName = orgName;
        this.permissionId = permissionId;
        this.modality = modality;
        this.anatomy = anatomy;
        this.diseaseType = diseaseType;
        this.dataFormat = dataFormat;
        this.age = age;
        this.gender = gender;
        this.uri = uri;
    }
    
}
