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

/**
 * {@code Contract} represents data contract.
 *
 * @author monika.jain@ge.com (212071558)
 */
@Entity
@JsonInclude(Include.NON_NULL)
public class Contract{

	public Contract() {
		super();
	}
	public Contract(Long id, @Size(max = 50) String schemaVersion, @NotNull @Size(min = 0, max = 255) String orgId,
			Object uri, String deidStatus, @Size(min = 0, max = 255) String dataOriginCountry,
			@Size(min = 0, max = 50) String active, @Size(min = 0, max = 255) String uploadBy, Date uploadDate,
			@Size(min = 0, max = 255) String agreementName, @Size(min = 0, max = 50) String primaryContactEmail,
			String agreementBeginDate, @Size(min = 0, max = 50) String dataUsagePeriod,
			@Size(min = 0, max = 100) String dataOriginState, List<ContractUseCase> useCases,List<ContractDataOriginCountriesStates> dataOriginCountriesStates, String dataLocationAllowed,
			@Size(min = 0, max = 50) String status) {
		super();
		this.id = id;
		this.schemaVersion = schemaVersion;
		this.orgId = orgId;
		this.uri = uri;
		this.deidStatus = deidStatus;
		this.dataOriginCountry = dataOriginCountry;
		this.active = active;
		this.uploadBy = uploadBy;
		this.uploadDate = uploadDate;
		this.agreementName = agreementName;
		this.primaryContactEmail = primaryContactEmail;
		this.agreementBeginDate = agreementBeginDate;
		this.dataUsagePeriod = dataUsagePeriod;
		this.dataOriginState = dataOriginState;
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
	@Convert(converter = JsonConverter.class)
	private Object uri; // NOSONAR

	//@NotNull
	@Column(name = "deid_status")
	private String deidStatus;

	@Size(min=0, max=255)
	@Column(name = "data_origin_country")
	private String dataOriginCountry;
	
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

	@Size(min=0, max=100)
	@Column(name = "data_origin_state")
	private String dataOriginState;

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

//	public String getBusinessCase() {
//		return businessCase;
//	}
//	public void setBusinessCase(String businessCase) {
//		this.businessCase = businessCase;
//	}

//	public String getDataOriginCountry() {
//		return dataOriginCountry;
//	}
//	public void setDataOriginCountry(String dataOriginCountry) {
//		this.dataOriginCountry = dataOriginCountry;
//	}
//	public String getUsageRights() {
//		return usageRights;
//	}
//	public void setUsageRights(String usageRights) {
//		this.usageRights = usageRights;
//	}
//	public String getUsageNotes() {
//		return usageNotes;
//	}
//	public void setUsageNotes(String usageNotes) {
//		this.usageNotes = usageNotes;
//	}

//	public Object getUri() {
//		return uri;
//	}
//	public void setUri(Object uri) {
//		this.uri = uri;
//	}
//
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
//
//	public Object getProperties() {
//		return properties;
//	}
//
//	public void setProperties(Object properties) {
//		this.properties = properties;
//	}
//
	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

/*	public List<ContractUseCase> getUseCases() {
		return useCases;
	}

	public void setUseCases(List<ContractUseCase> useCases) {
		this.useCases = useCases;
	}

	public LocalDateTime getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(LocalDateTime contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public List<String> getDataOriginCountries() {
		return dataOriginCountries;
	}

	public void setDataOriginCountries(List<String> dataOriginCountries) {
		this.dataOriginCountries = dataOriginCountries;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}*/

/*	public DataResidence getDataResidence() {
		return dataResidence;
	}

	public void setDataResidence(DataResidence dataResidence) {
		this.dataResidence = dataResidence;
	}*/

	/**
	 * updates the update_date column with the current date for each newly
	 * created object
	 */
	//@PrePersist TODO: This annotation breaks component test; is there any alternative?
/*	protected void onCreate() {
		uploadDate = LocalDateTime.now();
	}*/

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

	public Object getUri() {
		return uri;
	}
	public void setUri(Object uri) {
		this.uri = uri;
	}
	public String getDataOriginCountry() {
		return dataOriginCountry;
	}
	public void setDataOriginCountry(String dataOriginCountry) {
		this.dataOriginCountry = dataOriginCountry;
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
		this.uploadDate = uploadDate;
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
	public String getDataOriginState() {
		return dataOriginState;
	}
	public void setDataOriginState(String dataOriginState) {
		this.dataOriginState = dataOriginState;
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
				+ ", deidStatus=" + deidStatus + ", dataOriginCountry=" + dataOriginCountry + ", active=" + active
				+ ", uploadBy=" + uploadBy + ", uploadDate=" + uploadDate + ", agreementName=" + agreementName
				+ ", primaryContactEmail=" + primaryContactEmail + ", agreementBeginDate=" + agreementBeginDate
				+ ", dataUsagePeriod=" + dataUsagePeriod + ", dataOriginState=" + dataOriginState + ", useCases="
				+ useCases + ", dataOriginCountriesStates=" + dataOriginCountriesStates + ", dataLocationAllowed=" + dataLocationAllowed + ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((agreementBeginDate == null) ? 0 : agreementBeginDate.hashCode());
		result = prime * result + ((agreementName == null) ? 0 : agreementName.hashCode());
		result = prime * result + ((dataOriginCountry == null) ? 0 : dataOriginCountry.hashCode());
		result = prime * result + ((dataOriginState == null) ? 0 : dataOriginState.hashCode());
		result = prime * result + ((dataUsagePeriod == null) ? 0 : dataUsagePeriod.hashCode());
		result = prime * result + ((deidStatus == null) ? 0 : deidStatus.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((primaryContactEmail == null) ? 0 : primaryContactEmail.hashCode());
		result = prime * result + ((schemaVersion == null) ? 0 : schemaVersion.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((uploadBy == null) ? 0 : uploadBy.hashCode());
		result = prime * result + ((uploadDate == null) ? 0 : uploadDate.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + ((useCases == null) ? 0 : useCases.hashCode());
		result = prime * result + ((dataOriginCountriesStates == null) ? 0 : dataOriginCountriesStates.hashCode());
		result = prime * result + ((dataLocationAllowed == null) ? 0 : dataLocationAllowed.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (agreementBeginDate == null) {
			if (other.agreementBeginDate != null)
				return false;
		} else if (!agreementBeginDate.equals(other.agreementBeginDate))
			return false;
		if (agreementName == null) {
			if (other.agreementName != null)
				return false;
		} else if (!agreementName.equals(other.agreementName))
			return false;
		if (dataOriginCountry == null) {
			if (other.dataOriginCountry != null)
				return false;
		} else if (!dataOriginCountry.equals(other.dataOriginCountry))
			return false;
		if (dataOriginState == null) {
			if (other.dataOriginState != null)
				return false;
		} else if (!dataOriginState.equals(other.dataOriginState))
			return false;
		if (dataUsagePeriod == null) {
			if (other.dataUsagePeriod != null)
				return false;
		} else if (!dataUsagePeriod.equals(other.dataUsagePeriod))
			return false;
		if (deidStatus == null) {
			if (other.deidStatus != null)
				return false;
		} else if (!deidStatus.equals(other.deidStatus))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (primaryContactEmail == null) {
			if (other.primaryContactEmail != null)
				return false;
		} else if (!primaryContactEmail.equals(other.primaryContactEmail))
			return false;
		if (schemaVersion == null) {
			if (other.schemaVersion != null)
				return false;
		} else if (!schemaVersion.equals(other.schemaVersion))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (uploadBy == null) {
			if (other.uploadBy != null)
				return false;
		} else if (!uploadBy.equals(other.uploadBy))
			return false;
		if (uploadDate == null) {
			if (other.uploadDate != null)
				return false;
		} else if (!uploadDate.equals(other.uploadDate))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (useCases == null) {
			if (other.useCases != null)
				return false;
		} else if (!useCases.equals(other.useCases))
			return false;
		if (dataOriginCountriesStates == null) {
			if (other.dataOriginCountriesStates != null)
				return false;
		} else if (!dataOriginCountriesStates.equals(other.dataOriginCountriesStates))
			return false;
		if (dataLocationAllowed == null) {
			if (other.dataLocationAllowed != null)
				return false;
		} else if (!dataLocationAllowed.equals(other.dataLocationAllowed))
			return false;
		return true;
	}

/*	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Contract contract = (Contract) o;

		if (getUsageLength() != contract.getUsageLength()) return false;
		if (getId() != null ? !getId().equals(contract.getId()) : contract.getId() != null) return false;
		if (getSchemaVersion() != null ? !getSchemaVersion().equals(contract.getSchemaVersion()) : contract.getSchemaVersion() != null)
			return false;
		if (getOrgId() != null ? !getOrgId().equals(contract.getOrgId()) : contract.getOrgId() != null) return false;
		if (uri != null ? !uri.equals(contract.uri) : contract.uri != null) return false;
		if (getContractName() != null ? !getContractName().equals(contract.getContractName()) : contract.getContractName() != null)
			return false;
		if (getDeidStatus() != contract.getDeidStatus()) return false;
		if (getContactInfo() != null ? !getContactInfo().equals(contract.getContactInfo()) : contract.getContactInfo() != null)
			return false;
		if (getActive() != null ? !getActive().equals(contract.getActive()) : contract.getActive() != null)
			return false;
		if (getUseCases() != null ? !getUseCases().equals(contract.getUseCases()) : contract.getUseCases() != null)
			return false;
		if (getContractStartDate() != null ? !getContractStartDate().equals(contract.getContractStartDate()) : contract.getContractStartDate() != null)
			return false;
		if (getDataOriginCountries() != null ? !getDataOriginCountries().equals(contract.getDataOriginCountries()) : contract.getDataOriginCountries() != null)
			return false;
		if (getState() != null ? !getState().equals(contract.getState()) : contract.getState() != null) return false;
		if (getTerritory() != null ? !getTerritory().equals(contract.getTerritory()) : contract.getTerritory() != null)
			return false;
		if (getDataResidence() != contract.getDataResidence()) return false;
		if (getUploadBy() != null ? !getUploadBy().equals(contract.getUploadBy()) : contract.getUploadBy() != null)
			return false;
		return getUploadDate() != null ? getUploadDate().equals(contract.getUploadDate()) : contract.getUploadDate() == null;
	}*/

/*	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getSchemaVersion() != null ? getSchemaVersion().hashCode() : 0);
		result = 31 * result + (getOrgId() != null ? getOrgId().hashCode() : 0);
		result = 31 * result + (uri != null ? uri.hashCode() : 0);
		result = 31 * result + (getContractName() != null ? getContractName().hashCode() : 0);
		result = 31 * result + (getDeidStatus() != null ? getDeidStatus().hashCode() : 0);
		result = 31 * result + getUsageLength();
		result = 31 * result + (getContactInfo() != null ? getContactInfo().hashCode() : 0);
		result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
		result = 31 * result + (getUseCases() != null ? getUseCases().hashCode() : 0);
		result = 31 * result + (getContractStartDate() != null ? getContractStartDate().hashCode() : 0);
		result = 31 * result + (getDataOriginCountries() != null ? getDataOriginCountries().hashCode() : 0);
		result = 31 * result + (getState() != null ? getState().hashCode() : 0);
		result = 31 * result + (getTerritory() != null ? getTerritory().hashCode() : 0);
		result = 31 * result + (getDataResidence() != null ? getDataResidence().hashCode() : 0);
		result = 31 * result + (getUploadBy() != null ? getUploadBy().hashCode() : 0);
		result = 31 * result + (getUploadDate() != null ? getUploadDate().hashCode() : 0);
		return result;
	}*/

/*DO NOT DELETE. This code is required,
	but commented because it is breaking sonar because of condition coverage. 
	Comented to ensure RSNA build is delivered*/
	/*@Override
	public int hashCode() {//NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((businessCase == null) ? 0 : businessCase.hashCode());
		result = prime * result + ((contactInfo == null) ? 0 : contactInfo.hashCode());
		result = prime * result + ((contractName == null) ? 0 : contractName.hashCode());
		result = prime * result + ((dataOriginCountry == null) ? 0 : dataOriginCountry.hashCode());
		result = prime * result + ((deidStatus == null) ? 0 : deidStatus.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((schemaVersion == null) ? 0 : schemaVersion.hashCode());
		result = prime * result + ((uploadBy == null) ? 0 : uploadBy.hashCode());
		result = prime * result + ((uploadDate == null) ? 0 : uploadDate.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + usageLength;
		result = prime * result + ((usageNotes == null) ? 0 : usageNotes.hashCode());
		result = prime * result + ((usageRights == null) ? 0 : usageRights.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {//NOSONAR
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (businessCase == null) {
			if (other.businessCase != null)
				return false;
		} else if (!businessCase.equals(other.businessCase))
			return false;
		if (contactInfo == null) {
			if (other.contactInfo != null)
				return false;
		} else if (!contactInfo.equals(other.contactInfo))
			return false;
		if (contractName == null) {
			if (other.contractName != null)
				return false;
		} else if (!contractName.equals(other.contractName))
			return false;
		if (dataOriginCountry == null) {
			if (other.dataOriginCountry != null)
				return false;
		} else if (!dataOriginCountry.equals(other.dataOriginCountry))
			return false;
		if (deidStatus != other.deidStatus)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (schemaVersion == null) {
			if (other.schemaVersion != null)
				return false;
		} else if (!schemaVersion.equals(other.schemaVersion))
			return false;
		if (uploadBy == null) {
			if (other.uploadBy != null)
				return false;
		} else if (!uploadBy.equals(other.uploadBy))
			return false;
		if (uploadDate == null) {
			if (other.uploadDate != null)
				return false;
		} else if (!uploadDate.equals(other.uploadDate))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (usageLength != other.usageLength)
			return false;
		if (usageNotes == null) {
			if (other.usageNotes != null)
				return false;
		} else if (!usageNotes.equals(other.usageNotes))
			return false;
		if (usageRights == null) {
			if (other.usageRights != null)
				return false;
		} else if (!usageRights.equals(other.usageRights))
			return false;
		return true;
	}*/
	
}
