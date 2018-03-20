/*
 *  MetaDataTypes.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.exportannotations;

/**
 * {@code MetaDataTypes} enumerate the types of the meta data that an annotation can contain.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public enum MetaDataTypes {

    /**
     * Describes an annotation that contains only the required meta data for its annotation type.
     */
    REQUIRED_ONLY("only the required meta data"),

    /**
     * Describes an annotation that contains both the required optional meta data for its annotation type.
     */
    REQUIRED_AND_OPTIONAL("both required and optional meta data");

    private String description;

    /**
     * Creates a new {@code MetaDataTypes} enum with the provided description.
     *
     * @param description A description of this instance
     */
    MetaDataTypes(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
