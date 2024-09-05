package com.blackduck.integration.wait.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.blackduck.integration.wait.tracker.WaitIntervalTracker;
import com.blackduck.integration.wait.tracker.WaitIntervalTrackerProgressive;

public class WaitIntervalTrackerProgressiveTest {

    @Test
    void test() {
        WaitIntervalTracker tracker = new WaitIntervalTrackerProgressive(60L, 5L);
        assertEquals(60L, tracker.getTimeoutInSeconds());
        assertEquals(1L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(2L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(3L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(5L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(5L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(5L, tracker.getNextWaitIntervalInSeconds());
    }
}
