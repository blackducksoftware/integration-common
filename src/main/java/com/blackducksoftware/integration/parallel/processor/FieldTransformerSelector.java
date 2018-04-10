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
package com.blackducksoftware.integration.parallel.processor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FieldTransformerSelector<S, R> {
    private final String selectorFieldName;
    private final Map<Object, ItemTransformer<S, R>> transformerMap;

    public FieldTransformerSelector(final String selectorFieldName) {
        this.selectorFieldName = selectorFieldName;
        this.transformerMap = new HashMap<>();
    }

    public String getSelectorFieldName() {
        return selectorFieldName;
    }

    public void addTransformer(final Object selectorFieldValue, final ItemTransformer<S, R> transform) {
        transformerMap.put(selectorFieldValue, transform);
    }

    public void removeTransformer(final Object selectorFieldValue) {
        transformerMap.remove(selectorFieldValue);
    }

    public ItemTransformer<S, R> selectTransform(final S source) throws FieldTransformerException {
        try {
            if (hasTransformerObject(source)) {
                final Object selectorFieldValue = getSelectorFieldValue(source);
                return transformerMap.get(selectorFieldValue);
            } else {
                throw new FieldTransformerException(selectorFieldName, source.getClass());
            }
        } catch (final NullPointerException npe) {
            throw new FieldTransformerException(selectorFieldName, null, npe);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldTransformerException(selectorFieldName, source.getClass(), ex);
        }
    }

    public boolean hasTransformerObject(final S source) throws FieldTransformerException {
        Objects.requireNonNull(source, "Cannot select a transform for a null source object");
        try {
            final Object selectorFieldValue = getSelectorFieldValue(source);
            return transformerMap.containsKey(selectorFieldValue);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldTransformerException(selectorFieldName, source.getClass(), ex);
        }
    }

    private Object getSelectorFieldValue(final S source) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        final Field field = source.getClass().getField(selectorFieldName);
        final Object selectorField = field.get(source);
        return selectorField;
    }
}
