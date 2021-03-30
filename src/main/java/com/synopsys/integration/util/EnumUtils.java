/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class EnumUtils {
    public static String prettyPrint(Enum<?> enumValue) {
        String name = enumValue.name();
        String prettyName = WordUtils.capitalizeFully(name.replace("_", " "));
        return prettyName;
    }

    public static <T extends Enum<T>> List<T> parseCommaDelimitted(String commaDelimittedEnumString, Class<T> enumClass) {
        return Arrays
                       .stream(commaDelimittedEnumString.split(","))
                       .map(String::trim)
                       .filter(StringUtils::isNotBlank)
                       .map(token -> Enum.valueOf(enumClass, token))
                       .collect(Collectors.toList());
    }

    public static <T extends Enum<T>> List<T> convert(List<String> values, Class<T> enumClass) {
        return values
                       .stream()
                       .map(String::trim)
                       .filter(StringUtils::isNotBlank)
                       .map(token -> Enum.valueOf(enumClass, token))
                       .collect(Collectors.toList());
    }

}
