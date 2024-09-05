/*
 * integration-common
 *
 * Copyright (c) 2024 Blackduck, Inc.
 *
 * Use subject to the terms and conditions of the Blackduck End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.util;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.exception.IntegrationException;

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
