/*
 * integration-common
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait.tracker;

public class WaitIntervalTrackerConstant implements WaitIntervalTracker {
    private final long timeoutInSeconds;
    private final int waitIntervalInSeconds;

    public WaitIntervalTrackerConstant(long timeoutInSeconds, int waitIntervalInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
        // waitInterval needs to be less than the timeout
        if (waitIntervalInSeconds > timeoutInSeconds) {
            this.waitIntervalInSeconds = (int) timeoutInSeconds;
        } else {
            this.waitIntervalInSeconds = waitIntervalInSeconds;
        }
    }

    @Override
    public long getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    @Override
    public int getNextWaitIntervalInSeconds() {
        return waitIntervalInSeconds;
    }
}
