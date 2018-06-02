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
 * {@code CondensedDataCollection} is a POJO that is structurally similar to the {@link DataSet} entity with the following differences:
 *
 * <ul>
 *     <li>It does not store the list of image IDs like the {@code DataSet} entity and instead stores the number of image sets.</li>
 *     <li>It does not store the schema version, properties, or filters like the {@code DataSet} entity</li>
 * </ul>
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class CondensedDataCollection {

    ////////////////
    //
    // Properties //
    //
    ////////////////

    private final Long id;

    private final String name;

    private final String description;

    private final String type;

    private final String createdBy;

    private final String createdDate;

    private final String orgId;

    private final Integer numImageSets;

    /////////////////
    //
    // Constructor //
    //
    /////////////////

    public CondensedDataCollection(Long id, String name, String description, String type, String createdBy, String createdDate, String orgId, Integer numImageSets) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.orgId = orgId;
        this.numImageSets = numImageSets;
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

    public Integer getNumImageSets() {
        return numImageSets;
    }

    //////////////////
    //
    // Utility APIs //
    //
    //////////////////

    /**
     * Returns the provided {@code List} of {@link DataSet} entities as a {@code List} of {@link CondensedDataCollection}.
     *
     * @param dataSets the {@code List} of {@code DataSet}s to convert.
     * @return a {@code List} of {@code CondensedDataCollection}
     */
    public static List<CondensedDataCollection> fromDataSetEntities(List<DataSet> dataSets) {
        return dataSets.stream().map(CondensedDataCollection::fromDataSetEntity).collect(Collectors.toList());
    }

    /**
     * Returns the provided {@link DataSet} entity as a {@link CondensedDataCollection}.
     *
     * @param dataSet the {@code DataSet}s to convert.
     * @return a {@code CondensedDataCollection}
     */
    public static CondensedDataCollection fromDataSetEntity(DataSet dataSet) {
        return new CondensedDataCollection(
                dataSet.getId(),
                dataSet.getName(),
                dataSet.getDescription(),
                dataSet.getType(),
                dataSet.getCreatedBy(),
                dataSet.getCreatedDate(),
                dataSet.getOrgId(),
                dataSet.getImageSets().size()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != this.getClass()) return false;

        CondensedDataCollection that = (CondensedDataCollection) o;

        if (!getId().equals(that.getId())) return false;
        if (!getName().equals(that.getName())) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (!getType().equals(that.getType())) return false;
        if (!getCreatedBy().equals(that.getCreatedBy())) return false;
        if (!getCreatedDate().equals(that.getCreatedDate())) return false;
        if (!getOrgId().equals(that.getOrgId())) return false;
        return getNumImageSets().equals(that.getNumImageSets());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getType().hashCode();
        result = 31 * result + getCreatedBy().hashCode();
        result = 31 * result + getCreatedDate().hashCode();
        result = 31 * result + getOrgId().hashCode();
        result = 31 * result + getNumImageSets().hashCode();
        return result;
    }
}
