package com.synopsys.integration.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class BuilderPropertiesTest {
    @Test
    public void testPropertiesWithPrefix() {
        BuilderProperties properties = BuilderProperties.createWithPrefixAndStrings("POLARIS", new HashSet<>(Arrays.asList("ONE", "TWO", "THREE_FOUR")));

        Set<String> expectedEnviromentVariableKeys = new HashSet<>(Arrays.asList("POLARIS_ONE", "POLARIS_TWO", "POLARIS_THREE_FOUR"));
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("polaris.one", "polaris.two", "polaris.three.four"));

        assertEquals(expectedEnviromentVariableKeys, properties.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, properties.getPropertyKeys());
    }

    @Test
    public void testPropertiesWithPrefixEndingUnderscore() {
        BuilderProperties properties = BuilderProperties.createWithPrefixAndStrings("POLARIS_", new HashSet<>(Arrays.asList("ONE", "TWO", "THREE_FOUR")));

        Set<String> expectedEnviromentVariableKeys = new HashSet<>(Arrays.asList("POLARIS_ONE", "POLARIS_TWO", "POLARIS_THREE_FOUR"));
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("polaris.one", "polaris.two", "polaris.three.four"));

        assertEquals(expectedEnviromentVariableKeys, properties.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, properties.getPropertyKeys());
    }

    @Test
    public void testPropertiesWithoutPrefix() {
        BuilderProperties properties = BuilderProperties.createWithStrings(new HashSet<>(Arrays.asList("ONE", "TWO", "THREE_FOUR")));

        Set<String> expectedEnviromentVariableKeys = new HashSet<>(Arrays.asList("ONE", "TWO", "THREE_FOUR"));
        Set<String> expectedPropertyKeys = new HashSet<>(Arrays.asList("one", "two", "three.four"));

        assertEquals(expectedEnviromentVariableKeys, properties.getEnvironmentVariableKeys());
        assertEquals(expectedPropertyKeys, properties.getPropertyKeys());
    }

}
