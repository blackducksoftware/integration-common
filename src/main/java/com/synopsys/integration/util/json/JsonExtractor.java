/**
 * integration-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
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
package com.synopsys.integration.util.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.synopsys.integration.util.PathNode;

public class JsonExtractor {
    private final Gson gson;

    public JsonExtractor(final Gson gson) {
        this.gson = gson;
    }

    public JsonObject createJsonObject(final String json) {
        return gson.fromJson(json, JsonObject.class);
    }

    public <T> Optional<T> getFirstValueFromJson(final HierarchicalField field, final String json) {
        return getFirstValueFromJsonObject(field, createJsonObject(json));
    }

    public <T> Optional<T> getFirstValueFromJsonObject(final HierarchicalField field, final JsonObject object) {
        final List<T> values = getValuesFromJsonObject(field, object);
        if (values.isEmpty()) {
            return Optional.empty();
        }
        final int lastIndex = values.size() - 1;
        return Optional.of(values.get(lastIndex));
    }

    public <T> List<T> getValuesFromJson(final HierarchicalField field, final String json) {
        return getValuesFromJsonObject(field, createJsonObject(json));
    }

    public <T> List<T> getValuesFromJsonObject(final HierarchicalField field, final JsonObject object) {
        final List<String> fieldNameHierarchy = field.getFullPathToField();
        final PathNode<String> head = PathNode.createPath(fieldNameHierarchy);

        final List<JsonElement> foundElements = getInnerElements(object, head);
        final List<T> convertedObjects = new ArrayList<>();
        for (final JsonElement element : foundElements) {
            final T convertedElement = gson.fromJson(element, field.getType());
            convertedObjects.add(convertedElement);
        }
        return convertedObjects;
    }

    private List<JsonElement> getInnerElements(final JsonElement element, final PathNode<String> pathNode) {
        if (element == null) {
            return Collections.emptyList();
        } else {
            if (element.isJsonPrimitive()) {
                return Arrays.asList(element);
            } else {
                if (pathNode != null && element.isJsonObject()) {
                    final String key = pathNode.getKey();
                    final JsonObject jsonObject = element.getAsJsonObject();
                    final JsonElement foundElement = jsonObject.get(key);
                    return getInnerElements(foundElement, pathNode.getNext());
                } else if (element.isJsonArray()) {
                    final JsonArray foundArray = element.getAsJsonArray();
                    final List<JsonElement> foundValues = new ArrayList<>(foundArray.size());
                    for (final JsonElement arrayElement : foundArray) {
                        foundValues.addAll(getInnerElements(arrayElement, pathNode));
                    }
                    return foundValues;
                }
                return Arrays.asList(element);
            }
        }
    }
}
