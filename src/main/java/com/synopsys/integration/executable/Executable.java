/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.executable;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

import com.synopsys.integration.util.Stringable;

public class Executable extends Stringable {
    private final File workingDirectory;
    private final Map<String, String> environmentVariables = new HashMap<>();
    private final List<String> commandWithArguments = new ArrayList<>();

    public static Executable create(final File workingDirectory, Map<String, String> environmentVariables, final List<String> commandWithArguments) {
        return new Executable(workingDirectory, environmentVariables, commandWithArguments);
    }

    public static Executable create(final File workingDirectory, Map<String, String> environmentVariables, final String command, final List<String> arguments) {
        List<String> commandWithArguments = new ArrayList<>();
        commandWithArguments.add(command);
        commandWithArguments.addAll(arguments);
        return new Executable(workingDirectory, environmentVariables, commandWithArguments);
    }

    public static Executable create(final File workingDirectory, final List<String> commandWithArguments) {
        return create(workingDirectory, Collections.emptyMap(), commandWithArguments);
    }

    public static Executable create(final File workingDirectory, final String command, final List<String> arguments) {
        return create(workingDirectory, Collections.emptyMap(), command, arguments);
    }

    public static Executable create(final File workingDirectory, Map<String, String> environmentVariables, File executableFile) {
        return create(workingDirectory, environmentVariables, executableFile.getAbsolutePath(), Collections.emptyList());
    }

    public static Executable create(final File workingDirectory, Map<String, String> environmentVariables, File executableFile, List<String> arguments) {
        return create(workingDirectory, environmentVariables, executableFile.getAbsolutePath(), arguments);
    }

    public static Executable create(final File workingDirectory, File executableFile) {
        return create(workingDirectory, executableFile.getAbsolutePath(), Collections.emptyList());
    }

    public static Executable create(final File workingDirectory, File executableFile, List<String> arguments) {
        return create(workingDirectory, executableFile.getAbsolutePath(), arguments);
    }

    public static String getMaskedCommand(List<String> commandWithArguments) {
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

    public Executable(final File workingDirectory, final Map<String, String> environmentVariables, final List<String> commandWithArguments) {
        this.workingDirectory = workingDirectory;
        this.environmentVariables.putAll(environmentVariables);
        this.commandWithArguments.addAll(commandWithArguments);
    }

    public String getExecutableDescription() {
        return Executable.getMaskedCommand(commandWithArguments);
    }

    /**
     * As some executables can contain password arguments, this method is not recommended for production runtimes.
     */
    public String getUnmaskedInsecureExecutableDescription() {
        return StringUtils.join(commandWithArguments, ' ');
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public List<String> getCommandWithArguments() {
        return commandWithArguments;
    }

}
