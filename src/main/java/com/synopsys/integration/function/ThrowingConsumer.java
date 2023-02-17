/*
 * integration-common
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.function;

public interface ThrowingConsumer<T, E extends Throwable> {
    void accept(T t) throws E;

}
