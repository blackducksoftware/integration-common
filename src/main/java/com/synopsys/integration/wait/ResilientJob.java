/*
 * integration-common
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.exception.IntegrationTimeoutException;

public interface ResilientJob<T> {
    void attemptJob() throws IntegrationException;

    boolean wasJobCompleted();

    T onTimeout() throws IntegrationTimeoutException;

    T onComplete();

    String getName();
}
