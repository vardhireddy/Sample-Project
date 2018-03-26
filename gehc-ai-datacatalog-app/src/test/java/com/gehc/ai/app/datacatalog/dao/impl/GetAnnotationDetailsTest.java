package com.gehc.ai.app.datacatalog.dao.impl;

import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.FreeformRoi;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.FreeformRoiAnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.LabelAnnotationJson;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * {@code GetAnnotationDetailsTest} evaluates the behavior of the {@link DataCatalogDaoImpl#getAnnotationDetailsByImageSetIDs(List)} API.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
@RunWith(Parameterized.class)
@ContextConfiguration
public class GetAnnotationDetailsTest {

    ////////////////////////////////////////////////////////////////
    //
    // Rules needed to enable parameterized tests with SpringBoot //
    //
    ////////////////////////////////////////////////////////////////

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    //////////////////////
    //
    // Initialize mocks //
    //
    //////////////////////

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @InjectMocks
    DataCatalogDaoImpl dataCatalogDao;

    /////////////////////////////////////////
    //
    // Define parameterized test structure //
    //
    /////////////////////////////////////////

    private String annotationType;

    private ImageSetType imageSetType;

    private MetaDataTypes metaDataTypes;

    private List<Object[]> mockDBResults;

    private List<AnnotationJson> expectedAnnotDetailsAsJson;

    private String expectedAnnotDetailsAsCsv;

    /**
     * Create a new parameterized test.
     *
     * @param annotationType             The annotation type being tested
     * @param imageSetType               The type of image set (DICOM or non-DICOM) to which this annotation under test is associated
     * @param metaDataTypes              The types of meta data that is contained by the annotation under test
     * @param mockDBResults              The mock results from a mock DB query for retrieving the annotation details for a particular data collection ID
     * @param expectedAnnotDetailsAsJson The expected bean representation of the DB results
     */
    public GetAnnotationDetailsTest(String annotationType, ImageSetType imageSetType, MetaDataTypes metaDataTypes, List<Object[]> mockDBResults, List<AnnotationJson> expectedAnnotDetailsAsJson, String expectedAnnotDetailsAsCsv) {
        this.annotationType = annotationType;
        this.imageSetType = imageSetType;
        this.metaDataTypes = metaDataTypes;
        this.mockDBResults = mockDBResults;
        this.expectedAnnotDetailsAsJson = expectedAnnotDetailsAsJson;
        this.expectedAnnotDetailsAsCsv = expectedAnnotDetailsAsCsv;
    }

    /**
     * Returns the collection of parameterized test case that will be evaluated in the "Test specs" section.
     *
     * @return a {@code Collection} of test cases
     */
    @Parameters(name = "GIVEN a {0} annotation that is associated with a {1} image set AND contains {2} meta data")
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<>();

        for (AnnotationType annotType : AnnotationType.values()) {
            for (ImageSetType imageSetType : ImageSetType.values()) {
                for (MetaDataTypes metaDataTypes : MetaDataTypes.values()) {

                    params.add(
                            new Object[]{
                                    annotType.toString(),
                                    imageSetType,
                                    metaDataTypes,
                                    annotType.getMockDBResults(imageSetType, metaDataTypes),
                                    annotType.getExpectedAnnotDetailsAsJson(imageSetType, metaDataTypes),
                                    annotType.getExpectedAnnotDetailsAsCsv(imageSetType, metaDataTypes)
                            }
                    );

                }
            }
        }

        return params;
    }

    /**
     * {@code ImageSetType} enumerates the types of image set an annotation can be associated with based on image data format.
     */
    private enum ImageSetType {

        /**
         * A type of image set that is associated with DICOM image data.
         */
        DICOM("DICOM", "DCM", "patient123", "series123", "seriesUID", "\"series123\""),

        /**
         * A type of image set that is associated with non-DICOM image data (e.g. PNG, JPG, JPEG).
         */
        NON_DICOM("non-DICOM", "PNG", "file123.png", "file123.png/space123", "fileName,spaceID", "\"file123.png\",\"\"");

        private String description;

        private String fileExtension;

        private String patientID;

        private String seriesUID;

        private String csvColumnHeaders;

        private String csvColumnValues;

        /**
         * Creates a new {@code ImageSetType} with the provided description.
         *
         * @param description      A description of this {@code ImageSetType}
         * @param fileExtension    The file extension associated with this {@code ImageSetType}
         * @param patientID        The mock patient ID to use for this {@code ImageSetType}
         * @param seriesUID        The mock series UID to use for this {@code ImageSetType}
         * @param csvColumnHeaders The column headers that should be present in a CSV file for an annotation associated with this {@code ImageSetType}
         * @param csvColumnValues  The mock CSV column values to use for this {@code ImageSetType}
         */
        ImageSetType(String description, String fileExtension, String patientID, String seriesUID, String csvColumnHeaders, String csvColumnValues) {
            this.description = description;
            this.fileExtension = fileExtension;
            this.patientID = patientID;
            this.seriesUID = seriesUID;
            this.csvColumnHeaders = csvColumnHeaders;
            this.csvColumnValues = csvColumnValues;
        }

        @Override
        public String toString() {
            return this.description;
        }

        public String getFileExtension() {
            return this.fileExtension;
        }

        public String getPatientID() {
            return patientID;
        }

        public String getSeriesUID() {
            return seriesUID;
        }

        public String getCsvColumnHeaders() {
            return this.csvColumnHeaders;
        }

        public String getCsvColumnValues() {
            return csvColumnValues;
        }
    }

    /**
     * {@code MetaDataTypes} enumerates the types of the meta data that an annotation can contain.
     */
    private enum MetaDataTypes {

        /**
         * Describes an annotation that contains only the required meta data for its annotation type.
         */
        REQUIRED_ONLY("only the required"),

        /**
         * Describes an annotation that contains both the required optional meta data for its annotation type.
         */
        REQUIRED_AND_OPTIONAL("both required and optional");

        private String description;

        /**
         * Creates a new {@code MetaDataTypes} enum with the provided description.
         *
         * @param description A description of this instance
         */
        MetaDataTypes(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }

    }

    /**
     * {@code AnnotationType} enumerates the types of annotations supported by the Learing Factory.  Each enumerated value
     * defines its own test inputs and outputs.
     */
    private enum AnnotationType {

        LABEL() {
            @Override
            public List<Object[]> getMockDBResults(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                List<Object[]> mockDBResults = new ArrayList<>();

                // Set optional fields based on the defined meta data type spec
                String severity = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",\"value\":\"Absent\"" : "";
                String indication = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "Test indication" : null;
                String findings = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "Test findings" : null;

                Object[] newObj = new Object[]{imageSetType.getPatientID(), imageSetType.getSeriesUID(), imageSetType.getFileExtension(), 1, "label", null, null, null, "{\"name\":\"Foreign Bodies\"" + severity + "}", "{\"name\":\"Calcification\"}", null, null, null, null, null, null, null, null, null, null, indication, findings, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null};

                mockDBResults.add(newObj);
                return mockDBResults;
            }

            @Override
            List<AnnotationJson> getExpectedAnnotDetailsAsJson(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                List<AnnotationJson> annotationDetails = new ArrayList<>();

                // Set optional fields based on the defined meta data type spec
                String severity = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "Absent" : null;
                String indication = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "Test indication" : null;
                String findings = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "Test findings" : null;

                List<GEClass> geClasses = new ArrayList<>();
                geClasses.add(new GEClass("Foreign Bodies", severity, null));
                geClasses.add(new GEClass("Calcification", null, null));

                AnnotationJson annotation = new LabelAnnotationJson(imageSetType.getPatientID(), imageSetType.getSeriesUID(), imageSetType.getFileExtension(), 1L, "label", geClasses, indication, findings);
                annotationDetails.add(annotation);

                return annotationDetails;
            }

            @Override
            String getExpectedAnnotDetailsAsCsv(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                // Set optional fields based on the defined meta data type spec
                String optionalColumns = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",severity,indication,findings" : "";
                String optionalValuesForFirstAnnot = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",\"Absent\",\"Test indication\",\"Test findings\"" : "";
                String optionalValuesForSecondAnnot = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",\"\",\"Test indication\",\"Test findings\"" : "";

                return imageSetType.getCsvColumnHeaders() + ",annotationType,label" + optionalColumns + "\n" + imageSetType.getCsvColumnValues() + ",\"label\",\"Foreign Bodies\"" + optionalValuesForFirstAnnot + "\n" + imageSetType.getCsvColumnValues() + ",\"label\",\"Calcification\"" + optionalValuesForSecondAnnot + "\n";
            }
        },
        POLYGON() {
            @Override
            public List<Object[]> getMockDBResults(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                List<Object[]> mockDBResults = new ArrayList<>();

                // Set optional fields based on the defined meta data type spec
                String roiName = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "ROI Name" : null;

                Object[] newObj = new Object[]{imageSetType.getPatientID(), imageSetType.getSeriesUID(), imageSetType.getFileExtension(), 1, "polygon", roiName, "0", "[[-1.2345,6.789,10.1112],[-2.3456, 7.8901, 11.1213],[-3.4567, 8.9102, 12.1314]]", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null};
                mockDBResults.add(newObj);
                return mockDBResults;
            }

            @Override
            List<AnnotationJson> getExpectedAnnotDetailsAsJson(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                List<AnnotationJson> annotationDetails = new ArrayList<>();

                // Create three mock coordinates
                List<List<Double>> coords = new ArrayList<>();
                coords.add(createMockCoord(new Double[]{-1.2345, 6.789, 10.1112}));
                coords.add(createMockCoord(new Double[]{-2.3456, 7.8901, 11.1213}));
                coords.add(createMockCoord(new Double[]{-3.4567, 8.9102, 12.1314}));

                // Set optional fields based on the defined meta data type spec
                String roiName = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "ROI Name" : null;

                AnnotationJson annotation = new FreeformRoiAnnotationJson(imageSetType.getPatientID(), imageSetType.getSeriesUID(), imageSetType.getFileExtension(), 1L, FreeformRoi.POLYGON, "IMAGE", coords, "0", roiName);
                annotationDetails.add(annotation);
                return annotationDetails;
            }

            @Override
            String getExpectedAnnotDetailsAsCsv(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                // Set optional fields based on the defined meta data type spec
                String roiName = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",\"ROI Name\"" : "";
                String optionalColumns = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",name" : "";

                return imageSetType.getCsvColumnHeaders() + ",annotationType,coordSys,data,localID" + optionalColumns + "\n" + imageSetType.getCsvColumnValues() + ",\"polygon\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-2.3456, 7.8901, 11.1213], [-3.4567, 8.9102, 12.1314]]\",\"0\"" + roiName + "\n";
            }
        },
        CONTOUR() {
            @Override
            public List<Object[]> getMockDBResults(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                List<Object[]> mockDBResults = new ArrayList<>();

                // Set optional fields based on the defined meta data type spec
                String roiName = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "ROI Name" : null;

                Object[] newObj = new Object[]{imageSetType.getPatientID(), imageSetType.getSeriesUID(), imageSetType.getFileExtension(), 1, "contour", roiName, "0", "[[-1.2345,6.789,10.1112],[-2.3456, 7.8901, 11.1213],[-3.4567, 8.9102, 12.1314]]", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null};
                mockDBResults.add(newObj);
                return mockDBResults;
            }

            @Override
            List<AnnotationJson> getExpectedAnnotDetailsAsJson(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                List<AnnotationJson> annotationDetails = new ArrayList<>();

                // Create three mock coordinates
                List<List<Double>> coords = new ArrayList<>();
                coords.add(createMockCoord(new Double[]{-1.2345, 6.789, 10.1112}));
                coords.add(createMockCoord(new Double[]{-2.3456, 7.8901, 11.1213}));
                coords.add(createMockCoord(new Double[]{-3.4567, 8.9102, 12.1314}));

                // Set optional fields based on the defined meta data type spec
                String roiName = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? "ROI Name" : null;

                AnnotationJson annotation = new FreeformRoiAnnotationJson(imageSetType.getPatientID(), imageSetType.getSeriesUID(), imageSetType.getFileExtension(), 1L, FreeformRoi.CONTOUR, "IMAGE", coords, "0", roiName);
                annotationDetails.add(annotation);
                return annotationDetails;
            }

            @Override
            String getExpectedAnnotDetailsAsCsv(ImageSetType imageSetType, MetaDataTypes metaDataTypes) {
                // Set optional fields based on the defined meta data type spec
                String roiName = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",\"ROI Name\"" : "";
                String optionalColumns = metaDataTypes == MetaDataTypes.REQUIRED_AND_OPTIONAL ? ",name" : "";

                return imageSetType.getCsvColumnHeaders() + ",annotationType,coordSys,data,localID" + optionalColumns + "\n" + imageSetType.getCsvColumnValues() + ",\"contour\",\"IMAGE\",\"[[-1.2345, 6.789, 10.1112], [-2.3456, 7.8901, 11.1213], [-3.4567, 8.9102, 12.1314]]\",\"0\"" + roiName + "\n";
            }
        };

        abstract List<Object[]> getMockDBResults(ImageSetType imageSetType, MetaDataTypes metaDataTypes);

        abstract List<AnnotationJson> getExpectedAnnotDetailsAsJson(ImageSetType imageSetType, MetaDataTypes metaDataTypes);

        abstract String getExpectedAnnotDetailsAsCsv(ImageSetType imageSetType, MetaDataTypes metaDataTypes);
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    /**
     * Returns the provided array of {@code Double}s as a {@code List} of {@code Double}s
     *
     * @param coordElements The array {@code Double}s to convert
     * @return a {@code List} of {@code Double}s
     */
    private static List<Double> createMockCoord(Double[] coordElements) {
        return new ArrayList<Double>(Arrays.asList(coordElements));
    }

    ////////////////
    //
    // Test specs //
    //
    ////////////////

    private static final List<Long> MOCK_DATA_IMAGE_SET_IDS = new ArrayList<>();

    @Test
    public void itShouldReturnJsonBeanRepresentation() throws Exception {
        // ARRANGE
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        when(query.getResultList()).thenReturn(this.mockDBResults);

        // ACT
        List<AnnotationJson> result = dataCatalogDao.getAnnotationDetailsByImageSetIDs(MOCK_DATA_IMAGE_SET_IDS);

        // ASSERT
        assertEquals(this.expectedAnnotDetailsAsJson, result);
    }

    @Test
    public void itShouldReturnCsvStringRepresentation() throws Exception {
        // ARRANGE
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyObject())).thenReturn(null);
        when(query.getResultList()).thenReturn(this.mockDBResults);

        // ACT
        String result = dataCatalogDao.getAnnotationDetailsAsCsvByImageSetIDs(MOCK_DATA_IMAGE_SET_IDS);

        // ASSERT
        assertEquals(this.expectedAnnotDetailsAsCsv, result);
    }

}
