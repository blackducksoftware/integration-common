/**
 * integration-common
 *
 * Copyright (c) 2020 Synopsys, Inc.
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Can be used to mask java.lang.String fields of Stringable instances.
 */
public class MaskedStringFieldToStringBuilder extends ReflectionToStringBuilder {
    public static final String MASKED_VALUE = "************************";

    private Set<String> stringFieldNamesToMask = new HashSet<>();

    public MaskedStringFieldToStringBuilder(Object object, String... stringFieldNamesToMask) {
        super(object, ToStringStyle.JSON_STYLE);
        this.stringFieldNamesToMask.addAll(Arrays.asList(stringFieldNamesToMask));
    }

    @Override
    protected Object getValue(Field field) throws IllegalAccessException {
        if (stringFieldNamesToMask.contains(field.getName()) && field.getType().equals(String.class)) {
                String value = (String)super.getValue(field);
                if (StringUtils.isNotBlank(value)) {
                    return MASKED_VALUE;
                }
        }
        return super.getValue(field);
    }

}
