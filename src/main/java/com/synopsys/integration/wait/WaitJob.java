/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;

public class WaitJob<T extends Object> {
    public static final BooleanWaitJobCompleter BOOLEAN_COMPLETER = new BooleanWaitJobCompleter();

    public static final WaitJob<Boolean> createSimpleWait(WaitJobConfig waitJobConfig, WaitJobCondition waitJobCondition) {
        return new WaitJob<>(waitJobConfig, waitJobCondition, BOOLEAN_COMPLETER);
    }

    private final WaitJobConfig waitJobConfig;
    private final WaitJobCondition waitJobCondition;
    private final WaitJobCompleter<T> waitJobCompleter;

    public WaitJob(WaitJobConfig waitJobConfig, WaitJobCondition waitJobCondition, WaitJobCompleter<T> waitJobCompleter) {
        this.waitJobConfig = waitJobConfig;
        this.waitJobCondition = waitJobCondition;
        this.waitJobCompleter = waitJobCompleter;
    }

    public T waitFor() throws InterruptedException, IntegrationException {
        int attempts = 0;
        IntLogger intLogger = waitJobConfig.getIntLogger();
        long startTime = waitJobConfig.getStartTime();
        Duration currentDuration = Duration.ZERO;
        Duration maximumDuration = Duration.ofMillis(waitJobConfig.getTimeoutInSeconds() * 1000);
        String taskDescription = waitJobConfig.getTaskDescription();

        boolean allCompleted = waitJobCondition.isComplete();
        if (allCompleted) {
            String attemptPrefix = createAttemptPrefix(attempts, currentDuration, taskDescription);
            return complete(intLogger, attemptPrefix);
        }

        while (currentDuration.compareTo(maximumDuration) <= 0) {
            attempts++;
            String attemptPrefix = createAttemptPrefix(attempts, currentDuration, taskDescription);
            if (waitJobCondition.isComplete()) {
                return complete(intLogger, attemptPrefix);
            } else {
                intLogger.info(String.format("%snot done yet, waiting %s seconds and trying again...", attemptPrefix, waitJobConfig.getWaitIntervalInSeconds()));
                Thread.sleep(waitJobConfig.getWaitIntervalInSeconds() * 1000);
                currentDuration = Duration.ofMillis(System.currentTimeMillis() - startTime);
            }
        }

        return waitJobCompleter.handleTimeout();
    }

    private T complete(IntLogger intLogger, String attemptPrefix) throws IntegrationException {
        intLogger.info(String.format("%scomplete!", attemptPrefix));
        return waitJobCompleter.complete();
    }

    private String createAttemptPrefix(int attempts, Duration currentDuration, String taskDescription) {
        return String.format("Try #%s %s(elapsed: %s)...", attempts, taskDescription, DurationFormatUtils.formatDurationHMS(currentDuration.toMillis()));
    }

}
