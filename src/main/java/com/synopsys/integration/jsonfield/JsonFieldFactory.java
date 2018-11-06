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

import java.util.Arrays;
import java.util.List;

public class JsonFieldFactory {
    public static SingleJsonField<String> createStringSingleJsonField(final List<String> fieldPath) {
        return new SingleJsonField<>(fieldPath, String.class);
    }

    public static ListJsonField<String> createStringListJsonField(final List<String> fieldPath) {
        return new ListJsonField<>(fieldPath, String.class);
    }

    public static SingleJsonField<String> createStringSingleJsonField(final String... fieldPath) {
        return new SingleJsonField<>(Arrays.asList(fieldPath), String.class);
    }

    public static ListJsonField<String> createStringListJsonField(final String... fieldPath) {
        return new ListJsonField<>(Arrays.asList(fieldPath), String.class);
    }

    public static <T> SingleJsonField<T> createCustomSingleJsonField(final Class<T> fieldClass, final List<String> fieldPath) {
        return new SingleJsonField<>(fieldPath, fieldClass);
    }

    public static <T> ListJsonField<T> createCustomListJsonField(final Class<T> fieldClass, final List<String> fieldPath) {
        return new ListJsonField<>(fieldPath, fieldClass);
    }

    public static <T> SingleJsonField<T> createCustomSingleJsonField(final Class<T> fieldClass, final String... fieldPath) {
        return new SingleJsonField<>(Arrays.asList(fieldPath), fieldClass);
    }

    public static <T> ListJsonField<T> createCustomListJsonField(final Class<T> fieldClass, final String... fieldPath) {
        return new ListJsonField<>(Arrays.asList(fieldPath), fieldClass);
    }

}
