package com.gehc.ai.app.datacatalog.dao.impl;

import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.FreeformRoi;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.LabelAnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.FreeformRoiAnnotationJson;
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

    private List<Object[]> mockDBResults;

    private List<AnnotationJson> expectedAnnotDetails;

    /**
     * Create a new parameterized test.
     *
     * @param annotationType       The annotation type being tested
     * @param mockDBResults        The mock results from a mock DB query for retrieving the annotation details for a particular data collection ID
     * @param expectedAnnotDetails The expected bean representation of the DB results
     */
    public GetAnnotationDetailsTest(String annotationType, List<Object[]> mockDBResults, List<AnnotationJson> expectedAnnotDetails) {
        this.mockDBResults = mockDBResults;
        this.expectedAnnotDetails = expectedAnnotDetails;
    }

    @Parameters(name = "GIVEN a {0} annotation")
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<Object[]>();

        for (AnnotationType annotType : AnnotationType.values()) {
            params.add(
                    new Object[]{
                            annotType.toString(),
                            annotType.getMockDBResults(),
                            annotType.getExpectedAnnotDetails()
                    }
            );
        }

        return params;
    }

    private enum AnnotationType {

        LABEL() {
            @Override
            public List<Object[]> getMockDBResults() {
                List<Object[]> mockDBResults = new ArrayList<>();
                Object[] newObj = new Object[]{"1", "SUID", "DCM", 1L, "label", null, null, null, "{\"name\":\"Foreign Bodies\",\"value\":\"Absent\"}", "{\"name\":\"Calcification\"}", null, null, null, null, null, null, null, null, null, null, null, null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null};
                mockDBResults.add(newObj);
                return mockDBResults;
            }

            @Override
            List<AnnotationJson> getExpectedAnnotDetails() {
                List<AnnotationJson> annotationDetails = new ArrayList<>();
                List<GEClass> geClasses = new ArrayList<>();
                geClasses.add(new GEClass("Foreign Bodies", "Absent"));
                geClasses.add(new GEClass("Calcification", null));
                AnnotationJson annotation = new LabelAnnotationJson("1", "SUID", "DCM", 1L, "label", geClasses);
                annotationDetails.add(annotation);
                return annotationDetails;
            }
        },
        POLYGON() {
            @Override
            public List<Object[]> getMockDBResults() {
                List<Object[]> mockDBResults = new ArrayList<>();
                Object[] newObj = new Object[]{"1", "SUID", "DCM", 1L, "polygon", "ROI Name", "0", "[[-1.2345,6.789,10.1112],[-1.2345,6.789,10.1112],[-1.2345,6.789,10.1112]]", null, null, null, null, null, null, null, null, null, null, null, "IMAGE", null, null, "[\"1.3.6.1.4.1.14519.5.2.1.6279.6001.271903262329812014254288323695\", \"1.3.6.1.4.1.14519.5.2.1.6279.6001.278535546794012771343423876199\"]", null, null, null, null};
                mockDBResults.add(newObj);
                return mockDBResults;
            }

            @Override
            List<AnnotationJson> getExpectedAnnotDetails() {
                List<AnnotationJson> annotationDetails = new ArrayList<>();

                // Create three mock coordinates
                List<List<Double>> coords = new ArrayList<>();

                List<Double> coord = new ArrayList<>();
                coord.add(-1.2345);
                coord.add(6.789);
                coord.add(10.1112);
                coords.add(coord);

                coord = new ArrayList<>();
                coord.add(-1.2345);
                coord.add(6.789);
                coord.add(10.1112);
                coords.add(coord);

                coord = new ArrayList<>();
                coord.add(-1.2345);
                coord.add(6.789);
                coord.add(10.1112);
                coords.add(coord);

                AnnotationJson annotation = new FreeformRoiAnnotationJson("1", "SUID", "DCM", 1L, FreeformRoi.POLYGON, "IMAGE", coords, "0", "ROI name");
                annotationDetails.add(annotation);
                return annotationDetails;
            }
        };

        abstract List<Object[]> getMockDBResults();

        abstract List<AnnotationJson> getExpectedAnnotDetails();
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
        assertEquals(this.expectedAnnotDetails, result);
    }

}
