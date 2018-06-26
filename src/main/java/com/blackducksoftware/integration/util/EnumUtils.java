package com.blackducksoftware.integration.util;

import org.apache.commons.text.WordUtils;

public class EnumUtils {
    public static String prettyPrint(final Enum<?> enumValue) {
        final String name = enumValue.name();
        final String prettyName = WordUtils.capitalizeFully(name.toLowerCase().replace("_", " "));
        return prettyName;
    }

}
