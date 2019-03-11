/**
 * integration-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.builder;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class BuilderProperties {
    private final String prefix;
    private final Map<BuilderPropertyKey, String> values = new HashMap<>();

    public static BuilderProperties createWithPrefixAndStrings(String prefix, Set<String> keys) {
        Set<BuilderPropertyKey> builderPropertyKeys = keys.stream().map(BuilderPropertyKey::new).collect(Collectors.toSet());
        return new BuilderProperties(prefix, builderPropertyKeys);
    }

    public static BuilderProperties createWithStrings(Set<String> keys) {
        Set<BuilderPropertyKey> builderPropertyKeys = keys.stream().map(BuilderPropertyKey::new).collect(Collectors.toSet());
        return new BuilderProperties(builderPropertyKeys);
    }

    public BuilderProperties(String prefix, Set<BuilderPropertyKey> keys) {
        if (StringUtils.isNotBlank(prefix)) {
            prefix = new BuilderPropertyKey(prefix).getKey();
            this.prefix = prefix.endsWith("_") ? prefix : prefix + "_";
        } else {
            this.prefix = "";
        }

        keys.forEach(key -> values.put(key, null));
    }

    public BuilderProperties(Set<BuilderPropertyKey> keys) {
        this("", keys);
    }

    public String get(BuilderPropertyKey key) {
        return values.get(key);
    }

    public void set(BuilderPropertyKey key, String value) {
        values.put(key, value);
    }

    public void setProperty(String key, String value) {
        BuilderPropertyKey builderPropertyKey = calculateKeyFromString(key);
        set(builderPropertyKey, value);
    }

    public Set<BuilderPropertyKey> getKeys() {
        return values.keySet().stream().collect(Collectors.toSet());
    }

    public Set<String> getPropertyKeys() {
        return values.keySet().stream().map(key -> prefix + key.getKey()).map(key -> key.toLowerCase().replace("_", ".")).collect(Collectors.toSet());
    }

    public Set<String> getEnvironmentVariableKeys() {
        return values.keySet().stream().map(key -> prefix + key.getKey()).collect(Collectors.toSet());
    }

    public Map<BuilderPropertyKey, String> getProperties() {
        return values.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    public void setProperties(Set<? extends Map.Entry<String, String>> propertyEntries) {
        propertyEntries
                .stream()
                .map(entry -> {
                    BuilderPropertyKey builderPropertyKey = calculateKeyFromString(entry.getKey());
                    return new AbstractMap.SimpleEntry<>(builderPropertyKey, entry.getValue());
                })
                .forEach(entry -> set(entry.getKey(), entry.getValue()));
    }

    private BuilderPropertyKey calculateKeyFromString(String key) {
        if (StringUtils.isNotBlank(prefix) && key.startsWith(prefix)) {
            key = key.substring(prefix.length());
        }

        return new BuilderPropertyKey(key);
    }

}
