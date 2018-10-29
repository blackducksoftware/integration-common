package com.synopsys.integration.util.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.synopsys.integration.util.Stringable;
import com.synopsys.integration.util.json.field.ObjectHierarchicalField;
import com.synopsys.integration.util.json.field.StringHierarchicalField;

public class JsonExtractorTest {
    private final Gson gson = new Gson();
    private final JsonExtractor jsonExtractor = new JsonExtractor(gson);

    @Test
    public void createJsonObjectTest() {
        final String key = "key";
        final String value = "value";
        final String json = "{\"" + key + "\":\"" + value + "\"}";
        final JsonObject jsonObject = jsonExtractor.createJsonObject(json);

        final JsonElement element = jsonObject.get(key);
        assertEquals(value, element.getAsString());
    }

    @Test
    public void getValuesFromJsonTest() {
        final String key = "innerField";
        final String value = "thing that I want";
        final String json = "{\"content\":{\"someObject\":{\"" + key + "\":\"" + value + "\"}}}";
        final List<String> pathToField = Arrays.asList("content", "someObject");
        final StringHierarchicalField field = new StringHierarchicalField(pathToField, key, null, null);

        final List<String> values = jsonExtractor.getValuesFromJson(field, json);
        assertEquals(Arrays.asList(value), values);

        final Optional<String> optionalValue = jsonExtractor.getFirstValueFromJson(field, json);
        assertEquals(value, optionalValue.orElse(null));
    }

    @Test
    public void getValuesFromJsonWithArrayTest() {
        final String key = "innerField";
        final String value1 = "thing that I want";
        final String value2 = "other thing that I want";
        final String json = "{\"content\":{\"someObject\":[{\"" + key + "\":\"" + value1 + "\"},{\"" + key + "\":\"" + value2 + "\"}]}}";
        final List<String> pathToField = Arrays.asList("content", "someObject");
        final StringHierarchicalField field = new StringHierarchicalField(pathToField, key, null, null);

        final List<String> values = jsonExtractor.getValuesFromJson(field, json);
        assertEquals(Arrays.asList(value1, value2), values);

        final Optional<String> optionalValue = jsonExtractor.getFirstValueFromJson(field, json);
        assertEquals(value1, optionalValue.orElse(null));
    }

    @Test
    public void getValuesFromJsonWithMissingElementTest() {
        final String json = "{\"key\":\"value\"}";
        final List<String> pathToField = Arrays.asList("content", "someObject");
        final StringHierarchicalField field = new StringHierarchicalField(pathToField, "notKey", null, null);

        final List<String> values = jsonExtractor.getValuesFromJson(field, json);
        assertTrue(values.isEmpty());

        final Optional<String> optionalValue = jsonExtractor.getFirstValueFromJson(field, json);
        assertEquals(false, optionalValue.isPresent());
    }

    @Test
    public void getObjectValuesFromJsonTest() {
        final String fieldValue = "field";
        final DummyInnerObject dummyInnerObject = new DummyInnerObject();
        dummyInnerObject.field = fieldValue;
        final DummyObject dummyObject = new DummyObject();
        dummyObject.innerObject = dummyInnerObject;

        final Type dummyType = new TypeToken<DummyInnerObject>() {}.getType();
        final ObjectHierarchicalField field = new ObjectHierarchicalField(Collections.emptyList(), "innerObject", null, null, dummyType);

        final String json = gson.toJson(dummyObject);
        final Optional<DummyObject> values = jsonExtractor.getFirstValueFromJson(field, json);
        assertEquals(dummyInnerObject, values.orElse(null));
    }

    class DummyObject {
        public DummyInnerObject innerObject;
    }

    class DummyInnerObject extends Stringable {
        public String field;
    }
}
