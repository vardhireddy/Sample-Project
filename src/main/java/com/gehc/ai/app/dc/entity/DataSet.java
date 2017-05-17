package com.gehc.ai.app.dc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gehc.ai.app.dc.filters.JsonConverter;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "data_set")
public class DataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    @Column ( name = "schema_version" )
	private String schemaVersion;
	private String name;
	private String description;
	//@Column ( name = "image_sets" )
	//private String[] imageSets;
	@Column ( name = "created_date" )
	private String createdDate;
	//private int imageSetsSize;
    private String type;
    @Column ( name = "org_id" )
    private String orgId;
    @Column ( name = "created_by" )
    private String createdBy;
    /**
     * Flexible JSON object to store properties of data collection
     */
    @Convert(converter = JsonConverter.class)
    private Object properties;
    
    public Object getProperties() {
		return properties;
	}
	public void setProperties(Object properties) {
		this.properties = properties;
	}
	public Object getImageSets() {
		return imageSets;
	}
	public void setImageSets(Object imageSets) {
		this.imageSets = imageSets;
	}
	@Convert(converter = JsonConverter.class)
    @Column ( name = "image_sets" )
    private Object imageSets;
    
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
