/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait;

import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.exception.IntegrationTimeoutException;

public interface ResilientJob<T> {
    void attemptJob() throws IntegrationException;

    boolean wasJobCompleted();

    T onTimeout() throws IntegrationTimeoutException;

    T onCompletion() throws IntegrationException;

    String getName();
}
