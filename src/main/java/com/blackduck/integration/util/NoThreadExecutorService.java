/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class NoThreadExecutorService extends AbstractExecutorService {
    @Override
    public void shutdown() {
        // no-op
    }

    @Override
    public List<Runnable> shutdownNow() {
        return emptyList();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) {
        return false;
    }

    @Override
    public void execute(final Runnable command) {
        command.run();
    }

}
