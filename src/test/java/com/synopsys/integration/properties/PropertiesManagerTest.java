package com.synopsys.integration.properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class PropertiesManagerTest {
    private static final String FROM_FILE_VALID = "TEST_PROPERTY_TWO";
    private static final String FROM_FILE_INVALID = "SHOULD_NOT_EXIST";

    private static final String VARIABLE_NAME_FROM_ENVIRONMENT = "HOME";
    private static final String FROM_ENV_VALID = "home";
    private static final String FROM_ENV_INVALID = "should.not.exist";

    @Test
    public void testLoadProperties() {
        String propertyKey = RandomStringUtils.randomAlphanumeric(10);
        String propertyValue = RandomStringUtils.randomAlphanumeric(10);
        Properties properties = new Properties();
        properties.put(propertyKey, propertyValue);

        PropertiesManager propertiesManager = PropertiesManager.loadProperties(properties);
        assertTrue(propertiesManager.containsKey(propertyKey));
        assertEquals(propertyValue, propertiesManager.getProperty(propertyKey).orElse(null));
    }

    @Test
    public void testLoadFromFile() {
        String propertiesFullPath = Paths.get("").toAbsolutePath() + "/" + TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION;
        PropertiesManager propertiesManager = PropertiesManager.loadFromFile(propertiesFullPath);
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(propertiesManager.getProperty(FROM_FILE_INVALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_INVALID).isPresent());
    }

    @Test
    public void testLoadFromEnvironment() {
        PropertiesManager propertiesManager = PropertiesManager.loadFromEnvironment(Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertEquals(1, propertiesManager.getProperties().size());
    }

    @Test
    public void testLoadWithOverrides() {
        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION,
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(propertiesManager.getProperty(VARIABLE_NAME_FROM_ENVIRONMENT).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_FILE_INVALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_INVALID).isPresent());
    }

    @Test
    public void testPropertiesLoadPriority() {
        String testValue = RandomStringUtils.randomAlphanumeric(10);
        String testProperty = FROM_ENV_VALID + "=" + testValue;

        File testPropertiesFile = createTmpFile();
        assertDoesNotThrow(() -> Files.write(testPropertiesFile.toPath(), testProperty.getBytes(StandardCharsets.UTF_8)));

        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides(testPropertiesFile.getAbsolutePath(),
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertNotEquals(testValue, propertiesManager.getProperty(FROM_ENV_VALID).orElse(null));
    }

    @Test
    public void testNotFoundInput() {
        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides(RandomStringUtils.randomAlphanumeric(25),
            Collections.singletonMap(RandomStringUtils.randomAlphanumeric(50), RandomStringUtils.randomAlphanumeric(5)));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testNullInput() {
        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides(null, null);
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testEmptyStringInput() {
        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides("      \n\t  \r  ", Collections.singletonMap(" ", " "));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testBlankStringInput() {
        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides("", Collections.singletonMap("", ""));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    @Test
    public void testEmptyFileInput() {
        File emptyPropertiesFile = createTmpFile();

        PropertiesManager propertiesManager = PropertiesManager.loadWithOverrides(emptyPropertiesFile.getAbsolutePath(), Collections.singletonMap("", ""));
        assertTrue(propertiesManager.getProperties().isEmpty());
    }

    private File createTmpFile() {
        File emptyPropertiesFile = assertDoesNotThrow(() -> File.createTempFile(RandomStringUtils.randomAlphanumeric(10), ".properties"));
        assertDoesNotThrow(emptyPropertiesFile::createNewFile);
        emptyPropertiesFile.deleteOnExit();
        return emptyPropertiesFile;
    }

}
