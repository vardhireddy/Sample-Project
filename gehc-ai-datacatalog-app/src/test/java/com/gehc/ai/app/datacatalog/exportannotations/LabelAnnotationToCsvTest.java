/*
 *  LabelAnnotationToCsvTest.java
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
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@code LabelAnnotationToCsvTest} evaluates the behavior of the {@link LabelConverter#convert(JsonNode, String[])} API.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@RunWith(Parameterized.class)
public class LabelAnnotationToCsvTest {

    private ImageSetType imageSetType;
    private GEClassMetaDataTypes geClassMetaDataTypes;
    private ColumnHeaderTypes columnHeaderTypes;
    private String inputFile;
    private String[] columnHeaders;
    private String expectedOutput;

    private enum ImageSetType {
        DICOM("DICOM"),
        NON_DICOM("non-DICOM");

        private String name;

        private ImageSetType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum GEClassMetaDataTypes {
        REQUIRED_ONLY("only required meta data"),
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

    private enum ColumnHeaderTypes {
        REQUIRED_ONLY("only those for required meta data"),
        REQUIRED_AND_OPTIONAL("those for both required and optional meta data"),
        REQUIRED_AND_EXTRANEOUS("those for required and extraneous meta data"),
        REQUIRED_AND_OPTIONAL_AND_EXTRANEOUS("those for required, optional, and extraneous meta data");

        private String name;

        private ColumnHeaderTypes(String name) {
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
     * @param imageSetType         The type of image set this label annotation is associated with
     * @param geClassMetaDataTypes The types of meta included in this label annotation
     * @param columnHeaderTypes    The types of column headers that are expected to be included in the CSV output
     * @param inputFile            The path of the input JSON file that describes a label
     * @param columnHeaders        The column headers to use when writing out to CSV
     * @param expectedOutput       The expected CSV output produced by the {@link LabelConverter#convert(JsonNode, String[])} API
     */
    public LabelAnnotationToCsvTest(
            ImageSetType imageSetType,
            GEClassMetaDataTypes geClassMetaDataTypes,
            ColumnHeaderTypes columnHeaderTypes,
            String inputFile,
            String[] columnHeaders,
            String expectedOutput
    ) {
        this.imageSetType = imageSetType;
        this.geClassMetaDataTypes = geClassMetaDataTypes;
        this.columnHeaderTypes = columnHeaderTypes;
        this.inputFile = inputFile;
        this.columnHeaders = columnHeaders;
        this.expectedOutput = expectedOutput;
    }

    @Parameters(name = "GIVEN that the label JSON is associated with a {0} image set AND each GE class contains {1} AND the provided array of column headers contains {2}")
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<Object[]>();

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_ONLY,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-multiple-ge-classes-required-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "label"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Cardiomegaly\"\n\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Aorta Abnormalities\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "label"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Cardiomegaly\"\n\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Aorta Abnormalities\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL,
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "label", "severity"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Cardiomegaly\",\"21\"\n\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Aorta Abnormalities\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "label", "coordSys", "data"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Cardiomegaly\",\"\",\"\"\n\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Aorta Abnormalities\",\"\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-label-annotation-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "label", "severity", "coordSys", "data"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Cardiomegaly\",\"21\",\"\",\"\"\n\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"label\",\"Aorta Abnormalities\",\"\",\"\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        GEClassMetaDataTypes.REQUIRED_ONLY,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-label-annotation-non-dicom-multiple-ge-classes-required-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "label"},
                        "\"file123.png\",\"space123\",\"label\",\"Cardiomegaly\"\n\"file123.png\",\"space123\",\"label\",\"Aorta Abnormalities\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-label-annotation-non-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "label"},
                        "\"file123.png\",\"space123\",\"label\",\"Cardiomegaly\"\n\"file123.png\",\"space123\",\"label\",\"Aorta Abnormalities\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL,
                        "export-annotations-as-csv-test-files/valid-label-annotation-non-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "label", "severity"},
                        "\"file123.png\",\"space123\",\"label\",\"Cardiomegaly\",\"21\"\n\"file123.png\",\"space123\",\"label\",\"Aorta Abnormalities\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-label-annotation-non-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "label", "coordSys", "data"},
                        "\"file123.png\",\"space123\",\"label\",\"Cardiomegaly\",\"\",\"\"\n\"file123.png\",\"space123\",\"label\",\"Aorta Abnormalities\",\"\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        GEClassMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-label-annotation-non-dicom-multiple-ge-classes-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "label", "severity", "coordSys", "data"},
                        "\"file123.png\",\"space123\",\"label\",\"Cardiomegaly\",\"21\",\"\",\"\"\n\"file123.png\",\"space123\",\"label\",\"Aorta Abnormalities\",\"\",\"\",\"\"\n"
                }
        );


        return params;
    }

    @Test
    public void itShouldConvertValidLabel() throws Exception {
        // ARRANGE
        final String VALID_ANNOT_JSON = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(this.inputFile).toURI())));
        String[] columnHeaders = this.columnHeaders;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode validAnnotJson = mapper.readTree(VALID_ANNOT_JSON);

        // ACT
        final String ACTUAL_CSV = LabelConverter.convert(validAnnotJson, columnHeaders);

        // ASSERT
        assertThat(ACTUAL_CSV, is(equalTo(this.expectedOutput)));
    }

}
