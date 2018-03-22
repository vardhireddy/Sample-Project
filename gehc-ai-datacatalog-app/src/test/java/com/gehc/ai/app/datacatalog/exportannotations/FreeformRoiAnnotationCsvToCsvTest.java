/*
 *  FreeformRoiAnnotationToCsvTest.java
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
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@code FreeformRoiAnnotationToCsvTest} evaluates the behavior of the {@link AnnotationType#convertJsonToCsv(JsonNode, String[])} API for freeform ROI annotations
 * such as {@link AnnotationType#POLYGON}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@RunWith(Parameterized.class)
public class FreeformRoiAnnotationCsvToCsvTest {

    private ImageSetType imageSetType;
    private RoiMetaDataTypes roiMetaDataTypes;
    private ColumnHeaderTypes columnHeaderTypes;
    private String inputFile;
    private String[] columnHeaders;
    private String expectedOutput;

    private enum ImageSetType {
        DICOM("DICOM"),
        NON_DICOM("non-DICOM");

        private String name;

        ImageSetType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum RoiMetaDataTypes {
        REQUIRED_ONLY("only required meta data"),
        REQUIRED_AND_OPTIONAL("both required and optional meta data");

        private String name;

        RoiMetaDataTypes(String name) {
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

        ColumnHeaderTypes(String name) {
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
     * @param imageSetType       The type of image set this label annotation is associated with
     * @param roiMetaDataTypes The types of meta included in this label annotation
     * @param columnHeaderTypes  The types of column headers that are expected to be included in the CSV output
     * @param inputFile          The path of the input JSON file that describes a label
     * @param columnHeaders      The column headers to use when writing out to CSV
     * @param expectedOutput     The expected CSV output produced by the {@link AnnotationType#convertJsonToCsv(JsonNode, String[])} API
     */
    public FreeformRoiAnnotationCsvToCsvTest(
            ImageSetType imageSetType,
            RoiMetaDataTypes roiMetaDataTypes,
            ColumnHeaderTypes columnHeaderTypes,
            String inputFile,
            String[] columnHeaders,
            String expectedOutput
    ) {
        this.imageSetType = imageSetType;
        this.roiMetaDataTypes = roiMetaDataTypes;
        this.columnHeaderTypes = columnHeaderTypes;
        this.inputFile = inputFile;
        this.columnHeaders = columnHeaders;
        this.expectedOutput = expectedOutput;
    }

    @Parameters(name = "GIVEN that the ROI JSON is associated with a {0} image set AND each ROI contains {1} AND the provided array of column headers contains {2}")
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<Object[]>();

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        RoiMetaDataTypes.REQUIRED_ONLY,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID", "name"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\",\"Test ROI name\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID", "label", "severity"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\",\"\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-dicom-required-and-optional-meta-data.json",
                        new String[]{"seriesUID", "annotationType", "coordSys", "data", "localID", "name", "label", "severity"},
                        "\"76255696-105e-47b6-a6c6-ab4d947b7e3e\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\",\"Test ROI name\",\"\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        RoiMetaDataTypes.REQUIRED_ONLY,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-non-dicom-required-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "coordSys", "data", "localID"},
                        "\"file123.png\",\"\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_ONLY,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-non-dicom-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "coordSys", "data", "localID"},
                        "\"file123.png\",\"\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-non-dicom-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "coordSys", "data", "localID", "name"},
                        "\"file123.png\",\"\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\",\"Test ROI name\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-non-dicom-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "coordSys", "data", "localID", "label", "severity"},
                        "\"file123.png\",\"\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\",\"\",\"\"\n"
                }
        );

        params.add(
                new Object[]{
                        ImageSetType.NON_DICOM,
                        RoiMetaDataTypes.REQUIRED_AND_OPTIONAL,
                        ColumnHeaderTypes.REQUIRED_AND_OPTIONAL_AND_EXTRANEOUS,
                        "export-annotations-as-csv-test-files/valid-roi-2d-annotation-non-dicom-required-and-optional-meta-data.json",
                        new String[]{"fileName", "spaceID", "annotationType", "coordSys", "data", "localID", "name", "label", "severity"},
                        "\"file123.png\",\"\",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-1.2345, 6.789, 10.1112]]\",\"0\",\"Test ROI name\",\"\",\"\"\n"
                }
        );

        return params;
    }

    @Test
    public void itShouldConvertValidRoi2D() throws Exception {
        // ARRANGE
        final String VALID_ANNOT_JSON = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(this.inputFile).toURI())));
        String[] columnHeaders = this.columnHeaders;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode validAnnotJson = mapper.readTree(VALID_ANNOT_JSON);

        // ACT
        final String ACTUAL_CSV = AnnotationType.POLYGON.convertJsonToCsv(validAnnotJson, columnHeaders);

        // ASSERT
        assertThat(ACTUAL_CSV, is(equalTo(this.expectedOutput)));
    }

}
