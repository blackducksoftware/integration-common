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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;

/**
 * Uses '*' and '?' characters for matching, as defined here: <a href="https://commons.apache.org/proper/commons-io/javadocs/api-2.5/org/apache/commons/io/FilenameUtils.html#wildcardMatch(java.lang.String,%20java.lang.String)">FilenameUtils.wildcardMatch</a>
 */
public class ExcludedIncludedWildcardFilter extends ExcludedIncludedFilter {
    /**
     * Construct a filter with no excludes or includes.
     */
    public static ExcludedIncludedFilter CREATE_EMPTY() {
        return new ExcludedIncludedWildcardFilter(Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Provide a comma-separated string of values to exclude and/or a comma-separated string of values to include. Exclusion rules always win.
     */
    public static ExcludedIncludedFilter CREATE_FROM_COMMA_SEPARATED_STRINGS(String toExclude, String toInclude) {
        return new ExcludedIncludedWildcardFilter(TokenizerUtils.createSetFromString(toExclude), TokenizerUtils.createSetFromString(toInclude));
    }

    /**
     * Provide a collection of values to exclude and/or a collection of values to include. Exclusion rules always win.
     */
    public static ExcludedIncludedFilter CREATE_FROM_COLLECTIONS(Collection<String> toExclude, Collection<String> toInclude) {
        return new ExcludedIncludedWildcardFilter(toExclude, toInclude);
    }

    protected ExcludedIncludedWildcardFilter(final Collection<String> toExcludeList, final Collection<String> toIncludeList) {
        super(toExcludeList, toIncludeList);
    }

    @Override
    public boolean willExclude(String itemName) {
        return setContains(itemName, excludedSet, super::willExclude);
    }

    @Override
    public boolean willInclude(String itemName) {
        return setContains(itemName, includedSet, super::willInclude);
    }

    private boolean setContains(String itemName, Set<String> tokenSet, Predicate<String> superMethod) {
        for (String token : tokenSet) {
            if (FilenameUtils.wildcardMatch(itemName, token)) {
                return true;
            }
        }

        return superMethod.test(itemName);
    }

}
