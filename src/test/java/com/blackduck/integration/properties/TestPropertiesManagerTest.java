package com.blackduck.integration.properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.blackduck.integration.exception.IntegrationException;

public class TestPropertiesManagerTest {
    private static final String FROM_FILE_VALID = "TEST_PROPERTY_ONE";
    private static final String TEST_PROPERTY_VALUE = getRandom(10);
    private static final String FROM_FILE_INVALID = "SHOULD_NOT_EXIST";

    private static final String VARIABLE_NAME_FROM_ENVIRONMENT = "PATH";
    private static final String FROM_ENV_VALID = "path";
    private static final String FROM_ENV_INVALID = "should.not.exist";

    @BeforeEach
    public void init() {
        String testProperty = FROM_FILE_VALID + "=" + TEST_PROPERTY_VALUE;

        File testPropertiesFile = new File(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION);
        assertDoesNotThrow(() -> testPropertiesFile.getParentFile().mkdirs());
        assertDoesNotThrow(testPropertiesFile::createNewFile);
        assertDoesNotThrow(() -> Files.write(testPropertiesFile.toPath(), testProperty.getBytes(StandardCharsets.UTF_8)));
        assertTrue(testPropertiesFile.setReadable(true));
        testPropertiesFile.deleteOnExit();
    }

    @Test
    public void testLoadProperties() {
        String propertyKey = getRandom(10);
        String propertyValue = getRandom(10);
        Properties properties = new Properties();
        properties.put(propertyKey, propertyValue);

        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadProperties(properties);
        assertTrue(testPropertiesManager.containsKey(propertyKey));
        assertEquals(propertyValue, testPropertiesManager.getProperty(propertyKey).orElse(null));
    }

    @Test
    public void testLoadFromDefaultFile() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromDefaultFile();
        assertTrue(testPropertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(testPropertiesManager.containsKey(FROM_FILE_INVALID));
        assertFalse(testPropertiesManager.containsKey(FROM_ENV_VALID));
        assertFalse(testPropertiesManager.containsKey(FROM_ENV_INVALID));
    }

    @Test
    public void testLoadFromEnvironmentAndDefaultFile() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromEnvironmentAndDefaultFile(Collections.singletonMap("PATH", "path"));
        assertTrue(testPropertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(testPropertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(testPropertiesManager.containsKey(VARIABLE_NAME_FROM_ENVIRONMENT));
        assertFalse(testPropertiesManager.containsKey(FROM_ENV_INVALID));
        assertFalse(testPropertiesManager.containsKey(FROM_FILE_INVALID));
    }

    @Test
    public void testLoadFromFile() {
        String propertiesFullPath = Paths.get("").toAbsolutePath() + "/" + TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION;
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromFile(propertiesFullPath);
        assertTrue(testPropertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_FILE_VALID).isPresent());

        assertFalse(testPropertiesManager.containsKey(FROM_FILE_INVALID));
        assertFalse(testPropertiesManager.containsKey(FROM_ENV_VALID));
        assertFalse(testPropertiesManager.containsKey(FROM_ENV_INVALID));
    }

    @Test
    public void testLoadFromEnvironment() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromEnvironment(Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(testPropertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertEquals(1, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testLoadWithOverrides() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadWithOverrides(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION,
            Collections.singletonMap(VARIABLE_NAME_FROM_ENVIRONMENT, FROM_ENV_VALID));
        assertTrue(testPropertiesManager.containsKey(FROM_FILE_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_FILE_VALID).isPresent());
        assertTrue(testPropertiesManager.containsKey(FROM_ENV_VALID));
        assertTrue(testPropertiesManager.getProperty(FROM_ENV_VALID).isPresent());

        assertFalse(testPropertiesManager.containsKey(VARIABLE_NAME_FROM_ENVIRONMENT));
        assertFalse(testPropertiesManager.containsKey(FROM_ENV_INVALID));
        assertFalse(testPropertiesManager.containsKey(FROM_FILE_INVALID));
    }

    @Test
    public void testDoesNotExist() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadWithOverrides(getRandom(25),
            Collections.singletonMap(getRandom(10), getRandom(10)));
        assertEquals(0, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testNull() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadWithOverrides(null, null);
        assertEquals(0, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testEmpty() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadWithOverrides("", Collections.singletonMap("", ""));
        assertEquals(0, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testWhiteSpace() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadWithOverrides("      \n\t  \r  ",
            Collections.singletonMap("      \n\t  \r  ", "      \n\t  \r  "));
        assertEquals(0, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testDirectory() {
        Path tempDirectory = assertDoesNotThrow(() -> Files.createTempDirectory(getRandom(10)));
        tempDirectory.toFile().deleteOnExit();

        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromFile(tempDirectory.toString());
        assertEquals(0, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testFileNotReadable() {
        File testPropertiesFile = new File(TestPropertiesManager.DEFAULT_TEST_PROPERTIES_LOCATION);
        assertTrue(testPropertiesFile.length() > 0);
        assertTrue(testPropertiesFile.setReadable(false));
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromFile(testPropertiesFile.toString());
        assertEquals(0, testPropertiesManager.getProperties().size());
    }

    @Test
    public void testRequiredProperty() {
        TestPropertiesManager testPropertiesManager = TestPropertiesManager.loadFromDefaultFile();
        assertDoesNotThrow(() -> assertEquals(TEST_PROPERTY_VALUE, testPropertiesManager.getRequiredProperty(FROM_FILE_VALID)));
        assertThrows(IntegrationException.class, () -> testPropertiesManager.getRequiredProperty(getRandom(25)));
    }

    private static String getRandom(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

}
