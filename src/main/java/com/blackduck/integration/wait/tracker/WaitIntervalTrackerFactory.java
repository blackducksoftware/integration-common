/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait.tracker;

public class WaitIntervalTrackerFactory {

    private WaitIntervalTrackerFactory() {
    }

    public static WaitIntervalTracker createConstant(long timeoutInSeconds, long waitIntervalInSeconds) {
        return new WaitIntervalTrackerConstant(timeoutInSeconds, waitIntervalInSeconds);
    }

    public static WaitIntervalTracker createProgressive(long timeoutInSeconds, long maxWaitInSeconds) {
        return new WaitIntervalTrackerProgressive(timeoutInSeconds, maxWaitInSeconds);
    }
}
