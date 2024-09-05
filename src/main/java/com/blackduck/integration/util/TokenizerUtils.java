/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class TokenizerUtils {
    public static final TokenizerUtils INSTANCE = new TokenizerUtils();

    public static Set<String> createSetFromString(String s) {
        return INSTANCE.createSet(s);
    }

    public static List<String> createListFromString(String s) {
        return INSTANCE.createList(s);
    }

    public Set<String> createSet(String s) {
        return new HashSet<>(createList(s));
    }

    public List<String> createList(String s) {
        List<String> set = new LinkedList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(StringUtils.trimToEmpty(s), ",");
        while (stringTokenizer.hasMoreTokens()) {
            set.add(StringUtils.trimToEmpty(stringTokenizer.nextToken()));
        }
        return set;
    }

}
