/*
 *  JsonAnnotationDetailsExporter.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations;

import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@code JsonAnnotationDetailsExporter} exports DB result records, which describe annotations, as a list of beans which ecapsulate the JSON representations of those annotations.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class JsonAnnotationDetailsExporter {

    /**
     * Prevents the {@code JsonAnnotationDetailsExporter} class from being instantiated.
     */
    private JsonAnnotationDetailsExporter() {
        throw new AssertionError("JsonAnnotationDetailsExporter is a utility class and should not be instantiated.");
    }

    /**
     * Exports the provided DB result records, which describes annotations, as a CSV string.
     *
     * @param results          The DB result records which describe annotations
     * @param resultIndexMap   A mapping of single-valued result meta data to their index in the result record.
     * @param resultIndicesMap A mapping of multi-valued result meta data to their indices in the result record.
     * @return a {@code List} of {@link AnnotationJson} beans
     * @throws InvalidAnnotationException if at least one of the annotations described the DB result record is not a well-formed annotation
     */
    public static final List<AnnotationJson> exportAsJson(List<Object[]> results, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
        List<AnnotationJson> annotationDetails = new ArrayList<>();

        if (null != results && !results.isEmpty()) {
            for (final Object[] result : results) {
                String annotationTypeAsStr = (String) result[resultIndexMap.get("annotationType")];
                AnnotationType annotationType = AnnotationType.valueOf(annotationTypeAsStr.toUpperCase(Locale.ENGLISH));
                AnnotationJson dbResultAsJson = annotationType.convertDBResultToJson(result, resultIndexMap, resultIndicesMap);
                annotationDetails.add(dbResultAsJson);
            }
        }

        return annotationDetails;
    }

}
