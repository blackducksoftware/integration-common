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

import org.junit.jupiter.api.Assumptions;

/**
 * Create Properties object from properties file, environment, or both.
 * Intended to be used for Unit tests.
 *
 * @author danam
 * @since April 19, 2024
 */
public class TestPropertiesManager extends PropertiesManager {
    public static final String DEFAULT_TEST_PROPERTIES_FILE_NAME = "test.properties";
    public static final String DEFAULT_TEST_PROPERTIES_LOCATION = "src/main/resources/" + DEFAULT_TEST_PROPERTIES_FILE_NAME;

    /**
     * Load properties from default test file {@value DEFAULT_TEST_PROPERTIES_LOCATION}
     */
    public static TestPropertiesManager loadDefaultTestProperties() {
        return new TestPropertiesManager(DEFAULT_TEST_PROPERTIES_LOCATION, Collections.emptyMap());
    }

    /**
     * Load properties from environment and default test file {@value DEFAULT_TEST_PROPERTIES_LOCATION}
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The key is the name of the variable within the environment.
     *            The value is how the variable will be represented in Properties object.
     */
    public static TestPropertiesManager loadDefaultTestProperties(Map<String, String> environmentProperties) {
        return new TestPropertiesManager(DEFAULT_TEST_PROPERTIES_LOCATION, environmentProperties);
    }

    /**
     * Load properties from file and environment
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *            Data is expected to be in the format VARIABLE_NAME=VALUE.
     *            The VARIABLE_NAME is how the variable will be represented in the Properties object.
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The key is the name of the variable within the environment.
     *            The value is how the variable will be represented in Properties object.
     */
    public TestPropertiesManager(String propertiesFileLocation, Map<String, String> environmentProperties) {
        super(propertiesFileLocation, environmentProperties);
    }

    /**
     * Search for Property by Property name.
     * Use Assumptions.assumeTrue before retrieving property value.
     *
     * @param propertyKey
     *            String used to search Properties
     * @return
     *            Optional.ofNullable String
     */
    @Override
    public Optional<String> getProperty(String propertyKey) {
        Assumptions.assumeTrue(containsKey(propertyKey));
        return super.getProperty(propertyKey);
    }
}
