/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.wait;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.exception.IntegrationTimeoutException;

public class BooleanWaitJobCompleter implements WaitJobCompleter<Boolean> {
    @Override
    public Boolean complete() throws IntegrationException {
        return true;
    }

    @Override
    public Boolean handleTimeout() throws IntegrationTimeoutException {
        return false;
    }

}
