package com.synopsys.integration.executable;

import com.synopsys.integration.log.IntLogger;

/**
 * This will log the commands to execute at info level without actually executing anything. Any parameter with 'password' will be masked. Empty output will be returned.
 */
public class DryRunExecutableRunner implements ExecutableRunner {
    private IntLogger logger;

    public DryRunExecutableRunner(IntLogger logger) {
        this.logger = logger;
    }

    @Override
    public ExecutableOutput execute(Executable executable) {
        logger.info(executable.getExecutableDescription());
        return new ExecutableOutput("", "");
    }

}
