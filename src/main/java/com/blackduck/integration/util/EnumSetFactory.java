/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumSetFactory<E extends Enum<E>> {
    private final Class<E> enumClass;

    public EnumSetFactory(Class<E> enumClass) {
        this.enumClass = enumClass;

        Set<String> names = EnumSet.allOf(enumClass)
                                .stream()
                                .map(Enum::name)
                                .map(String::toUpperCase)
                                .collect(Collectors.toSet());

        if (names.contains("ALL") || names.contains("NONE")) {
            throw new IllegalArgumentException("EnumSetFactory can not support an enum with 'all' or 'none' as values.");
        }
    }

    public EnumSet<E> parseIncludedValues(String enumValues) {
        Set<String> valuesToInclude = TokenizerUtils.createSetFromString(enumValues);
        if (valuesToInclude.isEmpty()) {
            return EnumSet.noneOf(enumClass);
        }

        ExcludedIncludedAllNoneFilter filter = new ExcludedIncludedAllNoneFilter(Collections.emptySet(), valuesToInclude, false);
        return parse(filter);
    }

    public EnumSet<E> parse(ExcludedIncludedFilter excludedIncludedFilter) {
        Set<String> namesToInclude = EnumSet.allOf(enumClass)
                                         .stream()
                                         .map(Enum::name)
                                         .filter(excludedIncludedFilter::shouldInclude)
                                         .collect(Collectors.toSet());

        if (namesToInclude.isEmpty()) {
            return EnumSet.noneOf(enumClass);
        }

        return EnumSet.copyOf(EnumUtils.convert(namesToInclude, enumClass));
    }

}
