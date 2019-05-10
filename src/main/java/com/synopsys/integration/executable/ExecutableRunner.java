/**
 * detectable
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExecutableRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ExecutableOutput execute(ProcessBuilder processBuilder) throws ExecutableRunnerException {
        logger.info(String.format("Running process builder >%s", getMaskedCommand(processBuilder.command())));
        return executeProcessBuilder(processBuilder);
    }

    public ExecutableOutput execute(final Executable executable) throws ExecutableRunnerException {
        logger.info(String.format("Running executable >%s", getMaskedCommand(executable.getCommandWithArguments())));
        final ProcessBuilder processBuilder = executable.createProcessBuilder();
        return executeProcessBuilder(processBuilder);
    }

    public String getMaskedCommand(List<String> commandWithArguments) {
        final List<String> pieces = new ArrayList<>();
        for (final String argument : commandWithArguments) {
            if (argument.matches(".*password.*=.*")) {
                final String maskedArgument = argument.substring(0, argument.indexOf('=') + 1) + "********";
                pieces.add(maskedArgument);
            } else {
                pieces.add(argument);
            }
        }
        return StringUtils.join(pieces, ' ');
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