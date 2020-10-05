package com.synopsys.integration.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ExcludedIncludedFilterTest {
    @Test
    public void testConstructor() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = new ExcludedIncludedFilter("", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = new ExcludedIncludedFilter();
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = new ExcludedIncludedFilter();
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
    }

    @Test
    public void testExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("bad", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));

        excludedIncludedFilter = new ExcludedIncludedFilter("really_bad,also_really_bad", null);
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testExcludedWithAsterix() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("*bad*", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad"));

        assertFalse(excludedIncludedFilter.shouldInclude("*bad*"));
    }

    @Test
    public void testIncludedAndExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("bad", "good,bad");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));

        excludedIncludedFilter = new ExcludedIncludedFilter("really_bad,also_really_bad", "good");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

}
