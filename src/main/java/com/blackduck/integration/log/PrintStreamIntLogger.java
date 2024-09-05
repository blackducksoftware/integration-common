/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.log;

import java.io.PrintStream;

public class PrintStreamIntLogger extends IntLogger {
    private final PrintStream printStream;
    private LogLevel logLevel;

    public PrintStreamIntLogger(final PrintStream printStream, final LogLevel logLevel) {
        this.printStream = printStream;
        this.logLevel = logLevel;
    }

    @Override
    public void alwaysLog(final String txt) {
        printStream.println(txt);
    }

    @Override
    public void info(final String txt) {
        if (logLevel.isLoggable(LogLevel.INFO)) {
            printStream.println("INFO: " + txt);
        }
    }

    @Override
    public void error(final Throwable t) {
        if (logLevel.isLoggable(LogLevel.ERROR)) {
            t.printStackTrace(printStream);
        }
    }

    @Override
    public void error(final String txt, final Throwable t) {
        if (logLevel.isLoggable(LogLevel.ERROR)) {
            printStream.println("ERROR: " + txt);
            t.printStackTrace(printStream);
        }
    }

    @Override
    public void error(final String txt) {
        if (logLevel.isLoggable(LogLevel.ERROR)) {
            printStream.println("ERROR: " + txt);
        }
    }

    @Override
    public void warn(final String txt) {
        if (logLevel.isLoggable(LogLevel.WARN)) {
            printStream.println("WARN: " + txt);
        }
    }

    @Override
    public void trace(final String txt) {
        if (logLevel.isLoggable(LogLevel.TRACE)) {
            printStream.println("TRACE: " + txt);
        }
    }

    @Override
    public void trace(final String txt, final Throwable t) {
        if (logLevel.isLoggable(LogLevel.TRACE)) {
            printStream.println("TRACE: " + txt);
            t.printStackTrace(printStream);
        }
    }

    @Override
    public void debug(final String txt) {
        if (logLevel.isLoggable(LogLevel.DEBUG)) {
            printStream.println("DEBUG: " + txt);
        }
    }

    @Override
    public void debug(final String txt, final Throwable t) {
        if (logLevel.isLoggable(LogLevel.DEBUG)) {
            printStream.println("DEBUG: " + txt);
            t.printStackTrace(printStream);
        }
    }

    @Override
    public void setLogLevel(final LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public LogLevel getLogLevel() {
        return logLevel;
    }

}
