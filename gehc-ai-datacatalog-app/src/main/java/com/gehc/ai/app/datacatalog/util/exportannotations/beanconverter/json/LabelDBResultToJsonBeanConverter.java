/*
 *  LabelDBResultToJsonBeanConverter.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.LabelAnnotationJson;

import java.util.List;
import java.util.Map;

/**
 * {@code LabelDBResultToJsonBeanConverter} converts a DB result record which describes a label into a bean that encapsulates its JSON representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelDBResultToJsonBeanConverter implements DBResultToJsonBeanConverter<LabelAnnotationJson> {

    /**
     * Creates a new {@code LabelDBResultToJsonBeanConverter}.
     */
    public LabelDBResultToJsonBeanConverter() {

    }

    @Override
    public LabelAnnotationJson getJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException {

        // Extract the individual GE classes from the DB result record and aggregate them in a list.  Only include non-null values.
        Integer[] geClassIndices = resultIndiciesMap.get("geClasses");
        List<GEClass> geClasses = GEClass.toGEClasses(result, geClassIndices);

        return new LabelAnnotationJson(
                (String) result[resultIndexMap.get("patientID")],
                (String) result[resultIndexMap.get("seriesUID")],
                (String) result[resultIndexMap.get("imageSetFormat")],
                (Long) ((Integer) result[resultIndexMap.get("annotationID")]).longValue(),
                (String) result[resultIndexMap.get("annotationType")],
                geClasses,
                (String) result[resultIndexMap.get("indication")],
                (String) result[resultIndexMap.get("findings")]
        );
    }

}
