/*
 * integration-common
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.builder;

public abstract class IntegrationBuilder<T extends Buildable> {
    public T build() throws IllegalArgumentException {
        assertValid();

        return buildWithoutValidation();
    }

    /**
     * This method is for builders to instantiate the object they wrap. It is reasonable for a builder to
     * assume that all values are safe before this method will be called.
     */
    protected abstract T buildWithoutValidation();

    protected abstract void validate(BuilderStatus builderStatus);

    public final void assertValid() throws IllegalArgumentException {
        BuilderStatus builderStatus = validateAndGetBuilderStatus();

        if (!builderStatus.isValid()) {
            throw new IllegalArgumentException(builderStatus.getFullErrorMessage());
        }
    }

    public final boolean isValid() {
        BuilderStatus builderStatus = validateAndGetBuilderStatus();
        return builderStatus.isValid();
    }

    public final BuilderStatus validateAndGetBuilderStatus() {
        BuilderStatus builderStatus = new BuilderStatus();

        validate(builderStatus);

        return builderStatus;
    }

}
