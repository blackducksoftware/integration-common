/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.properties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Create Properties object from properties file, environment, or both.
 * Not intended to be used for Unit tests.
 *
 * @author danam
 * @since April 19, 2024
 */
public class PropertiesManager {

    private final Properties properties = new Properties();

    /**
     * Load properties from file
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *            Data is expected to be in the format VARIABLE_NAME=VALUE.
     *            The VARIABLE_NAME is how the variable will be represented in the Properties object.
     */
    public static PropertiesManager loadProperties(String propertiesFileLocation) {
        return new PropertiesManager(propertiesFileLocation, null);
    }

    /**
     * Load properties from environment
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The key is the name of the variable within the run environment.
     *            The value is how the variable will be represented in Properties object.
     */
    public static PropertiesManager loadProperties(Map<String, String> environmentProperties) {
        return new PropertiesManager(null, environmentProperties);
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
     *            The key is the name of the variable within the run environment.
     *            The value is how the variable will be represented in Properties object.
     */
    public PropertiesManager(String propertiesFileLocation, Map<String, String> environmentProperties) {
        addPropertiesFromFile(propertiesFileLocation);
        addPropertiesFromEnv(environmentProperties);
    }

    /**
     * Get all loaded properties
     *
     * @return Properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Search for Property by Property name
     *
     * @param propertyKey
     *            String used to search Properties
     * @return
     *            Optional.ofNullable String
     */
    public Optional<String> getProperty(String propertyKey) {
        return Optional.ofNullable(getProperties().getProperty(propertyKey));
    }

    /**
     * Check for existence of Property by Property name
     *
     * @param propertyKey
     *            String used to search Properties
     * @return
     *            boolean
     */
    public boolean containsKey(String propertyKey) {
        return properties.containsKey(propertyKey);
    }

    /**
     * Load properties from file
     *
     * @param propertiesLocation
     *            The path to file containing variables to load.
     */
    private void addPropertiesFromFile(String propertiesLocation) {
        if (StringUtils.isNotEmpty(propertiesLocation)) {
            try (InputStream inputStream = Files.newInputStream(Paths.get(propertiesLocation))) {
                getProperties().load(inputStream);
            } catch (IOException ioException) {
                System.out.println("Failed to load properties from " + propertiesLocation);
            }
        }
    }

    /**
     * Load properties from environment
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     */
    private void addPropertiesFromEnv(Map<String, String> environmentProperties) {
        if (MapUtils.isNotEmpty(environmentProperties)) {
            for (String envVarName : environmentProperties.keySet()) {
                String envVarValue = System.getenv(envVarName);
                if (StringUtils.isNotEmpty(envVarValue)) {
                    getProperties().put(environmentProperties.get(envVarName), envVarValue);
                }
            }
        }
    }
}