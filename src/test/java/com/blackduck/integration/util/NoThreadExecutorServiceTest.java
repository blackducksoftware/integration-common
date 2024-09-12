package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class NoThreadExecutorServiceTest {
    @Test
    void execute() {
        final ExecutorService executorService = new NoThreadExecutorService();
        final AtomicInteger counter = new AtomicInteger();
        executorService.submit(counter::incrementAndGet);
        executorService.submit(counter::incrementAndGet);
        assertEquals(2, counter.get());
    }

}
