/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class BufferedIntLogger extends IntLogger {
    private final Map<LogLevel, List<String>> outputMap = new HashMap<>();

    public BufferedIntLogger() {
        outputMap.put(LogLevel.OFF, new ArrayList<>());
        outputMap.put(LogLevel.ERROR, new ArrayList<>());
        outputMap.put(LogLevel.WARN, new ArrayList<>());
        outputMap.put(LogLevel.INFO, new ArrayList<>());
        outputMap.put(LogLevel.DEBUG, new ArrayList<>());
        outputMap.put(LogLevel.TRACE, new ArrayList<>());
    }

    public void resetLogs(LogLevel level) {
        outputMap.put(level, new ArrayList<>());
    }

    public void resetAllLogs() {
        EnumSet.allOf(LogLevel.class).forEach(this::resetLogs);
    }

    public List<String> getOutputList(LogLevel level) {
        return outputMap.get(level);
    }

    public String getOutputString(LogLevel level) {
        return StringUtils.trimToNull(StringUtils.join(getOutputList(level), '\n'));
    }

    public String getErrorOutputString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @Override
    public void alwaysLog(String txt) {
        outputMap.get(LogLevel.OFF).add(txt);
    }

    @Override
    public void debug(String txt) {
        outputMap.get(LogLevel.DEBUG).add(txt);
    }

    @Override
    public void debug(String txt, Throwable e) {
        outputMap.get(LogLevel.DEBUG).add(txt);
        outputMap.get(LogLevel.DEBUG).add(getErrorOutputString(e));
    }

    @Override
    public void error(Throwable e) {
        outputMap.get(LogLevel.ERROR).add(getErrorOutputString(e));
    }

    @Override
    public void error(String txt) {
        outputMap.get(LogLevel.ERROR).add(txt);
    }

    @Override
    public void error(String txt, Throwable e) {
        outputMap.get(LogLevel.ERROR).add(txt);
        outputMap.get(LogLevel.ERROR).add(getErrorOutputString(e));
    }

    @Override
    public void info(String txt) {
        outputMap.get(LogLevel.INFO).add(txt);
    }

    @Override
    public void trace(String txt) {
        outputMap.get(LogLevel.TRACE).add(txt);
    }

    @Override
    public void trace(String txt, Throwable e) {
        outputMap.get(LogLevel.TRACE).add(txt);
        outputMap.get(LogLevel.TRACE).add(getErrorOutputString(e));
    }

    @Override
    public void warn(String txt) {
        outputMap.get(LogLevel.WARN).add(txt);
    }

    @Override
    public void setLogLevel(LogLevel level) {
        // Not supported.
    }

    @Override
    public LogLevel getLogLevel() {
        // Not supported.
        return null;
    }

}
