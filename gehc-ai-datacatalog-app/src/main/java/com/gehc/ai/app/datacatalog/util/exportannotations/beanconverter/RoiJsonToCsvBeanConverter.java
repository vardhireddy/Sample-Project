/*
 *  RoiJsonToCsvBeanConverter.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.ImageSetAssociation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.RoiAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code RoiJsonToCsvBeanConverter} converts a JSON representation of an ROI annotation to its corresponding bean(s) representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class RoiJsonToCsvBeanConverter implements JsonToCsvBeanConverter {

    /**
     * Creates a new {@code RoiJsonToCsvBeanConverter}.
     */
    public RoiJsonToCsvBeanConverter() {

    }

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Returns the provided {@link JsonNode} as a {@code List} of {@code RoiAnnotation} beans.
     *
     * @return a {@code List} of {@code RoiAnnotation} beans
     */
    @Override
    public List<RoiAnnotation> getAnnotationBeans(JsonNode roiNode) throws InvalidAnnotationException {
        List<RoiAnnotation> roiBeans = new ArrayList<>();

        final RoiAnnotation roiBean;

        if (ImageSetAssociation.isAssociatedWithDicom(roiNode)) {
            roiBean = createDicomRoi(roiNode);
        } else if (ImageSetAssociation.isAssociatedWithNonDicom(roiNode)) {
            roiBean = createNonDicomRoi(roiNode);
        } else {
            throw new InvalidAnnotationException("The provided annotation does not map to either a DICOM or non-DICOM image set!");
        }

        roiBeans.add(roiBean);

        return roiBeans;
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    /**
     * Creates a {@code RoiAnnotation} that is associated with a DICOM image set whose contents are derived from the provided JSON representation of a ROI
     *
     * @param roiNode The parent {@link JsonNode} object that should contain information such as the series instance UID and annotation type
     * @return a new {@code RoiAnnotation}
     * @throws InvalidAnnotationException if the provided JSON representation contains coordinates that are not represented as an array of double values
     */
    private static RoiAnnotation createDicomRoi(JsonNode roiNode) throws InvalidAnnotationException {
        final String seriesUID = roiNode.get("seriesUID").textValue();
        final Map<String, Object> commonMetaData = getCommonMetaData(roiNode);

        return new RoiAnnotation(
                seriesUID,
                (String) commonMetaData.get("annotationType"),
                (String) commonMetaData.get("coordSys"),
                (List<List<Double>>) commonMetaData.get("data"),
                (String) commonMetaData.get("localID"),
                (String) commonMetaData.get("name")
        );
    }

    /**
     * Creates a {@code RoiAnnotation} that is associated with a non-DICOM image set whose contents are derived from the provided JSON representation of a ROI
     *
     * @param roiNode The parent {@link JsonNode} object that should contain information such as the non-DICOM image's original file name and the S3 space ID where the non-DICOM is stored
     * @return a new {@code RoiAnnotation}
     * @throws InvalidAnnotationException if the provided JSON representation contains coordinates that are not represented as an array of double values
     */
    private static RoiAnnotation createNonDicomRoi(JsonNode roiNode) throws InvalidAnnotationException {
        final String fileName = roiNode.get("patientId").textValue();
        final String spaceID = "";
        final Map<String, Object> commonMetaData = getCommonMetaData(roiNode);

        return new RoiAnnotation(
                fileName,
                spaceID,
                (String) commonMetaData.get("annotationType"),
                (String) commonMetaData.get("coordSys"),
                (List<List<Double>>) commonMetaData.get("data"),
                (String) commonMetaData.get("localID"),
                (String) commonMetaData.get("name")
        );
    }

    /**
     * Returns a map of meta-data properties that are applicable to both DICOM and non-DICOM ROI annotations.
     *
     * @param roiNode The parent {@link JsonNode} which should contain information common across all ROIs
     * @return a {@code Map} where the key is the meta-data property and the value is that property's value
     * @throws InvalidAnnotationException if the provided JSON representation contains coordinates that are not represented as an array of double values
     */
    private static Map<String, Object> getCommonMetaData(JsonNode roiNode) throws InvalidAnnotationException {
        Map<String, Object> commonMetaData = new HashMap<>();
        commonMetaData.put("annotationType", roiNode.get("annotationType").textValue());
        commonMetaData.put("coordSys", roiNode.get("coordSys").textValue());
        commonMetaData.put("localID", roiNode.get("localID").textValue());
        commonMetaData.put("name", roiNode.get("name").textValue());

        List<List<Double>> coords = new ArrayList<>();
        JsonNode coordNodes = roiNode.get("data");
        if (coordNodes.isArray()) {
            for (final JsonNode coordNode : coordNodes) {
                if (coordNode.isArray()) {
                    List<Double> coordComponents = new ArrayList<>();
                    for (final JsonNode coordComponentNode : coordNode) {
                        coordComponents.add(coordComponentNode.doubleValue());
                    }
                    coords.add(coordComponents);
                }
            }
        } else {
            throw new InvalidAnnotationException("The coordinates for this ROI are not represented as an array");
        }

        commonMetaData.put("data", coords);

        return commonMetaData;
    }

}
