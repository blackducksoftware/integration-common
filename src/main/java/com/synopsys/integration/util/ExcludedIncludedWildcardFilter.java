/**
 * integration-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
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

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;

/***
 * ExcludedIncludedWildcardFiler enhances functionality of ExcludedIncludedFilter by allowing users to pass in expressions, using '*' and '?' wildcard characters,
 * that describe a set of Strings according to the implementation of FilenameUtils.wildcardMatch
 * <a href="https://commons.apache.org/proper/commons-io/javadocs/api-2.5/org/apache/commons/io/FilenameUtils.html#wildcardMatch(java.lang.String,%20java.lang.String)">FilenameUtils.wildcardMatch</a>
 */
public class ExcludedIncludedWildcardFilter extends ExcludedIncludedFilter {
    public ExcludedIncludedWildcardFilter(final String toExclude, final String toInclude) {
        super(toExclude, toInclude);
    }

    @Override
    public boolean willExclude(final String itemName) {
        return setContains(itemName, excludedSet, super::willExclude);
    }

    @Override
    public boolean willInclude(final String itemName) {
        return setContains(itemName, includedSet, super::willInclude);
    }

    private boolean setContains(final String itemName, final Set<String> tokenSet, Function<String, Boolean> superMethod) {
        for (String token : tokenSet) {
            if (FilenameUtils.wildcardMatch(itemName, token)) {
                return true;
            }
        }

        return superMethod.apply(itemName);
    }

}
