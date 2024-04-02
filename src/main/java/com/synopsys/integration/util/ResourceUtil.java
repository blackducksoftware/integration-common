/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class ResourceUtil {
    public static String getResourceAsString(final Class<?> clazz, final String resourceName, final String encoding) throws IOException {
        return getResourceAsString(clazz, resourceName, Charsets.toCharset(encoding));
    }

    public static String getResourceAsString(final Class<?> clazz, final String resourceName, final Charset encoding) throws IOException {
        final InputStream inputStream = clazz.getResourceAsStream(resourceName);
        if (inputStream != null) {
            return IOUtils.toString(inputStream, encoding);
        }
        return null;
    }

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException e) {
            // ignore
        }
    }
}
