package com.synopsys.integration.util;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class ResourceUtilTest {
    private static final String TEST_RESOURCE = "/test-resource.txt";

    @Test
    public void testReturnsValues() throws Exception {
        assertNotNull(ResourceUtil.getResourceAsString(getClass(), TEST_RESOURCE, StandardCharsets.UTF_8));
        assertNotNull(ResourceUtil.getResourceAsString(getClass(), TEST_RESOURCE, "UTF-8"));
    }

}
