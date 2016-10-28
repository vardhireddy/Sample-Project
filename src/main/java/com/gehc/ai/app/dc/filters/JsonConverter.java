package com.gehc.ai.app.dc.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;

/**
 * Created by 200014175 on 10/18/2016.
 * Adapted from: http://stackoverflow.com/questions/25738569/jpa-map-json-column-to-java-object
 */
public class JsonConverter implements AttributeConverter<Object, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object meta) {
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
    public Object convertToEntityAttribute(String dbData) {
        try {
        		if(null != dbData){
        			return objectMapper.readValue(dbData, Object.class);
        		}else{
        			return null;
        		}
        } catch (IOException ex) {
            // logger.error("Unexpected IOEx decoding json from database: " + dbData);
            return null;
        }
    }

}
