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

import org.junit.jupiter.api.Test;

public class ExcludedIncludedFilterTest {
    @Test
    public void testConstructor() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));

        excludedIncludedFilter = new ExcludedIncludedFilter(null, null);
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
    }

    @Test
    public void testExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("bad", "");
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));

        excludedIncludedFilter = new ExcludedIncludedFilter("really_bad,also_really_bad", null);
        assertTrue(excludedIncludedFilter.shouldInclude("whatever"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

    @Test
    public void testIncludedAndExcluded() {
        ExcludedIncludedFilter excludedIncludedFilter = new ExcludedIncludedFilter("bad", "good,bad");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertFalse(excludedIncludedFilter.shouldInclude("bad"));

        excludedIncludedFilter = new ExcludedIncludedFilter("really_bad,also_really_bad", "good");
        assertFalse(excludedIncludedFilter.shouldInclude("whatever"));
        assertTrue(excludedIncludedFilter.shouldInclude("good"));
        assertFalse(excludedIncludedFilter.shouldInclude("really_bad"));
        assertFalse(excludedIncludedFilter.shouldInclude("also_really_bad"));
    }

}
