/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait.tracker;

public interface WaitIntervalTracker {

    long getTimeoutInSeconds();
    long getNextWaitIntervalInSeconds();
}
