/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.executable;

import java.util.function.Consumer;

/**
 * This will provide the command to execute to the provided Consumer without actually executing anything. Any parameter with 'password' will be masked. Empty output will be returned.
 */
public class DryRunExecutableRunner implements ExecutableRunner {
    private Consumer<String> loggingMethod;

    public DryRunExecutableRunner(Consumer<String> loggingMethod) {
        this.loggingMethod = loggingMethod;
    }

    @Override
    public ExecutableOutput execute(Executable executable) {
        loggingMethod.accept(executable.getExecutableDescription());
        return new ExecutableOutput("", "");
    }

}
