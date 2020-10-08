/**
 * integration-common
 *
 * Copyright (c) 2020 Synopsys, Inc.
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

import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

import com.synopsys.integration.log.IntLogger;

public class ProcessBuilderRunner implements ExecutableRunner {
    private final Consumer<String> outputConsumer;
    private final Consumer<String> traceConsumer;

    public ProcessBuilderRunner(final IntLogger logger) {
        this.outputConsumer = logger::info;
        this.traceConsumer = logger::trace;
    }

    public ExecutableOutput execute(ProcessBuilder processBuilder) throws ExecutableRunnerException {
        outputConsumer.accept(String.format("Running process builder >%s", Executable.getMaskedCommand(processBuilder.command())));
        return executeProcessBuilder(processBuilder);
    }

    @Override
    public ExecutableOutput execute(final Executable executable) throws ExecutableRunnerException {
        outputConsumer.accept(String.format("Running executable >%s", executable.getExecutableDescription()));
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
                final ExecutableStreamThread standardOutputThread = new ExecutableStreamThread(standardOutputStream, outputConsumer, traceConsumer);
                standardOutputThread.start();

                final ExecutableStreamThread errorOutputThread = new ExecutableStreamThread(standardErrorStream, outputConsumer, traceConsumer);
                errorOutputThread.start();

                final int returnCode = process.waitFor();
                outputConsumer.accept(String.format("process finished: %d", returnCode));

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