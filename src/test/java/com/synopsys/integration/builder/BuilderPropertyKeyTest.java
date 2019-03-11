package com.synopsys.integration.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BuilderPropertyKeyTest {
    @Test
    public void testValidKeys() {
        assertKeyEquals("ONE_TWO", "ONE_TWO");
        assertKeyEquals("terrible key", "TERRIBLE_KEY");
        assertKeyEquals("terrible  key", "TERRIBLE__KEY");
        assertKeyEquals("terrible_key", "TERRIBLE_KEY");
        assertKeyEquals("TERRIBLE KEY", "TERRIBLE_KEY");
        assertKeyEquals("terrible.key", "TERRIBLE_KEY");
    }

    private void assertKeyEquals(String keyToCreate, String expectedKeyValue) {
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(keyToCreate);
        assertNotNull(builderPropertyKey);
        assertEquals(expectedKeyValue, builderPropertyKey.getKey());
    }

}
