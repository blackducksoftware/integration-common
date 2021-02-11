/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
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
package com.synopsys.integration.log;

import org.slf4j.Logger;

public class Slf4jIntLogger extends IntLogger {
    private final Logger logger;

    public Slf4jIntLogger(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void alwaysLog(final String txt) {
        switch (this.getLogLevel()) {
            case TRACE:
                logger.trace(txt);
                break;
            case DEBUG:
                logger.debug(txt);
                break;
            case INFO:
                logger.info(txt);
                break;
            case WARN:
                logger.warn(txt);
                break;
            default:
                logger.error(txt);
                break;
        }
    }

    @Override
    public void info(final String txt) {
        logger.info(txt);
    }

    @Override
    public void error(final Throwable t) {
        logger.error("Throwable: " + t.getMessage(), t);
    }

    @Override
    public void error(final String txt, final Throwable t) {
        logger.error(txt, t);
    }

    @Override
    public void error(final String txt) {
        logger.error(txt);
    }

    @Override
    public void warn(final String txt) {
        logger.warn(txt);
    }

    @Override
    public void trace(final String txt) {
        logger.trace(txt);
    }

    @Override
    public void trace(final String txt, final Throwable t) {
        logger.trace(txt, t);
    }

    @Override
    public void debug(final String txt) {
        logger.debug(txt);
    }

    @Override
    public void debug(final String txt, final Throwable t) {
        logger.debug(txt, t);
    }

    @Override
    public void setLogLevel(final LogLevel logLevel) {
        // Not supported.
    }

    @Override
    public LogLevel getLogLevel() {
        if (logger.isTraceEnabled()) {
            return LogLevel.TRACE;
        } else if (logger.isDebugEnabled()) {
            return LogLevel.DEBUG;
        } else if (logger.isInfoEnabled()) {
            return LogLevel.INFO;
        } else if (logger.isWarnEnabled()) {
            return LogLevel.WARN;
        } else if (logger.isErrorEnabled()) {
            return LogLevel.ERROR;
        } else {
            return LogLevel.OFF;
        }
    }

}
