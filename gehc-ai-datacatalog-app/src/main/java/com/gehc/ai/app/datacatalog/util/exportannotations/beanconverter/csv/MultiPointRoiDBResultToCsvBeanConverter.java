/*
 *  MultiPointRoiDBResultToCsvBeanConverter.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.ImageSetAssociation;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.MultiPointRoiAnnotationCsv;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv.RoiAnnotationCsv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.gehc.ai.app.datacatalog.util.exportannotations.beanconverter.ObjectMapperUtil.mapToString;

/**
 * {@code MultiPointRoiDBResultToCsvBeanConverter} converts a DB result record, which describes a freeform ROI annotation, to its corresponding CSV bean(s) representation.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class MultiPointRoiDBResultToCsvBeanConverter implements DBResultToCsvBeanConverter<RoiAnnotationCsv> {

    private Supplier<RoiDataConverter> roiDataSupplier;

    /**
     * Creates a new {@code MultiPointRoiDBResultToCsvBeanConverter} with the provided {@code Supplier} of a {@link RoiDataConverter}
     *
     * @param roiDataSupplier The {@code Supplier} that returns an instance of {@link RoiDataConverter}
     */
    public MultiPointRoiDBResultToCsvBeanConverter(Supplier<RoiDataConverter> roiDataSupplier) {
        this.roiDataSupplier = roiDataSupplier;
    }

    ////////////////
    //
    // Public API //
    //
    ////////////////

    /**
     * Returns the provided DB result record, which describes a freeform ROI annotation, as a {@code List} of {@code RoiAnnotationCsv} beans.
     *
     * @param result           The DB result record, which describes a freeform ROI annotation, to convert
     * @param resultIndexMap   A mapping of single-valued result meta data to their index in the result record
     * @param resultIndicesMap A mapping of multi-valued result meta data to their indices in the result record
     * @return a {@code List} of {@code RoiAnnotationCsv} beans
     */
    @Override
    public List<RoiAnnotationCsv> getAnnotationBeans(Object[] result, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) throws InvalidAnnotationException {
        List<RoiAnnotationCsv> roiBeans = new ArrayList<>();

        final RoiAnnotationCsv roiBean;
        final int imageSetFormatIndex = resultIndexMap.get("imageSetFormat");

        if (ImageSetAssociation.isAssociatedWithDicom(result, imageSetFormatIndex)) {
            roiBean = createDicomRoi(result, resultIndexMap);
        } else if (ImageSetAssociation.isAssociatedWithNonDicom(result, imageSetFormatIndex)) {
            roiBean = createNonDicomRoi(result, resultIndexMap);
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
     * Creates a {@code RoiAnnotationCsv} that is associated with a DICOM image set whose contents are derived from the provided DB result record representation of a ROI
     *
     * @param result         The DB result record, which describes a freeform ROI annotation, to convert
     * @param resultIndexMap A mapping of a result meta data to its index in the result record
     * @return a new {@code RoiAnnotationCsv}
     * @throws InvalidAnnotationException if the provided DB result record representation contains coordinates that are not represented as an array of double values
     */
    private MultiPointRoiAnnotationCsv createDicomRoi(Object[] result, Map<String, Integer> resultIndexMap) throws InvalidAnnotationException {
        final String seriesUID = (String) result[resultIndexMap.get("seriesUID")];
        final Map<String, Object> commonMetaData = getCommonMetaData(result, resultIndexMap);

        return new MultiPointRoiAnnotationCsv(
                seriesUID,
                (String) commonMetaData.get("annotationType"),
                (String) commonMetaData.get("coordSys"),
                (List<List<Double>>) commonMetaData.get("data"),
                (String) commonMetaData.get("localID"),
                (String) commonMetaData.get("name")
        );
    }

    /**
     * Creates a {@code RoiAnnotationCsv} that is associated with a non-DICOM image set whose contents are derived from the provided DB result record representation of a ROI
     *
     * @param result         The DB result record, which describes a freeform ROI annotation, to convert
     * @param resultIndexMap A mapping of a result meta data to its index in the result record
     * @return a new {@code RoiAnnotationCsv}
     * @throws InvalidAnnotationException if the provided DB result record representation contains coordinates that are not represented as an array of double values
     */
    private MultiPointRoiAnnotationCsv createNonDicomRoi(Object[] result, Map<String, Integer> resultIndexMap) throws InvalidAnnotationException {
        // For non-DICOM files which do not actually have a patient ID associated with them, the convention is to use the original file name as the patient ID
        final String fileName = (String) result[resultIndexMap.get("patientID")];
        final String spaceID = "";
        final Map<String, Object> commonMetaData = getCommonMetaData(result, resultIndexMap);

        return new MultiPointRoiAnnotationCsv(
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
     * @param result         The DB result record, which describes a freeform ROI annotation, to convert
     * @param resultIndexMap A mapping of a result meta data to its index in the result record
     * @return a {@code Map} where the key is the meta-data property and the value is that property's value
     * @throws InvalidAnnotationException if the provided DB result record representation contains coordinates that are not represented as an array of double values
     */
    private Map<String, Object> getCommonMetaData(Object[] result, Map<String, Integer> resultIndexMap) throws InvalidAnnotationException {
        Map<String, Object> commonMetaData = new HashMap<>();
        commonMetaData.put("annotationType", (String) result[resultIndexMap.get("annotationType")]);
        commonMetaData.put("coordSys", mapToString(result[resultIndexMap.get("coordSys")]));
        commonMetaData.put("localID", mapToString(result[resultIndexMap.get("roiLocalID")]));
        commonMetaData.put("name", mapToString(result[resultIndexMap.get("roiName")]));
        commonMetaData.put("data", this.roiDataSupplier.get().convertRoiData(result[resultIndexMap.get("roiData")]));

        return commonMetaData;
    }


}
