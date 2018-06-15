/*
 * ContractByDataSetId.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.rest.response;

import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ContractByDataSetId {
    private static Logger logger = LoggerFactory.getLogger(ContractByDataSetId.class);

    private final Long id;
    private final Contract.DeidStatus deidStatus;
    private final String active;
    private final boolean contractExpired;
    private final String uploadBy;
    private final Date uploadDate;
    private final String agreementName;
    private final String primaryContactEmail;
    private final String agreementBeginDate;
    private final String dataUsagePeriod;
    private final List<ContractUseCase> useCases;
    private final Contract.UploadStatus uploadStatus;
    private final List<ContractDataOriginCountriesStates> dataOriginCountriesAndStates;
    private final Contract.DataLocationAllowed dataLocationAllowed;


    public ContractByDataSetId(Long id, Contract.DeidStatus deidStatus, String active, boolean hasContractExpired, String uploadBy, Date uploadDate, String agreementName, String primaryContactEmail, String agreementBeginDate, String dataUsagePeriod, List<ContractUseCase> useCases, Contract.UploadStatus uploadStatus, List<ContractDataOriginCountriesStates> dataOriginCountriesAndStates, Contract.DataLocationAllowed dataLocationAllowed) {
        this.id = id;
        this.deidStatus = deidStatus;
        this.active = active;
        this.contractExpired = hasContractExpired;
        this.uploadBy = uploadBy;
        this.uploadDate = uploadDate;
        this.agreementName = agreementName;
        this.primaryContactEmail = primaryContactEmail;
        this.agreementBeginDate = agreementBeginDate;
        this.dataUsagePeriod = dataUsagePeriod;
        this.useCases = useCases;
        this.uploadStatus = uploadStatus;
        this.dataOriginCountriesAndStates = dataOriginCountriesAndStates;
        this.dataLocationAllowed = dataLocationAllowed;
    }

    public Long getId() {
        return id;
    }

    public Contract.DeidStatus getDeidStatus() {
        return deidStatus;
    }

    public String getActive() {
        return active;
    }

    public boolean isContractExpired() {
        return contractExpired;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getAgreementName() {
        return agreementName;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public String getAgreementBeginDate() {
        return agreementBeginDate;
    }

    public String getDataUsagePeriod() {
        return dataUsagePeriod;
    }

    public List<ContractUseCase> getUseCases() {
        return useCases;
    }

    public Contract.UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public List<ContractDataOriginCountriesStates> getDataOriginCountriesAndStates() {
        return dataOriginCountriesAndStates;
    }

    public Contract.DataLocationAllowed getDataLocationAllowed() {
        return dataLocationAllowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractByDataSetId that = (ContractByDataSetId) o;

        if (isContractExpired() != that.isContractExpired()) return false;
        if (!getId().equals(that.getId())) return false;
        if (getDeidStatus() != that.getDeidStatus()) return false;
        if (!getActive().equals(that.getActive())) return false;
        if (!getUploadBy().equals(that.getUploadBy())) return false;
        if (!getUploadDate().equals(that.getUploadDate())) return false;
        if (!getAgreementName().equals(that.getAgreementName())) return false;
        if (!getPrimaryContactEmail().equals(that.getPrimaryContactEmail())) return false;
        if (!getAgreementBeginDate().equals(that.getAgreementBeginDate())) return false;
        if (!getDataUsagePeriod().equals(that.getDataUsagePeriod())) return false;
        if (!getUseCases().equals(that.getUseCases())) return false;
        if (getUploadStatus() != that.getUploadStatus()) return false;
        if (!getDataOriginCountriesAndStates().equals(that.getDataOriginCountriesAndStates())) return false;
        return getDataLocationAllowed() == that.getDataLocationAllowed();
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDeidStatus().hashCode();
        result = 31 * result + getActive().hashCode();
        result = 31 * result + (isContractExpired() ? 1 : 0);
        result = 31 * result + getUploadBy().hashCode();
        result = 31 * result + getUploadDate().hashCode();
        result = 31 * result + getAgreementName().hashCode();
        result = 31 * result + getPrimaryContactEmail().hashCode();
        result = 31 * result + getAgreementBeginDate().hashCode();
        result = 31 * result + getDataUsagePeriod().hashCode();
        result = 31 * result + getUseCases().hashCode();
        result = 31 * result + getUploadStatus().hashCode();
        result = 31 * result + getDataOriginCountriesAndStates().hashCode();
        result = 31 * result + getDataLocationAllowed().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ContractByDataSetId{" +
                "id=" + id +
                ", deidStatus=" + deidStatus +
                ", active='" + active + '\'' +
                ", contractExpired=" + contractExpired +
                ", uploadBy='" + uploadBy + '\'' +
                ", uploadDate=" + uploadDate +
                ", agreementName='" + agreementName + '\'' +
                ", primaryContactEmail='" + primaryContactEmail + '\'' +
                ", agreementBeginDate='" + agreementBeginDate + '\'' +
                ", dataUsagePeriod='" + dataUsagePeriod + '\'' +
                ", useCases=" + useCases +
                ", uploadStatus=" + uploadStatus +
                ", dataOriginCountriesAndStates=" + dataOriginCountriesAndStates +
                ", dataLocationAllowed=" + dataLocationAllowed +
                '}';
    }

    /**
     * Creates a map of active and inactive ContractByDataSetIds from the given list of contract entities
     * @param contractList - list of contract entities
     * @return - map of active and inactive ContractByDataSetIds
     */
    public static Map<String,List<ContractByDataSetId>> fromDataSetEntities(List<Contract> contractList) {
        return contractList.stream().map(ContractByDataSetId::fromContractEntity)
                .collect(Collectors.groupingBy(ContractByDataSetId::getActive));
    }

    /**
     * Converts the contract Entity to ContractByDataSetId
     * @param contract - contract Entity
     * @return ContractByDataSetId
     */
    public static ContractByDataSetId fromContractEntity(Contract contract){

        boolean contractExpired = isContractExpired(contract.getAgreementBeginDate(),contract.getDataUsagePeriod());

        return new ContractByDataSetId(contract.getId(),
                contract.getDeidStatus(),
                contract.getActive(),
                contractExpired,
                contract.getUploadBy(),
                contract.getUploadDate(),
                contract.getAgreementName(),
                contract.getPrimaryContactEmail(),
                contract.getAgreementBeginDate(),
                contract.getDataUsagePeriod(),
                contract.getUseCases(),
                contract.getUploadStatus(),
                contract.getDataOriginCountriesStates(),
                contract.getDataLocationAllowed());
    }

    /**
     * Verifies if a Contract has expired
     * @param agreementBeginDate - agreement begin date of the contract
     * @param dataUsagePeriod - dataUsagePeriod of the contract
     * @return true if contract expired.
     */
    public static boolean isContractExpired(String agreementBeginDate, String dataUsagePeriod){
        //set the current system date
        java.util.Date currentDate = new java.util.Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();

        //specify the input string date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        java.util.Date agreementDate = new java.util.Date();

        try {
            //convert String agreementBeginDate to util.Date
            agreementDate = formatter.parse(agreementBeginDate);
        }catch (Exception e){
            logger.error("Error parsing agreementBeginDate :{}", e.getMessage());
        }

        c.setTime(agreementDate);

        int yearsToAdd = Integer.valueOf(dataUsagePeriod)/12;
        // manipulate date
        c.add(Calendar.YEAR, yearsToAdd);

        // convert calendar to date
        java.util.Date expirationDate = c.getTime();

        return currentDate.equals(expirationDate) || currentDate.after(expirationDate);
    }


}
