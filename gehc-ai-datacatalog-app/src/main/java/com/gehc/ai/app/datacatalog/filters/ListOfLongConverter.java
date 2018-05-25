/*
 * ListOfLongConverter.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * {@code ListOfLongConverter} converts {@code List<Long>} entities to their DB representations and vice-versa.
 *
 * Adapted from: http://stackoverflow.com/questions/25738569/jpa-map-json-column-to-java-object
 */
public class ListOfLongConverter implements AttributeConverter<List<Long>, String> {

    private static Logger logger = LoggerFactory.getLogger(ListOfLongConverter.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> meta) {
        try {
            if (null != meta) {
                return objectMapper.writeValueAsString(meta);
            } else {
                return null;
            }
        } catch (JsonProcessingException ex) {
            logger.error("Could not convert List<Long> to data base column " + ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        try {
            if (null != dbData) {
                return objectMapper.readValue(dbData, new TypeReference<List<Long>>() {
                });
            } else {
                return Collections.emptyList();
            }
        } catch (IOException ex) {
            logger.error("Could not convert DB column to List<Long> " + ex.getMessage());
            return Collections.emptyList();
        }
    }

}
