package com.synopsys.integration.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.SilentLogger;

public class CommonZipExpanderTest {
    @Test
    public void testZipFileIsExtracted() throws Exception {
        final Path tempDirectoryPath = Files.createTempDirectory("unziptesting");
        final File tempDirectory = tempDirectoryPath.toFile();
        Assert.assertEquals(0, tempDirectory.listFiles().length);

        final IntLogger logger = new SilentLogger();
        final CommonZipExpander commonZipExpander = new CommonZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            commonZipExpander.expand(zipFileStream, tempDirectory);
        }

        Assert.assertEquals(1, tempDirectory.listFiles().length);
        final File parent = new File(tempDirectory, "parent");
        final File a = new File(parent, "a.txt");
        final File b = new File(parent, "b.txt");
        final File child = new File(parent, "child");
        final File c = new File(child, "c.txt");
        final File d = new File(child, "d.txt");

        Assert.assertTrue(parent.isDirectory() && parent.exists());
        Assert.assertTrue(child.isDirectory() && child.exists());

        Assert.assertEquals(3, parent.listFiles().length);
        Assert.assertEquals(2, child.listFiles().length);

        Assert.assertTrue(a.isFile() && a.exists());
        Assert.assertTrue(b.isFile() && b.exists());
        Assert.assertTrue(c.isFile() && c.exists());
        Assert.assertTrue(d.isFile() && d.exists());

        FileUtils.deleteDirectory(tempDirectory);
    }

}
