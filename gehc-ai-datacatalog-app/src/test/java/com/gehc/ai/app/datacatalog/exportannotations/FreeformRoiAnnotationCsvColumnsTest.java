/*
 *  FreeformRoiAnnotationColumnsTest.java
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.util.exportannotations.AnnotationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@code FreeformRoiAnnotationColumnsTest} evaluates the behavior of the {@link AnnotationType#getColumnHeaders(JsonNode)} API for freeform ROI annotations
 * such as {@link AnnotationType#POLYGON}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@RunWith(Parameterized.class)
public class FreeformRoiAnnotationCsvColumnsTest {

    private ImageSetType imageSetType;
    private MetaDataTypes metaDataTypes;
    private String optionalMetaData;
    private String inputFile;
    private Set<String> expectedColumnHeaders;

    private enum OptionalMetaData {
        NAME("ROI name");

        private String name;

        OptionalMetaData(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * Creates a new parameterized test case.
     *
     * @param imageSetType          The type of image set the label annotation is associated with
     * @param metaDataTypes         The types of meta data included in the label annotation
     * @param optionalMetaData      The optional meta data included by the label annotation
     * @param inputFile             The path of the input JSON file
     * @param expectedColumnHeaders The expected column headers produced by the {@link AnnotationType#getColumnHeaders(JsonNode)}API
     */
    public FreeformRoiAnnotationCsvColumnsTest(
            ImageSetType imageSetType,
            MetaDataTypes metaDataTypes,
            String optionalMetaData,
            String inputFile,
            Set<String> expectedColumnHeaders
    ) {
        this.imageSetType = imageSetType;
        this.metaDataTypes = metaDataTypes;
        this.optionalMetaData = optionalMetaData;
        this.inputFile = inputFile;
        this.expectedColumnHeaders = expectedColumnHeaders;
    }

    @Parameters(name = "GIVEN that the ROI JSON is associated with a {0} image set AND each ROI contains {1} AND the optional meta data are {2}")
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<Object[]>();

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        MetaDataTypes.REQUIRED_ONLY,
                        Arrays.toString(new OptionalMetaData[]{}),
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-columns.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID"}))
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        MetaDataTypes.REQUIRED_AND_OPTIONAL,
                        Arrays.toString(new OptionalMetaData[]{OptionalMetaData.NAME}),
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-columns-and-name-column.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID", "name"}))
                }
        );

        return params;
    }

    @Test
    public void itShouldReturnTheCorrectHeaders() throws Exception {
        // ARRANGE
        final String validAnnotJson = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(this.inputFile).toURI())));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode validAnnotJsonNode = mapper.readTree(validAnnotJson);

        // ACT
        final Set<String> actualColumnHeaders = AnnotationType.POLYGON.getColumnHeaders(validAnnotJsonNode);

        // ASSERT
        assertThat(actualColumnHeaders, is(this.expectedColumnHeaders));
    }

}
