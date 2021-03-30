/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.util.function.Supplier;

public class WaitJob {
    private WaitJobConfig waitJobConfig;

    public static WaitJob create(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        return new WaitJob(new WaitJobConfig(intLogger, timeoutInSeconds, startTime, waitIntervalInSeconds, waitJobTask));
    }

    public static WaitJob create(IntLogger intLogger, long timeoutInSeconds, Supplier<Long> startTimeSupplier, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        return new WaitJob(new WaitJobConfig(intLogger, timeoutInSeconds, startTimeSupplier, waitIntervalInSeconds, waitJobTask));
    }

    public static WaitJob createUsingSystemTimeWhenInvoked(IntLogger intLogger, long timeoutInSeconds, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        return new WaitJob(new WaitJobConfig(intLogger, timeoutInSeconds, WaitJobConfig.CURRENT_TIME_SUPPLIER, waitIntervalInSeconds, waitJobTask));
    }

    public static WaitJob create(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds, String taskName, WaitJobTask waitJobTask) {
        return new WaitJob(new WaitJobConfig(intLogger, timeoutInSeconds, startTime, waitIntervalInSeconds, taskName, waitJobTask));
    }

    public static WaitJob create(IntLogger intLogger, long timeoutInSeconds, Supplier<Long> startTimeSupplier, int waitIntervalInSeconds, String taskName, WaitJobTask waitJobTask) {
        return new WaitJob(new WaitJobConfig(intLogger, timeoutInSeconds, startTimeSupplier, waitIntervalInSeconds, taskName, waitJobTask));
    }

    public static WaitJob createUsingSystemTimeWhenInvoked(IntLogger intLogger, long timeoutInSeconds, int waitIntervalInSeconds, String taskName, WaitJobTask waitJobTask) {
        return new WaitJob(new WaitJobConfig(intLogger, timeoutInSeconds, WaitJobConfig.CURRENT_TIME_SUPPLIER, waitIntervalInSeconds, taskName, waitJobTask));
    }

    public WaitJob(WaitJobConfig waitJobConfig) {
        this.waitJobConfig = waitJobConfig;
    }

    public boolean waitFor() throws InterruptedException, IntegrationException {
        int attempts = 0;
        long startTime = waitJobConfig.getStartTime();
        Duration currentDuration = Duration.ZERO;
        Duration maximumDuration = Duration.ofMillis(waitJobConfig.getTimeoutInSeconds() * 1000);
        IntLogger intLogger = waitJobConfig.getIntLogger();

        String taskDescription = waitJobConfig
                .getTaskName()
                .map(s -> String.format("for task %s ", s))
                .orElse("");
        while (currentDuration.compareTo(maximumDuration) <= 0) {
            String attemptMessagePrefix = String.format("Try #%s %s(elapsed: %s)...", ++attempts, taskDescription, DurationFormatUtils.formatDurationHMS(currentDuration.toMillis()));
            if (waitJobConfig.getWaitJobTask().isComplete()) {
                intLogger.info(String.format("%scomplete!", attemptMessagePrefix));
                return true;
            } else {
                intLogger.info(String.format("%snot done yet, waiting %s seconds and trying again...", attemptMessagePrefix, waitJobConfig.getWaitIntervalInSeconds()));
                Thread.sleep(waitJobConfig.getWaitIntervalInSeconds() * 1000);
                currentDuration = Duration.ofMillis(System.currentTimeMillis() - startTime);
            }
        }

        return false;
    }

}
