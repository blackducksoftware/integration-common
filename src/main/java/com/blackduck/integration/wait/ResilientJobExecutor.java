/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.log.IntLogger;

public class ResilientJobExecutor {
    protected final ResilientJobConfig jobConfig;

    public ResilientJobExecutor(final ResilientJobConfig jobConfig) {
        this.jobConfig = jobConfig;
    }

    public <T> T executeJob(ResilientJob<T> resilientJob) throws InterruptedException, IntegrationException {
        int attempts = 1;
        IntLogger intLogger = jobConfig.getIntLogger();
        long startTime = jobConfig.getStartTime();
        Duration currentDuration = Duration.ZERO;
        Duration maximumDuration = Duration.ofMillis(jobConfig.getTimeoutInSeconds() * 1000);
        String taskDescription = String.format("for task %s ", resilientJob.getName());

        do {
            String attemptPrefix = String.format("Try #%s %s(elapsed: %s)...", attempts, taskDescription, DurationFormatUtils.formatDurationHMS(currentDuration.toMillis()));
            resilientJob.attemptJob();
            if (resilientJob.wasJobCompleted()) {
                intLogger.info(String.format("%scomplete!", attemptPrefix));
                return resilientJob.onCompletion();
            } else {
                long waitTimeInSeconds = jobConfig.getWaitIntervalInSeconds();
                intLogger.info(String.format("%snot done yet, waiting %s seconds and trying again...", attemptPrefix, waitTimeInSeconds));
                Thread.sleep(waitTimeInSeconds * 1000);
                currentDuration = Duration.ofMillis(System.currentTimeMillis() - startTime);
            }
            attempts++;
        } while (currentDuration.compareTo(maximumDuration) <= 0);

        return resilientJob.onTimeout();
    }
}
