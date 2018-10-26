package com.synopsys.integration.util.json;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.google.gson.Gson;
import com.synopsys.integration.util.json.field.BooleanHierarchicalField;
import com.synopsys.integration.util.json.field.DoubleHierarchicalField;
import com.synopsys.integration.util.json.field.IntegerHierarchicalField;
import com.synopsys.integration.util.json.field.StringHierarchicalField;

public class JsonInjectorTest {
    private final Gson gson = new Gson();
    private final JsonInjector jsonInjector = new JsonInjector(gson);

    @Test
    public void insertBooleanValueTest() {
        final String key = "testKey";
        final Boolean originalValue = Boolean.FALSE;
        final Boolean newValue = Boolean.TRUE;
        final String originalJson = createJson(key, originalValue, null);
        final BooleanHierarchicalField field = new BooleanHierarchicalField(Collections.emptyList(), key, null, null);

        final String modifiedJson = jsonInjector.insertValue(field, originalJson, newValue);
        assertEquals(createJson(key, newValue, null), modifiedJson);
    }

    @Test
    public void insertDoubleValueTest() {
        final String key = "testKey";
        final Double originalValue = Double.valueOf(1.5);
        final Double newValue = Double.valueOf(2.5);
        final String originalJson = createJson(key, originalValue, null);
        final DoubleHierarchicalField field = new DoubleHierarchicalField(Collections.emptyList(), key, null, null);

        final String modifiedJson = jsonInjector.insertValue(field, originalJson, newValue);
        assertEquals(createJson(key, newValue, null), modifiedJson);
    }

    @Test
    public void insertIntegerValueTest() {
        final String key = "testKey";
        final Integer originalValue = Integer.valueOf(1);
        final Integer newValue = Integer.valueOf(2);
        final String originalJson = createJson(key, originalValue, null);
        final IntegerHierarchicalField field = new IntegerHierarchicalField(Collections.emptyList(), key, null, null);

        final String modifiedJson = jsonInjector.insertValue(field, originalJson, newValue);
        assertEquals(createJson(key, newValue, null), modifiedJson);
    }

    @Test
    public void insertStringValueTest() {
        final Character surroundingChar = '"';

        final String key = "testKey";
        final String originalValue = "old";
        final String newValue = "new";
        final String originalJson = createJson(key, originalValue, surroundingChar);
        final StringHierarchicalField field = new StringHierarchicalField(Collections.emptyList(), key, null, null);

        final String modifiedJson = jsonInjector.insertValue(field, originalJson, newValue);
        assertEquals(createJson(key, newValue, surroundingChar), modifiedJson);
    }

    private String createJson(final String key, final Object value, final Character surroundingCharacter) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{\"exampleKey\":\"exampleValue\",\"");
        builder.append(key);
        builder.append("\":");
        if (surroundingCharacter != null) {
            builder.append(surroundingCharacter);
        }
        builder.append(value);
        if (surroundingCharacter != null) {
            builder.append(surroundingCharacter);
        }
        builder.append("}");
        return builder.toString();
    }
}
