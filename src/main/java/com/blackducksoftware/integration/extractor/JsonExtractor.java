package com.blackducksoftware.integration.extractor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonExtractor {

    private final Gson gson;

    public JsonExtractor(final Gson gson) {
        this.gson = gson;
    }

    public <T> T extractObject(final JsonElement jsonString, final Class<T> clazz) {
        final T object = gson.fromJson(jsonString, clazz);
        return object;
    }

    public <T> T extractObject(final String jsonString, final Class<T> clazz) {
        final T object = gson.fromJson(jsonString, clazz);
        return object;
    }
}
