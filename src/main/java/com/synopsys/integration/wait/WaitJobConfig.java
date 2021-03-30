/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import com.synopsys.integration.log.IntLogger;

import java.util.Optional;
import java.util.function.Supplier;

public class WaitJobConfig {
    public static final Supplier<Long> CURRENT_TIME_SUPPLIER = System::currentTimeMillis;

    private final IntLogger intLogger;
    private final long timeoutInSeconds;
    private final Supplier<Long> startTimeSupplier;
    private final int waitIntervalInSeconds;
    private final String taskName;
    private final WaitJobTask waitJobTask;

    public WaitJobConfig(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        this(intLogger, timeoutInSeconds, () -> startTime, waitIntervalInSeconds, null, waitJobTask);
    }

    public WaitJobConfig(IntLogger intLogger, long timeoutInSeconds, Supplier<Long> startTimeSupplier, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        this(intLogger, timeoutInSeconds, startTimeSupplier, waitIntervalInSeconds, null, waitJobTask);
    }

    public WaitJobConfig(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds, String taskName, WaitJobTask waitJobTask) {
        this(intLogger, timeoutInSeconds, () -> startTime, waitIntervalInSeconds, taskName, waitJobTask);
    }

    public WaitJobConfig(IntLogger intLogger, long timeoutInSeconds, Supplier<Long> startTimeSupplier, int waitIntervalInSeconds, String taskName, WaitJobTask waitJobTask) {
        this.intLogger = intLogger;
        this.timeoutInSeconds = timeoutInSeconds;
        this.startTimeSupplier = startTimeSupplier;
        this.waitIntervalInSeconds = waitIntervalInSeconds;
        this.taskName = taskName;
        this.waitJobTask = waitJobTask;
    }

    public long getStartTime() {
        return startTimeSupplier.get();
    }

    public Optional<String> getTaskName() {
        return Optional.ofNullable(taskName);
    }

    public IntLogger getIntLogger() {
        return intLogger;
    }

    public long getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public Supplier<Long> getStartTimeSupplier() {
        return startTimeSupplier;
    }

    public int getWaitIntervalInSeconds() {
        return waitIntervalInSeconds;
    }

    public WaitJobTask getWaitJobTask() {
        return waitJobTask;
    }

}
