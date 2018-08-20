package com.synopsys.integration.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.SilentLogger;

public class CleanupZipExpanderTest {
    @Test
    public void testOtherDirectoriesAreDeleted() throws Exception {
        final File tempDirectory = setupFiles();

        final IntLogger logger = new SilentLogger();
        final CommonZipExpander zipExpander = new CleanupZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            zipExpander.expand(zipFileStream, tempDirectory);
        }

        Assert.assertEquals(2, tempDirectory.listFiles().length);

        FileUtils.deleteDirectory(tempDirectory);
    }

    @Test
    public void testEverythingDeleted() throws Exception {
        final File tempDirectory = setupFiles();

        final IntLogger logger = new SilentLogger();
        final CommonZipExpander zipExpander = new CleanupZipExpander(logger, true);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            zipExpander.expand(zipFileStream, tempDirectory);
        }

        Assert.assertEquals(1, tempDirectory.listFiles().length);

        FileUtils.deleteDirectory(tempDirectory);
    }

    private File setupFiles() throws IOException {
        final Path tempDirectoryPath = Files.createTempDirectory("unziptesting");
        final File tempDirectory = tempDirectoryPath.toFile();
        Assert.assertEquals(0, tempDirectory.listFiles().length);

        final File extraDirectory = new File(tempDirectory, "extra");
        final File extraChild = new File(extraDirectory, "child");
        final File anotherExtraDirectory = new File(tempDirectory, "anotherextra");
        final File aFile = new File(tempDirectory, "safe");

        extraChild.mkdirs();
        anotherExtraDirectory.mkdirs();
        aFile.createNewFile();
        Assert.assertEquals(3, tempDirectory.listFiles().length);

        return tempDirectory;
    }

}
