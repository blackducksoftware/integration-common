package com.synopsys.integration.log;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

import org.junit.Test;

public class IntBufferedLoggerTest {
    @Test
    public void testLogger() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final IntBufferedLogger logger = new IntBufferedLogger();
        assertNull(logger.getLogLevel());

        assertLogLevel(logger, "debug", LogLevel.DEBUG, new Throwable("debug throwable"));
        assertLogLevel(logger, "error", LogLevel.ERROR, new Throwable("error throwable"));
        assertLogLevel(logger, "info", LogLevel.INFO, new Throwable("info throwable"));
        assertLogLevel(logger, "trace", LogLevel.TRACE, new Throwable("trace throwable"));
        assertLogLevel(logger, "warn", LogLevel.WARN, new Throwable("warn throwable"));

        assertLogLevelsEmpty(logger);
        logger.alwaysLog("always log this");
        assertLogLevelsEmptyExcept(logger, LogLevel.OFF);
        assertEquals("always log this", logger.getOutputString(LogLevel.OFF));
        logger.resetAllLogs();

        assertLogLevelsEmpty(logger);
        logger.error(new Throwable("just a throwable"));
        assertLogLevelsEmptyExcept(logger, LogLevel.ERROR);
        assertTrue(logger.getOutputString(LogLevel.ERROR).startsWith("java.lang.Throwable: just a throwable"));

        logger.setLogLevel(LogLevel.OFF);
    }

    private void assertLogLevel(final IntBufferedLogger logger, final String testString, final LogLevel testLevel, final Throwable throwable)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertLogLevelsEmpty(logger);

        final Method loggingMethod = logger.getClass().getDeclaredMethod(testString, String.class);
        loggingMethod.invoke(logger, "testing " + testString);
        assertLogLevelsEmptyExcept(logger, testLevel);
        assertEquals("testing " + testString, logger.getOutputString(testLevel));

        try {
            final Method loggingThrowableMethod = logger.getClass().getDeclaredMethod(testString, String.class, Throwable.class);
            loggingThrowableMethod.invoke(logger, "testing throwable " + testString, throwable);
            assertLogLevelsEmptyExcept(logger, testLevel);
            assertTrue(logger.getOutputString(testLevel).startsWith("testing " + testString + "\ntesting throwable " + testString + "\njava.lang.Throwable: " + testString + " throwable"));
        } catch (final NoSuchMethodException e) {
            // only some log levels have a throwable option
        }

        logger.resetAllLogs();
    }

    private void assertLogLevelsEmpty(final IntBufferedLogger logger) {
        final EnumSet<LogLevel> allLogLevels = EnumSet.allOf(LogLevel.class);
        allLogLevels.forEach(level -> assertNull(logger.getOutputString(level)));
    }

    private void assertLogLevelsEmptyExcept(final IntBufferedLogger logger, final LogLevel exceptLogLevel) {
        final EnumSet<LogLevel> allLogLevelsExcept = EnumSet.allOf(LogLevel.class);
        allLogLevelsExcept.remove(exceptLogLevel);
        allLogLevelsExcept.forEach(level -> assertNull(logger.getOutputString(level)));

        assertNotNull(logger.getOutputString(exceptLogLevel));
        assertNotEquals("", logger.getOutputString(exceptLogLevel));
    }

}
