/*
 *  DBResultToCsvBeanConverter.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.csv;

import com.fasterxml.jackson.databind.JsonNode;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.AnnotationCsv;

import java.util.List;
import java.util.Map;

/**
 * The {@code DBResultToCsvBeanConverter} interface should be implemented by those classes which intend to convert a
 * {@link JsonNode} into a {@code List} of beans each of which encapsulates the CSV representation of an annotation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public interface DBResultToCsvBeanConverter<T extends AnnotationCsv> {

    /**
     * Returns the provided DB result record, which describes an annotation, as a {@code List} of {@code AnnotationCsv}s.
     *
     * @param result            The DB result record, which describes an annotation, to convert
     * @param resultIndexMap    A mapping of a result meta data to its index in the result record.
     * @param resultIndiciesMap A mapping of a result meta data to its indices in the result record.
     * @return a {@code List} of {@code AnnotationCsv}s
     * @throws InvalidAnnotationException if the provided {@code JsonNode} represents an annotation that does not have
     *                                    the expected properties or property values
     */
    List<T> getAnnotationBeans(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndiciesMap) throws InvalidAnnotationException;

}
