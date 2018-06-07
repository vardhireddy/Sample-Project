/*
 * ContractUseCase.java
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
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@code ContractUseCase} represents who and how data associated with a contract can be used.
 *
 * @author andrew.c.wong@ge.com (212069153), monika.jain@ge.com (212071558), uma.tabib1@ge.com (212691936)
 */
@JsonInclude(Include.NON_NULL)
public class ContractUseCase {

    private DataUser dataUser;

    private DataUsage dataUsage;

    private String dataNotes;

    /**
     * Creates a new {@code ContractUseCase}.
     */
    public ContractUseCase() {
    }

    /**
     * Creates a new {@code ContractUseCase} using the specified data user type, data usage type, and data notes.
     *
     * @param dataUser  The type of user that can use a particular data asset.
     * @param dataUsage The manner in which a particular data asset can be used.
     * @param dataNotes Additional notes that describe who and how a particular data asset can be used.
     */
    public ContractUseCase(DataUser dataUser, DataUsage dataUsage, String dataNotes) {
        super();
        this.dataUser = dataUser;
        this.dataUsage = dataUsage;
        this.dataNotes = dataNotes;
    }

    /**
     * {@code DataUser} enumerates the types of user that can use a particular data asset.
     */
    public enum DataUser {
        @JsonProperty("geOnshore")
        GE_ONSHORE,
        @JsonProperty("geGlobal")
        GE_GLOBAL,
        @JsonProperty("partnersOnshore")
        THIRD_PARTY_PARTNERS_ONSHORE,
        @JsonProperty("partnersGlobal")
        THIRD_PARTY_PARTNERS_GLOBAL
    }

    /**
     * {@code DataUsage} enumerates the manners in which a data asset can be used by a data user.
     */
    public enum DataUsage {
        @JsonProperty("trainingAndModel")
        TRAINING_AND_MODEL_DEVELOPMENT,
        @JsonProperty("annotationAndCuration")
        ANNOTATION_AND_CURATION,
        @JsonProperty("dataSharing")
        DATA_SHARING
    }

    public String getDataNotes() {
        return dataNotes;
    }

    public void setDataNotes(String dataNotes) {
        this.dataNotes = dataNotes;
    }

    public DataUser getDataUser() {
        return dataUser;
    }

    public void setDataUser(DataUser dataUser) {
        this.dataUser = dataUser;
    }

    public DataUsage getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(DataUsage dataUsage) {
        this.dataUsage = dataUsage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractUseCase that = (ContractUseCase) o;

        if (getDataUser() != that.getDataUser()) return false;
        return getDataUsage() == that.getDataUsage();
    }

    @Override
    public int hashCode() {
        int result = getDataUser().hashCode();
        result = 31 * result + getDataUsage().hashCode();
        return result;
    }

}