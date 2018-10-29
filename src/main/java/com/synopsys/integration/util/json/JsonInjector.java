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

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.synopsys.integration.util.json.field.BooleanHierarchicalField;
import com.synopsys.integration.util.json.field.HierarchicalField;
import com.synopsys.integration.util.json.field.NumberHierarchicalField;
import com.synopsys.integration.util.json.field.StringHierarchicalField;

public class JsonInjector extends JsonAccessor {
    public JsonInjector(final Gson gson) {
        super(gson);
    }

    public String insertValue(final BooleanHierarchicalField field, final String json, final Boolean newValue) {
        return insertValue((HierarchicalField) field, json, newValue);
    }

    public String insertValue(final NumberHierarchicalField field, final String json, final Number newValue) {
        return insertValue((HierarchicalField) field, json, newValue);
    }

    public String insertValue(final StringHierarchicalField field, final String json, final String newValue) {
        return insertValue((HierarchicalField) field, json, newValue);
    }

    public JsonObject insertValue(final BooleanHierarchicalField field, final JsonObject jsonObject, final Boolean newValue) {
        return insertValue((HierarchicalField) field, jsonObject, newValue);
    }

    public JsonObject insertValue(final NumberHierarchicalField field, final JsonObject jsonObject, final Number newValue) {
        return insertValue((HierarchicalField) field, jsonObject, newValue);
    }

    public JsonObject insertValue(final StringHierarchicalField field, final JsonObject jsonObject, final String newValue) {
        return insertValue((HierarchicalField) field, jsonObject, newValue);
    }

    private String insertValue(final HierarchicalField field, final String json, final Object newValue) {
        final JsonObject result = insertValue(field, createJsonObject(json), newValue);
        return gson.toJson(result);
    }

    private JsonObject insertValue(final HierarchicalField field, final JsonObject jsonObject, final Object newValue) {
        final List<JsonElement> innerElements = getInnerElements(field.getPathToParent(), jsonObject);
        for (final JsonElement element : innerElements) {
            if (element.isJsonObject()) {
                addProperty(field.getType(), element.getAsJsonObject(), field.getKey(), newValue);
            } else {
                throw new IllegalStateException("The JsonElement at the end of the specified path was not a JsonObject");
            }
        }
        return jsonObject;
    }

    private void addProperty(final Type type, final JsonObject jsonObject, final String key, final Object newValue) {
        final Class<?> newValueClass = newValue.getClass();
        if (newValue == null && type.getTypeName().equals(newValueClass.getTypeName())) {
            jsonObject.addProperty(key, (Character) null);
        } else if (isAssignable(Boolean.class, newValueClass)) {
            jsonObject.addProperty(key, Boolean.class.cast(newValue));
        } else if (isAssignable(Number.class, newValueClass)) {
            jsonObject.addProperty(key, Number.class.cast(newValue));
        } else if (isAssignable(String.class, newValueClass)) {
            jsonObject.addProperty(key, String.class.cast(newValue));
        } else {
            throw new IllegalArgumentException("Invalid Type for newValue: " + newValueClass.getTypeName());
        }
    }

    private boolean isAssignable(final Class<?> potentialClass, final Class<?> newValueClass) {
        return potentialClass.isAssignableFrom(newValueClass);
    }
}
