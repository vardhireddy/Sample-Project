package com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv;

import org.junit.Test;

/**
 * {@code ColumnHeaderTest} evaluates the behavior of {@link ColumnHeader}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class ColumnHeaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIllegalArgumentExceptionWhenGivenNegativePriority() {
        // ARRANGE
        final int illegalPriority = -1;

        // ACT
        new ColumnHeader("Test header", illegalPriority);
    }

}
