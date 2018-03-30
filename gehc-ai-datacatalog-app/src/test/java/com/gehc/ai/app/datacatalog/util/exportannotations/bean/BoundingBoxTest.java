package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * [@code BoundingBoxTest} evaluates the behavior of {@link BoundingBox}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingBoxTest {

    @Test
    public void equalsContract() {
        EqualsVerifier
                .forClass(BoundingBox.class)
                .withNonnullFields("xDir","yDir","origin")
                .withRedefinedSubclass(BoundingCube.class)
                .verify();
    }

}
