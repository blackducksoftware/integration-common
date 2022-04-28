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
    public void testTaskTimesOut() throws IntegrationException, InterruptedException {
        WaitJobCondition waitJobCondition = () -> false;
        Assertions.assertThrows(IntegrationTimeoutException.class, () -> WaitJob.waitFor(waitJobConfig, waitJobCondition, "holypants"));
    }

}
