package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code BoundingCubeAnnotationJsonTest} evaluates the behavior of {@link BoundingCubeAnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingCubeAnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BoundingCubeAnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType", "coordSys", "localID", "data")
                .withRedefinedSuperclass()
                .verify();
    }

}

