package com.synopsys.integration.wait;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.exception.IntegrationTimeoutException;
import com.synopsys.integration.log.BufferedIntLogger;
import com.synopsys.integration.log.LogLevel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WaitJobTest {
    private final BufferedIntLogger testingLogger = new BufferedIntLogger();
    private final ResilientJobConfig waitJobConfig = new ResilientJobConfig(testingLogger, 5, ResilientJobConfig.CURRENT_TIME_SUPPLIER, 1);
    private final ResilientJobConfig waitJobConfigFibonacciWaitIntervals = new ResilientJobConfig(testingLogger, 60, System.currentTimeMillis(), 1, true);

    @Test
    public void testTaskCompletesImmediately() throws IntegrationException, InterruptedException {
        WaitJobCondition waitJobCondition = () -> true;
        boolean completed = WaitJob.waitFor(waitJobConfig, waitJobCondition, "holypants");

        assertTrue(completed);

        String output = testingLogger.getOutputString(LogLevel.INFO);
        assertFalse(output.contains("Try #0"));
        assertTrue(output.contains("Try #1"));
        assertFalse(output.contains("Try #2"));

        assertTrue(output.contains("holypants"));
        assertTrue(output.contains("complete!"));
        assertFalse(output.contains("not done yet"));
    }

    @Test
    public void testTaskCompletesEventually() throws IntegrationException, InterruptedException {
        WaitJobCondition waitJobCondition = new WaitJobCondition() {
            private int count = 0;

            @Override
            public boolean isComplete() {
                return ++count > 2;
            }
        };
        boolean completed = WaitJob.waitFor(waitJobConfig, waitJobCondition, "holypants");

        assertTrue(completed);

        String output = testingLogger.getOutputString(LogLevel.INFO);
        assertFalse(output.contains("Try #0"));
        assertTrue(output.contains("Try #1"));
        assertTrue(output.contains("Try #2"));

        assertTrue(output.contains("holypants"));
        assertTrue(output.contains("complete!"));
        assertTrue(output.contains("not done yet"));
    }

    @Test
    public void testTaskCompletesEventuallyFibonacciWaitIntervals() throws IntegrationException, InterruptedException {
        WaitJobCondition waitJobCondition = new WaitJobCondition() {
            private int count = 0;

            @Override
            public boolean isComplete() {
                return ++count > 5;
            }
        };
        boolean completed = WaitJob.waitFor(waitJobConfigFibonacciWaitIntervals, waitJobCondition, "holypants");

        assertTrue(completed);

        String output = testingLogger.getOutputString(LogLevel.INFO);
        // this usage should have an increasing time delay interval for each iteration
        // according to a modified Fibonacci sequence (1 2 3 5... rather than 1 1 2 3 5...
        assertFalse(output.contains("Try #0"));
        assertTrue(output.contains("Try #1") && output.contains("1 seconds"));
        assertTrue(output.contains("Try #2") && output.contains("2 seconds"));
        assertTrue(output.contains("Try #3") && output.contains("3 seconds"));
        assertTrue(output.contains("Try #4") && output.contains("5 seconds"));
        assertTrue(output.contains("Try #5") && output.contains("8 seconds"));

        assertTrue(output.contains("holypants"));
        assertTrue(output.contains("complete!"));
        assertTrue(output.contains("not done yet"));
    }

    @Test
    public void testTaskTimesOut() throws IntegrationException, InterruptedException {
        WaitJobCondition waitJobCondition = () -> false;
        Assertions.assertThrows(IntegrationTimeoutException.class, () -> WaitJob.waitFor(waitJobConfig, waitJobCondition, "holypants"));
    }

}
