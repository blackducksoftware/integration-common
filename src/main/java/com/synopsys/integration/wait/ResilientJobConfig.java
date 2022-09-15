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

    private int prev1 = 1;
    private int prev2 = 0;
    private int max = 60;

    private boolean waitIsProgressive = false;
    private String flatInterval = System.getProperty(POLLING_INTERVAL_PROPERTY);

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
        if (null == flatInterval && this.waitIsProgressive) {
            return this.getWaitIntervalFibonacciInSeconds2();  //progressive interval
        } else if (flatInterval.matches("[0-9]+")){
            return Integer.getInteger(POLLING_INTERVAL_PROPERTY).intValue(); //constant polling interval added by sys prop
        }
        return waitIntervalInSeconds; // default 1 second interval
    }

    public int getWaitIntervalFibonacciInSeconds() {
        int now = 1;
        if (prev2 == -1 && prev1 == 0) {
            now = prev1 - prev2;
        } else {
            now = prev2 + prev1;
        }
        if (now >= max) {
            return max;
        }

        prev2 = prev1;
        prev1 = now;
        return now;
    }
}
