/*
 *  LabelDBResultToCsvBeanConverter.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ImageSetAssociation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.LabelAnnotationCsv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code LabelDBResultToCsvBeanConverter} converts a DB result record, which describes a label annotation, to a corresponding CSV bean(s) representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelDBResultToCsvBeanConverter implements DBResultToCsvBeanConverter<LabelAnnotationCsv> {

    /**
     * Creates a new {@code LabelDBResultToCsvBeanConverter}.
     */
    public LabelDBResultToCsvBeanConverter() {

    }

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Returns the provided DB result record, which describes a label annotation, as a {@code List} of {@code LabelAnnotationCsv} beans.
     *
     * @param result            The DB result record, which describes a label annotation, to convert
     * @param resultIndexMap    A mapping of a result meta data to its index in the result record
     * @param resultIndicesMap A mapping of a result meta data to its indices in the result record
     * @return a {@code List} of {@code LabelAnnotationCsv} beans
     */
    @Override
    public List<LabelAnnotationCsv> getAnnotationBeans(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {

        // Extract the individual GE classes from the DB result record and aggregate them in a list.  Only include non-null values.
        Integer[] geClassIndices = resultIndicesMap.get("geClasses");
        List<GEClass> geClasses = GEClass.toGEClasses(result, geClassIndices);

        // Convert each GE class into its CSV bean representations
        List<LabelAnnotationCsv> labelBeans = new ArrayList<>();
        for (GEClass geClass : geClasses) {
            final LabelAnnotationCsv labelBean;
            final int imageSetFormatIndex = resultIndexMap.get("imageSetFormat");
            if (ImageSetAssociation.isAssociatedWithDicom(result, imageSetFormatIndex)) {
                labelBean = createDicomLabel(result, resultIndexMap, geClass);
            } else if (ImageSetAssociation.isAssociatedWithNonDicom(result, imageSetFormatIndex)) {
                labelBean = createNonDicomLabel(result, resultIndexMap, geClass);
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
     * Creates a {@code LabelAnnotationCsv} that is associated with a DICOM image set whose contents are derived from the provided
     * GE class object and its parent object.
     *
     * @param result         The DB result record, which describes a label annotation, to convert
     * @param resultIndexMap A mapping of a result meta data to its index in the result record
     * @param geClass        The specific GE class for which this {@code LabelAnnotationCsv} is being created.  This should contain information such as the label's name and value
     * @return a new {@code LabelAnnotationCsv}
     */
    private static LabelAnnotationCsv createDicomLabel(Object[] result, Map<String, Integer> resultIndexMap, GEClass geClass) {
        final String seriesUID = (String) result[resultIndexMap.get("seriesUID")];
        final Map<String, String> commonMetaData = getCommonMetaData(result, resultIndexMap, geClass);

        return new LabelAnnotationCsv(seriesUID, commonMetaData.get("annotationType"), commonMetaData.get("name"), commonMetaData.get("value"), commonMetaData.get("indication"), commonMetaData.get("findings"));
    }

    /**
     * Creates a {@code LabelAnnotationCsv} that is associated with a non-DICOM image set whose contents are derived from the provided
     * GE class object and its parent object.
     *
     * @param result         The DB result record, which describes a label annotation, to convert
     * @param resultIndexMap A mapping of a result meta data to its index in the result record
     * @param geClass        The specific GE class for which this {@code LabelAnnotationCsv} is being created.  This should contain information such as the label's name and value.
     * @return a new {@code LabelAnnotationCsv}
     */
    private static LabelAnnotationCsv createNonDicomLabel(Object[] result, Map<String, Integer> resultIndexMap, GEClass geClass) {
        // For non-DICOM files which do not actually have a patient ID associated with them, the convention is to use the original file name as the patient ID
        final String fileName = (String) result[resultIndexMap.get("patientID")];
        final String spaceID = "";
        final Map<String, String> commonMetaData = getCommonMetaData(result, resultIndexMap, geClass);

        return new LabelAnnotationCsv(fileName, spaceID, commonMetaData.get("annotationType"), commonMetaData.get("name"), commonMetaData.get("value"), commonMetaData.get("indication"), commonMetaData.get("findings"));
    }

    /**
     * Returns a map of meta-data properties that are applicable to both DICOM and non-DICOM label annotations.
     *
     * @param result         The DB result record, which describes a label annotation, to convert
     * @param resultIndexMap A mapping of a result meta data to its index in the result record
     * @param geClass        The object that defines GE class specific information
     * @return a {@code Map} where the key is the meta-data property and the value is that property's value
     */
    private static Map<String, String> getCommonMetaData(Object[] result, Map<String, Integer> resultIndexMap, GEClass geClass) {
        Map<String, String> commonMetaData = new HashMap<>();
        commonMetaData.put("name", geClass.getName());
        commonMetaData.put("annotationType", (String) result[resultIndexMap.get("annotationType")]);
        commonMetaData.put("value", geClass.getValue());
        commonMetaData.put("indication", (String) result[resultIndexMap.get("indication")]);
        commonMetaData.put("findings", (String) result[resultIndexMap.get("findings")]);

        return commonMetaData;
    }

}
