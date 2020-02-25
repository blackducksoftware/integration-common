package com.synopsys.integration.wait;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.BufferedIntLogger;
import com.synopsys.integration.log.LogLevel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class WaitJobTest {
    @Test
    public void testTaskNameLogged() throws IntegrationException, InterruptedException {
        BufferedIntLogger testingLogger = new BufferedIntLogger();

        WaitJob waitJob = WaitJob.createUsingSystemTimeWhenInvoked(testingLogger, 5, 1, "holypants", createTask());
        waitJob.waitFor();

        String output = testingLogger.getOutputString(LogLevel.INFO);
        Assert.assertTrue(output.contains("holypants"));
    }

    @Test
    public void testTaskNameNotLogged() throws IntegrationException, InterruptedException {
        BufferedIntLogger testingLogger = new BufferedIntLogger();

        WaitJob waitJob = WaitJob.createUsingSystemTimeWhenInvoked(testingLogger, 5, 1, createTask());
        waitJob.waitFor();

        String output = testingLogger.getOutputString(LogLevel.INFO);
        Assert.assertFalse(output.contains("holypants"));
    }

    private WaitJobTask createTask() {
        return new WaitJobTask() {
            private int count = 0;

            @Override
            public boolean isComplete() throws IntegrationException {
                return ++count > 1;
            }
        };
    }

}
