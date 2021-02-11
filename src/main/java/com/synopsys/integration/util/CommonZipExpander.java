/*
 * integration-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;

/**
 * Used for expanding a given archive to a target directory. Subclasses can
 * override beforeExpansion and/or afterExpansion as needed for additional
 * processing.
 */
public class CommonZipExpander {
    protected final IntLogger logger;
    private final Expander expander;

    public CommonZipExpander(IntLogger logger) {
        this(logger, new Expander());
    }

    public CommonZipExpander(IntLogger logger, Expander expander) {
        this.logger = logger;
        this.expander = expander;
    }

    public void expand(final File sourceArchiveFile, final File targetExpansionDirectory) throws IOException, ArchiveException, IntegrationException {
        expandUnknownFile(sourceArchiveFile, targetExpansionDirectory);
    }

    public void expand(final InputStream sourceArchiveStream, final File targetExpansionDirectory) throws IOException, ArchiveException, IntegrationException {
        // it is important to first create the zip file as a stream cannot be
        // unzipped correctly in all cases
        // "If possible, you should always prefer ZipFile over
        // ZipArchiveInputStream."
        // https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile
        final File tempZipFile = File.createTempFile("tmpzip", null);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempZipFile)) {
                IOUtils.copy(sourceArchiveStream, fileOutputStream);
            }

            if (!tempZipFile.exists() || tempZipFile.length() <= 0) {
                throw new IntegrationException(String.format("The zip file (%s) was not created correctly. Please try again.", tempZipFile.getAbsolutePath()));
            }

            expandUnknownFile(tempZipFile, targetExpansionDirectory);
        } finally {
            FileUtils.deleteQuietly(tempZipFile);
        }
    }

    private void expandUnknownFile(File unknownFile, File targetExpansionDirectory) throws IOException, IntegrationException, ArchiveException {
        String format;
        // we need to use an InputStream where inputStream.markSupported() == true
        try (InputStream i = new BufferedInputStream(Files.newInputStream(unknownFile.toPath()))) {
            format = ArchiveStreamFactory.detect(i);
        }

        beforeExpansion(unknownFile, targetExpansionDirectory);

        // in the case of zip files, commons-compress creates, but does not close, the ZipFile. To avoid this unclosed resource, we handle it ourselves.
        try {
            if (ArchiveStreamFactory.ZIP.equals(format)) {
                try (ZipFile zipFile = new ZipFile(unknownFile)) {
                    expander.expand(zipFile, targetExpansionDirectory);
                }
            } else {
                expander.expand(unknownFile, targetExpansionDirectory);
            }
        } catch (IOException | ArchiveException e) {
            logger.error(String.format("Couldn't extract the archive file (%s) - validate the contents and permissions: " + e.getMessage(), unknownFile.getAbsolutePath()));
            throw e;
        }

        afterExpansion(unknownFile, targetExpansionDirectory);
    }

    public void beforeExpansion(final File sourceArchiveFile, final File targetExpansionDirectory) throws IntegrationException {
        // Can be overridden by a subclass for additional processing.
    }

    public void afterExpansion(final File sourceArchiveFile, final File targetExpansionDirectory) throws IntegrationException {
        // Can be overridden by a subclass for additional processing.
    }

}
