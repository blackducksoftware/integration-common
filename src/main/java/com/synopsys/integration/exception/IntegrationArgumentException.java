package com.synopsys.integration.exception;

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
