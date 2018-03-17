/*
 * GEClass.java
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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@code GEClass} is entity that represents a GE class label annotation.
 */
public class GEClass {
    /**
     * The name of this GE class (e.g. "Pneumothorax")
     */
    private String name;

    /**
     * The value describing this GE class (e.g. "Severe")
     */
    private String value;

    /**
     * Creates a new {@code GEClass} entity.
     *
     * @param name            The name of the GE class
     * @param value           The value of the GE class
     */
    public GEClass(@JsonProperty("name") String name, @JsonProperty("value") String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
