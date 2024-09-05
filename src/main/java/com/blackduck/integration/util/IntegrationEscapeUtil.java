/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.util.ArrayList;
import java.util.List;

public class IntegrationEscapeUtil {
    /**
     * Replaces any/all non-alphanumeric characters with underscores.
     */
    public List<String> replaceWithUnderscore(final List<String> pieces) {
        if (null == pieces || pieces.isEmpty()) {
            return pieces;
        }

        final List<String> escapedPieces = new ArrayList<>(pieces.size());
        for (final String piece : pieces) {
            final String escaped = replaceWithUnderscore(piece);
            escapedPieces.add(escaped);
        }

        return escapedPieces;
    }

    /**
     * Replaces any/all non-alphanumeric characters with underscores.
     */
    public String replaceWithUnderscore(final String s) {
        if (null == s) {
            return null;
        }

        // Replace anything that is not an alphabetic character or a digit with an underscore.
        // See https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html for further details.
        return s.replaceAll("[^\\p{IsAlphabetic}\\p{Digit}]", "_");
    }

}
