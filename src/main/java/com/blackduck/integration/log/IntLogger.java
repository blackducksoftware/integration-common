/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.log;

import com.blackduck.integration.util.IntEnvironmentVariables;

public abstract class IntLogger {
    public abstract void alwaysLog(String txt);

    public abstract void info(String txt);

    public abstract void error(Throwable t);

    public abstract void error(String txt, Throwable t);

    public abstract void error(String txt);

    public abstract void warn(String txt);

    public abstract void trace(String txt);

    public abstract void trace(String txt, Throwable t);

    public abstract void debug(String txt);

    public abstract void debug(String txt, Throwable t);

    public abstract void setLogLevel(LogLevel logLevel);

    public void errorAndDebug(String txt, Throwable t) {
        error(txt);
        debug(t.getMessage(), t);
    }

    public void setLogLevel(final IntEnvironmentVariables variables) {
        final String logLevel = variables.getValue("BLACK_DUCK_LOG_LEVEL", "INFO");
        try {
            setLogLevel(LogLevel.valueOf(logLevel.toUpperCase()));
        } catch (final IllegalArgumentException e) {
            setLogLevel(LogLevel.INFO);
        }
    }

    public abstract LogLevel getLogLevel();

}
