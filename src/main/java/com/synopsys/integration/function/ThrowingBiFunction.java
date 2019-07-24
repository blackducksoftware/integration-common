package com.synopsys.integration.function;

public interface ThrowingBiFunction<T, U, R, E extends Throwable> {
    /**
     * Applies this function, which may throw an exception, to the given arguments.
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u) throws E;

}
