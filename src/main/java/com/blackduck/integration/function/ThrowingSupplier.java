/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.function;

public interface ThrowingSupplier<T, E extends Throwable> {
    /**
     * Gets a result which may throw an exception.
     */
    T get() throws E;

}
