/*
 *  BoundingBoxDBResultToJsonBeanConverter.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.BoundingBox;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.BoundingBoxAnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.BoundingBoxType;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToInteger;
import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToString;

/**
 * {@code BoundingBoxDBResultToJsonBeanConverter} converts a DB result record which describes a bouding box into a bean that encapsulates its JSON representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingBoxDBResultToJsonBeanConverter implements DBResultToJsonBeanConverter<BoundingBoxAnnotationJson> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public BoundingBoxAnnotationJson getJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException {
        return new BoundingBoxAnnotationJson(
                (String) result[resultIndexMap.get("patientID")],
                (String) result[resultIndexMap.get("seriesUID")],
                (String) result[resultIndexMap.get("imageSetFormat")],
                (Long) ((Integer) result[resultIndexMap.get("annotationID")]).longValue(),
                BoundingBoxType.valueOf(((String) result[resultIndexMap.get("annotationType")]).toUpperCase(Locale.ENGLISH)),
                mapToString(result[resultIndexMap.get("coordSys")]),
                toBoundingBox(result[resultIndexMap.get("roiData")]),
                mapToString(result[resultIndexMap.get("roiLocalID")]),
                mapToString(result[resultIndexMap.get("roiName")]),
                mapToInteger(result[resultIndexMap.get("roiIndex")])
        );
    }

    /**
     * Converts the provided {@code} Object representation of a coordinate data into its bean representation.
     *
     * @param coords The {@code} Object representation of the coordinate data to be converted
     * @return The bean representation
     * @throws InvalidAnnotationException if the {@code Object representation} of the coordinate data does not encode a JSON string
     */
    private static BoundingBox toBoundingBox(Object coords) throws InvalidAnnotationException {
        try {
            return (BoundingBox) mapper.readValue(
                    coords.toString(),
                    BoundingBox.class
            );
        } catch (IOException e) {
            throw new InvalidAnnotationException(e.getMessage());
        }
    }

}
