/**
 * integration-common
 * <p>
 * Copyright (c) 2020 Synopsys, Inc.
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.util;

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
