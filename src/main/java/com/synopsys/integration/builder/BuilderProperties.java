/**
 * integration-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
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
import java.util.stream.Collectors;

public class BuilderProperties {
    private final Map<BuilderPropertyKey, String> values = new HashMap<>();

    public static BuilderProperties createWithStrings(Set<String> keys) {
        Set<BuilderPropertyKey> builderPropertyKeys = keys.stream().map(BuilderPropertyKey::new).collect(Collectors.toSet());
        return new BuilderProperties(builderPropertyKeys);
    }

    public BuilderProperties(Set<BuilderPropertyKey> keys) {
        keys.forEach(key -> values.put(key, null));
    }

    public String get(BuilderPropertyKey key) {
        return values.get(key);
    }

    public void set(BuilderPropertyKey key, String value) {
        values.put(key, value);
    }

    public void setProperty(String key, String value) {
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(key);
        set(builderPropertyKey, value);
    }

    public Set<BuilderPropertyKey> getKeys() {
        return new HashSet<>(values.keySet());
    }

    public Set<String> getPropertyKeys() {
        return values.keySet().stream().map(BuilderPropertyKey::getKey).map(key -> key.toLowerCase().replace("_", ".")).collect(Collectors.toSet());
    }

    public Set<String> getEnvironmentVariableKeys() {
        return values.keySet().stream().map(BuilderPropertyKey::getKey).collect(Collectors.toSet());
    }

    public Map<BuilderPropertyKey, String> getProperties() {
        return new HashMap<>(values);
    }

    public void setProperties(Set<? extends Map.Entry<String, String>> propertyEntries) {
        propertyEntries
            .stream()
            .map(entry -> {
                BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(entry.getKey());
                return new AbstractMap.SimpleEntry<>(builderPropertyKey, entry.getValue());
            })
            .forEach(entry -> set(entry.getKey(), entry.getValue()));
    }

}
