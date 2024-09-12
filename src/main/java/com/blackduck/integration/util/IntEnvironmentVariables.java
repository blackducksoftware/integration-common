/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.HashMap;
import java.util.Map;

public class IntEnvironmentVariables {
    public static final String BDS_CACERTS_OVERRIDE = "BDS_CACERTS_OVERRIDE";

    private final Map<String, String> environmentVariables = new HashMap<>();

    public static final IntEnvironmentVariables includeSystemEnv() {
        IntEnvironmentVariables intEnvironmentVariables = new IntEnvironmentVariables();
        intEnvironmentVariables.putAll(System.getenv());
        return intEnvironmentVariables;
    }

    public static final IntEnvironmentVariables empty() {
        return new IntEnvironmentVariables();
    }

    private IntEnvironmentVariables() {
        // for clarity, the static creators are required
    }

    public void putAll(Map<String, String> map) {
        environmentVariables.putAll(map);
    }

    public void put(String key, String value) {
        environmentVariables.put(key, value);
    }

    public boolean containsKey(String key) {
        return environmentVariables.containsKey(key);
    }

    public String getValue(String key) {
        return getValue(key, null);
    }

    public String getValue(String key, String defaultValue) {
        String value = environmentVariables.get(key);
        if (value == null && defaultValue != null) {
            value = defaultValue;
        }
        return value;
    }

    public Map<String, String> getVariables() {
        Map<String, String> variables = new HashMap<>();
        variables.putAll(environmentVariables);
        return variables;
    }

}
