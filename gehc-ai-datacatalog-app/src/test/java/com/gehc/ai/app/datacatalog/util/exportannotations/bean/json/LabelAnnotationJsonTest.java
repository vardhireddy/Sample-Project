package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code LabelAnnotationJsonTest} evaluates the behavior of {@link LabelAnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelAnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(LabelAnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType", "geClasses")
                .withRedefinedSuperclass()
                .verify();
    }

}
