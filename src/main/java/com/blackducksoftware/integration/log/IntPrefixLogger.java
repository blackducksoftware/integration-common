package com.blackducksoftware.integration.log;

import org.slf4j.Logger;

/**
 * Occasionally, a common prefix needs to be added to a set of log messages.
 */
public class IntPrefixLogger extends Slf4jIntLogger {
    private final String prefix;

    public IntPrefixLogger(final String prefix, final Logger logger) {
        super(logger);
        this.prefix = prefix;
    }

    @Override
    public void alwaysLog(final String txt) {
        super.alwaysLog(logMessage(txt));
    }

    @Override
    public void info(final String txt) {
        super.info(logMessage(txt));
    }

    @Override
    public void error(final Throwable t) {
        error("", t);
    }

    @Override
    public void error(final String txt, final Throwable t) {
        super.error(logMessage(txt), t);
    }

    @Override
    public void error(final String txt) {
        super.error(logMessage(txt));
    }

    @Override
    public void warn(final String txt) {
        super.warn(logMessage(txt));
    }

    @Override
    public void trace(final String txt) {
        super.trace(logMessage(txt));
    }

    @Override
    public void trace(final String txt, final Throwable t) {
        super.trace(logMessage(txt), t);
    }

    @Override
    public void debug(final String txt) {
        super.debug(logMessage(txt));
    }

    @Override
    public void debug(final String txt, final Throwable t) {
        super.debug(logMessage(txt), t);
    }

    private String logMessage(final String txt) {
        return String.format("%s - %s", prefix, txt);
    }

}
