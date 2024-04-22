/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.properties;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Assumptions;

/**
 * Create Properties object from a Properties object, a file, the environment,
 * or both a file and the environment.
 * Intended to be used for Unit tests.
 *
 * @author danam
 * @since April 19, 2024
 */
public class TestPropertiesManager extends PropertiesManager {
    public static final String DEFAULT_TEST_PROPERTIES_FILE_NAME = "test.properties";
    public static final String DEFAULT_TEST_PROPERTIES_LOCATION = "src/main/resources/" + DEFAULT_TEST_PROPERTIES_FILE_NAME;

    /**
     * Load properties from existing Properties.
     *
     * @param properties
     *            An existing Properties object.
     * @return TestPropertiesManager
     */
    public static TestPropertiesManager loadProperties(Properties properties) {
        return new TestPropertiesManager(properties);
    }

    /**
     * Load properties from <b>default</b> test file only: {@value DEFAULT_TEST_PROPERTIES_LOCATION}
     * @return TestPropertiesManager
     */
    public static TestPropertiesManager loadFromDefaultFile() {
        return TestPropertiesManager.loadWithOverrides(DEFAULT_TEST_PROPERTIES_LOCATION, Collections.emptyMap());
    }

    /**
     * Load properties from <b>default</b> test file : {@value DEFAULT_TEST_PROPERTIES_LOCATION}
     * and from environment.
     * For properties that exist in both, environment will take precedence.
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The map key is the name of the variable within the run environment.
     *            The map value is how the variable will be represented in Properties object.
     * @return TestPropertiesManager
     */
    public static TestPropertiesManager loadFromEnvironmentAndDefaultFile(Map<String, String> environmentProperties) {
        return TestPropertiesManager.loadWithOverrides(DEFAULT_TEST_PROPERTIES_LOCATION, environmentProperties);
    }

    /**
     * Load properties from file only.
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *            Data is expected to be in the format NAME=VALUE.
     *            The NAME is how the variable will be represented in the Properties object.
     * @return TestPropertiesManager
     */
    public static TestPropertiesManager loadFromFile(String propertiesFileLocation) {
        return TestPropertiesManager.loadWithOverrides(propertiesFileLocation, null);
    }

    /**
     * Load properties from environment only.
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The map key is the name of the variable within the run environment.
     *            The map value is how the variable will be represented in Properties object.
     * @return TestPropertiesManager
     */
    public static TestPropertiesManager loadFromEnvironment(Map<String, String> environmentProperties) {
        return TestPropertiesManager.loadWithOverrides(null, environmentProperties);
    }

    /**
     * Load properties from file and environment.
     * For properties that exist in both, environment will take precedence.
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *            Data is expected to be in the format NAME=VALUE.
     *            The NAME is how the variable will be represented in the Properties object.
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The map key is the name of the variable within the run environment.
     *            The map value is how the variable will be represented in Properties object.
     * @return TestPropertiesManager
     */
    public static TestPropertiesManager loadWithOverrides(String propertiesFileLocation, Map<String, String> environmentProperties) {
        TestPropertiesManager testPropertiesManager = new TestPropertiesManager();
        testPropertiesManager.addPropertiesFromFile(propertiesFileLocation);
        testPropertiesManager.addPropertiesFromEnv(environmentProperties);
        return testPropertiesManager;
    }

    private TestPropertiesManager() {
        super();
    }

    private TestPropertiesManager(Properties properties) {
        super(properties);
    }

    /**
     * Search for Property by Property name.
     * Use Assumptions.assumeTrue before retrieving property value.
     *
     * @param propertyKey
     *            String used to search Properties.
     * @return Optional.ofNullable String
     */
    @Override
    public Optional<String> getProperty(String propertyKey) {
        Assumptions.assumeTrue(containsKey(propertyKey));
        return super.getProperty(propertyKey);
    }
}
