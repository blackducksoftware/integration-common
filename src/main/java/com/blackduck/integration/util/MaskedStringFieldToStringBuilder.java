/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
