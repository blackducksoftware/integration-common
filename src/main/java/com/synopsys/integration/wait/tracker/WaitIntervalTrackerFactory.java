package com.synopsys.integration.wait.tracker;

public class WaitIntervalTrackerFactory {

    public static WaitIntervalTracker createConstant(long timeoutInSeconds, int waitIntervalInSeconds) {
        return new WaitIntervalTrackerConstant(timeoutInSeconds, waitIntervalInSeconds);
    }

    public static WaitIntervalTracker createProgressive(long timeoutInSeconds, int maxWaitInSeconds) {
        return new WaitIntervalTrackerProgressive(timeoutInSeconds, maxWaitInSeconds);
    }
}
