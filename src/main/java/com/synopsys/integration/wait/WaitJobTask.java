/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import com.synopsys.integration.exception.IntegrationException;

public interface WaitJobTask {
    boolean isComplete() throws IntegrationException;

}
