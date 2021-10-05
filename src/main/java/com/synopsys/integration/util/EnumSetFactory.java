/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

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
        if ("ALL".equalsIgnoreCase(enumValues)) {
            return EnumSet.allOf(enumClass);
        } else if ("NONE".equalsIgnoreCase(enumValues)) {
            return EnumSet.noneOf(enumClass);
        } else {
            return EnumSet.copyOf(EnumUtils.parseCommaDelimitted(enumValues, enumClass));
        }
    }

    public EnumSet<E> parse(ExcludedIncludedFilter excludedIncludedFilter) {
        if (1 == excludedIncludedFilter.excludedSet.size() && excludedIncludedFilter.includedSet.isEmpty()) {
            String excludedValue = excludedIncludedFilter.excludedSet.iterator().next();
            if ("ALL".equalsIgnoreCase(excludedValue)) {
                return EnumSet.noneOf(enumClass);
            } else if ("NONE".equalsIgnoreCase(excludedValue)) {
                return EnumSet.allOf(enumClass);
            }
        }

        if (1 == excludedIncludedFilter.includedSet.size() && excludedIncludedFilter.excludedSet.isEmpty()) {
            String includedValue = excludedIncludedFilter.includedSet.iterator().next();
            if ("ALL".equalsIgnoreCase(includedValue)) {
                return EnumSet.allOf(enumClass);
            } else if ("NONE".equalsIgnoreCase(includedValue)) {
                return EnumSet.noneOf(enumClass);
            }
        }

        Set<String> namesToInclude = EnumSet.allOf(enumClass)
            .stream()
            .map(Enum::name)
            .filter(excludedIncludedFilter::shouldInclude)
            .collect(Collectors.toSet());

        return EnumSet.copyOf(EnumUtils.convert(namesToInclude, enumClass));
    }

}
