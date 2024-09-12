/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.function;

public interface ThrowingSupplier<T, E extends Throwable> {
    /**
     * Gets a result which may throw an exception.
     */
    T get() throws E;

}
