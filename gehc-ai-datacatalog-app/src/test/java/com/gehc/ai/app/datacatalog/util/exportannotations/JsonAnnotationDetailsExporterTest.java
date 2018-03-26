package com.gehc.ai.app.datacatalog.util.exportannotations;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

/**
 * {@code JsonAnnotationDetailsExporterTest} evaluates the behavior of {@link JsonAnnotationDetailsExporter}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class JsonAnnotationDetailsExporterTest {

    @Test(expected = AssertionError.class)
    public void itShouldThrowAssertionErrorWhenAttemptingToInstantiate() throws Throwable {
        // ARRANGE
        Constructor<JsonAnnotationDetailsExporter> constructor = JsonAnnotationDetailsExporter.class.getDeclaredConstructor();
        assumeThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);

        // ACT
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            // We expect to get an InvocationTargetException that wraps the expected AssertionError
            throw e.getTargetException();
        }
    }

}
