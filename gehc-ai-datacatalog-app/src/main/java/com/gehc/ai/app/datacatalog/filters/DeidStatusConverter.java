/*
 * DeidStatusConverter.java
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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gehc.ai.app.datacatalog.entity.Contract.DeidStatus;

import java.io.IOException;

@Converter(autoApply = false)
public class DeidStatusConverter implements AttributeConverter<DeidStatus, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DeidStatus deidStatus) {
        try {
            if (null != deidStatus) {
                return objectMapper.writeValueAsString(deidStatus);
            } else {
                return null;
            }
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    @Override
    public DeidStatus convertToEntityAttribute(String dbData) {
        try {
            if (null != dbData) {
                return objectMapper.readValue(dbData, DeidStatus.class);
            } else {
                return null;
            }
        } catch (IOException ex) {
            return null;
        }
    }
}
