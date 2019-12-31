/**
 * integration-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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

    public WaitJob(WaitJobConfig waitJobConfig) {
        this.waitJobConfig = waitJobConfig;
    }

    public boolean waitFor() throws InterruptedException, IntegrationException {
        int attempts = 0;
        long startTime = waitJobConfig.getStartTime();
        Duration currentDuration = Duration.ZERO;
        Duration maximumDuration = Duration.ofMillis(waitJobConfig.getTimeoutInSeconds() * 1000);
        IntLogger intLogger = waitJobConfig.getIntLogger();

        while (currentDuration.compareTo(maximumDuration) <= 0) {
            intLogger.info(String.format("Try #%s (elapsed: %s)...", ++attempts, DurationFormatUtils.formatDurationHMS(currentDuration.toMillis())));
            if (waitJobConfig.getWaitJobTask().isComplete()) {
                intLogger.info("...complete!");
                return true;
            } else {
                intLogger.info(String.format("...not done yet, waiting %s seconds and trying again...", waitJobConfig.getWaitIntervalInSeconds()));
                Thread.sleep(waitJobConfig.getWaitIntervalInSeconds() * 1000);
                currentDuration = Duration.ofMillis(System.currentTimeMillis() - startTime);
            }
        }

        return false;
    }

}
