package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code BoundingBoxAnnotationJsonTest} evaluates the behavior of {@link BoundingBoxAnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingBoxAnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BoundingBoxAnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType", "coordSys", "localID", "data")
                .withRedefinedSuperclass()
                .verify();
    }

}

