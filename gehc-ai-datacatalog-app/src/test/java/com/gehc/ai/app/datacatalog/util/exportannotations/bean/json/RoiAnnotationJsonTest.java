package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code RoiAnnotationJsonTest} evaluates the behavior of {@link RoiAnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class RoiAnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RoiAnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType", "coordSys", "localID")
                .withRedefinedSuperclass()
                .withRedefinedSubclass(FreeformRoiAnnotationJson.class)
                .verify();
    }

}

