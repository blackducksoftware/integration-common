package com.synopsys.integration.properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPropertiesManagerTest {
    private static final String FROM_FILE_VALID = "TEST_PROPERTY_ONE";
    private static final String FROM_FILE_INVALID = "SHOULD_NOT_EXIST";

    private static final String VARIABLE_NAME_FROM_ENVIRONMENT = "PATH";
    private static final String FROM_ENV_VALID = "path";
    private static final String FROM_ENV_INVALID = "should.not.exist";

    @BeforeEach
    public void init() {
        String testPropertyValue = RandomStringUtils.randomAlphanumeric(10);
        String testProperty = FROM_FILE_VALID + "=" + testPropertyValue;

        File testPropertiesFile = new File(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION);
        assertDoesNotThrow(() -> testPropertiesFile.getParentFile().mkdirs());
        assertDoesNotThrow(testPropertiesFile::createNewFile);
        assertDoesNotThrow(() -> Files.write(testPropertiesFile.toPath(), testProperty.getBytes(StandardCharsets.UTF_8)));
        testPropertiesFile.deleteOnExit();
    }

    @Test
    public void testLoadProperties() {
        String propertyKey = RandomStringUtils.randomAlphanumeric(10);
        String propertyValue = RandomStringUtils.randomAlphanumeric(10);
        Properties properties = new Properties();
        properties.put(propertyKey, propertyValue);

        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadProperties(properties);
        assertTrue(testPropertiesManager.containsKey(propertyKey));
        assertEquals(propertyValue, testPropertiesManager.getProperty(propertyKey).orElse(null));
    }

    @Test
    public void testLoadFromDefaultFile() {
        PropertiesManager propertiesManager = TestPropertiesManager.loadFromDefaultFile();
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(propertiesManager.containsKey(FROM_FILE_INVALID));
        assertFalse(propertiesManager.containsKey(FROM_ENV_VALID));
        assertFalse(propertiesManager.containsKey(FROM_ENV_INVALID));
    }

    @Test
    public void testLoadFromEnvironmentAndDefaultFile() {
        PropertiesManager propertiesManager = TestPropertiesManager.loadFromEnvironmentAndDefaultFile(Collections.singletonMap("PATH", "path"));
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(propertiesManager.containsKey(VARIABLE_NAME_FROM_ENVIRONMENT));
        assertFalse(propertiesManager.containsKey(FROM_ENV_INVALID));
        assertFalse(propertiesManager.containsKey(FROM_FILE_INVALID));
    }

    @Test
    public void testLoadFromFile() {
        String propertiesFullPath = Paths.get("").toAbsolutePath() + "/" + TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION;
        PropertiesManager propertiesManager = TestPropertiesManager.loadFromFile(propertiesFullPath);
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(propertiesManager.containsKey(FROM_FILE_INVALID));
        assertFalse(propertiesManager.containsKey(FROM_ENV_VALID));
        assertFalse(propertiesManager.containsKey(FROM_ENV_INVALID));
    }

    @Test
    public void testLoadFromEnvironment() {
        PropertiesManager propertiesManager = TestPropertiesManager.loadFromEnvironment(Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertEquals(1, propertiesManager.getProperties().size());
    }

    @Test
    public void testLoadWithOverrides() {
        PropertiesManager propertiesManager = TestPropertiesManager.loadWithOverrides(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION,
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(propertiesManager.containsKey(VARIABLE_NAME_FROM_ENVIRONMENT));
        assertFalse(propertiesManager.containsKey(FROM_ENV_INVALID));
        assertFalse(propertiesManager.containsKey(FROM_FILE_INVALID));
    }
}
