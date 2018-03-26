package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * {@code AnnotationJsonTest} evaluates the behavior of {@link AnnotationJson}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class AnnotationJsonTest {

    @Test
    public void equalsContract() {
        EqualsVerifier
                .forClass(AnnotationJson.class)
                .withNonnullFields("patientID", "seriesUID", "imageSetFormat", "annotationID", "annotationType")
                .withRedefinedSubclass(LabelAnnotationJson.class)
                .verify();
    }

}
