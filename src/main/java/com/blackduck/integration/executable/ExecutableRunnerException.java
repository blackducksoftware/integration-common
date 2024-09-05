/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.executable;

public class ExecutableRunnerException extends Exception {
    private static final long serialVersionUID = -4117278710469900787L;

    public ExecutableRunnerException(final Throwable innerException) {
        super(innerException);
    }
}
