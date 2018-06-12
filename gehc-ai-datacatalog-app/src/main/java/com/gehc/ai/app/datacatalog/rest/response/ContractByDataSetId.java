package com.gehc.ai.app.datacatalog.rest.response;

import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public ContractByDataSetId() {
    }

    public ContractByDataSetId(Long id, Contract.DeidStatus deidStatus, String active, boolean hasContractExpired, String uploadBy, Date uploadDate, String agreementName, String primaryContactEmail, String agreementBeginDate, String dataUsagePeriod, List<ContractUseCase> useCases, Contract.UploadStatus uploadStatus, List<ContractDataOriginCountriesStates> dataOriginCountriesAndStates, Contract.DataLocationAllowed dataLocationAllowed) {
        this.id = id;
        this.deidStatus = deidStatus;
        this.active = active;
        this.hasContractExpired = hasContractExpired;
        this.uploadBy = uploadBy;
        this.uploadDate = uploadDate;
        this.agreementName = agreementName;
        this.primaryContactEmail = primaryContactEmail;
        this.agreementBeginDate = agreementBeginDate;
        this.dataUsagePeriod = dataUsagePeriod;
        this.useCases = useCases;
        this.uploadStatus = uploadStatus;
        DataOriginCountriesAndStates = dataOriginCountriesAndStates;
        DataLocationAllowed = dataLocationAllowed;
    }

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

    public boolean hasContractExpired() {
        return hasContractExpired;
    }

    public void setHasContractExpired(boolean hasContractExpired) {
        this.hasContractExpired = hasContractExpired;
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
        }catch (Exception e){}

        c.setTime(agreementDate);

        int yearsToAdd = Integer.valueOf(dataUsagePeriod)/12;
        // manipulate date
        c.add(Calendar.YEAR, yearsToAdd);

        // convert calendar to date
        java.util.Date expirationDate = c.getTime();

        return currentDate.equals(expirationDate) || currentDate.after(expirationDate);
    }


}
