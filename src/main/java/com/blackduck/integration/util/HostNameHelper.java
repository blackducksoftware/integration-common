/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameHelper {
    public static final String DEFAULT_UNKNOWN_HOST_NAME = "unknown-host";

    public static String getMyHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            return DEFAULT_UNKNOWN_HOST_NAME;
        }
    }

    public static String getMyHostName(final String defaultUnknownHostName) {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            return defaultUnknownHostName;
        }
    }

}
