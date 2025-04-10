package com.blackduck.integration.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NameVersionTest {

    @Test
    public void testEquals() {
        NameVersion nameVersion1 = new NameVersion("moduleA", "1.0.0");
        NameVersion nameVersion2 = new NameVersion("moduleA", "2.0.0");

        assertNotEquals(nameVersion1, nameVersion2);
    }

    @Test
    public void testHashCode() {
        NameVersion nameVersion1 = new NameVersion("moduleA", "1.0.0");
        NameVersion nameVersion2 = new NameVersion("moduleA", "2.0.0");

        Map<NameVersion, String> map = new HashMap<>();
        map.putIfAbsent(nameVersion1, "moduleAv1");
        map.putIfAbsent(nameVersion2, "moduleAv2");
        assertEquals(map.size(), 2);
    }
}
