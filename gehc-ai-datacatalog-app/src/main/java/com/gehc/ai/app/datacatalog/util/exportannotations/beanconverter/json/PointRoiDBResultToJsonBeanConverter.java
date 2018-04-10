/*
 *  PointRoiDBResultToJsonBeanConverter.java
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

import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.PointRoiAnnotationJson;

import java.util.Map;

import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToListOfDoubles;
import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToString;
import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToInteger;

/**
 * {@code PointRoiDBResultToJsonBeanConverter} converts a DB result record which describes a point ROI into a bean that encapsulates its JSON representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class PointRoiDBResultToJsonBeanConverter implements DBResultToJsonBeanConverter<PointRoiAnnotationJson> {

    @Override
    public PointRoiAnnotationJson getJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException {

        return new PointRoiAnnotationJson(
                (String) result[resultIndexMap.get("patientID")],
                (String) result[resultIndexMap.get("seriesUID")],
                (String) result[resultIndexMap.get("imageSetFormat")],
                (Long) ((Integer) result[resultIndexMap.get("annotationID")]).longValue(),
                (String) result[resultIndexMap.get("annotationType")],
                mapToString(result[resultIndexMap.get("coordSys")]),
                mapToListOfDoubles(result[resultIndexMap.get("roiData")]),
                mapToString(result[resultIndexMap.get("roiLocalID")]),
                mapToString(result[resultIndexMap.get("roiName")]),
                mapToInteger(result[resultIndexMap.get("roiIndex")])
        );
    }


}
