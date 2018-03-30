/*
 *  RoiDataConverter.java
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

import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;

import java.util.List;

/**
 * {@code RoiDataConverter} should be implemented by those classes which intend to convert an {@code Object}, that encapsulates
 * data on how to render an ROI instance, into a 2D {@code List} of {@code Double}s.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public interface RoiDataConverter {

    /**
     * Converts the provided {@code Object} that encapsulates data that describes how to render an ROI instance to a 2D {@code List} of {@Double}s.
     *
     * @param roiObj The {@code Object} that encapsulates data that describes how to render an ROI instance
     * @return a 2D {@code List} of {@code Double}s
     * @throws InvalidAnnotationException if the {@code Object representation} doesn't encapsulate an ROI
     */
    List<List<Double>> convertRoiData(Object roiObj) throws InvalidAnnotationException;

}
