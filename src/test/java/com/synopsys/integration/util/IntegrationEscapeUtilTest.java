/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
 *******************************************************************************/
package com.synopsys.integration.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class IntegrationEscapeUtilTest {
    @Test
    public void testEscapingForUri() {
        final IntegrationEscapeUtil integrationEscapeUtil = new IntegrationEscapeUtil();
        assertEquals(null, integrationEscapeUtil.escapeForUri(null));
        assertEquals("", integrationEscapeUtil.escapeForUri(""));
        assertEquals("_a_b_c_1_2___3", integrationEscapeUtil.escapeForUri("!a$b:c@1 2-- 3"));

        final List<String> messyStrings = Arrays.asList(new String[] { "#A(B)C++=", "def", "~\tgh1<>23*i.." });
        final List<String> cleanStrings = Arrays.asList(new String[] { "_A_B_C___", "def", "__gh1__23_i__" });
        assertEquals(cleanStrings, integrationEscapeUtil.escapePiecesForUri(messyStrings));
    }
}
