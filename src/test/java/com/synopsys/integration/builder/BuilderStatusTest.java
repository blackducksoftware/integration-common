package com.synopsys.integration.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class BuilderStatusTest {
    @Test
    public void testNoErrors() {
        BuilderStatus builderStatus = new BuilderStatus();

        assertTrue(builderStatus.isValid());
        assertTrue(builderStatus.getErrorMessages().isEmpty());
        assertTrue(builderStatus.getFullErrorMessage().isEmpty());
        assertTrue(builderStatus.getFullErrorMessage(":").isEmpty());
    }

    @Test
    public void testSingleError() {
        BuilderStatus builderStatus = new BuilderStatus();
        String error = "Error 1";
        builderStatus.addErrorMessage(error);

        assertFalse(builderStatus.isValid());
        assertEquals(error, builderStatus.getErrorMessages().get(0));
        assertEquals(error, builderStatus.getFullErrorMessage());
        assertEquals(error, builderStatus.getFullErrorMessage(":"));
    }

    @Test
    public void testMultipleErrors() {
        BuilderStatus builderStatus = new BuilderStatus();
        String errorOne = "Error 1";
        String errorTwo = "Error 1";
        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add(errorOne);
        expectedErrors.add(errorTwo);
        builderStatus.addAllErrorMessages(expectedErrors);

        assertFalse(builderStatus.isValid());
        assertEquals(errorOne, builderStatus.getErrorMessages().get(0));
        assertEquals(errorTwo, builderStatus.getErrorMessages().get(1));
        assertEquals(expectedErrors, builderStatus.getErrorMessages());
        assertEquals(StringUtils.join(expectedErrors, ":"), builderStatus.getFullErrorMessage(":"));
    }
}
