package com.synopsys.integration.wait.tracker;

public interface WaitIntervalTracker {

    long getTimeoutInSeconds();
    int getNextWaitIntervalInSeconds();
}
