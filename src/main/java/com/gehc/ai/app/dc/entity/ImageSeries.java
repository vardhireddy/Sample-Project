package com.gehc.ai.app.dc.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gehc.ai.app.dc.filters.JsonConverter;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "image_series")
public class ImageSeries implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "schema_version")
	private String schemaVersion;
	/**
	 * The organization who owns the data.
	 */
	@Column(name = "org_id")
	private String orgId;
	private String modality;
	private String anatomy;
	@Column(name = "data_format")
	private String dataFormat;
	private String uri;
	@Column(name = "series_instance_uid")
	private String seriesInstanceUid;
	private String description;
	private String institution;
	private String equipment;
	@Column(name = "instance_count")
	private int instanceCount;
	@Convert(converter = JsonConverter.class)
	private Object properties;
	/**
	 * An identifier for the one who uploaded the data. This allows to query for
	 * the data uploaded by a specific person.
	 */
	@Column(name = "upload_by")
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
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Object getProperties() {
		return properties;
	}
	public void setProperties(Object properties) {
		this.properties = properties;
	}
	public ImageSeries(Long id, String schemaVersion, Long studyDbId, Long patientDbId, String seriesInstanceUid,
			String modality, String anatomy, String description, String institution, String equipment,
			String dataFormat, String uri, int instanceCount, String orgId, String uploadBy, Date uploadDate,
			Object properties) {
		super();
		this.id = id;
		this.schemaVersion = schemaVersion;
		this.studyDbId = studyDbId;
		this.patientDbId = patientDbId;
		this.seriesInstanceUid = seriesInstanceUid;
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
	public ImageSeries() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ImageSeries [id=" + id + ", schemaVersion=" + schemaVersion + ", studyDbId=" + studyDbId
				+ ", patientDbId=" + patientDbId + ", seriesInstanceUid=" + seriesInstanceUid + ", modality=" + modality
				+ ", anatomy=" + anatomy + ", description=" + description + ", institution=" + institution
				+ ", equipment=" + equipment + ", dataFormat=" + dataFormat + ", uri=" + uri + ", instanceCount="
				+ instanceCount + ", orgId=" + orgId + ", uploadBy=" + uploadBy + ", uploadDate=" + uploadDate
				+ ", properties=" + properties + "]";
	}
}
