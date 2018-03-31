/*
 *  BoundingCubeDataConverter.java
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.BoundingCube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code BoundingCubeDataConverter} converts a DB result record, which describes a freeform ROI annotation, to its corresponding CSV bean(s) representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingCubeDataConverter implements RoiDataConverter {

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Converts the provided {@code Object} that encapsulates data that describes how to render an ROI instance to a 2D {@code List} of {@Double}s.
     *
     * @param boundingCubeObj An {@code Object} that encapsulates a bounding cube
     * @return a 2D {@code List} of {@code Double}s
     * @throws InvalidAnnotationException if the {@code Object representation} doesn't encapsulate a bounding cube
     */
    @Override
    public List<List<Double>> convertRoiData(Object boundingCubeObj) throws InvalidAnnotationException {
        BoundingCube boundingCube = toBoundingCube(boundingCubeObj);
        List<List<Double>> data = new ArrayList<>();
        data.add(boundingCube.getxdir());
        data.add(boundingCube.getydir());
        data.add(boundingCube.getzdir());
        data.add(boundingCube.getOrigin());

        return data;
    }

    /**
     * Converts the provided {@code} Object representation of a bounding cube into its bean representation.
     *
     * @param boundingCube The {@code} Object representation of the bounding cube to be converted
     * @return The bean representation
     * @throws InvalidAnnotationException if the {@code Object representation} doesn't encapsulate a bounding cube
     */
    private static BoundingCube toBoundingCube(Object boundingCube) throws InvalidAnnotationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (BoundingCube) mapper.readValue(boundingCube.toString(), BoundingCube.class);
        } catch (IOException e) {
            throw new InvalidAnnotationException("The provided object," + boundingCube + ", was expected to represent a bounding cube but does not.");
        }
    }

}
