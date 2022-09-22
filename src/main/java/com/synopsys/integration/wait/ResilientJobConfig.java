/*
 * integration-common
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import java.util.function.Supplier;

import com.synopsys.integration.log.IntLogger;

public class ResilientJobConfig {
    public static final Supplier<Long> CURRENT_TIME_SUPPLIER = System::currentTimeMillis;

    private final String POLLING_INTERVAL_PROPERTY = "ConstantPollingInterval";
    private final IntLogger intLogger;
    private final long timeoutInSeconds;
    private final Supplier<Long> startTimeSupplier;
    private final int waitIntervalInSeconds;

    // sequence initializers
    private int prev1 = 1;
    private int prev2 = 0;
    private int max = 60;
    private int now = 1;

    private boolean waitIsProgressive = false;

    public ResilientJobConfig(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds, boolean waitIsProgressive) {
        this(intLogger, timeoutInSeconds, () -> startTime, waitIntervalInSeconds);
        this.waitIsProgressive  = waitIsProgressive;
    }

    public ResilientJobConfig(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds) {
        this(intLogger, timeoutInSeconds, () -> startTime, waitIntervalInSeconds);
    }

    public ResilientJobConfig(IntLogger intLogger, long timeoutInSeconds, Supplier<Long> startTimeSupplier, int waitIntervalInSeconds) {
        this.intLogger = intLogger;
        this.timeoutInSeconds = timeoutInSeconds;
        this.startTimeSupplier = startTimeSupplier;

        // waitInterval needs to be less than the timeout
        if (waitIntervalInSeconds > timeoutInSeconds) {
            this.waitIntervalInSeconds = (int) timeoutInSeconds;
        } else {
            this.waitIntervalInSeconds = waitIntervalInSeconds;
        }
    }

    public IntLogger getIntLogger() {
        return intLogger;
    }

    public long getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public long getStartTime() {
        return startTimeSupplier.get();
    }

    public int getWaitIntervalInSeconds() {
        if (this.waitIsProgressive) {
            return  this.getWaitIntervalFibonacciInSeconds();  //progressive interval
        }
        return waitIntervalInSeconds; // default 1 second interval
    }

    public int getWaitIntervalFibonacciInSeconds() {
        now = prev2 + prev1;
        if (now >= max) {
            return max;
        }

        prev2 = prev1;
        prev1 = now;
        return now;
    }
}
