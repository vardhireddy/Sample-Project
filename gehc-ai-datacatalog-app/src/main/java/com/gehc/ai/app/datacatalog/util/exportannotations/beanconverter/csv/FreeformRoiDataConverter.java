/*
 *  FreeformRoiDataConverter.java
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;

import java.io.IOException;
import java.util.List;

/**
 * {@code FreeformRoiDataConverter} converts a DB result record, which describes a freeform ROI annotation, to its corresponding CSV bean(s) representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class FreeformRoiDataConverter implements RoiDataConverter {

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Converts the provided {@code Object} that encapsulates data that describes how to render an ROI instance to a 2D {@code List} of {@Double}s.
     *
     * @param freeFormRoiObj An {@code Object} that encapsulates a freeform ROI
     * @return a 2D {@code List} of {@code Double}s
     * @throws InvalidAnnotationException if the {@code Object representation} doesn't encapsulate a freeform ROI
     */
    @Override
    public List<List<Double>> convertRoiData(Object freeFormRoiObj) throws InvalidAnnotationException{
        return toCoordsList(freeFormRoiObj);
    }

    /**
     * Converts the provided {@code} Object representation of a coordinate data into its bean representation.
     *
     * @param freeFormRoiObj The {@code} Object representation of the coordinate data to be converted
     * @return The bean representation
     * @throws InvalidAnnotationException if the {@code Object representation} of the coordinate data does not encode a nested list of doubles
     */
    private static List<List<Double>> toCoordsList(Object freeFormRoiObj) throws InvalidAnnotationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (List<List<Double>>) mapper.readValue(
                    freeFormRoiObj.toString(),
                    new TypeReference<List<List<Double>>>() {
                    });
        } catch (IOException e) {
            throw new InvalidAnnotationException(e.getMessage());
        }
    }

}
