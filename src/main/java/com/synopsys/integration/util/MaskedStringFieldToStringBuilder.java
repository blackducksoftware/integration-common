/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
