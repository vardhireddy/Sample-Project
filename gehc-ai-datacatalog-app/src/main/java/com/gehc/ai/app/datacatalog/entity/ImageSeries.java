/*
 * ImageSeries.java
 *
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.entity;

import static com.gehc.ai.app.common.constants.ValidationConstants.ENTITY_NAME;
import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;
import static com.gehc.ai.app.common.constants.ValidationConstants.DESCRIPTION;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "image_set")
public class ImageSeries implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "schema_version")
	@Size(max=50)
    @Pattern(regexp = ENTITY_NAME)
	private String schemaVersion;
	/**
	 * The organization who owns the data.
	 */
	@Column(name = "org_id")
	@Size(min=0, max=255)
	@Pattern(regexp = UUID)
	private String orgId;
	
	@Size(min=0, max=50)
	@Pattern(regexp = ENTITY_NAME)
	private String modality;
	
	@Size(min=0, max=50)
	@Pattern(regexp = ENTITY_NAME)
	private String anatomy;
	
	@Column(name = "data_format")
	@Size(min=0, max=50)
	@Pattern(regexp = ENTITY_NAME)
	private String dataFormat;
	
	private String uri;
	
	@Column(name = "series_instance_uid")
	@Size(min=0, max=255)
	@Pattern(regexp = ENTITY_NAME)
	private String seriesInstanceUid;
	
	@Size(min=0, max=100)
	@Pattern(regexp = DESCRIPTION)
	private String description;
	
	@Size(min=0, max=100)
	@Pattern(regexp = ENTITY_NAME)
	private String institution;
	

	private String equipment;
	

	private String manufacturer;
	
	@Column(name = "image_type")
	@Pattern(regexp = ENTITY_NAME)
	private String imageType;
	
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	@Pattern(regexp = ENTITY_NAME)
	private String view;
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@Column(name = "instance_count")
	private int instanceCount;
	@Convert(converter = JsonConverter.class)
	private Object properties; // NOSONAR
	/**
	 * An identifier for the one who uploaded the data. This allows to query for
	 * the data uploaded by a specific person.
	 */
	@Column(name = "upload_by")
	@Size(min=0, max=255)
	@Pattern(regexp = ENTITY_NAME)
	private String uploadBy;
	/**
	 * Date data was uploaded into database. Should be left to database to
	 * provide.
	 */
	@Column(name = "upload_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Date uploadDate;
	/**
	 * Patient table ID. Establishes a correlation with the patient table
	 */
	@Column(name = "patient_dbid")
	private Long patientDbId;
	
	@Column(name = "study_dbid")
	private Long studyDbId;
	
	@OneToOne
	@JoinColumn(name="id") 
	private Patient patient;
    
    @OneToMany(mappedBy = "imageSet", cascade = CascadeType.ALL)
    private List<Annotation> annotation;
	
	private String acqDate;
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
	private String acqTime;
	
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
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
	public Long getStudyDbId() {
		return studyDbId;
	}
	public void setStudyDbId(Long studyDbId) {
		this.studyDbId = studyDbId;
	}
	public Long getPatientDbId() {
		return patientDbId;
	}
	public void setPatientDbId(Long patientDbId) {
		this.patientDbId = patientDbId;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getAnatomy() {
		return anatomy;
	}
	public void setAnatomy(String anatomy) {
		this.anatomy = anatomy;
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
	public String getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getInstanceCount() {
		return instanceCount;
	}
	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}
	public Date getUploadDate() {
		return new Date(uploadDate.getTime());
	}
	@JsonIgnore
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = new Date(uploadDate.getTime());
	}
	public Object getProperties() {
		return properties;
	} // NOSONAR
	public void setProperties(Object properties) {
		this.properties = properties;
	} // NOSONAR
	
	public ImageSeries() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImageSeries(Long id, String schemaVersion, String orgId, String modality, String anatomy, String dataFormat,
			String uri, String seriesInstanceUid, String description, String institution, String equipment,
			String manufacturer, String imageType, String view, int instanceCount, Object properties, String uploadBy,
			Date uploadDate, Long patientDbId, Long studyDbId, Patient patient,
			String acqDate, String acqTime) {
		super();
		this.id = id;
		this.schemaVersion = schemaVersion;
		this.orgId = orgId;
		this.modality = modality;
		this.anatomy = anatomy;
		this.dataFormat = dataFormat;
		this.uri = uri;
		this.seriesInstanceUid = seriesInstanceUid;
		this.description = description;
		this.institution = institution;
		this.equipment = equipment;
		this.manufacturer = manufacturer;
		this.imageType = imageType;
		this.view = view;
		this.instanceCount = instanceCount;
		this.properties = properties;
		this.uploadBy = uploadBy;
		this.uploadDate = new Date(uploadDate.getTime());
		this.patientDbId = patientDbId;
		this.studyDbId = studyDbId;
		this.patient = patient;
		this.acqDate = acqDate;
		this.acqTime = acqTime;
	}
	@Override
	public String toString() {
		return "ImageSeries [id=" + id + ", schemaVersion=" + schemaVersion + ", orgId=" + orgId + ", modality="
				+ modality + ", anatomy=" + anatomy + ", dataFormat=" + dataFormat + ", uri=" + uri
				+ ", seriesInstanceUid=" + seriesInstanceUid + ", description=" + description + ", institution="
				+ institution + ", equipment=" + equipment + ", manufacturer=" + manufacturer + ", imageType="
				+ imageType + ", view=" + view + ", instanceCount=" + instanceCount + ", properties=" + properties
				+ ", uploadBy=" + uploadBy + ", uploadDate=" + uploadDate + ", patientDbId=" + patientDbId
				+ ", studyDbId=" + studyDbId + ", patient=" + patient + ", acqDate="
				+ acqDate + ", acqTime=" + acqTime + "]";
	}
}
