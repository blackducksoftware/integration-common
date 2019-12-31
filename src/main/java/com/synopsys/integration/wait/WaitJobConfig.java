/**
 * blackduck-common
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

import com.synopsys.integration.log.IntLogger;

import java.util.function.Supplier;

public class WaitJobConfig {
    public static final Supplier<Long> CURRENT_TIME_SUPPLIER = System::currentTimeMillis;

    private final IntLogger intLogger;
    private final long timeoutInSeconds;
    private final Supplier<Long> startTimeSupplier;
    private final int waitIntervalInSeconds;
    private final WaitJobTask waitJobTask;

    public WaitJobConfig(IntLogger intLogger, long timeoutInSeconds, long startTime, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        this(intLogger, timeoutInSeconds, () -> startTime, waitIntervalInSeconds, waitJobTask);
    }

    public WaitJobConfig(IntLogger intLogger, long timeoutInSeconds, Supplier<Long> startTimeSupplier, int waitIntervalInSeconds, WaitJobTask waitJobTask) {
        this.intLogger = intLogger;
        this.timeoutInSeconds = timeoutInSeconds;
        this.startTimeSupplier = startTimeSupplier;
        this.waitIntervalInSeconds = waitIntervalInSeconds;
        this.waitJobTask = waitJobTask;
    }

    public long getStartTime() {
        return startTimeSupplier.get();
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
