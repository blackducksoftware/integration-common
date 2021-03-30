/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.log;

import org.apache.commons.lang3.StringUtils;

/**
 * The declared order of the LogLevels are important - your set level is loggable as are all the levels to the left. For example, if you set your LogLevel to INFO, INFO will be loggable, as will the levels to its left, WARN and ERROR. As
 * there are no levels to the left of OFF, nothing will be logged when that level is set.
 */
public enum LogLevel {
    OFF,
    ERROR,
    WARN,
    INFO,
    DEBUG,
    TRACE;

    public static LogLevel fromString(final String level) {
        if (StringUtils.isNotBlank(level)) {
            try {
                return LogLevel.valueOf(level.toUpperCase());
            } catch (final IllegalArgumentException e) {
                // Ignored
            }
        }
        return LogLevel.INFO;
    }

    /**
     * Will return true if logLevel is loggable for this logLevel, false otherwise.
     */
    public boolean isLoggable(final LogLevel logLevel) {
        return this.compareTo(logLevel) >= 0;
    }

}
