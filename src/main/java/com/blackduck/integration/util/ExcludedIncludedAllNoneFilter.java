/*
 * integration-common
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcludedIncludedAllNoneFilter extends ExcludedIncludedFilter {
    public static final String ALL = "ALL";
    public static final String NONE = "NONE";

    private final boolean allNoneSpecified;
    private final boolean excludeAll;

    public ExcludedIncludedAllNoneFilter(Collection<String> toExcludeList, Collection<String> toIncludeList, boolean lenient) {
        super(toExcludeList, toIncludeList, lenient);

        Set<String> excludingAllOrNone = reduceToAllOrNone(toExcludeList);
        Set<String> includingAllOrNone = reduceToAllOrNone(toIncludeList);

        if (excludingAllOrNone.contains(ALL) || includingAllOrNone.contains(NONE)) {
            allNoneSpecified = true;
            excludeAll = true;
        } else if (excludingAllOrNone.contains(NONE) || includingAllOrNone.contains(ALL)) {
            allNoneSpecified = true;
            excludeAll = false;
        } else {
            allNoneSpecified = false;
            excludeAll = false;
        }
    }

    public ExcludedIncludedAllNoneFilter(Collection<String> toExcludeList, Collection<String> toIncludeList) {
        this(toExcludeList, toIncludeList, true);
    }

    private Set<String> reduceToAllOrNone(Collection<String> list) {
        return list
                   .stream()
                   .filter(s -> ALL.equalsIgnoreCase(s) || NONE.equalsIgnoreCase(s))
                   .collect(Collectors.toSet());
    }

    @Override
    public boolean willExclude(String itemName) {
        if (allNoneSpecified) {
            return excludeAll;
        } else {
            return super.willExclude(itemName);
        }
    }

    @Override
    public boolean willInclude(String itemName) {
        if (allNoneSpecified) {
            return !excludeAll;
        } else {
            return super.willInclude(itemName);
        }
    }

}
