/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.builder;

import org.apache.commons.lang3.StringUtils;

import com.blackduck.integration.util.Stringable;

public class BuilderPropertyKey extends Stringable {
    public static final String createKey(String key) {
        return StringUtils.trimToEmpty(key).toUpperCase().replaceAll("[^A-Za-z0-9]", "_");
    }

    private final String key;

    public BuilderPropertyKey(String key) {
        this.key = createKey(key);
    }

    public String getKey() {
        return key;
    }

}
