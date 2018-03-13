/*
 *  LabelAnnotationColumnsTest.java
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
import com.gehc.ai.app.datacatalog.util.exportannotations.LabelConverter;
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
 * {@code LabelAnnotationColumnsTest} evaluates the behavior of the {@link LabelConverter#getColumnHeaders(JsonNode)} API.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@RunWith(Parameterized.class)
public class LabelAnnotationColumnsTest {

    private ImageSetType imageSetType;
    private GEClassMetaDataTypes geClassMetaDataTypes;
    private String optionalMetaData;
    private String inputFile;
    private Set<String> expectedColumnHeaders;

    private enum ImageSetType {
        DICOM, NON_DICOM
    }

    private enum GEClassMetaDataTypes {
        REQUIRED_ONLY("only the required meta data"),
        REQUIRED_AND_OPTIONAL("both required and optional meta data");

        private String name;

        private GEClassMetaDataTypes(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum OptionalMetaData {
        SEVERITY("severity"),
        INDICATION("indication"),
        FINDINGS("findings");

        private String name;

        private OptionalMetaData(String name) {
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
     * @param geClassMetaDataTypes  The types of meta data included in the label annotation
     * @param optionalMetaData      The optional meta data included by the label annotation
     * @param inputFile             The path of the input JSON file
     * @param expectedColumnHeaders The expected column headers produced by the {@link LabelConverter#getColumnHeaders(JsonNode)} API
     */
    public LabelAnnotationColumnsTest(
            ImageSetType imageSetType,
            GEClassMetaDataTypes geClassMetaDataTypes,
            String optionalMetaData,
            String inputFile,
            Set<String> expectedColumnHeaders
    ) {
        this.imageSetType = imageSetType;
        this.geClassMetaDataTypes = geClassMetaDataTypes;
        this.optionalMetaData = optionalMetaData;
        this.inputFile = inputFile;
        this.expectedColumnHeaders = expectedColumnHeaders;
    }

    @Parameters(name = "GIVEN that the label JSON is associated with a {0} image set AND each GE class contains {1} AND the optional meta data are {2}")
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<Object[]>();

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_ONLY,
                        Arrays.toString(new OptionalMetaData[]{}),
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-required-columns.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "label"}))
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        Arrays.toString(new OptionalMetaData[]{OptionalMetaData.SEVERITY}),
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-required-columns-and-severity-column.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "label", "severity"}))
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        Arrays.toString(new OptionalMetaData[]{OptionalMetaData.INDICATION}),
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-required-columns-and-indication-column.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "label", "indication"}))
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        Arrays.toString(new OptionalMetaData[]{OptionalMetaData.FINDINGS}),
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-required-columns-and-findings-column.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "label", "findings"}))
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        Arrays.toString(new OptionalMetaData[]{OptionalMetaData.SEVERITY, OptionalMetaData.INDICATION, OptionalMetaData.FINDINGS}),
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-required-columns-and-severity-indication-findings-columns.json",
                        new LinkedHashSet<String>(Arrays.asList(new String[]{"seriesUID", "annotationType", "label", "severity", "indication", "findings"}))
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
        final Set<String> actualColumnHeaders = LabelConverter.getColumnHeaders(validAnnotJsonNode);

        // ASSERT
        assertThat(actualColumnHeaders, is(this.expectedColumnHeaders));
    }

}
