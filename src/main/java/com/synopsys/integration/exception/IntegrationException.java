/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.exception;

public class IntegrationException extends Exception {
    private static final long serialVersionUID = 6033954233007843793L;

    public IntegrationException() {
        super();
    }

    public IntegrationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IntegrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IntegrationException(final String message) {
        super(message);
    }

    public IntegrationException(final Throwable cause) {
        super(cause);
    }

}
