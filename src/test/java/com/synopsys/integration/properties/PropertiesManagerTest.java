package com.synopsys.integration.properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.synopsys.integration.exception.IntegrationException;

public class PropertiesManagerTest {
    private static final String FROM_FILE_VALID = "TEST_PROPERTY_TWO";
    private static final String FROM_FILE_INVALID = "SHOULD_NOT_EXIST";

    private static final String VARIABLE_NAME_FROM_ENVIRONMENT = "HOME";
    private static final String FROM_ENV_VALID = "home";
    private static final String FROM_ENV_INVALID = "should.not.exist";

    @BeforeEach
    public void init() {
        String testPropertyValue = getRandom(10);
        String testProperty = FROM_FILE_VALID + "=" + testPropertyValue;

        File testPropertiesFile = new File(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION);
        assertDoesNotThrow(() -> testPropertiesFile.getParentFile().mkdirs());
        assertDoesNotThrow(testPropertiesFile::createNewFile);
        assertDoesNotThrow(() -> Files.write(testPropertiesFile.toPath(), testProperty.getBytes(StandardCharsets.UTF_8)));
        testPropertiesFile.deleteOnExit();
    }

    @Test
    public void testLoadProperties() {
        String propertyKey = getRandom(10);
        String propertyValue = getRandom(10);
        Properties properties = new Properties();
        properties.put(propertyKey, propertyValue);

        PropertiesManager propertiesManager = PropertiesManager.loadProperties(properties);
        assertTrue(propertiesManager.containsKey(propertyKey));
        assertEquals(propertyValue, propertiesManager.getProperty(propertyKey).orElse(null));
    }

    @Test
    public void testLoadFromFile() {
        String propertiesFullPath = Paths.get("").toAbsolutePath() + "/" + TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION;
        PropertiesManager propertiesManager = assertDoesNotThrow(() -> PropertiesManager.loadFromFile(propertiesFullPath));
        assertTrue(propertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(propertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(propertiesManager.getProperty(FROM_FILE_INVALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());
        assertFalse(propertiesManager.getProperty(FROM_ENV_INVALID).isPresent());
    }

    @Test
    public void testLoadFromEnvironment() {
        PropertiesManager propertiesManager = assertDoesNotThrow(() -> PropertiesManager.loadFromEnvironment(
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID)));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(propertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertEquals(1, propertiesManager.getProperties().size());
    }

    @Test
    public void testLoadWithOverrides() {
        PropertiesManager propertiesManager = assertDoesNotThrow(() -> PropertiesManager.loadWithOverrides(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION,
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID)));
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
        String testValue = getRandom(10);
        String testProperty = FROM_ENV_VALID + "=" + testValue;

        File testPropertiesFile = createTmpFile();
        assertDoesNotThrow(() -> Files.write(testPropertiesFile.toPath(), testProperty.getBytes(StandardCharsets.UTF_8)));

        PropertiesManager propertiesManager = assertDoesNotThrow(() -> PropertiesManager.loadWithOverrides(testPropertiesFile.getAbsolutePath(),
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID)));
        assertTrue(propertiesManager.containsKey(FROM_ENV_VALID));
        assertNotEquals(testValue, propertiesManager.getProperty(FROM_ENV_VALID).orElse(null));
        assertEquals(System.getenv(VARIABLE_NAME_FROM_ENVIRONMENT), propertiesManager.getProperty(FROM_ENV_VALID).orElse(null));
    }

    @Test
    public void dana() {
        new EnvironmentVariables();

    }

    @Test
    public void testDoesNotExist() {
        assertThrows(IntegrationException.class, () -> PropertiesManager.loadFromFile(getRandom(25)));

        PropertiesManager propertiesManager = assertDoesNotThrow(() ->
            PropertiesManager.loadFromEnvironment(Collections.singletonMap(getRandom(10), getRandom(10))));
        assertEquals(0, propertiesManager.getProperties().size());
    }

    @Test
    public void testNull() {
        assertThrows(IntegrationException.class, () -> PropertiesManager.loadFromFile(null));

        PropertiesManager propertiesManager = assertDoesNotThrow(() -> PropertiesManager.loadFromEnvironment( null));
        assertEquals(0, propertiesManager.getProperties().size());
    }

    @Test
    public void testEmpty() {
        assertThrows(IntegrationException.class, () -> PropertiesManager.loadFromFile(""));

        PropertiesManager propertiesManager = assertDoesNotThrow(() -> PropertiesManager.loadFromEnvironment(Collections.singletonMap("", "")));
        assertEquals(0, propertiesManager.getProperties().size());
    }

    @Test
    public void testWhiteSpace() {
        assertThrows(IntegrationException.class, () -> PropertiesManager.loadFromFile("      \n\t  \r  "));

        PropertiesManager propertiesManager = assertDoesNotThrow(() ->
            PropertiesManager.loadFromEnvironment(Collections.singletonMap("      \n\t  \r  ", "      \n\t  \r  ")));
        assertEquals(0, propertiesManager.getProperties().size());
    }

    @Test
    public void testFileIsDirectory() {
        Path tempDirectory = assertDoesNotThrow(() -> Files.createTempDirectory(getRandom(10)));
        tempDirectory.toFile().deleteOnExit();

        assertThrows(IntegrationException.class, () -> PropertiesManager.loadFromFile(tempDirectory.toString()));
    }

    @Test
    public void testFileNotReadable() {
        File testPropertiesFile = new File(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION);
        assertTrue(testPropertiesFile.length() > 0);
        assertTrue(testPropertiesFile.setReadable(false));

        assertThrows(IntegrationException.class, () -> PropertiesManager.loadFromFile(testPropertiesFile.toString()));
    }

    private File createTmpFile() {
        File emptyPropertiesFile = assertDoesNotThrow(() -> File.createTempFile(getRandom(10), ".properties"));
        assertDoesNotThrow(emptyPropertiesFile::createNewFile);
        emptyPropertiesFile.deleteOnExit();
        return emptyPropertiesFile;
    }

    private String getRandom(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

}
