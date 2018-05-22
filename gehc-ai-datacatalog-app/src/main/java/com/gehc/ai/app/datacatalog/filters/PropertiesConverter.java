/*
 * PropertiesConverter.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Properties;

import javax.persistence.AttributeConverter;
import java.io.IOException;

/**
 * Created by 200014175 on 10/18/2016.
 * Adapted from: http://stackoverflow.com/questions/25738569/jpa-map-json-column-to-java-object
 */
public class PropertiesConverter implements AttributeConverter<Properties, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Properties meta) {
        try {
        		if(null != meta){
        			return objectMapper.writeValueAsString(meta);
        		}else{
        			return null;
        		}
        } catch (JsonProcessingException ex) {
            return null;
            // or throw an error
        }
    }

    @Override
    public Properties convertToEntityAttribute(String dbData) {
        try {
        		if(null != dbData){
        			return objectMapper.readValue(dbData, Properties.class);
        		}else{
        			return null;
        		}
        } catch (IOException ex) {
            // logger.error("Unexpected IOEx decoding json from database: " + dbData);
            return null;
        }
    }

}
