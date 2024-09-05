/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;

/**
 * Uses '*' and '?' characters for matching, as defined here: <a href="https://commons.apache.org/proper/commons-io/javadocs/api-2.5/org/apache/commons/io/FilenameUtils.html#wildcardMatch(java.lang.String,%20java.lang.String)">FilenameUtils.wildcardMatch</a>
 */
public class ExcludedIncludedWildcardFilter extends ExcludedIncludedFilter {
    /**
     * An empty filter with no excludes or includes.
     */
    public static final ExcludedIncludedWildcardFilter EMPTY = new ExcludedIncludedWildcardFilter(Collections.emptyList(), Collections.emptyList());

    /**
     * Provide a comma-separated string of values to exclude and/or a comma-separated string of values to include. Exclusion rules always win.
     */
    public static ExcludedIncludedWildcardFilter fromCommaSeparatedStrings(String toExclude, String toInclude) {
        return new ExcludedIncludedWildcardFilter(TokenizerUtils.createSetFromString(toExclude), TokenizerUtils.createSetFromString(toInclude));
    }

    /**
     * Provide a collection of values to exclude and/or a collection of values to include. Exclusion rules always win.
     */
    public static ExcludedIncludedWildcardFilter fromCollections(Collection<String> toExclude, Collection<String> toInclude) {
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
