/**
 * integration-common
 *
 * Copyright (c) 2020 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ExcludedIncludedFilter {
    protected final Set<String> excludedSet;
    protected final Set<String> includedSet;

    public ExcludedIncludedFilter() {
        this(Collections.emptyList(), Collections.emptyList());
    }
    /**
     * Provide a comma-separated list of names to exclude and/or a comma-separated list of names to include. Exclusion rules always win.
     */
    public ExcludedIncludedFilter(String toExclude, String toInclude) {
        excludedSet = createSetFromString(toExclude);
        includedSet = createSetFromString(toInclude);
    }
    public ExcludedIncludedFilter(List<String> toExcludeList, List<String> toIncludeList) {
        excludedSet = createSetFromList(toExcludeList);
        includedSet = createSetFromList(toIncludeList);
    }


    public boolean willExclude(String itemName) {
        if (excludedSet.contains(itemName)) {
            return true;
        }
        return false;
    }

    public boolean willInclude(String itemName) {
        if (includedSet.isEmpty() || includedSet.contains(itemName)) {
            return true;
        }
        return false;
    }

    public final boolean shouldInclude(String itemName) {
        if (willExclude(itemName)) {
            return false;
        } else {
            return willInclude(itemName);
        }
    }

    private Set<String> createSetFromString(String s) {
        Set<String> set = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(StringUtils.trimToEmpty(s), ",");
        while (stringTokenizer.hasMoreTokens()) {
            set.add(StringUtils.trimToEmpty(stringTokenizer.nextToken()));
        }
        return set;
    }
    private Set<String> createSetFromList(List<String> list) {
        final Set<String> set = new HashSet<>();
        CollectionUtils.addAll(set, list);
        return set;
    }
}
