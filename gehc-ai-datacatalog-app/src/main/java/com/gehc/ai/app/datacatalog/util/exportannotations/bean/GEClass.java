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
package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code GEClass} is entity that represents a GE class label annotation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class GEClass {
    /**
     * {@link ObjectMapper} to use when converting an {@code Object} reprsentation into its {@code GEClass} bean representation.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

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
     * @param name  The name of the GE class
     * @param value The value of the GE class
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

    @Override
    public boolean equals(Object o) {
        // Auto-generated
        if (this == o) {
            return true;
        }
        if (!(o instanceof GEClass)) {
            return false;
        }

        GEClass geClass = (GEClass) o;
        if (getName() != null ? !getName().equals(geClass.getName()) : geClass.getName() != null) {
            return false;
        }
        return getValue() != null ? getValue().equals(geClass.getValue()) : geClass.getValue() == null;
    }

    @Override
    public int hashCode() {
        // Auto-generated
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }

    /**
     * Converts the provided {@code} Object representation of a GE class into its bean representation.
     *
     * @param geClass The {@code} Object representation of the GE class to be converted
     * @return The bean representation i.e. {@link GEClass}
     * @throws InvalidAnnotationException if the {@code Object representation} of the GE class does not encode a JSON string
     */
    public static GEClass toGEClass(Object geClass) throws InvalidAnnotationException {
        try {
            return (GEClass) mapper.readValue(geClass.toString(), GEClass.class);
        } catch (IOException e) {
            throw new InvalidAnnotationException(e.getMessage());
        }
    }

    /**
     * Converts the provided DB result record, which contains GE class definitions, into a list of {@code GEClass} beans.
     *
     * @param result         The DB result record which contains GE class definitions
     * @param geClassIndices The array of indices which indicate where in the result record all the GE class definitions can be found
     * @return a {@code List} of {@code GEClass} beans
     * @throws InvalidAnnotationException
     */
    public static List<GEClass> toGEClasses(Object[] result, Integer[] geClassIndices) throws InvalidAnnotationException {
        // Extract the individual GE classes and aggregate them in a list.  Only include non-null values.
        List<GEClass> geClasses = new ArrayList<>();

        for (Integer index : geClassIndices) {
            if (result[index] != null) {
                geClasses.add(GEClass.toGEClass(result[index]));
            }
        }

        return geClasses;
    }
}
