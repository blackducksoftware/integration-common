package com.synopsys.integration.jsonfield;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonFieldResult<T> {
    private final JsonObject jsonObject;
    private final List<JsonElement> foundElements;
    private final List<T> values;

    public JsonFieldResult(final JsonObject jsonObject, final List<JsonElement> foundElements, final List<T> values) {
        this.jsonObject = jsonObject;
        this.foundElements = foundElements;
        this.values = values;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public List<JsonElement> getFoundElements() {
        return foundElements;
    }

    public List<T> getValues() {
        return values;
    }

}
