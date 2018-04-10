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

import com.blackducksoftware.integration.exception.IntegrationException;

public class FieldTransformerException extends IntegrationException {
    private static final long serialVersionUID = -63273969752332347L;

    private final static String createMessage(final String selectorFieldName, final Class<?> classContainingField) {
        return String.format("Cannot find a transformer for Field: %s in Class: %s ", selectorFieldName, classContainingField);
    }

    public FieldTransformerException(final String selectorFieldName, final Class<?> classContainingField, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(createMessage(selectorFieldName, classContainingField), cause, enableSuppression, writableStackTrace);
    }

    public FieldTransformerException(final String selectorFieldName, final Class<?> classContainingField, final Throwable cause) {
        super(createMessage(selectorFieldName, classContainingField), cause);
    }

    public FieldTransformerException(final String selectorFieldName, final Class<?> classContainingField) {
        super(createMessage(selectorFieldName, classContainingField));
    }
}
