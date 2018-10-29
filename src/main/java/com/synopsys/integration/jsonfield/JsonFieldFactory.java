package com.synopsys.integration.jsonfield;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class JsonFieldFactory {
    private static final Type INTEGER_TYPE = new TypeToken<Integer>() {}.getType();
    private static final Type DOUBLE_TYPE = new TypeToken<Double>() {}.getType();
    private static final Type BOOLEAN_TYPE = new TypeToken<Boolean>() {}.getType();
    private static final Type STRING_TYPE = new TypeToken<String>() {}.getType();

    public static HierarchicalField createIntegerJsonField(final List<String> fieldPath) {
        return new HierarchicalField(fieldPath, INTEGER_TYPE);
    }

    public static HierarchicalField createDoublesonField(final List<String> fieldPath) {
        return new HierarchicalField(fieldPath, DOUBLE_TYPE);
    }

    public static HierarchicalField createBooleanJsonField(final List<String> fieldPath) {
        return new HierarchicalField(fieldPath, BOOLEAN_TYPE);
    }

    public static HierarchicalField createStringJsonField(final List<String> fieldPath) {
        return new HierarchicalField(fieldPath, STRING_TYPE);
    }

}
