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
 * Create Properties object from a Properties object, a file, the environment,
 * or both a file and the environment.
 * <b>Not</b> intended to be used for Unit tests.
 *
 * @author danam
 * @since April 19, 2024
 */
public class PropertiesManager {

    private final Properties properties;

    /**
     * Load properties from existing Properties.
     *
     * @param properties
     *            An existing Properties object.
     * @return PropertiesManager
     */
    public static PropertiesManager loadProperties(Properties properties) {
        return new PropertiesManager(properties);
    }

    /**
     * Load properties from file only.
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *            Data is expected to be in the format NAME=VALUE.
     *            The NAME is how the variable will be represented in the Properties object.
     * @return PropertiesManager
     */
    public static PropertiesManager loadFromFile(String propertiesFileLocation) {
        return PropertiesManager.loadWithOverrides(propertiesFileLocation, null);
    }

    /**
     * Load properties from environment only.
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The map key is the name of the variable within the run environment.
     *            The map value is how the variable will be represented in Properties object.
     * @return PropertiesManager
     */
    public static PropertiesManager loadFromEnvironment(Map<String, String> environmentProperties) {
        return PropertiesManager.loadWithOverrides(null, environmentProperties);
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
     * @return PropertiesManager
     */
    public static PropertiesManager loadWithOverrides(String propertiesFileLocation, Map<String, String> environmentProperties) {
        PropertiesManager propertiesManager = new PropertiesManager();
        propertiesManager.addPropertiesFromFile(propertiesFileLocation);
        propertiesManager.addPropertiesFromEnv(environmentProperties);
        return propertiesManager;
    }

    protected PropertiesManager() {
        this.properties = new Properties();
    }

    protected PropertiesManager(Properties properties) {
        this.properties = new Properties();
        this.properties.putAll(properties);
    }

    /**
     * Get all loaded properties.
     *
     * @return Properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Search for Property by Property name.
     *
     * @param propertyKey
     *            String used to search Properties.
     * @return Optional.ofNullable String
     */
    public Optional<String> getProperty(String propertyKey) {
        return Optional.ofNullable(getProperties().getProperty(propertyKey));
    }

    /**
     * Check for existence of Property by Property name.
     *
     * @param propertyKey
     *            String used to search Properties.
     * @return boolean
     */
    public boolean containsKey(String propertyKey) {
        return getProperties().containsKey(propertyKey);
    }

    /**
     * Load properties from file.
     *
     * @param propertiesLocation
     *            The path to file containing variables to load.
     */
    protected void addPropertiesFromFile(String propertiesLocation) {
        if (StringUtils.isNotBlank(propertiesLocation)) {
            try (InputStream inputStream = Files.newInputStream(Paths.get(propertiesLocation))) {
                getProperties().load(inputStream);
            } catch (IOException ioException) {
                System.out.println("Failed to load properties from " + propertiesLocation);
            }
        }
    }

    /**
     * Load properties from environment.
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     */
    protected void addPropertiesFromEnv(Map<String, String> environmentProperties) {
        if (MapUtils.isNotEmpty(environmentProperties)) {
            for (String envVarName : environmentProperties.keySet()) {
                String envVarValue = System.getenv(envVarName);
                if (StringUtils.isNotBlank(envVarValue)) {
                    getProperties().put(environmentProperties.get(envVarName), envVarValue);
                }
            }
        }
    }
}