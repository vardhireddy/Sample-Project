/*
 *  FreeformRoiDBResultToJsonBeanConverter.java
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.FreeformRoi;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.FreeformRoiAnnotationJson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToString;

/**
 * {@code LabelDBResultToJsonBeanConverter} converts a DB result record which describes a freeform ROI into a bean that encapsulates its JSON representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class FreeformRoiDBResultToJsonBeanConverter implements DBResultToJsonBeanConverter<FreeformRoiAnnotationJson> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public FreeformRoiAnnotationJson getJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException {

        return new FreeformRoiAnnotationJson(
                (String) result[resultIndexMap.get("patientID")],
                (String) result[resultIndexMap.get("seriesUID")],
                (String) result[resultIndexMap.get("imageSetFormat")],
                (Long) ((Integer) result[resultIndexMap.get("annotationID")]).longValue(),
                FreeformRoi.valueOf(((String) result[resultIndexMap.get("annotationType")]).toUpperCase(Locale.ENGLISH)),
                mapToString(result[resultIndexMap.get("coordSys")]),
                toCoordsList(result[resultIndexMap.get("roiData")]),
                mapToString( result[resultIndexMap.get("roiLocalID")]),
                mapToString(result[resultIndexMap.get("roiName")])
        );
    }

    /**
     * Converts the provided {@code} Object representation of a coordinate data into its bean representation.
     *
     * @param coords The {@code} Object representation of the coordinate data to be converted
     * @return The bean representation
     * @throws InvalidAnnotationException if the {@code Object representation} of the coordinate data does not encode a JSON string
     */
    private static List<List<Double>> toCoordsList(Object coords) throws InvalidAnnotationException {
        try {
            return (List<List<Double>>) mapper.readValue(
                    coords.toString(),
                    new TypeReference<List<List<Double>>>() {
                    });
        } catch (IOException e) {
            throw new InvalidAnnotationException(e.getMessage());
        }
    }

}
