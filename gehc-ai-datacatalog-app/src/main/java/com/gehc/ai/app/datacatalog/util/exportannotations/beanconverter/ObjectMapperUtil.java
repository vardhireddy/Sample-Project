/*
 *  ObjectMapperUtil.java
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;

import java.io.IOException;
import java.util.List;

/**
 * {@code ObjectMapperUtil} provides utility for mapping JSON strings to their {@code Object} representations.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class ObjectMapperUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Maps a JSON string that only represents string content (i.e. a stringified string) to a corresponding {@link String} representation.
     *
     * @param strObj The {@code Object} which encapsulates the JSON string
     * @return a {@code String}
     * @throws InvalidAnnotationException if the provided {@code Object} does not encapsulate a JSON string
     */
    public static String mapToString(Object strObj) throws InvalidAnnotationException {
        try {
            return strObj != null ? (String) mapper.readValue(strObj.toString(), String.class) : (String) strObj;
        } catch (IOException e) {
            throw new InvalidAnnotationException("The provided annotation property, " + strObj + ", was expected to be a JSON string but was not.");
        }
    }

    /**
     * Maps a JSON string that only represents a list of double values to a corresponding {@link List<Double>} representation.
     *
     * @param strObj The {@code Object} which encapsulates the JSON string
     * @return a {@code List<Double>}
     * @throws InvalidAnnotationException if the provided {@code Object} does not encapsulate a JSON string
     */
    public static List<Double> mapToListOfDoubles(Object strObj) throws InvalidAnnotationException {
        try {
            return strObj != null ? (List<Double>) mapper.readValue(strObj.toString(), new TypeReference<List<Double>>() {
            }) : (List<Double>) strObj;
        } catch (IOException e) {
            throw new InvalidAnnotationException("The provided annotation property, " + strObj + ", was expected to be a JSON string but was not.");
        }
    }

    /**
     * Maps a JSON string that only represents a list of strings to a corresponding {@link List<String>} representation.
     *
     * @param strObj The {@code Object} which encapsulates the JSON string
     * @return a {@code List<String>}
     * @throws InvalidAnnotationException if the provided {@code Object} does not encapsulate a JSON string
     */
    public static List<String> mapToListOfStrings(Object strObj) throws InvalidAnnotationException {
        try {
            return strObj != null ? (List<String>) mapper.readValue(strObj.toString(), new TypeReference<List<String>>() {
            }) : (List<String>) strObj;
        } catch (IOException e) {
            throw new InvalidAnnotationException("The provided annotation property, " + strObj + ", was expected to be a JSON string but was not.");
        }
    }

}
