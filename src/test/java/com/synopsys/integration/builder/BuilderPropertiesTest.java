package com.synopsys.integration.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class BuilderPropertiesTest {
    public static final HashSet<String> DEFAULT_STRINGS = new HashSet<>(Arrays.asList("ONE", "TWO", "THREE_FOUR"));
    public static final BuilderProperties DEFAULT_PROPERTIES = BuilderProperties.createWithStrings(DEFAULT_STRINGS);

    @Test
    public void testPropertiesWithPrefix() {
        BuilderProperties properties = BuilderProperties.createWithStrings(new HashSet<>(Arrays.asList("POLARIS ONE", "POLARIS.TWO", "polaris_THREE_FOUR")));

        Set<String> expectedEnviromentVariableKeys = new HashSet<>(Arrays.asList("POLARIS_ONE", "POLARIS_TWO", "POLARIS_THREE_FOUR"));
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("polaris.one", "polaris.two", "polaris.three.four"));

        assertEquals(expectedEnviromentVariableKeys, properties.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, properties.getPropertyKeys());
    }

    @Test
    public void testPropertiesWithoutPrefix() {
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("one", "two", "three.four"));

        assertEquals(DEFAULT_STRINGS, DEFAULT_PROPERTIES.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, DEFAULT_PROPERTIES.getPropertyKeys());
    }

    @Test
    public void testGetProperties() {
        BuilderProperties properties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);

        properties.getProperties()
            .forEach((k, v) -> assertEquals(properties.get(k), v));
    }

    @Test
    public void testAddProperties() {
        String setString = "SET_STRING";
        String setBuilderPropertyKey = "SET_BUILDER_PROPERTY_KEY";
        HashSet<String> expectedKeys = DEFAULT_STRINGS;
        expectedKeys.add(setString);
        expectedKeys.add(setBuilderPropertyKey);
        BuilderProperties expectedBuilderProperties = BuilderProperties.createWithStrings(expectedKeys);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);

        builderProperties.setProperty(setString, "");
        BuilderPropertyKey builderPropertyKey = new BuilderPropertyKey(setBuilderPropertyKey);
        builderProperties.set(builderPropertyKey, "");

        assertEquals(expectedBuilderProperties.getKeys(), builderProperties.getKeys());
    }

    @Test
    public void testSetProperties() {
        String propertyKeyOne = "PROPERTY_ONE";
        String propertyKeyTwo = "PROPERTY_TWO";

        HashSet<String> expectedKeys = DEFAULT_STRINGS;
        expectedKeys.add(propertyKeyOne);
        expectedKeys.add(propertyKeyTwo);
        BuilderProperties expectedBuilderProperties = BuilderProperties.createWithStrings(expectedKeys);

        BuilderProperties builderProperties = BuilderProperties.createWithStrings(DEFAULT_STRINGS);

        Map<String, String> properties = new HashMap<>();
        properties.put(propertyKeyOne, "");
        properties.put(propertyKeyTwo, "");
        builderProperties.setProperties(properties.entrySet());

        assertEquals(expectedBuilderProperties.getKeys(), builderProperties.getKeys());
    }
}
