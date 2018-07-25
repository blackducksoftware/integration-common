package com.blackducksoftware.integration.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.blackducksoftware.integration.test.tool.TestLogger;

public class CleanupZipExpanderTest {
    @Test
    public void testOtherDirectoriesAreDeleted() throws Exception {
        File tempDirectory = setupFiles();

        TestLogger logger = new TestLogger();
        CommonZipExpander zipExpander = new CleanupZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            zipExpander.extract(zipFileStream, tempDirectory);
        }

        Assert.assertEquals(2, tempDirectory.listFiles().length);

        FileUtils.deleteDirectory(tempDirectory);
    }

    @Test
    public void testEverythingDeleted() throws Exception {
        File tempDirectory = setupFiles();

        TestLogger logger = new TestLogger();
        CommonZipExpander zipExpander = new CleanupZipExpander(logger, true);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            zipExpander.extract(zipFileStream, tempDirectory);
        }

        Assert.assertEquals(1, tempDirectory.listFiles().length);

        FileUtils.deleteDirectory(tempDirectory);
    }

    private File setupFiles() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("unziptesting");
        File tempDirectory = tempDirectoryPath.toFile();
        Assert.assertEquals(0, tempDirectory.listFiles().length);

        File extraDirectory = new File(tempDirectory, "extra");
        File extraChild = new File(extraDirectory, "child");
        File anotherExtraDirectory = new File(tempDirectory, "anotherextra");
        File aFile = new File(tempDirectory, "safe");

        extraChild.mkdirs();
        anotherExtraDirectory.mkdirs();
        aFile.createNewFile();
        Assert.assertEquals(3, tempDirectory.listFiles().length);

        return tempDirectory;
    }

}
