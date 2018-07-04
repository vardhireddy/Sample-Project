/*
 * Upload.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.entity;

import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.ListOfStringConverter;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public final class Upload {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "schema_version")
	@Size(min=2, max=50)
    @NotNull
	private String schemaVersion;

	/**
	 * The organization who owns the data.
	 */
	@Column(name = "org_id")
	@Size(min=1, max=255)
    @NotNull
	private String orgId;

    @Column(name = "data_type")
    @Convert(converter = ListOfStringConverter.class)
	private List<String> dataType;

	@NotNull
    @Column(name = "contract_id")
	private Long contractId;
	
	/**
	 * Space uuid in S3.
	 */
	@Column(name = "space_id")
	@Size(min=1, max=255)
	private String spaceId;

    @Convert(converter = ListOfStringConverter.class)
	private List<String> summary;
	
	@Convert(converter = JsonConverter.class)
	private Map<String,String> tags;
	
	@Convert(converter = JsonConverter.class)
	private Map<String,String> status;
	
	/**
	 * An identifier for the one who uploaded the data. This allows to query for
	 * the data uploaded by a specific person.
	 */
	@Column(name = "upload_by")
	@Size(min=0, max=255)
	private String uploadBy;

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="upload_date")
    @CreationTimestamp
	private Timestamp uploadDate;

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="last_modified")
    @UpdateTimestamp
    private Timestamp lastModified;

}
