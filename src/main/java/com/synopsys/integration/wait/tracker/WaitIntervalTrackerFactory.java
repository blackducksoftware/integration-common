/*
 * integration-common
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait.tracker;

public class WaitIntervalTrackerFactory {

    public static WaitIntervalTracker createConstant(long timeoutInSeconds, int waitIntervalInSeconds) {
        return new WaitIntervalTrackerConstant(timeoutInSeconds, waitIntervalInSeconds);
    }

    public static WaitIntervalTracker createProgressive(long timeoutInSeconds, int maxWaitInSeconds) {
        return new WaitIntervalTrackerProgressive(timeoutInSeconds, maxWaitInSeconds);
    }
}
