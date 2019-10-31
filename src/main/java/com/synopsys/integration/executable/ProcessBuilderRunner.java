/**
 * integration-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
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
package com.synopsys.integration.executable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class ProcessBuilderRunner implements ExecutableRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ExecutableOutput execute(ProcessBuilder processBuilder) throws ExecutableRunnerException {
        logger.info(String.format("Running process builder >%s", getMaskedCommand(processBuilder.command())));
        return executeProcessBuilder(processBuilder);
    }

    public ExecutableOutput execute(final Executable executable) throws ExecutableRunnerException {
        logger.info(String.format("Running executable >%s", getMaskedCommand(executable.getCommandWithArguments())));
        final ProcessBuilder processBuilder = createProcessBuilder(executable);
        return executeProcessBuilder(processBuilder);
    }

    public ProcessBuilder createProcessBuilder(Executable executable) {
        final ProcessBuilder processBuilder = new ProcessBuilder(executable.getCommandWithArguments());
        processBuilder.directory(executable.getWorkingDirectory());
        final Map<String, String> processBuilderEnvironment = processBuilder.environment();
        for (final String key : executable.getEnvironmentVariables().keySet()) {
            populateEnvironmentMap(processBuilderEnvironment, key, executable.getEnvironmentVariables().get(key));
        }
        return processBuilder;
    }

    private void populateEnvironmentMap(final Map<String, String> environment, final Object key, final Object value) {
        // ProcessBuilder's environment's keys and values must be non-null java.lang.String's
        if (key != null && value != null) {
            final String keyString = key.toString();
            final String valueString = value.toString();
            if (keyString != null && valueString != null) {
                environment.put(keyString, valueString);
            }
        }
    }

    private ExecutableOutput executeProcessBuilder(ProcessBuilder processBuilder) throws ExecutableRunnerException {
        try {
            final Process process = processBuilder.start();

            try (InputStream standardOutputStream = process.getInputStream(); InputStream standardErrorStream = process.getErrorStream()) {
                final ExecutableStreamThread standardOutputThread = new ExecutableStreamThread(standardOutputStream, logger::info, logger::trace);
                standardOutputThread.start();

                final ExecutableStreamThread errorOutputThread = new ExecutableStreamThread(standardErrorStream, logger::info, logger::trace);
                errorOutputThread.start();

                final int returnCode = process.waitFor();
                logger.info("process finished: " + returnCode);

                standardOutputThread.join();
                errorOutputThread.join();

                final String standardOutput = standardOutputThread.getExecutableOutput().trim();
                final String errorOutput = errorOutputThread.getExecutableOutput().trim();

                final ExecutableOutput output = new ExecutableOutput(returnCode, standardOutput, errorOutput);
                return output;
            }
        } catch (final Exception e) {
            throw new ExecutableRunnerException(e);
        }
    }

}