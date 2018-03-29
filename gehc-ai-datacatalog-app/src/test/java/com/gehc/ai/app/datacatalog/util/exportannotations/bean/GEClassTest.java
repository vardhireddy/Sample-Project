package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * [@code GEClassTest} evaluates the behavior of {@link GEClass}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class GEClassTest {

    @Test
    public void equalsContract() {
        EqualsVerifier
                .forClass(GEClass.class)
                .withNonnullFields("name")
                .verify();
    }

}
