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

import java.util.Arrays;

/**
 * {@code ImageSetAssociation} provides utility methods for determining the type of image set an annotation is associated with.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class ImageSetAssociation {

    private static final String[] NON_DICOM_FORMATS = new String[]{"PNG", "JPG", "JPEG"};

    /**
     * Returns whether the provided DB result record, which describes an annotation, is associated with a DICOM image set.
     *
     * @param result              A DB result record which describes an annotation
     * @param imageSetFormatIndex The index within the result record that points to the image set format
     * @return {@code true} if the provided result record is associated with a DICOM image set; otherwise, {@code false}.
     */
    public static final boolean isAssociatedWithDicom(Object[] result, int imageSetFormatIndex) {
        return ((String) result[imageSetFormatIndex]).equalsIgnoreCase("DCM");
    }

    /**
     * Returns whether the provided DB result record, which describes an annotation, is associated with a non-DICOM image set.
     *
     * @param result              A DB result record which describes an annotation
     * @param imageSetFormatIndex The index within the result record that points to the image set format
     * @return {@code true} if the provided result record is associated with a non-DICOM image set; otherwise, {@code false}.
     */
    public static final boolean isAssociatedWithNonDicom(Object[] result, int imageSetFormatIndex) {
        final String imageSetFormat = (String) result[imageSetFormatIndex];
        return Arrays.stream(NON_DICOM_FORMATS).anyMatch(nonDicomFormat -> (imageSetFormat.equalsIgnoreCase(nonDicomFormat)));
    }

}
