package com.synopsys.integration.function;

public interface ThrowingConsumer<T, E extends Throwable> {
    void accept(T t) throws E;

}
