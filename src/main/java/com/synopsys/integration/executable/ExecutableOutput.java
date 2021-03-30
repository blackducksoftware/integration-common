/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.executable;

import java.util.Arrays;
import java.util.List;

public class ExecutableOutput {
    private int returnCode = 0;
    private final String standardOutput;
    private final String errorOutput;

    public ExecutableOutput(final int returnCode, final String standardOutput, final String errorOutput) {
        this.returnCode = returnCode;
        this.standardOutput = standardOutput;
        this.errorOutput = errorOutput;
    }

    public ExecutableOutput(final String standardOutput, final String errorOutput) {
        this(0, standardOutput, errorOutput);
    }

    public List<String> getStandardOutputAsList() {
        return Arrays.asList(standardOutput.split(System.lineSeparator()));
    }

    public List<String> getErrorOutputAsList() {
        return Arrays.asList(errorOutput.split(System.lineSeparator()));
    }

    public String getStandardOutput() {
        return standardOutput;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public int getReturnCode() {
        return returnCode;
    }
}
