/**
 * integration-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
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

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ExcludedIncludedFilter {
    private final Set<String> excludedSet;
    private final Set<String> includedSet;
    private final Pattern regexToExclude;
    private final Pattern regexToInclude;

    /**
     * Provide a comma-separated list of names to exclude and/or a comma-separated list of names to include. Exclusion rules always win.
     */

    public ExcludedIncludedFilter(final String toExclude, final String toInclude) {
        this(toExclude, toInclude, null, null);
    }

    /**
     * Provide a comma-separated list of names to exclude and/or a comma-separated list of names to include.  In addition, may provide a regular
     * expression describing a pattern of names to exclude and/or a pattern of names to include. Exclusion rules always win.
     */
    public ExcludedIncludedFilter(final String toExclude, final String toInclude, String regexToExclude, String regexToInclude) {
        excludedSet = createSetFromString(toExclude);
        includedSet = createSetFromString(toInclude);
        this.regexToExclude = regexToExclude != null ? Pattern.compile(regexToExclude) : Pattern.compile("");
        this.regexToInclude = regexToInclude != null ? Pattern.compile(regexToInclude) : Pattern.compile("");
    }

    public boolean shouldInclude(final String itemName) {
        if (excludedSet.contains(itemName)) {
            return false;
        }

        if (regexToExclude.matcher(itemName).matches()) {
            return false;
        }

        if (regexToInclude.pattern().length() > 0 && !regexToInclude.matcher(itemName).matches()) {
            return false;
        }

        return (includedSet.isEmpty() || includedSet.contains(itemName));
    }

    private Set<String> createSetFromString(final String s) {
        final Set<String> set = new HashSet<>();
        final StringTokenizer stringTokenizer = new StringTokenizer(StringUtils.trimToEmpty(s), ",");
        while (stringTokenizer.hasMoreTokens()) {
            set.add(StringUtils.trimToEmpty(stringTokenizer.nextToken()));
        }
        return set;
    }

}
