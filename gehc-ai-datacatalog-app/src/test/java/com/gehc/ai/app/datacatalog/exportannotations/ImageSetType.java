/*
 *  ImageSetType.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.exportannotations;

/**
 * {@code ImageSetType} enumerates the types of image set an annotation can be associated with based on image data format.
 */
public enum ImageSetType {

    /**
     * A type of image set that is associated with DICOM image data.
     */
    DICOM("DICOM"),

    /**
     * A type of image set that is associated with non-DICOM image data (e.g. PNG, JPG, JPEG).
     */
    NON_DICOM("non-DICOM");

    private String description;

    /**
     * Creates a new {@code ImageSetType} with the provided description.
     *
     * @param description A description of this instance
     */
    ImageSetType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
