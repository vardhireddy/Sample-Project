package com.gehc.ai.app.dc.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gehc.ai.app.dc.filters.JsonConverter;

@Entity
public class AnnotationProperties {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="schema_version")
    private String schemaVersion;
    /**
     * The organization who owns the data. 
     */
    @Column(name="org_id")
    private String orgId;
    
    @Column(name="resource_name")
    private String resourceName;
    public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
     * Flexible JSON object to store classes
     */
    @Convert(converter = JsonConverter.class)
    private Object classes;
    
    @Column(name="created_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    
    @Column(name="created_by")
    private String createdBy;

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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Object getClasses() {
		return classes;
	}

	public void setClasses(Object classes) {
		this.classes = classes;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public AnnotationProperties() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}