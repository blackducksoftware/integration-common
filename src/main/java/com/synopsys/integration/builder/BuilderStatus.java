/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.builder;

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
