/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait.tracker;

public class WaitIntervalTrackerProgressive implements WaitIntervalTracker {
    private final long timeoutInSeconds;
    private final long maxWaitIntervalSeconds;

    // sequence initializers
    private long prev1 = 1L;
    private long prev2 = 0L;

    public WaitIntervalTrackerProgressive(long timeoutInSeconds, long maxWaitIntervalSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
        this.maxWaitIntervalSeconds = maxWaitIntervalSeconds;
    }

    @Override
    public long getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    @Override
    public long getNextWaitIntervalInSeconds() {
        long now = prev2 + prev1;
        if (now >= maxWaitIntervalSeconds) {
            return maxWaitIntervalSeconds;
        }
        prev2 = prev1;
        prev1 = now;
        return now;
    }
}
