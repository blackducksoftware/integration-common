package com.blackduck.integration.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.blackduck.integration.builder.BuilderProperties;
import com.blackduck.integration.builder.BuilderPropertyKey;

public class BuilderPropertiesTest {
    public static final HashSet<String> DEFAULT_STRINGS = new HashSet<>(Arrays.asList("ONE", "TWO", "THREE_FOUR"));

    @Test
    public void testPropertiesWithPrefix() {
        BuilderProperties builderProperties = BuilderProperties.createWithStrings(new HashSet<>(Arrays.asList("POLARIS ONE", "POLARIS.TWO", "polaris_THREE_FOUR")));

        Set<String> expectedEnviromentVariableKeys = new HashSet<>(Arrays.asList("POLARIS_ONE", "POLARIS_TWO", "POLARIS_THREE_FOUR"));
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("polaris.one", "polaris.two", "polaris.three.four"));

        assertEquals(expectedEnviromentVariableKeys, builderProperties.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, builderProperties.getPropertyKeys());
    }

    @Test
    public void testPropertiesWithoutPrefix() {
        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("one", "two", "three.four"));

        assertEquals(DEFAULT_STRINGS, builderProperties.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, builderProperties.getPropertyKeys());
    }

    @Test
    public void testGetProperties() {
        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);

        builderProperties.getProperties()
            .forEach((k, v) -> assertEquals(builderProperties.get(k), v));
    }

    @Test
    public void testGetKeys() {
        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);

        builderProperties.getKeys()
            .forEach(key -> assertTrue(DEFAULT_STRINGS.contains(key.getKey())));
    }

    @Test
    public void testSetShouldInclude() {
        String validTestKey = "ONE";
        String validTestKeyValue = "1";
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(validTestKey);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        builderProperties.set(builderPropertyKey, validTestKeyValue);

        assertEquals(validTestKeyValue, builderProperties.get(builderPropertyKey));
    }

    @Test
    public void testSetShouldNotInclude() {
        String invalidTestKey = "FIVE";
        String invalidTestKeyValue = "5";
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(invalidTestKey);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        builderProperties.set(builderPropertyKey, invalidTestKeyValue);

        assertNull(builderProperties.get(builderPropertyKey));
    }

    @Test
    public void testSetPropertyShouldInclude() {
        String validTestKey = "ONE";
        String validTestKeyValue = "1";
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(validTestKey);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        builderProperties.setProperty(validTestKey, validTestKeyValue);

        assertEquals(validTestKeyValue, builderProperties.get(builderPropertyKey));
    }

    @Test
    public void testSetPropertyShouldNotInclude() {
        String invalidTestKey = "FIVE";
        String invalidTestKeyValue = "5";
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(invalidTestKey);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        builderProperties.setProperty(invalidTestKey, invalidTestKeyValue);

        assertNull(builderProperties.get(builderPropertyKey));
    }

    @Test
    public void testSetPropertiesShouldInclude() {
        String validTestKeyOne = "ONE";
        String validTestKeyValueOne = "1";
        String validTestKeyTwo = "TWO";
        String validTestKeyValueTwo = "2";
        Map<String, String> validProperties = new HashMap<>();
        validProperties.put(validTestKeyOne, validTestKeyValueOne);
        validProperties.put(validTestKeyTwo, validTestKeyValueTwo);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        builderProperties.setProperties(validProperties.entrySet());

        assertEquals(validTestKeyValueOne, builderProperties.get(new BuilderPropertyKey(validTestKeyOne)));
        assertEquals(validTestKeyValueTwo, builderProperties.get(new BuilderPropertyKey(validTestKeyTwo)));
    }

    @Test
    public void testSetPropertiesShouldNotInclude() {
        String invalidTestKeyOne = "FIVE";
        String invalidTestKeyValueOne = "5";
        String invalidTestKeyTwo = "SIX";
        String invalidTestKeyValueTwo = "6";
        Map<String, String> validProperties = new HashMap<>();
        validProperties.put(invalidTestKeyOne, invalidTestKeyValueOne);
        validProperties.put(invalidTestKeyTwo, invalidTestKeyValueTwo);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);
        builderProperties.setProperties(validProperties.entrySet());

        assertNull(builderProperties.get(new BuilderPropertyKey(invalidTestKeyOne)));
        assertNull(builderProperties.get(new BuilderPropertyKey(invalidTestKeyTwo)));
    }

}
