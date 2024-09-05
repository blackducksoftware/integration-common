/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.exception;

public class IntegrationArgumentException extends IntegrationException {
    private static final long serialVersionUID = -1712992766578663029L;

    public IntegrationArgumentException() {
        super();
    }

    public IntegrationArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IntegrationArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntegrationArgumentException(String message) {
        super(message);
    }

    public IntegrationArgumentException(Throwable cause) {
        super(cause);
    }

}
