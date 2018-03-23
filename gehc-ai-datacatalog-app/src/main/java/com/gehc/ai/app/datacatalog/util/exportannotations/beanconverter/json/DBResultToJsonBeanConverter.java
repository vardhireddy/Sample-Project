/*
 *  DBResultToJsonBeanConverter.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;

import java.util.Map;

/**
 * {@code DBResultToJsonConverter} converts a DB result record which describes an annotation into a bean that encapsulates its JSON representation.
 *
 * @param <T> The type of {@link AnnotationJson} bean that will be returned by this converter.
 * @author andrew.c.wong@ge.com (212069153)
 */
public interface DBResultToJsonBeanConverter<T extends AnnotationJson> {

    /**
     * Converts the provided DB result into a bean that encapsulates its JSON representation.
     *
     * @param result            The DB result to convert
     * @param resultIndexMap    A map that, for meta data that is described by a single value, maps meta data to its index in the result record
     * @param resultIndiciesMap A map that, for meta data that is described by multiple values, maps meta data to its indices in the result record
     * @return an {@link AnnotationJson} bean representation of the DB result
     * @throws InvalidAnnotationException if the annotation described by the DB result is not well-formed
     */
    T getJson(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException;

}
