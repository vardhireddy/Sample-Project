/*
 *  LabelDBResultToJsonConverter.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.LabelAnnotationJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code LabelDBResultToJsonConverter} converts a DB result record which describes a label into a bean that encapsulates its JSON representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelDBResultToJsonConverter implements DBResultToJsonConverter<LabelAnnotationJson> {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a new {@code LabelDBResultToJsonConverter}.
     */
    public LabelDBResultToJsonConverter() {

    }

    @Override
    public LabelAnnotationJson getJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException {

        // Extract the individual GE classes and aggregate them in a list.  Only include non-null values.
        Integer[] geClassIndices = resultIndiciesMap.get("geClasses");
        List<GEClass> geClasses = new ArrayList<>();

        for (Integer index : geClassIndices) {
            if (result[index] != null) {
                geClasses.add(toGEClass(result[index]));
            }
        }

        return new LabelAnnotationJson(
                (String) result[resultIndexMap.get("patientID")],
                (String) result[resultIndexMap.get("seriesUID")],
                (String) result[resultIndexMap.get("imageSetFormat")],
                (Long) result[resultIndexMap.get("annotationID")],
                (String) result[resultIndexMap.get("annotationType")],
                geClasses
        );
    }

    /**
     * Converts the provided {@code} Object representation of a GE class into its bean representation.
     *
     * @param geClass The {@code} Object representation of the GE class to be converted
     * @return The bean representation i.e. {@link GEClass}
     * @throws InvalidAnnotationException if the {@code Object representation} of the GE class does not encode a JSON string
     */
    private static GEClass toGEClass(Object geClass) throws InvalidAnnotationException {
        try {
            return (GEClass) mapper.readValue(geClass.toString(), GEClass.class);
        } catch (IOException e) {
            throw new InvalidAnnotationException(e.getMessage());
        }
    }

}
