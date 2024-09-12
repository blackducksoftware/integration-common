/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class BuilderStatus {
    private final List<String> errorMessages = new ArrayList<>();

    public boolean isValid() {
        return errorMessages.isEmpty();
    }

    public void addErrorMessage(String errorMessage) {
        errorMessages.add(errorMessage);
    }

    public void addAllErrorMessages(List<String> errorMessages) {
        this.errorMessages.addAll(errorMessages);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getFullErrorMessage() {
        return getFullErrorMessage(" ");
    }

    public String getFullErrorMessage(String errorMessageSeparator) {
        return StringUtils.join(errorMessages, errorMessageSeparator);
    }

}
