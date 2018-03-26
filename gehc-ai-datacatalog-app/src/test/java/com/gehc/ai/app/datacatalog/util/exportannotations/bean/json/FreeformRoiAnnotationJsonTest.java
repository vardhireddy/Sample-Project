package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code FreeformRoiAnnotationJsonTest} evaluates the behavior of {@link FreeformRoiAnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class FreeformRoiAnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(FreeformRoiAnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType", "coordSys", "localID", "data")
                .withRedefinedSuperclass()
                .verify();
    }

}

