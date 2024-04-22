package com.synopsys.integration.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class TestPropertiesManagerTest {
    private static final String FROM_FILE_VALID = "SERVER_URL";
    private static final String FROM_FILE_INVALID = "SHOULD_NOT_EXIST";

    private static final String VARIABLE_NAME_FROM_ENVIRONMENT = "PATH";
    private static final String FROM_ENV_VALID = "path";
    private static final String FROM_ENV_INVALID = "should.not.exist";

    @Test
    public void testPropertiesExpectedFromFileOnly() {
        PropertiesManager propertiesManager = TestPropertiesManager.loadDefaultTestProperties();
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(propertiesManager.containsKey(FROM_FILE_INVALID));
        assertFalse(propertiesManager.containsKey(FROM_ENV_VALID));
        assertFalse(propertiesManager.containsKey(FROM_ENV_INVALID));
    }

    @Test
    public void testPropertiesExpectedFromEnvOnly() {
        PropertiesManager propertiesManager = new TestPropertiesManager(RandomStringUtils.randomAlphanumeric(25), Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertEquals(1, propertiesManager.getProperties().size());
    }

    @Test
    public void testPropertiesExpectedFromBoth() {
        PropertiesManager propertiesManager = TestPropertiesManager.loadDefaultTestProperties(Collections.singletonMap("PATH", "path"));
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(propertiesManager.containsKey(VARIABLE_NAME_FROM_ENVIRONMENT));
        assertFalse(propertiesManager.containsKey(FROM_ENV_INVALID));
        assertFalse(propertiesManager.containsKey(FROM_FILE_INVALID));
    }
}
