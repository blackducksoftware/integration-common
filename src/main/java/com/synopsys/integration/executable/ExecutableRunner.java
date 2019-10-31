package com.synopsys.integration.executable;

public interface ExecutableRunner {
    ExecutableOutput execute(Executable executable) throws ExecutableRunnerException;

}
