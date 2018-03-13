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
package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

/**
 * {@code ImageSetAssociation} provides utility methods for determining the type of image set an annotation is associated with.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class ImageSetAssociation {

    /**
     * Returns whether the provided {@link JsonNode} is associated with a DICOM image set.
     *
     * @param labelNode The {@code JsonNode} to evaluate
     * @return {@code true} if the provided {@code JsonNode} is associated with a DICOM image set; otherwise, {@code false}.
     */
    public static final boolean isAssociatedWithDicom(JsonNode labelNode) {
        return Objects.nonNull(labelNode.get("seriesUID"));
    }

    /**
     * Returns whether the provided {@link JsonNode} is associated with a non-DICOM image set.
     *
     * @param labelNode The {@code JsonNode} to evaluate
     * @return {@code true} if the provided {@code JsonNode} is associated with a non-DICOM image set; otherwise, {@code false}.
     */
    public static final boolean isAssociatedWithNonDicom(JsonNode labelNode) {
        return Objects.nonNull(labelNode.get("fileName")) && Objects.nonNull(labelNode.get("spaceID"));
    }

}
