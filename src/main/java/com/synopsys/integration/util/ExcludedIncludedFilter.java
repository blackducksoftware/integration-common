/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

/**
 * This filter makes it possible to choose the smallest set for filtering:
 * either the items to include or items to exclude from the full set. Exclusion
 * rules always win.
 *
 * If you provide no Strings to either list, nothing will be filtered. This
 * default can be overridden by setting lenient = false.
 */
public class ExcludedIncludedFilter {
    public static final ExcludedIncludedFilter EMPTY = new ExcludedIncludedFilter(Collections.emptyList(), Collections.emptyList());

    protected final Set<String> excludedSet;
    protected final Set<String> includedSet;
    protected final boolean lenient;

    /**
     * Provide a comma-separated string of values to exclude and/or a
     * comma-separated string of values to include.
     */
    public static ExcludedIncludedFilter fromCommaSeparatedStrings(String toExclude, String toInclude) {
        return new ExcludedIncludedFilter(TokenizerUtils.createSetFromString(toExclude), TokenizerUtils.createSetFromString(toInclude));
    }

    public ExcludedIncludedFilter(Collection<String> toExcludeList, Collection<String> toIncludeList) {
        this(toExcludeList, toIncludeList, true);
    }

    /**
     * Provide a collection of values to exclude and/or a collection of values
     * to include. Exclusion rules always win.
     *
     * If lenient = true (default case), no filtering will occur and
     * all items will be included.
     *
     * If lenient = false, unless items are specifically included, they
     * will be excluded.
     */
    public ExcludedIncludedFilter(Collection<String> toExcludeList, Collection<String> toIncludeList, boolean lenient) {
        excludedSet = new HashSet<>(toExcludeList);
        includedSet = new HashSet<>(toIncludeList);
        this.lenient = lenient;
    }

    public boolean willExclude(String itemName) {
        if (excludedSet.contains(itemName)) {
            return true;
        }
        return false;
    }

    public boolean willInclude(String itemName) {
        if (includedSet.isEmpty()) {
            return lenient;
        }

        return includedSet.contains(itemName);
    }

    public final boolean shouldInclude(String itemName) {
        if (willExclude(itemName)) {
            return false;
        } else {
            return willInclude(itemName);
        }
    }

}
