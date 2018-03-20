/*
 *  LabelJsonToCsvBeanConverter.java
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
import com.fasterxml.jackson.databind.node.NullNode;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.ImageSetAssociation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.LabelAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@code LabelJsonToCsvBeanConverter} converts a JSON representation of a label annotation to a corresponding bean(s) representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelJsonToCsvBeanConverter implements JsonToCsvBeanConverter {

    /**
     * Creates a new {@code LabelJsonToCsvBeanConverter}.
     */
    public LabelJsonToCsvBeanConverter() {

    }

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Returns the provided {@link JsonNode} as a {@code List} of {@code LabelAnnotation} beans.
     *
     * @return a {@code List} of {@code LabelAnnotation} beans
     */
    @Override
    public List<LabelAnnotation> getAnnotationBeans(JsonNode labelNode) throws InvalidAnnotationException {

        List<Map.Entry<String, JsonNode>> geClasses = getGEClasses(labelNode);
        List<LabelAnnotation> labelBeans = new ArrayList<>();

        for (Map.Entry<String, JsonNode> geClass : geClasses) {
            final LabelAnnotation labelBean;

            if (ImageSetAssociation.isAssociatedWithDicom(labelNode)) {
                labelBean = createDicomLabel(labelNode, geClass);
            } else if (ImageSetAssociation.isAssociatedWithNonDicom(labelNode)) {
                labelBean = createNonDicomLabel(labelNode, geClass);
            } else {
                throw new InvalidAnnotationException("The provided annotation does not map to either a DICOM or non-DICOM image set!");
            }

            labelBeans.add(labelBean);
        }

        return labelBeans;
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    /**
     * Creates a {@code LabelAnnotation} that is associated with a DICOM image set whose contents are derived from the provided
     * GE class object and its parent object.
     *
     * @param labelNode The parent {@link JsonNode} object that should contain information such as the series instance UID and annotation type
     * @param geClass   The specific GE class for which this {@code LabelAnnotation} is being created.  This should contain information such as the label's name, severity, indication, and findings.
     * @return a new {@code LabelAnnotation}
     */
    private static LabelAnnotation createDicomLabel(JsonNode labelNode, Map.Entry<String, JsonNode> geClass) {
        final String seriesUID = labelNode.get("seriesUID").textValue();
        final Map<String, String> commonMetaData = getCommonMetaData(labelNode, geClass);

        return new LabelAnnotation(seriesUID, commonMetaData.get("annotationType"), commonMetaData.get("name"), commonMetaData.get("value"), commonMetaData.get("indication"), commonMetaData.get("findings"));
    }

    /**
     * Creates a {@code LabelAnnotation} that is associated with a non-DICOM image set whose contents are derived from the provided
     * GE class object and its parent object.
     *
     * @param labelNode The parent {@link JsonNode} object that should contain information such as the non-DICOM image's original file name and the S3 space ID where the non-DICOM is stored
     * @param geClass   The specific GE class for which this {@code LabelAnnotation} is being created.  This should contain information such as the label's name, severity, indication, and findings.
     * @return a new {@code LabelAnnotation}
     */
    private static LabelAnnotation createNonDicomLabel(JsonNode labelNode, Map.Entry<String, JsonNode> geClass) {
        final String fileName = labelNode.get("patientId").textValue();
        final String spaceID = "";
        final Map<String, String> commonMetaData = getCommonMetaData(labelNode, geClass);

        return new LabelAnnotation(fileName, spaceID, commonMetaData.get("annotationType"), commonMetaData.get("name"), commonMetaData.get("value"), commonMetaData.get("indication"), commonMetaData.get("findings"));
    }

    /**
     * Returns a map of meta-data properties that are applicable to both DICOM and non-DICOM label annotations.
     *
     * @param labelNode The parent {@link JsonNode} which should contain information common across all GE classes
     * @param geClass   The object that defines GE class specific information
     * @return a {@code Map} where the key is the meta-data property and the value is that property's value
     */
    private static Map<String, String> getCommonMetaData(JsonNode labelNode, Map.Entry<String, JsonNode> geClass) {
        Map<String, String> commonMetaData = new HashMap<>();
        commonMetaData.put("name", geClass.getValue().get("name").textValue());
        commonMetaData.put("annotationType", labelNode.get("annotationType").textValue());
        commonMetaData.put("value", getOptionalFieldFromGEClass(geClass, "value"));
        commonMetaData.put("indication", getOptionalFieldFromLabelNode(labelNode, "indication"));
        commonMetaData.put("findings", getOptionalFieldFromLabelNode(labelNode, "findings"));

        return commonMetaData;
    }

    /**
     * Returns the non-null GE class child nodes of the provided node.
     *
     * @param node The parent {@code JsonNode} to inspect.
     * @return a {@code List} of GE classes
     */
    private static List<Map.Entry<String, JsonNode>> getGEClasses(JsonNode node) {
        Iterator<Map.Entry<String, JsonNode>> objPropIterator = node.fields();
        Iterable<Map.Entry<String, JsonNode>> objPropIterable = () -> objPropIterator;
        Stream<Map.Entry<String, JsonNode>> objectProperties = StreamSupport.stream(objPropIterable.spliterator(), false);
        return objectProperties.filter(entry -> entry.getKey().contains("geClass") && !(entry.getValue() instanceof NullNode)).collect(Collectors.toList());
    }

    /**
     * Returns an optional field of a GE class object if it exists.
     *
     * @param labelNode     The label node to inspect.
     * @param optionalField The optional field that is to be retrieved from the specified GE class object.
     * @return A {@code String} representation of the optional field if it exists; otherwise, {@code null}.
     */
    private static String getOptionalFieldFromLabelNode(JsonNode labelNode, String optionalField) {
        JsonNode fieldValue = labelNode.get(optionalField);
        return fieldValue != null ? fieldValue.textValue() : null;
    }

    /**
     * Returns an optional field of a GE class object if it exists.
     *
     * @param geClass       The GE class object to inspect.
     * @param optionalField The optional field that is to be retrieved from the specified GE class object.
     * @return A {@code String} representation of the optional field if it exists; otherwise, {@code null}.
     */
    private static String getOptionalFieldFromGEClass(Map.Entry<String, JsonNode> geClass, String optionalField) {
        JsonNode fieldValue = geClass.getValue().get(optionalField);
        return fieldValue != null ? fieldValue.textValue() : null;
    }

}
