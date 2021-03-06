/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class ExcludedIncludedFilter {
    /**
     * An empty filter with no excludes or includes.
     */
    public static final ExcludedIncludedFilter EMPTY = new ExcludedIncludedFilter(Collections.emptyList(), Collections.emptyList());

    protected final Set<String> excludedSet;
    protected final Set<String> includedSet;

    /**
     * Provide a comma-separated string of values to exclude and/or a comma-separated string of values to include. Exclusion rules always win.
     */
    public static ExcludedIncludedFilter fromCommaSeparatedStrings(String toExclude, String toInclude) {
        return new ExcludedIncludedFilter(TokenizerUtils.createSetFromString(toExclude), TokenizerUtils.createSetFromString(toInclude));
    }

    /**
     * Provide a collection of values to exclude and/or a collection of values to include. Exclusion rules always win.
     */
    public static ExcludedIncludedFilter fromCollections(Collection<String> toExclude, Collection<String> toInclude) {
        return new ExcludedIncludedFilter(toExclude, toInclude);
    }

    protected ExcludedIncludedFilter(Collection<String> toExcludeList, Collection<String> toIncludeList) {
        excludedSet = new HashSet<>(toExcludeList);
        includedSet = new HashSet<>(toIncludeList);
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

}
