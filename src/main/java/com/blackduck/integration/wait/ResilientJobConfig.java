/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait;

import java.util.function.Supplier;

import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.wait.tracker.WaitIntervalTracker;

public class ResilientJobConfig {
    public static final Supplier<Long> CURRENT_TIME_SUPPLIER = System::currentTimeMillis;
    private final IntLogger intLogger;
    private final Supplier<Long> startTimeSupplier;
    private final WaitIntervalTracker waitIntervalTracker;

    public ResilientJobConfig(IntLogger intLogger, long startTime, WaitIntervalTracker waitIntervalTracker) {
        this(intLogger, () -> startTime, waitIntervalTracker);

    }

    public ResilientJobConfig(IntLogger intLogger, Supplier<Long> startTimeSupplier, WaitIntervalTracker waitIntervalTracker) {
        this.intLogger = intLogger;
        this.waitIntervalTracker  = waitIntervalTracker;
        this.startTimeSupplier = startTimeSupplier;
    }

    public IntLogger getIntLogger() {
        return intLogger;
    }

    public long getTimeoutInSeconds() {
        return waitIntervalTracker.getTimeoutInSeconds();
    }

    public long getStartTime() {
        return startTimeSupplier.get();
    }

    public long getWaitIntervalInSeconds() {
        return waitIntervalTracker.getNextWaitIntervalInSeconds();
    }
}
