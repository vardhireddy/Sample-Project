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
import java.util.Objects;

/**
 * {@code GEClass} is entity that represents a GE class label annotation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class GEClass {
    /**
     * {@link ObjectMapper} to use when converting an {@code Object} reprsentation into its {@code GEClass} bean representation.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * The name of this GE class (e.g. "Pneumothorax")
     */
    private final String name;

    /**
     * The value describing this GE class (e.g. "Severe")
     */
    private final String value;

    /**
     * The patient outcome associated with the GE class.
     *
     * @deprecated The patient outcome is no longer supported because this attribute only applies to a particular institution's
     * data and therefore is not extensible to other institution's data
     */
    @Deprecated
    private final String patientOutcome;

    /**
     * Creates a new {@code GEClass} entity.
     *
     * @param name           (Required) The name of the GE class
     * @param value          (Optional)The value of the GE class
     * @param patientOutcome (Optional)(Deprecated) The patient outcome associated with the GE class.
     */
    public GEClass(@JsonProperty("name") String name, @JsonProperty("value") String value, @JsonProperty("patient_outcome") @Deprecated String patientOutcome) {
        this.name = Objects.requireNonNull(name);
        this.value = value;
        this.patientOutcome = patientOutcome;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    /**
     * Gets the patient outcome.
     *
     * @deprecated The patient outcome is no longer supported because this attribute only applies to a particular institution's
     * data and therefore is not extensible to other institution's data
     */
    @Deprecated
    public String getPatientOutcome() {
        return patientOutcome;
    }

    @Override
    public final boolean equals(Object o) {
        // Auto-generated
        if (this == o) return true;
        if (o == null) return false;
        if (!(o.getClass() == this.getClass())) return false;

        GEClass geClass = (GEClass) o;

        if (!getName().equals(geClass.getName())) return false;
        if (getValue() != null ? (!getValue().equals(geClass.getValue())) : (geClass.getValue() != null)) return false;
        return (getPatientOutcome() != null) ? (getPatientOutcome().equals(geClass.getPatientOutcome())) : (geClass.getPatientOutcome() == null);
    }

    @Override
    public final int hashCode() {
        // Auto-generated
        int result = getName().hashCode();
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getPatientOutcome() != null ? getPatientOutcome().hashCode() : 0);
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
