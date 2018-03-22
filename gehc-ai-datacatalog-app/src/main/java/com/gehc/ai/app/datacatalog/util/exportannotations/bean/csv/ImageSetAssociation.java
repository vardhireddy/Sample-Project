/*
 *  ImageSetAssociation.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@code ImageSetAssociation} provides utility methods for determining the type of image set an annotation is associated with.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class ImageSetAssociation {

    private static final String IMAGE_SET_FORMAT_PROPERTY = "imageSetFormat";

    private static final String[] NON_DICOM_FORMATS = new String[]{"PNG", "JPG", "JPEG"};

    /**
     * Returns whether the provided {@link JsonNode} is associated with a DICOM image set.
     *
     * @param labelNode The {@code JsonNode} to evaluate
     * @return {@code true} if the provided {@code JsonNode} is associated with a DICOM image set; otherwise, {@code false}.
     */
    public static final boolean isAssociatedWithDicom(JsonNode labelNode) {
        return nodeDefinesImageSetFormat(labelNode) && labelNode.get("imageSetFormat").textValue().equalsIgnoreCase("DCM");
    }

    /**
     * Returns whether the provided {@link JsonNode} is associated with a non-DICOM image set.
     *
     * @param labelNode The {@code JsonNode} to evaluate
     * @return {@code true} if the provided {@code JsonNode} is associated with a non-DICOM image set; otherwise, {@code false}.
     */
    public static final boolean isAssociatedWithNonDicom(JsonNode labelNode) {
        String imageSetFormat = labelNode.get(IMAGE_SET_FORMAT_PROPERTY).textValue();
        return nodeDefinesImageSetFormat(labelNode) && Arrays.stream(NON_DICOM_FORMATS).anyMatch(format -> imageSetFormat.equalsIgnoreCase(format));
    }

    /**
     * Returns whether the provided {@link JsonNode} defines a property called {@code imageSetFormat}.
     * Without this property, we cannot evaluate the image set format to which this label is associated.
     *
     * @param node The {@link JsonNode} to evaluate
     * @return {@code true} if the provided {@code JsonNode} defines the {@code imageSetFormat} property; otherwise, {@code false}
     */
    private static boolean nodeDefinesImageSetFormat(JsonNode node) {
        return Objects.nonNull(node.get(IMAGE_SET_FORMAT_PROPERTY)) && !(node.get(IMAGE_SET_FORMAT_PROPERTY) instanceof NullNode);
    }

}
