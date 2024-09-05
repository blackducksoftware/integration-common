/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.executable;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

import com.blackduck.integration.log.IntLogger;

public class ProcessBuilderRunner implements ExecutableRunner {
    private final IntLogger logger;
    private final Consumer<String> threadOutputConsumer;
    private final Consumer<String> threadTraceConsumer;

    public ProcessBuilderRunner(final IntLogger logger) {
        this.logger = logger;
        this.threadOutputConsumer = logger::info;
        this.threadTraceConsumer = logger::trace;
    }

    public ProcessBuilderRunner(final IntLogger logger, final Consumer<String> threadOutputConsumer, final Consumer<String> threadTraceConsumer) {
        this.logger = logger;
        this.threadOutputConsumer = threadOutputConsumer;
        this.threadTraceConsumer = threadTraceConsumer;
    }

    public ExecutableOutput execute(ProcessBuilder processBuilder) throws ExecutableRunnerException {
        logger.info(String.format("Running process builder >%s", Executable.getMaskedCommand(processBuilder.command())));
        return executeProcessBuilder(processBuilder);
    }

    @Override
    public ExecutableOutput execute(final Executable executable) throws ExecutableRunnerException {
        logger.info(String.format("Running executable >%s", executable.getExecutableDescription()));
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
                final ExecutableStreamThread standardOutputThread = new ExecutableStreamThread(standardOutputStream, threadOutputConsumer, threadTraceConsumer);
                standardOutputThread.start();

                final ExecutableStreamThread errorOutputThread = new ExecutableStreamThread(standardErrorStream, threadOutputConsumer, threadTraceConsumer);
                errorOutputThread.start();

                final int returnCode = process.waitFor();
                logger.info(String.format("Process return code: %d", returnCode));

                standardOutputThread.join();
                errorOutputThread.join();

                final String standardOutput = standardOutputThread.getExecutableOutput().trim();
                final String errorOutput = errorOutputThread.getExecutableOutput().trim();

                final ExecutableOutput output = new ExecutableOutput(returnCode, standardOutput, errorOutput);
                return output;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ExecutableRunnerException(e);
        } catch (final Exception e) {
            throw new ExecutableRunnerException(e);
        }
    }

}
