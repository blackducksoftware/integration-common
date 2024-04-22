package com.synopsys.integration.properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class PropertiesManagerTest {
    private static final String FROM_FILE_VALID = "TEST_PROPERTY_TWO";
    private static final String FROM_FILE_INVALID = "SHOULD_NOT_EXIST";

    private static final String VARIABLE_NAME_FROM_ENVIRONMENT = "HOME";
    private static final String FROM_ENV_VALID = "home";
    private static final String FROM_ENV_INVALID = "should.not.exist";

    @Test
    public void testPropertiesExpectedFromFileOnly() {
        String propertiesFullPath = Paths.get("").toAbsolutePath() + "/" + TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION;
        PropertiesManager propertiesManager = PropertiesManager.loadProperties(propertiesFullPath);
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(propertiesManager.getProperty(FROM_FILE_INVALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_INVALID).isPresent());
    }

    @Test
    public void testPropertiesExpectedFromEnvOnly() {
        PropertiesManager propertiesManager = PropertiesManager.loadProperties(Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertEquals(1, propertiesManager.getProperties().size());
    }

    @Test
    public void testPropertiesExpectedFromBoth() {
        PropertiesManager propertiesManager = new PropertiesManager(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION, Collections.singletonMap(
            VARIABLE_NAME_FROM_ENVIRONMENT,
            FROM_ENV_VALID
        ));
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(propertiesManager.getProperty(VARIABLE_NAME_FROM_ENVIRONMENT).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_FILE_INVALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_INVALID).isPresent());
    }

    @Test
    public void testNotFoundInput() {
        PropertiesManager propertiesManager = new PropertiesManager(RandomStringUtils.randomAlphanumeric(25), Collections.singletonMap(RandomStringUtils.randomAlphanumeric(50), RandomStringUtils.randomAlphanumeric(5)));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testNullInput() {
        PropertiesManager propertiesManager = new PropertiesManager(null, null);
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testEmptyStringInput() {
        PropertiesManager propertiesManager = new PropertiesManager("", Collections.singletonMap("", ""));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testEmptyFileInput() {
        File testPropertiesFile = assertDoesNotThrow(() -> File.createTempFile(RandomStringUtils.randomAlphanumeric(10), ".properties"));
        assertDoesNotThrow(testPropertiesFile::createNewFile);
        testPropertiesFile.deleteOnExit();

        PropertiesManager propertiesManager = new PropertiesManager(testPropertiesFile.getAbsolutePath(), Collections.singletonMap("", ""));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }
}
