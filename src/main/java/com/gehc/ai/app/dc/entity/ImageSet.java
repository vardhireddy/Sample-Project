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
    private String schemaVersion;
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
    private Long studyDbId;
    private Long patientDbId;
    public Long getPatientDbId() {
		return patientDbId;
	}
	public void setPatientDbId(Long patientDbId) {
		this.patientDbId = patientDbId;
	}
	private String seriesInstanceUid;
	private String acqDate;
    private String acqTime;
    private String modality;
    private String anatomy;
    private String description;
    private String institution;
    private String equipment;
    private String dataFormat;
    private String uri;
    private int instanceCount;    
    private String orgId;    
    private String uploadBy;
    private String uploadDate;
    private Object properties;

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
        return seriesInstanceUid;
    }
    /**
     * @param seriesId the seriesId to set
     */
    public void setSeriesId( String seriesId ) {
        this.seriesInstanceUid = seriesId;
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
	public Long getStudyDbId() {
		return studyDbId;
	}
	public void setStudyDbId(Long studyDbId) {
		this.studyDbId = studyDbId;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public String getAcqDate() {
		return acqDate;
	}
	public void setAcqDate(String acqDate) {
		this.acqDate = acqDate;
	}
	public String getAcqTime() {
		return acqTime;
	}
	public void setAcqTime(String acqTime) {
		this.acqTime = acqTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public int getInstanceCount() {
		return instanceCount;
	}
	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}
	public String getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Object getProperties() {
		return properties;
	}
	public void setProperties(Object properties) {
		this.properties = properties;
	}
	public ImageSet(String schemaVersion, String id, Long studyDbId,
			Long patientDbId, String seriesInstanceUid, String acqDate,
			String acqTime, String modality, String anatomy,
			String description, String institution, String equipment,
			String dataFormat, String uri, int instanceCount, String orgId,
			String uploadBy, String uploadDate, Object properties) {
		super();
		this.schemaVersion = schemaVersion;
		this.id = id;
		this.studyDbId = studyDbId;
		this.patientDbId = patientDbId;
		this.seriesInstanceUid = seriesInstanceUid;
		this.acqDate = acqDate;
		this.acqTime = acqTime;
		this.modality = modality;
		this.anatomy = anatomy;
		this.description = description;
		this.institution = institution;
		this.equipment = equipment;
		this.dataFormat = dataFormat;
		this.uri = uri;
		this.instanceCount = instanceCount;
		this.orgId = orgId;
		this.uploadBy = uploadBy;
		this.uploadDate = uploadDate;
		this.properties = properties;
	}
	@Override
	public String toString() {
		return "ImageSet [schemaVersion=" + schemaVersion + ", id=" + id
				+ ", studyDbId=" + studyDbId + ", patientDbId=" + patientDbId
				+ ", seriesInstanceUid=" + seriesInstanceUid + ", acqDate="
				+ acqDate + ", acqTime=" + acqTime + ", modality=" + modality
				+ ", anatomy=" + anatomy + ", description=" + description
				+ ", institution=" + institution + ", equipment=" + equipment
				+ ", dataFormat=" + dataFormat + ", uri=" + uri
				+ ", instanceCount=" + instanceCount + ", orgId=" + orgId
				+ ", uploadBy=" + uploadBy + ", uploadDate=" + uploadDate
				+ ", properties=" + properties + "]";
	}     
	
}
