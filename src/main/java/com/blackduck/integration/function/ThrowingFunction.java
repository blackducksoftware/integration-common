/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.function;

public interface ThrowingFunction<T, R, E extends Throwable> {
    /**
     * Applies this function, which may throw an exception, to the given argument.
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws E;

}
