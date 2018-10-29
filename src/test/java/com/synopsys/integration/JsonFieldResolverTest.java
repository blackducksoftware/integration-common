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
import com.synopsys.integration.jsonfield.HierarchicalField;
import com.synopsys.integration.jsonfield.JsonFieldFactory;
import com.synopsys.integration.jsonfield.JsonFieldResolver;

public class JsonFieldResolverTest {
    @Test
    public void testResolvingAStringValue() throws IOException {
        final Gson gson = new Gson();
        final JsonFieldResolver jsonFieldResolver = new JsonFieldResolver(gson);

        final List<String> vulnerabilityIdsFieldPath = Arrays.asList("items", "content", "deletedVulnerabilityIds", "vulnerabilityId");
        final HierarchicalField jsonField = JsonFieldFactory.createStringJsonField(vulnerabilityIdsFieldPath);

        final String json = IOUtils.toString(getClass().getResourceAsStream("/test-notification.json"), StandardCharsets.UTF_8);

        final Set<String> ids = new HashSet<>(jsonFieldResolver.resolveValues(json, jsonField));
        final Set<String> expectedIds = new HashSet<>(Arrays.asList("BDSA-2018-3479", "BDSA-2018-2091", "BDSA-2014-0102", "CVE-2016-1000031", "BDSA-2013-0001", "BDSA-2013-0013", "CVE-2016-3092"));
        Assert.assertEquals(expectedIds, ids);
    }

}
