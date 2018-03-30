package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code PointRoiAnnotationJsonTest} evaluates the behavior of {@link PointRoiAnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class PointRoiAnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(PointRoiAnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType", "coordSys", "localID", "data")
                .withRedefinedSuperclass()
                .verify();
    }

}

