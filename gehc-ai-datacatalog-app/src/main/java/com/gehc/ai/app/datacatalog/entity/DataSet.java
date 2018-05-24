/*
 * DataSet.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gehc.ai.app.datacatalog.filters.FiltersConverter;
import com.gehc.ai.app.datacatalog.filters.ListOfLongConverter;
import com.gehc.ai.app.datacatalog.filters.PropertiesConverter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

import static com.gehc.ai.app.common.constants.ValidationConstants.DATA_SET_TYPE;
import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;

/**
 * This class holds the DataSet/DataCollection
 * 
 * This is an Auto-generated Class from http://www.jsonschema2pojo.org/
 * Make sure to add/generated this class again if any changes are required for the model
 *  
 * @author Madhu Y (305024964)
 *
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "schemaVersion", "description", "type", "createdBy", "createdDate", "orgId",
		"properties", "imageSets", "filters" })
@Table(name = "data_set")
public class DataSet implements Serializable {

	private final static long serialVersionUID = -4814834407124267893L;
	
	@Id
    @GeneratedValue( strategy = GenerationType.AUTO )
	@JsonProperty("id")
	private Long id;
	
	@Size(min=1, max=200)
	@NotNull
	@JsonProperty("name")
	private String name;
	
    @Column ( name = "schema_version" )
    @Size(max=50)
	@JsonProperty("schemaVersion")
	private String schemaVersion;
    
	//need to fix it to handle for null or empty string when pattern tag added
	@Size(min=0, max=500)
	@JsonProperty("description")
	private String description;
	
	@Size(min=1, max=50)
	@Pattern(regexp = DATA_SET_TYPE)
	@NotNull
	@JsonProperty("type")
	private String type;
	
    @Column ( name = "created_by" )
    @Size(min=0, max=200)
	@JsonProperty("createdBy")
	private String createdBy;
	
	@Column(name = "created_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
	@JsonProperty("createdDate")
	private String createdDate;
	
    @Column ( name = "org_id" )
	@Size(min=1, max=255)
	@Pattern(regexp = UUID)
    @NotNull
	@JsonProperty("orgId")
	private String orgId;
    
    @Convert(converter = PropertiesConverter.class)
	@JsonProperty("properties")
	private Properties properties;
	
	@Convert(converter = ListOfLongConverter.class)
    @Column ( name = "image_sets" )
	@NotNull
	@JsonProperty("imageSets")
	private List<Long> imageSets = null;
    
	@Convert(converter = FiltersConverter.class)
	@JsonProperty("filters")
	private Filters filters;

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("schemaVersion")
	public String getSchemaVersion() {
		return schemaVersion;
	}

	@JsonProperty("schemaVersion")
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("createdBy")
	public String getCreatedBy() {
		return createdBy;
	}

	@JsonProperty("createdBy")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@JsonProperty("createdDate")
	public String getCreatedDate() {
		return createdDate;
	}

	@JsonProperty("createdDate")
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@JsonProperty("orgId")
	public String getOrgId() {
		return orgId;
	}

	@JsonProperty("orgId")
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@JsonProperty("properties")
	public Properties getProperties() {
		return properties;
	}

	@JsonProperty("properties")
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@JsonProperty("imageSets")
	public List<Long> getImageSets() {
		return imageSets;
	}

	@JsonProperty("imageSets")
	public void setImageSets(List<Long> imageSets) {
		this.imageSets = imageSets;
	}

	@JsonProperty("filters")
	public Filters getFilters() {
		return filters;
	}

	@JsonProperty("filters")
	public void setFilters(Filters filters) {
		this.filters = filters;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("name", name).append("schemaVersion", schemaVersion)
				.append("description", description).append("type", type).append("createdBy", createdBy)
				.append("createdDate", createdDate).append("orgId", orgId).append("properties", properties)
				.append("imageSets", imageSets).append("filters", filters).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(properties).append(type).append(orgId).append(id).append(createdBy)
				.append(description).append(name).append(createdDate).append(schemaVersion)
				.append(filters).append(imageSets).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != this.getClass()) {
			return false;
		}
		DataSet rhs = ((DataSet) other);
		return new EqualsBuilder().append(properties, rhs.properties).append(type, rhs.type).append(orgId, rhs.orgId)
				.append(id, rhs.id).append(createdBy, rhs.createdBy)
				.append(description, rhs.description)
				.append(name, rhs.name).append(createdDate, rhs.createdDate).append(schemaVersion, rhs.schemaVersion)
				.append(filters, rhs.filters).append(imageSets, rhs.imageSets).isEquals();
	}

}
