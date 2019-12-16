package com.synopsys.integration.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ExcludedIncludedWildcardFilterTest {
    @Test
    public void testExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedWildcardFilter("*bad*", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("badMonkey"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testIncludedAndExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedWildcardFilter("*bad*", "good*,bad");
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
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedWildcardFilter("bad\\*", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad"));
        assertTrue(excludedIncludedFilter.shouldInclude("bad*"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad\\"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad\\Monkey"));
    }

}
