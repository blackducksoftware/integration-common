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
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.synopsys.integration.util.json.field.HierarchicalField;

public class JsonExtractor extends JsonAccessor {
    public JsonExtractor(final Gson gson) {
        super(gson);
    }

    public <T> Optional<T> getFirstValueFromJson(final HierarchicalField field, final String json) {
        return getFirstValueFromJsonObject(field, createJsonObject(json));
    }

    public <T> Optional<T> getFirstValueFromJsonObject(final HierarchicalField field, final JsonObject object) {
        final List<T> values = getValuesFromJsonObject(field, object);
        return values.stream().findFirst();
    }

    public <T> List<T> getValuesFromJson(final HierarchicalField field, final String json) {
        return getValuesFromJsonObject(field, createJsonObject(json));
    }

    public <T> List<T> getValuesFromJsonObject(final HierarchicalField field, final JsonObject object) {
        final List<JsonElement> foundElements = getInnerElements(field.getPathToInnerField(), object);

        final List<T> convertedObjects = new ArrayList<>();
        for (final JsonElement element : foundElements) {
            final T convertedElement = gson.fromJson(element, field.getType());
            convertedObjects.add(convertedElement);
        }
        return convertedObjects;
    }
}
