/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.function;

public interface ThrowingFunction<T, R, E extends Throwable> {
    /**
     * Applies this function, which may throw an exception, to the given argument.
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws E;

}
