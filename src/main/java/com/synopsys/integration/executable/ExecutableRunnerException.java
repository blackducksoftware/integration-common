/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.executable;

public class ExecutableRunnerException extends Exception {
    private static final long serialVersionUID = -4117278710469900787L;

    public ExecutableRunnerException(final Throwable innerException) {
        super(innerException);
    }
}
