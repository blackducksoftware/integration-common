/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

import org.apache.commons.lang3.SystemUtils;

public enum OperatingSystemType {
    LINUX,
    MAC,
    WINDOWS;

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
