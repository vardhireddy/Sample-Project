/*
 * Contract.java
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

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gehc.ai.app.common.constants.ValidationConstants;
import com.gehc.ai.app.datacatalog.filters.JsonConverter;
import com.gehc.ai.app.datacatalog.filters.ListOfStringConverter;

/**
 * {@code Contract} represents data contract.
 *
 * @author monika.jain@ge.com (212071558)
 */
@Entity
@JsonInclude(Include.NON_NULL)
public final class Contract implements Cloneable{

	public Contract() {
		super();
	}
	public Contract(Long id, @Size(max = 50) String schemaVersion, @NotNull @Size(min = 0, max = 255) String orgId,
			List<String> uri, String deidStatus, @Size(min = 0, max = 255)
			@Size(min = 0, max = 50) String active, @Size(min = 0, max = 255) String uploadBy, Date uploadDate,
			@Size(min = 0, max = 255) String agreementName, @Size(min = 0, max = 50) String primaryContactEmail,
			String agreementBeginDate, @Size(min = 0, max = 50) String dataUsagePeriod, List<ContractUseCase> useCases,List<ContractDataOriginCountriesStates> dataOriginCountriesStates, String dataLocationAllowed,
			@Size(min = 0, max = 50) String status) {
		super();
		this.id = id;
		this.schemaVersion = schemaVersion;
		this.orgId = orgId;
		this.uri = uri;
		this.deidStatus = deidStatus;
		this.active = active;
		this.uploadBy = uploadBy;
		this.uploadDate = uploadDate;
		this.agreementName = agreementName;
		this.primaryContactEmail = primaryContactEmail;
		this.agreementBeginDate = agreementBeginDate;
		this.dataUsagePeriod = dataUsagePeriod;
		this.useCases = useCases;
		this.dataOriginCountriesStates = dataOriginCountriesStates;
		this.status = status;
		this.dataLocationAllowed = dataLocationAllowed;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Size(max=50)
	private String schemaVersion;

	/**
	 * The organization who owns the data.
	 */
	@NotNull
	@Size(min=0, max=255)
	@Column(name = "org_id")
	private String orgId;

	//@NotNull
	@Convert(converter = ListOfStringConverter.class)
	private List<String> uri;

	//@NotNull
	@Column(name = "deid_status")
	private String deidStatus;
	
	@Size(min=0, max=50)
	private String active;	

	/**
	 * An identifier for the one who uploaded the data. This allows to query for
	 * the data uploaded by a specific person.
	 */
	//@NotNull
	@Size(min=0, max=255)
	@Column(name = "upload_by")	
	private String uploadBy;

    /**
     * Date data was uploaded into database. Should be left to database to provide.
     */
    @Column(name="upload_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date uploadDate;    

	@Size(min=0, max=255)
	@Column(name = "agreement_name")
	private String agreementName;

	@Size(min=0, max=50)
	@Column(name = "primary_contact_email")	
	private String primaryContactEmail;
	
    public String getPrimaryContactEmail() {
		return primaryContactEmail;
	}
	public void setPrimaryContactEmail(String primaryContactEmail) {
		this.primaryContactEmail = primaryContactEmail;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "agreement_begin_date")
    private String agreementBeginDate;

	@Size(min=0, max=50)
	@Column(name = "data_usage_period")	
	private String dataUsagePeriod;

	@Convert(converter = JsonConverter.class)
	@Column(name = "use_cases")
	private List<ContractUseCase> useCases; // NOSONAR

	@Convert(converter = JsonConverter.class)
	@Column(name = "data_origin_countries_states")
	private List<ContractDataOriginCountriesStates> dataOriginCountriesStates; // NOSONAR

	//@NotNull
	@Column(name = "data_location_allowed")
	private String dataLocationAllowed;
	
	@Size(min=0, max=50)
	private String status;

	public List<ContractUseCase> getUseCases() {
		return useCases;
	}
	public void setUseCases(List<ContractUseCase> useCases) {
		this.useCases = useCases;
	}
	public List<ContractDataOriginCountriesStates> getDataOriginCountriesStates() {
		return dataOriginCountriesStates;
	}
	public void setDataOriginCountriesStates(List<ContractDataOriginCountriesStates> dataOriginCountriesStates) {
		this.dataOriginCountriesStates = dataOriginCountriesStates;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public enum DeidStatus {

		HIPAA_COMPLIANT(ValidationConstants.HIPAA_COMPLIANT),
		DEIDS_TO_LOCAL_STANDARDS(ValidationConstants.DEIDS_TO_LOCAL_STANDARDS);

		private String displayName;

		DeidStatus(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}

		// Optionally and/or additionally, toString.
		@Override
		public String toString() {
			return displayName;
		}

		public static DeidStatus fromDisplayName(String displayName) {
			switch (displayName) {
				case ValidationConstants.HIPAA_COMPLIANT:
					return DeidStatus.HIPAA_COMPLIANT;
				case ValidationConstants.DEIDS_TO_LOCAL_STANDARDS:
					return DeidStatus.DEIDS_TO_LOCAL_STANDARDS;
				default:
					throw new IllegalArgumentException("DeidStatus [" + displayName + "] not supported");
			}
		}
	}

	public enum DataLocationAllowed {

		ONSHORE_ONLY_WHERE_THE_DATA_WAS_HARVESTED("Onshore Only (Where the data was harvested)"), GLOBAL("Global");

		private String displayName;

		private DataLocationAllowed(String displayName){
			this.displayName = displayName;
		}

		@Override
		public String toString(){
			return this.displayName;
		}
	}

	public enum DataResidence{
		ONSHORE_ONLY("Onshore Only"), GLOBAL("Global");

		private DataResidence(String displayName) {
			this.displayName = displayName;
		}

		private String displayName;

		@Override
		public String toString(){
			return this.displayName;
		}

	}

	public List<String> getUri() {
		return uri;
	}
	public void setUri(List<String> uri) {
		this.uri = uri;
	}
	public String getDeidStatus() {
		return deidStatus;
	}
	public void setDeidStatus(String deidStatus) {
		this.deidStatus = deidStatus;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		Date original =  uploadDate;
		this.uploadDate = new Date(original.getTime());
	}
	public String getAgreementName() {
		return agreementName;
	}
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	public String getAgreementBeginDate() {
		return agreementBeginDate;
	}
	public void setAgreementBeginDate(String agreementBeginDate) {
		this.agreementBeginDate = agreementBeginDate;
	}
	public String getDataUsagePeriod() {
		return dataUsagePeriod;
	}
	public void setDataUsagePeriod(String dataUsagePeriod) {
		this.dataUsagePeriod = dataUsagePeriod;
	}

	public String getDataLocationAllowed() {
		return dataLocationAllowed;
	}
	public void setDataLocationAllowed(String dataLocationAllowed) {
		this.dataLocationAllowed = dataLocationAllowed;
	}

	@Override
	public String toString() {
		return "Contract [id=" + id + ", schemaVersion=" + schemaVersion + ", orgId=" + orgId + ", uri=" + uri
				+ ", deidStatus=" + deidStatus + ", active=" + active
				+ ", uploadBy=" + uploadBy + ", uploadDate=" + uploadDate + ", agreementName=" + agreementName
				+ ", primaryContactEmail=" + primaryContactEmail + ", agreementBeginDate=" + agreementBeginDate
				+ ", dataUsagePeriod=" + dataUsagePeriod + ", useCases="
				+ useCases + ", dataOriginCountriesStates=" + dataOriginCountriesStates + ", dataLocationAllowed=" + dataLocationAllowed + ", status=" + status + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Contract contract = (Contract) o;

		if (getId() != null ? !getId().equals(contract.getId()) : contract.getId() != null) return false;
		if (!getSchemaVersion().equals(contract.getSchemaVersion())) return false;
		if (!getOrgId().equals(contract.getOrgId())) return false;
		if (getUri() != null ? !getUri().equals(contract.getUri()) : contract.getUri() != null) return false;
		if (!getDeidStatus().equals(contract.getDeidStatus())) return false;
		if (!getActive().equals(contract.getActive())) return false;
		if (!getUploadBy().equals(contract.getUploadBy())) return false;
		if (getUploadDate() != null ? !getUploadDate().equals(contract.getUploadDate()) : contract.getUploadDate() != null)
			return false;
		if (!getAgreementName().equals(contract.getAgreementName())) return false;
		if (!getPrimaryContactEmail().equals(contract.getPrimaryContactEmail())) return false;
		if (!getAgreementBeginDate().equals(contract.getAgreementBeginDate())) return false;
		if (!getDataUsagePeriod().equals(contract.getDataUsagePeriod())) return false;
		if (!getUseCases().equals(contract.getUseCases())) return false;
		if (!getDataOriginCountriesStates().equals(contract.getDataOriginCountriesStates())) return false;
		if (!getDataLocationAllowed().equals(contract.getDataLocationAllowed())) return false;
		return getStatus().equals(contract.getStatus());
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getSchemaVersion().hashCode();
		result = 31 * result + getOrgId().hashCode();
		result = 31 * result + (getUri() != null ? getUri().hashCode() : 0);
		result = 31 * result + getDeidStatus().hashCode();
		result = 31 * result + getActive().hashCode();
		result = 31 * result + getUploadBy().hashCode();
		result = 31 * result + (getUploadDate() != null ? getUploadDate().hashCode() : 0);
		result = 31 * result + getAgreementName().hashCode();
		result = 31 * result + getPrimaryContactEmail().hashCode();
		result = 31 * result + getAgreementBeginDate().hashCode();
		result = 31 * result + getDataUsagePeriod().hashCode();
		result = 31 * result + getUseCases().hashCode();
		result = 31 * result + getDataOriginCountriesStates().hashCode();
		result = 31 * result + getDataLocationAllowed().hashCode();
		result = 31 * result + getStatus().hashCode();
		return result;
	}
}
