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
package com.synopsys.integration.util;

import java.util.HashMap;
import java.util.Map;

public abstract class IntegrationBuilder<T extends Buildable, E extends Enum<E>> {
    public static String convertToPropertyKey(String environmentVariableKey) {
        return environmentVariableKey.toLowerCase().replace("_", ".");
    }

    protected Map<E, String> values = new HashMap<>();

    public T build() throws IllegalArgumentException {
        assertValid();

        return buildWithoutValidation();
    }

    /**
     * This method is for builders to instantiate the object they wrap. It is reasonable for a builder to
     * assume that all values are safe before this method will be called.
     */
    protected abstract T buildWithoutValidation();

    protected abstract void validate(BuilderStatus builderStatus);

    public final void assertValid() throws IllegalArgumentException {
        BuilderStatus builderStatus = validateAndGetBuilderStatus();

        if (!builderStatus.isValid()) {
            throw new IllegalArgumentException(builderStatus.getFullErrorMessage());
        }
    }

    public final boolean isValid() {
        BuilderStatus builderStatus = validateAndGetBuilderStatus();
        return builderStatus.isValid();
    }

    public final BuilderStatus validateAndGetBuilderStatus() {
        BuilderStatus builderStatus = new BuilderStatus();

        validate(builderStatus);

        return builderStatus;
    }

    public String get(E key) {
        return values.get(key);
    }

    public void put(E key, String value) {
        values.put(key, value);
    }

}
