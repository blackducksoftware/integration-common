/**
 * integration-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
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
package com.synopsys.integration.util;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;

/**
 * This will delete any/all directories in the expansion directory prior to
 * expanding.
 */
public class CleanupZipExpander extends CommonZipExpander {
    private final boolean alsoDeleteFiles;

    public CleanupZipExpander(final IntLogger logger) {
        this(logger, false);
    }

    /**
     * If alsoDeleteFiles is true then files will be deleted as well as
     * directories in the expansion directory prior to expanding.
     */
    public CleanupZipExpander(final IntLogger logger, final boolean alsoDeleteFiles) {
        super(logger);
        this.alsoDeleteFiles = alsoDeleteFiles;
    }

    @Override
    public void beforeExpansion(final File sourceArchiveFile, final File targetExpansionDirectory) throws IntegrationException {
        final File[] toDelete = targetExpansionDirectory.listFiles(file -> file.isDirectory() || alsoDeleteFiles);
        if (toDelete != null && toDelete.length > 0) {
            logger.warn(String.format(
                    "There were items in %s that are being deleted. This may happen under normal conditions, but please do not place items in the expansion directory as this directory is assumed to be under the integration's control.",
                    targetExpansionDirectory.getAbsolutePath()));
            Arrays.stream(toDelete).forEach(FileUtils::deleteQuietly);
        }
    }

}
