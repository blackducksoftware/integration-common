/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import org.apache.commons.lang3.SystemUtils;

public enum OperatingSystemType {
    LINUX,
    MAC,
    WINDOWS,
    ALPINE_LINUX;

    public static OperatingSystemType determineFromSystem() {
        if (SystemUtils.IS_OS_MAC) {
            return MAC;
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return WINDOWS;
        } else {
            return LINUX;
        }
    }

    public String prettyPrint() {
        return EnumUtils.prettyPrint(this);
    }

}
