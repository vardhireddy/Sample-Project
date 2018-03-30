package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * [@code BoundingCubeTest} evaluates the behavior of {@link BoundingCube}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingCubeTest {

    @Test
    public void equalsContract() {
        EqualsVerifier
                .forClass(BoundingCube.class)
                .withNonnullFields("xDir", "yDir", "zDir", "origin")
                .withRedefinedSuperclass()
                .verify();
    }

}
