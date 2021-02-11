/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
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
