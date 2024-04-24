/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.exception.IntegrationException;

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
     * Load properties from existing Properties object.
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
     * Validation is performed against the properties file, but not it's contents.
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *            Data is expected to be in the format NAME=VALUE.
     *            The NAME is how the variable will be represented in the Properties object.
     * @return PropertiesManager
     *
     * @throws IOException
     *            If an I/O error occurs when reading from the stream.
     * @throws IntegrationException
     *            If the properties file is invalid, does not exist,
     *            is a directory or is unreadable.
     */
    public static PropertiesManager loadFromFile(String propertiesFileLocation) throws IOException, IntegrationException {
        return PropertiesManager.loadWithOverrides(propertiesFileLocation, null);
    }

    /**
     * Load properties from environment only.
     * If the variable exists in the environment without a value,
     * this empty value will still be added to the properties.
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *            The map key is the name of the variable within the run environment.
     *            The map value is how the variable will be represented in Properties object.
     * @return PropertiesManager
     */
    public static PropertiesManager loadFromEnvironment(Map<String, String> environmentProperties) {
        PropertiesManager propertiesManager = new PropertiesManager();
        propertiesManager.addPropertiesFromEnv(environmentProperties);
        return propertiesManager;
    }

    /**
     * Load properties from file and environment.
     * For properties that exist in both, environment will take precedence.
     * Validation is performed against the properties file, but not it's contents.
     * If the variable exists in the environment without a value,
     * this empty value will still be added to the properties.
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
     *
     * @throws IOException
     *            If an I/O error occurs when reading from the stream.
     * @throws IntegrationException
     *            If the properties file is invalid, does not exist,
     *            is a directory or is unreadable.
     */
    public static PropertiesManager loadWithOverrides(String propertiesFileLocation, Map<String, String> environmentProperties) throws IOException, IntegrationException {
        PropertiesManager propertiesManager = new PropertiesManager();
        propertiesManager.addProperties(propertiesFileLocation, environmentProperties);
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
     * Add values to Properties after validating Properties File.
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     *
     * @throws IOException
     *            If an I/O error occurs when reading from the stream.
     * @throws IntegrationException
     *            If the properties file is invalid, does not exist,
     *            is a directory or is unreadable.
     */
    protected void addProperties(String propertiesFileLocation, Map<String, String> environmentProperties) throws IOException, IntegrationException {
        validatePropertiesLocation(propertiesFileLocation);
        addPropertiesFromFile(propertiesFileLocation);
        addPropertiesFromEnv(environmentProperties);
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
     * Search for Property by property key.
     *
     * @param propertyKey
     *            String used to search Properties.
     * @return Optional.ofNullable String
     */
    public Optional<String> getProperty(String propertyKey) {
        return Optional.ofNullable(properties.getProperty(propertyKey));
    }

    /**
     * Check for existence of Property by property key.
     *
     * @param propertyKey
     *            String used to search Properties.
     * @return boolean
     */
    public boolean containsKey(String propertyKey) {
        return properties.containsKey(propertyKey);
    }

    /**
     * Perform validation on propertiesFileLocation.
     *
     * @param propertiesFileLocation
     *            The path to file containing variables to load.
     *
     * @throws IntegrationException
     *            If the properties file is invalid, does not exist,
     *            is a directory, or is unreadable.
     */
    protected void validatePropertiesLocation(String propertiesFileLocation) throws IntegrationException {
        if (StringUtils.isBlank(propertiesFileLocation)) {
            throw new IntegrationException("Input file name must be valid.");
        }

        File propertiesFile = new File(propertiesFileLocation);
        if (!propertiesFile.exists() || !propertiesFile.isFile() || !propertiesFile.canRead()) {
            throw new IntegrationException("Input file name must be an existing file that can be read.");
        }
    }

    /**
     * Load properties from file. No validation is performed against the contents of the file.
     *
     * @param propertiesLocation
     *            The path to file containing variables to load.
     *
     * @throws IOException
     *            If an I/O error occurs when reading from the stream.
     */
    protected void addPropertiesFromFile(String propertiesLocation) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(propertiesLocation))) {
            properties.load(inputStream);
        }
    }

    /**
     * Load properties from environment. If the variable exists in the environment
     * without a value, this empty value will still be added to the properties.
     *
     * @param environmentProperties
     *            A map of variables used to pull from run environment.
     */
    protected void addPropertiesFromEnv(Map<String, String> environmentProperties) {
        if (MapUtils.isNotEmpty(environmentProperties)) {
            Map<String, String> systemEnvVars = System.getenv();
            for (String envVarName : environmentProperties.keySet()) {
                if (systemEnvVars.containsKey(envVarName)) {
                    properties.put(environmentProperties.get(envVarName), systemEnvVars.get(envVarName));
                }
            }
        }
    }
}