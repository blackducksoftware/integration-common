package com.synopsys.integration.log;

import com.synopsys.integration.util.IntEnvironmentVariables;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntLoggerTest {
    @Test
    public void testSetLogLevelWithVariables() {
        IntLogger logger = new PrintStreamIntLogger(System.out, LogLevel.INFO);
        IntEnvironmentVariables variables = IntEnvironmentVariables.empty();
        logger.setLogLevel(variables);
        assertEquals(LogLevel.INFO, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "FAKE");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.INFO, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "error");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.ERROR, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "erRor");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.ERROR, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "OFF");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.OFF, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "ERROR");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.ERROR, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "WARN");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.WARN, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "INFO");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.INFO, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "DEBUG");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.DEBUG, logger.getLogLevel());

        variables.put("BLACK_DUCK_LOG_LEVEL", "TRACE");
        logger.setLogLevel(variables);
        assertEquals(LogLevel.TRACE, logger.getLogLevel());
    }

}
