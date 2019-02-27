package com.synopsys.integration.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BuilderPropertyKeyTest {
    @Test
    public void testValidKeys() {
        BuilderPropertyKey key = new BuilderPropertyKey("ONE_TWO");
        assertNotNull(key);

        try {
            new BuilderPropertyKey("terrible key");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            new BuilderPropertyKey("terrible_key");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            new BuilderPropertyKey("TERRIBLE KEY");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

}
