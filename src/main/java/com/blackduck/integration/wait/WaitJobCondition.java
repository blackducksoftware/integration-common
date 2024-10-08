/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.wait;

import com.blackduck.integration.exception.IntegrationException;

@FunctionalInterface
public interface WaitJobCondition {
    boolean isComplete() throws IntegrationException;

}
