/**
 * integration-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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

/**
 * Occasionally, a common prefix needs to be added to a set of log messages.
 */
public class Slf4jPrefixIntLogger extends Slf4jIntLogger {
    private final String prefix;

    public Slf4jPrefixIntLogger(final String prefix, final Logger logger) {
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
