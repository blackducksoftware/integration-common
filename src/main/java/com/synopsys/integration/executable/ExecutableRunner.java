/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.executable;

public interface ExecutableRunner {
    ExecutableOutput execute(Executable executable) throws ExecutableRunnerException;

}
