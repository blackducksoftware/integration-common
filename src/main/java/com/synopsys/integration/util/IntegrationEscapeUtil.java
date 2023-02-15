/*
 * integration-common
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

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

        return s.replaceAll("[^\\p{IsAlphabetic}\\p{Digit}]", "_");
    }

}
