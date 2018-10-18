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
package com.synopsys.integration.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class IntegrationBuilder<T> {
    protected List<String> errorMessages;

    private String errorMessageSeparator = " ";

    public T build() throws IllegalArgumentException {
        assertValid();

        return buildWithoutValidation();
    }

    /**
     * This method is for other builders to use to instantiate the object they wrap. If invalid values are used or any Exceptions thrown, null values should be used.
     * @return
     */
    protected abstract T buildWithoutValidation();

    public void assertValid() throws IllegalArgumentException {
        final String errorMessage = createErrorMessage();
        if (!errorMessage.isEmpty()) {
            throw new IllegalStateException(errorMessage);
        }
    }

    public boolean isValid() {
        return createErrorMessage().isEmpty();
    }

    public String createErrorMessage() {
        errorMessages = new ArrayList<>();
        populateIndividualErrorMessages();
        return StringUtils.join(errorMessages, errorMessageSeparator);
    }

    protected abstract void populateIndividualErrorMessages();

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessageSeparator(final String errorMessageSeperator) {
        errorMessageSeparator = errorMessageSeparator;
    }

}
