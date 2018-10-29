package com.synopsys.integration.jsonfield;

import java.util.List;

public class JsonFieldFactory {
    public static HierarchicalField createIntegerJsonField(final List<String> fieldPath) {
        return new HierarchicalField<>(fieldPath, Integer.class);
    }

    public static HierarchicalField createDoublesonField(final List<String> fieldPath) {
        return new HierarchicalField<>(fieldPath, Double.class);
    }

    public static HierarchicalField createBooleanJsonField(final List<String> fieldPath) {
        return new HierarchicalField<>(fieldPath, Boolean.class);
    }

    public static HierarchicalField createStringJsonField(final List<String> fieldPath) {
        return new HierarchicalField<>(fieldPath, String.class);
    }

}
