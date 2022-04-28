/*
 * integration-common
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.exception.IntegrationTimeoutException;

public class WaitJob implements ResilientJob<Boolean> {
    private final WaitJobCondition waitJobCondition;
    private final String name;
    private boolean complete;

    public WaitJob(final WaitJobCondition waitJobCondition, String name) {
        this.waitJobCondition = waitJobCondition;
        this.name = name;
    }

    public static Boolean waitFor(ResilientJobConfig jobConfig, WaitJobCondition waitJobCondition, String name) throws IntegrationException, InterruptedException {
        WaitJob waitJob = new WaitJob(waitJobCondition, name);
        ResilientJobExecutor jobExecutor = new ResilientJobExecutor(jobConfig);
        return jobExecutor.executeJob(waitJob);
    }

    @Override
    public void attemptJob() throws IntegrationException {
        complete = waitJobCondition.isComplete();
    }

    @Override
    public boolean wasJobCompleted() {
        return complete;
    }

    @Override
    public Boolean onTimeout() throws IntegrationTimeoutException {
        throw new IntegrationTimeoutException();
    }

    @Override
    public Boolean onCompletion() {
        return complete;
    }

    @Override
    public String getName() {
        return name;
    }
}
