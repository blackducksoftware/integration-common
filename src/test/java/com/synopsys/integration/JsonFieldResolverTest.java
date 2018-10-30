package com.synopsys.integration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.synopsys.integration.jsonfield.JsonField;
import com.synopsys.integration.jsonfield.JsonFieldFactory;
import com.synopsys.integration.jsonfield.JsonFieldResolver;
import com.synopsys.integration.jsonfield.JsonFieldResult;

public class JsonFieldResolverTest {
    @Test
    public void testResolvingAStringValue() throws IOException {
        final Gson gson = new Gson();
        final JsonFieldResolver jsonFieldResolver = new JsonFieldResolver(gson);

        final List<String> vulnerabilityIdsFieldPath = Arrays.asList("items", "content", "deletedVulnerabilityIds", "vulnerabilityId");
        final JsonField<String> jsonField = JsonFieldFactory.createStringJsonField(vulnerabilityIdsFieldPath);

        final String json = IOUtils.toString(getClass().getResourceAsStream("/test-notification.json"), StandardCharsets.UTF_8);
        final JsonFieldResult<String> jsonFieldResult = jsonFieldResolver.resolveValues(json, jsonField);

        final Set<String> ids = new HashSet<>(jsonFieldResult.getValues());
        final Set<String> expectedIds = new HashSet<>(Arrays.asList("BDSA-2018-3479", "BDSA-2018-2091", "BDSA-2014-0102", "CVE-2016-1000031", "BDSA-2013-0001", "BDSA-2013-0013", "CVE-2016-3092"));
        Assert.assertEquals(expectedIds, ids);
    }

    @Test
    public void testFieldThatDoesNotExist() throws IOException {
        final Gson gson = new Gson();
        final JsonFieldResolver jsonFieldResolver = new JsonFieldResolver(gson);

        final List<String> vulnerabilityIdsFieldPath = Arrays.asList("items", "content", "deletedVulnerabilityIds", "notThere");
        final JsonField<String> jsonField = JsonFieldFactory.createStringJsonField(vulnerabilityIdsFieldPath);

        final String json = IOUtils.toString(getClass().getResourceAsStream("/test-notification.json"), StandardCharsets.UTF_8);
        final JsonFieldResult<String> jsonFieldResult = jsonFieldResolver.resolveValues(json, jsonField);

        Assert.assertNotNull(jsonFieldResult.getJsonObject());
        Assert.assertTrue(jsonFieldResult.getFoundElements().isEmpty());
        Assert.assertTrue(jsonFieldResult.getValues().isEmpty());
    }

    @Test
    public void testBadPath() throws IOException {
        final Gson gson = new Gson();
        final JsonFieldResolver jsonFieldResolver = new JsonFieldResolver(gson);

        final List<String> vulnerabilityIdsFieldPath = Arrays.asList("items", "notThere", "deletedVulnerabilityIds", "notThere");
        final JsonField<String> jsonField = JsonFieldFactory.createStringJsonField(vulnerabilityIdsFieldPath);

        final String json = IOUtils.toString(getClass().getResourceAsStream("/test-notification.json"), StandardCharsets.UTF_8);
        final JsonFieldResult<String> jsonFieldResult = jsonFieldResolver.resolveValues(json, jsonField);

        Assert.assertNotNull(jsonFieldResult.getJsonObject());
        Assert.assertTrue(jsonFieldResult.getFoundElements().isEmpty());
        Assert.assertTrue(jsonFieldResult.getValues().isEmpty());
    }

    @Test
    public void testChangingFoundValues() throws IOException {
        final Gson gson = new Gson();
        final JsonFieldResolver jsonFieldResolver = new JsonFieldResolver(gson);

        final List<String> nameFieldPath = Arrays.asList("projectName");
        final JsonField<String> jsonField = JsonFieldFactory.createStringJsonField(nameFieldPath);

        final String json = IOUtils.toString(getClass().getResourceAsStream("/simple.json"), StandardCharsets.UTF_8);
        Assert.assertTrue(json.replaceAll(" ", "").contains("\"projectName\":\"test\""));
        final JsonFieldResult<String> jsonFieldResult = jsonFieldResolver.resolveValues(json, jsonField);

        jsonFieldResult.getJsonObject().addProperty("projectName", "a new value");
        final String revisedJson = gson.toJson(jsonFieldResult.getJsonObject());
        Assert.assertFalse(revisedJson.replaceAll(" ", "").contains("\"projectName\":\"test\""));
        Assert.assertTrue(revisedJson.replaceAll(" ", "").contains("\"projectName\":\"anewvalue\""));
        System.out.println(revisedJson);
    }

}
