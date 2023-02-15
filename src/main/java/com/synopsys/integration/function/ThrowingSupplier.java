/*
 * integration-common
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.function;

public interface ThrowingSupplier<T, E extends Throwable> {
    /**
     * Gets a result which may throw an exception.
     */
    T get() throws E;

}
