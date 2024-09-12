package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ExcludedIncludedFilterTest {
    @Test
    public void testConstructor() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = ExcludedIncludedFilter.EMPTY;
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = ExcludedIncludedFilter.EMPTY;
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
    }

    @Test
    public void testExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("bad", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));

        excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("really_bad,also_really_bad", null);
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testExcludedWithAsterix() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("*bad*", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad"));

        assertFalse(excludedIncludedFilter.shouldInclude("*bad*"));
    }

    @Test
    public void testIncludedAndExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("bad", "good,bad");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));

        excludedIncludedFilter = ExcludedIncludedFilter.fromCommaSeparatedStrings("really_bad,also_really_bad", "good");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    public void testNothingSetExplicitly() {
        ExcludedIncludedFilter excludedIncludedFilter = ExcludedIncludedFilter.EMPTY;

        assertTrue(excludedIncludedFilter.shouldInclude("steak"));
        assertTrue(excludedIncludedFilter.shouldInclude("chicken"));
        //wow, this filter will include *anything*
        assertTrue(excludedIncludedFilter.shouldInclude("tofu"));
    }

}
