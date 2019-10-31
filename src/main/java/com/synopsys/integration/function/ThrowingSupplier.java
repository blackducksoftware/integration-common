package com.synopsys.integration.function;

public interface ThrowingSupplier<T, E extends Throwable> {
    /**
     * Gets a result which may throw an exception.
     */
    T get() throws E;

}
