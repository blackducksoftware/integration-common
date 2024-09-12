package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ExcludedIncludedWildcardFilterTest {
    @Test
    public void testExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedWildcardFilter.fromCommaSeparatedStrings("*bad*", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("badMonkey"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testIncludedAndExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedWildcardFilter.fromCommaSeparatedStrings("*bad*", "good*,bad");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertTrue(excludedIncludedFilter.shouldInclude("goodMonkey"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("goodbad"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testExcludedEscapeCharactersNotSupported() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedWildcardFilter.fromCommaSeparatedStrings("bad\\*", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad*"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad\\"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad\\Monkey"));
    }

    @Test
    public void testIncludedAndExcludedList() {
        List<String> toExclude = Arrays.asList("redherring", "*bad*");
        List<String> toInclude = Arrays.asList("good*", "bad");
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedWildcardFilter.fromCollections(toExclude, toInclude);
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertTrue(excludedIncludedFilter.shouldInclude("goodMonkey"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("goodbad"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testNothingIncludedOrExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedWildcardFilter.EMPTY;
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertTrue(excludedIncludedFilter.shouldInclude("goodMonkey"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad"));
        assertTrue(excludedIncludedFilter.shouldInclude("goodbad"));
        assertTrue(excludedIncludedFilter.shouldInclude("really_bad"));
        assertTrue(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

}
