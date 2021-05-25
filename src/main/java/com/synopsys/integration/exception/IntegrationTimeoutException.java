/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.exception;

public class IntegrationTimeoutException extends IntegrationException {
    public IntegrationTimeoutException() {
    }

    public IntegrationTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IntegrationTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntegrationTimeoutException(String message) {
        super(message);
    }

    public IntegrationTimeoutException(Throwable cause) {
        super(cause);
    }

}
