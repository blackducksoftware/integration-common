package com.synopsys.integration.wait.tracker;

public class WaitIntervalTrackerProgressive implements WaitIntervalTracker {
    private final long timeoutInSeconds;
    private final int maxWaitIntervalSeconds;

    // sequence initializers
    private int prev1 = 1;
    private int prev2 = 0;
    private int now = 1;

    public WaitIntervalTrackerProgressive(long timeoutInSeconds, int maxWaitIntervalSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
        this.maxWaitIntervalSeconds = maxWaitIntervalSeconds;
    }

    @Override
    public long getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    @Override
    public int getNextWaitIntervalInSeconds() {
        now = prev2 + prev1;
        if (now >= maxWaitIntervalSeconds) {
            return maxWaitIntervalSeconds;
        }
        prev2 = prev1;
        prev1 = now;
        return now;
    }
}
