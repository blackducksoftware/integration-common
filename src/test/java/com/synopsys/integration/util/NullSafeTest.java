package com.synopsys.integration.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

public class NullSafeTest {

    private Function<String, String> NULL_RESULT = (s) -> null;
    private Function<String, String> REMOVE_FIRST_LETTER = (s) -> s.substring(1);

    @Test
    public void testOneArgument() {
        String s = "123456789";
        s = NullSafe.of(s).get(REMOVE_FIRST_LETTER);
        assertEquals("23456789", s);
    }

    @Test
    public void testThenOneArgument() {
        String s = "123456789";
        s = NullSafe.of(s).then(REMOVE_FIRST_LETTER).get();
        assertEquals("23456789", s);
    }

    @Test
    public void testOneArgumentNullResult() {
        String s = "123456789";
        s = NullSafe.of(s).get(NULL_RESULT);
        assertNull(s);
    }

    @Test
    public void testOneArgumentNullInput() {
        String s = null;
        s = NullSafe.of(s).get(REMOVE_FIRST_LETTER);
        assertNull(s);
    }

    @Test
    public void testFourArguments() {
        String s = "123456789";
        s = NullSafe.of(s)
                .then(REMOVE_FIRST_LETTER)
                .then(REMOVE_FIRST_LETTER)
                .then(REMOVE_FIRST_LETTER)
                .get(REMOVE_FIRST_LETTER);
        assertEquals("56789", s);
    }

    @Test
    public void testFourArgumentsWithNullFunction() {
        String s = "123456789";
        s = NullSafe.of(s)
                .then(REMOVE_FIRST_LETTER)
                .then(REMOVE_FIRST_LETTER)
                .then(NULL_RESULT)
                .get(REMOVE_FIRST_LETTER);
        assertNull(s);
    }

    @Test
    public void testFourArgumentsWithNullFunctionGetOrThrow() {
        String s = "123456789";
        RuntimeException ex = new RuntimeException("Should not be thrown.");

        String result = null;
        try {
            result = NullSafe.of(s)
                         .then(REMOVE_FIRST_LETTER)
                         .then(REMOVE_FIRST_LETTER)
                         .then(REMOVE_FIRST_LETTER)
                         .getOrThrow(ex);
        } catch (Exception e) {
            fail("Exception was not supposed to be thrown.");
        }
        assertEquals("456789", result);
    }

    @Test
    public void testFourArgumentsWithNullFunctionAndException() {
        String exceptionMsg = "something fishy";
        String s = "123456789";
        RuntimeException ex = new RuntimeException(exceptionMsg);

        Throwable e = assertThrows(RuntimeException.class, () -> {
            NullSafe.of(s)
                .then(REMOVE_FIRST_LETTER)
                .then(REMOVE_FIRST_LETTER)
                .then(NULL_RESULT)
                .then(REMOVE_FIRST_LETTER)
                .getOrThrow(ex);
        });
        assertEquals(exceptionMsg, e.getMessage());
    }

    @Test
    public void testGetOrDefault() {
        String s = "123456789";

        String result = NullSafe.of(s)
                            .then(REMOVE_FIRST_LETTER)
                            .then(REMOVE_FIRST_LETTER)
                            .then(REMOVE_FIRST_LETTER)
                            .getOrDefault("default");
        assertEquals("456789", result);
    }

    @Test
    public void testGetOrDefaultReturnDefault() {
        String s = "123456789";

        String result = NullSafe.of(s)
                            .then(REMOVE_FIRST_LETTER)
                            .then(REMOVE_FIRST_LETTER)
                            .then(NULL_RESULT)
                            .then(REMOVE_FIRST_LETTER)
                            .getOrDefault("default");
        assertEquals("default", result);
    }

}
