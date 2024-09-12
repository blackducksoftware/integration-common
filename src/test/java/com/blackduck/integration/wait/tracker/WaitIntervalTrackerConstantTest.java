package com.blackduck.integration.wait.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class WaitIntervalTrackerConstantTest {

    @Test
    void testTypical() {
        WaitIntervalTracker tracker = new WaitIntervalTrackerConstant(60L, 15L);
        assertEquals(60L, tracker.getTimeoutInSeconds());
        assertEquals(15L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(15L, tracker.getNextWaitIntervalInSeconds());
        assertEquals(15L, tracker.getNextWaitIntervalInSeconds());
    }

    @Test
    void testIntervalTooBig() {
        WaitIntervalTracker tracker = new WaitIntervalTrackerConstant(60L, 70L);
        assertEquals(60L, tracker.getTimeoutInSeconds());
        assertEquals(60L, tracker.getNextWaitIntervalInSeconds());
    }
}
