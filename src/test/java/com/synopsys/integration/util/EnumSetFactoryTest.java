package com.synopsys.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

public class EnumSetFactoryTest {
    private final EnumSetFactory<BestTvShows> bestShowsFactory = new EnumSetFactory<>(BestTvShows.class);

    @Test
    public void testEmptyString() {
        EnumSet<BestTvShows> noShows = bestShowsFactory.parseIncludedValues("");
        assertTrue(noShows.isEmpty());
    }

    @Test
    public void testTerribleShow() {
        EnumSet<BestTvShows> terribleShows = bestShowsFactory.parseIncludedValues("THE_INCREDIBLE_HULK");
        assertTrue(terribleShows.isEmpty());
    }

    @Test
    public void testSimpleString() {
        String values = "BATMAN_THE_ANIMATED_SERIES, MURDER_SHE_WROTE, GET_SMART, FIREFLY, EUREKA";
        EnumSet<BestTvShows> selectShows = bestShowsFactory.parseIncludedValues(values);

        assertEquals(5, selectShows.size());

        assertTrue(selectShows.contains(BestTvShows.BATMAN_THE_ANIMATED_SERIES));
        assertTrue(selectShows.contains(BestTvShows.GET_SMART));
        assertTrue(selectShows.contains(BestTvShows.MURDER_SHE_WROTE));
        assertTrue(selectShows.contains(BestTvShows.FIREFLY));
        assertTrue(selectShows.contains(BestTvShows.EUREKA));
    }

    @Test
    public void testAll() {
        ExcludedIncludedFilter includeAll = new ExcludedIncludedAllNoneFilter(Collections.emptyList(), Arrays.asList("ALL"));
        ExcludedIncludedFilter excludeAll = new ExcludedIncludedAllNoneFilter(Arrays.asList("ALL"), Collections.emptyList());

        EnumSet<BestTvShows> all = EnumSet.allOf(BestTvShows.class);
        EnumSet<BestTvShows> none = EnumSet.noneOf(BestTvShows.class);

        assertEquals(all, bestShowsFactory.parse(includeAll));
        assertEquals(none, bestShowsFactory.parse(excludeAll));
        assertEquals(all, bestShowsFactory.parseIncludedValues("ALL"));
    }

    @Test
    public void testNone() {
        ExcludedIncludedFilter includeNone = new ExcludedIncludedAllNoneFilter(Collections.emptyList(), Arrays.asList("NONE"));
        ExcludedIncludedFilter excludeNone = new ExcludedIncludedAllNoneFilter(Arrays.asList("NONE"), Collections.emptyList());

        EnumSet<BestTvShows> all = EnumSet.allOf(BestTvShows.class);
        EnumSet<BestTvShows> none = EnumSet.noneOf(BestTvShows.class);

        assertEquals(none, bestShowsFactory.parse(includeNone));
        assertEquals(all, bestShowsFactory.parse(excludeNone));
        assertEquals(none, bestShowsFactory.parseIncludedValues("NONE"));
    }

    @Test
    public void testBadEnum() {
        try {
            EnumSetFactory<NaughtyNaughtyEnum> badEnum = new EnumSetFactory<>(NaughtyNaughtyEnum.class);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("EnumSetFactory"));
        }
    }

    @Test
    public void testFilteringExcludeOverrides() {
        String mostlyIncluded = "VERONICA_MARS, THE_NEWSROOM, ARCHER, BATTLESTAR_GALACTICA, HOUSE";
        String exclude = "BATTLESTAR_GALACTICA";

        ExcludedIncludedFilter testingOverride = ExcludedIncludedFilter.fromCommaSeparatedStrings(exclude, mostlyIncluded);
        EnumSet<BestTvShows> trulyIncluded = bestShowsFactory.parse(testingOverride);

        assertEquals(4, trulyIncluded.size());

        assertTrue(trulyIncluded.contains(BestTvShows.VERONICA_MARS));
        assertTrue(trulyIncluded.contains(BestTvShows.THE_NEWSROOM));
        assertTrue(trulyIncluded.contains(BestTvShows.ARCHER));
        assertTrue(trulyIncluded.contains(BestTvShows.HOUSE));
    }

}
