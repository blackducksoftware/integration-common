package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.blackduck.integration.util.ResourceUtil;

public class ResourceUtilTest {
    private static final String TEST_RESOURCE = "/test-resource.txt";

    @Test
    public void testReturnsValues() throws Exception {
        assertNotNull(ResourceUtil.getResourceAsString(getClass(), TEST_RESOURCE, StandardCharsets.UTF_8));
        assertNotNull(ResourceUtil.getResourceAsString(getClass(), TEST_RESOURCE, "UTF-8"));
    }

}
