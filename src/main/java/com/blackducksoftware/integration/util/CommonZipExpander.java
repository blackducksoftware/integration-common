/**
 * integration-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
package com.blackducksoftware.integration.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.log.IntLogger;

/**
 * Used for expanding a given archive to a target directory. Subclasses can
 * override beforeExpansion and/or afterExpansion as needed for additional
 * processing.
 */
public class CommonZipExpander {
    protected final IntLogger logger;

    public CommonZipExpander(IntLogger logger) {
        this.logger = logger;
    }

    public void expand(File sourceArchiveFile, File targetExpansionDirectory) throws IOException, ArchiveException, IntegrationException {
        beforeExpansion(sourceArchiveFile, targetExpansionDirectory);

        Expander expander = new Expander();
        try {
            expander.expand(sourceArchiveFile, targetExpansionDirectory);
        } catch (IOException | ArchiveException e) {
            logger.error("Couldn't extract the zip file - check the file's permissions: " + e.getMessage());
            throw e;
        }

        afterExpansion(sourceArchiveFile, targetExpansionDirectory);
    }

    public void expand(InputStream sourceArchiveStream, File targetExpansionDirectory) throws IOException, ArchiveException, IntegrationException {
        // it is important to first create the zip file as a stream cannot be
        // unzipped correctly in all cases
        // "If possible, you should always prefer ZipFile over
        // ZipArchiveInputStream."
        // https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile
        File tempZipFile = File.createTempFile("tmpzip", null);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempZipFile)) {
                IOUtils.copy(sourceArchiveStream, fileOutputStream);
            }

            if (!tempZipFile.exists() || tempZipFile.length() <= 0) {
                throw new IntegrationException("The zip file was not created correctly. Please try again.");
            }

            expand(tempZipFile, targetExpansionDirectory);
        } finally {
            FileUtils.deleteQuietly(tempZipFile);
        }
    }

    public void beforeExpansion(File sourceArchiveFile, File targetExpansionDirectory) throws IntegrationException {
    }

    public void afterExpansion(File sourceArchiveFile, File targetExpansionDirectory) throws IntegrationException {
    }

}
