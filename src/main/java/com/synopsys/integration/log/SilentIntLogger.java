/*
 * integration-common
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.log;

public class SilentIntLogger extends IntLogger {
    @Override
    public void alwaysLog(final String arg0) {
        // Silent
    }

    @Override
    public void debug(final String arg0, final Throwable arg1) {
        // Silent
    }

    @Override
    public void debug(final String arg0) {
        // Silent
    }

    @Override
    public void error(final String arg0, final Throwable arg1) {
        // Silent
    }

    @Override
    public void error(final String arg0) {
        // Silent
    }

    @Override
    public void error(final Throwable arg0) {
        // Silent
    }

    @Override
    public LogLevel getLogLevel() {
        return LogLevel.OFF;
    }

    @Override
    public void info(final String arg0) {
        // Silent
    }

    @Override
    public void setLogLevel(final LogLevel arg0) {
        // Silent
    }

    @Override
    public void trace(final String arg0, final Throwable arg1) {
        // Silent
    }

    @Override
    public void trace(final String arg0) {
        // Silent
    }

    @Override
    public void warn(final String arg0) {
        // Silent
    }

}
