package com.gehc.ai.app.datacatalog.rest.response;

import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;

import java.sql.Date;
import java.util.List;

public class ContractByDataSetId {

    private Long id;
    private Contract.DeidStatus deidStatus;
    private String active;
    private boolean hasContractExpired;
    private String uploadBy;
    private Date uploadDate;
    private String agreementName;
    private String primaryContactEmail;
    private String agreementBeginDate;
    private String dataUsagePeriod;
    private List<ContractUseCase> useCases;
    private Contract.UploadStatus uploadStatus;
    private List<ContractDataOriginCountriesStates> DataOriginCountriesAndStates;
    private Contract.DataLocationAllowed DataLocationAllowed;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contract.DeidStatus getDeidStatus() {
        return deidStatus;
    }

    public void setDeidStatus(Contract.DeidStatus deidStatus) {
        this.deidStatus = deidStatus;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
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

    public List<ContractUseCase> getUseCases() {
        return useCases;
    }

    public void setUseCases(List<ContractUseCase> useCases) {
        this.useCases = useCases;
    }

    public Contract.UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(Contract.UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public List<ContractDataOriginCountriesStates> getDataOriginCountriesAndStates() {
        return DataOriginCountriesAndStates;
    }

    public void setDataOriginCountriesAndStates(List<ContractDataOriginCountriesStates> dataOriginCountriesAndStates) {
        DataOriginCountriesAndStates = dataOriginCountriesAndStates;
    }

    public Contract.DataLocationAllowed getDataLocationAllowed() {
        return DataLocationAllowed;
    }

    public void setDataLocationAllowed(Contract.DataLocationAllowed dataLocationAllowed) {
        DataLocationAllowed = dataLocationAllowed;
    }

    public boolean isHasContractExpired() {
        return hasContractExpired;
    }

    public void setHasContractExpired(boolean hasContractExpired) {
        this.hasContractExpired = hasContractExpired;
    }
}
