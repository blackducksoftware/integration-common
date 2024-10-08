package com.blackduck.integration.wait;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.exception.IntegrationTimeoutException;
import com.blackduck.integration.log.BufferedIntLogger;
import com.blackduck.integration.log.LogLevel;
import com.blackduck.integration.wait.tracker.WaitIntervalTrackerFactory;

public class WaitJobTest {
    private final BufferedIntLogger testingLogger = new BufferedIntLogger();
    private final ResilientJobConfig waitJobConfig = new ResilientJobConfig(testingLogger, ResilientJobConfig.CURRENT_TIME_SUPPLIER, WaitIntervalTrackerFactory.createConstant(5L, 1L));
    private final ResilientJobConfig waitJobConfigFibonacciWaitIntervals = new ResilientJobConfig(testingLogger, System.currentTimeMillis(), WaitIntervalTrackerFactory.createProgressive(60L, 60L));

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
