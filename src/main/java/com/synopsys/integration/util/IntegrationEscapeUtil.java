/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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

        final String escaped = s.replaceAll("[^A-Za-z0-9]", "_");
        return escaped;
    }

}
