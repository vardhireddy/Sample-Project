/*
 *  JsonToCsvBeanConverter.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.Annotation;

import java.util.List;

/**
 * The {@code JsonToCsvBeanConverter} interface should be implemented by those classes which intend to convert a
 * {@link JsonNode} into a {@code List} of beans each of which encapsulates the CSV representation of an annotation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public interface JsonToCsvBeanConverter<T extends Annotation> {

    /**
     * Returns the provided {@link JsonNode} as a {@code List} of {@code Annotation}s.
     *
     * @param node The {@code JsonNode} to convert
     * @return a {@code List} of {@code Annotation}s
     * @throws InvalidAnnotationException if the provided {@code JsonNode} represents an annotation that does not have
     *                                    the expected properties or property values
     */
    List<T> getAnnotationBeans(JsonNode node) throws InvalidAnnotationException;

}
