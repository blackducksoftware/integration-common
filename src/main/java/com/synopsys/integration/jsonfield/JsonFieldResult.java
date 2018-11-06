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
package com.synopsys.integration.jsonfield;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class JsonFieldResult<T> {
    private final JsonObject jsonObject;
    private final List<JsonElement> foundElements;

    public JsonFieldResult(final JsonObject jsonObject, final List<JsonElement> foundElements) {
        this.jsonObject = jsonObject;
        this.foundElements = foundElements;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public List<JsonElement> getFoundElements() {
        return foundElements;
    }

}
