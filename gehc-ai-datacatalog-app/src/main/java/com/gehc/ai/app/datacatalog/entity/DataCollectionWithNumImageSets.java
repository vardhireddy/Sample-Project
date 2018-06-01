/*
 * DataCollectionWithNumImageSets.java
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code DataCollectionWithNumImageSets} is a POJO that is structurally similar to the {@link DataSet} entity with the distinction
 * that it does not store the list of image IDs like the {@code DataSet} entity and instead stores the number of image sets.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class DataCollectionWithNumImageSets {

    ////////////////
    //
    // Properties //
    //
    ////////////////

    private final Long id;

    private final String name;

    private final String schemaVersion;

    private final String description;

    private final String type;

    private final String createdBy;

    private final String createdDate;

    private final String orgId;

    private final Properties properties;

    private final Integer numImageSets;

    private final Filters filters;

    /////////////////
    //
    // Constructor //
    //
    /////////////////

    public DataCollectionWithNumImageSets(Long id, String name, String schemaVersion, String description, String type, String createdBy, String createdDate, String orgId, Properties properties, Integer numImageSets, Filters filters) {
        this.id = id;
        this.name = name;
        this.schemaVersion = schemaVersion;
        this.description = description;
        this.type = type;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.orgId = orgId;
        this.properties = properties;
        this.numImageSets = numImageSets;
        this.filters = filters;
    }

    /////////////
    //
    // Getters //
    //
    /////////////

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getOrgId() {
        return orgId;
    }

    public Properties getProperties() {
        return properties;
    }

    public Filters getFilters() {
        return filters;
    }

    public Integer getNumImageSets() {
        return numImageSets;
    }

    //////////////////
    //
    // Utility APIs //
    //
    //////////////////

    /**
     * Returns the provided {@code List} of {@link DataSet} entities as a {@code List} of {@link DataCollectionWithNumImageSets}.
     *
     * @param dataSets the {@code List} of {@code DataSet}s to convert.
     * @return a {@code List} of {@code DataCollectionWithNumImageSets}
     */
    public static List<DataCollectionWithNumImageSets> fromDataSetEntities(List<DataSet> dataSets) {
        return dataSets.stream().map(DataCollectionWithNumImageSets::fromDataSetEntity).collect(Collectors.toList());
    }

    /**
     * Returns the provided {@link DataSet} entity as a {@link DataCollectionWithNumImageSets}.
     *
     * @param dataSet the {@code DataSet}s to convert.
     * @return a {@code DataCollectionWithNumImageSets}
     */
    public static DataCollectionWithNumImageSets fromDataSetEntity(DataSet dataSet) {
        return new DataCollectionWithNumImageSets(
                dataSet.getId(),
                dataSet.getName(),
                dataSet.getSchemaVersion(),
                dataSet.getDescription(),
                dataSet.getType(),
                dataSet.getCreatedBy(),
                dataSet.getCreatedDate(),
                dataSet.getOrgId(),
                dataSet.getProperties(),
                dataSet.getImageSets().size(),
                dataSet.getFilters()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataCollectionWithNumImageSets that = (DataCollectionWithNumImageSets) o;

        if (!getId().equals(that.getId())) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getSchemaVersion().equals(that.getSchemaVersion())) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (!getType().equals(that.getType())) return false;
        if (!getCreatedBy().equals(that.getCreatedBy())) return false;
        if (!getCreatedDate().equals(that.getCreatedDate())) return false;
        if (!getOrgId().equals(that.getOrgId())) return false;
        if (getProperties() != null ? !getProperties().equals(that.getProperties()) : that.getProperties() != null)
            return false;
        if (!getNumImageSets().equals(that.getNumImageSets())) return false;
        return getFilters().equals(that.getFilters());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getSchemaVersion().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getType().hashCode();
        result = 31 * result + getCreatedBy().hashCode();
        result = 31 * result + getCreatedDate().hashCode();
        result = 31 * result + getOrgId().hashCode();
        result = 31 * result + (getProperties() != null ? getProperties().hashCode() : 0);
        result = 31 * result + getNumImageSets().hashCode();
        result = 31 * result + getFilters().hashCode();
        return result;
    }
}
