/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class TokenizerUtils {
    public static final TokenizerUtils INSTANCE = new TokenizerUtils();

    public static Set<String> createSetFromString(String s) {
        return INSTANCE.createSet(s);
    }

    public Set<String> createSet(String s) {
        Set<String> set = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(StringUtils.trimToEmpty(s), ",");
        while (stringTokenizer.hasMoreTokens()) {
            set.add(StringUtils.trimToEmpty(stringTokenizer.nextToken()));
        }
        return set;
    }

}
