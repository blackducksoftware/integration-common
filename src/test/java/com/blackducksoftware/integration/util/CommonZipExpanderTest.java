package com.blackducksoftware.integration.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.blackducksoftware.integration.test.tool.TestLogger;

public class CommonZipExpanderTest {
    @Test
    public void testZipFileIsExtracted() throws Exception {
        Path tempDirectoryPath = Files.createTempDirectory("unziptesting");
        File tempDirectory = tempDirectoryPath.toFile();
        Assert.assertEquals(0, tempDirectory.listFiles().length);

        TestLogger logger = new TestLogger();
        CommonZipExpander commonZipExpander = new CommonZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            commonZipExpander.expand(zipFileStream, tempDirectory);
        }

        Assert.assertEquals(1, tempDirectory.listFiles().length);
        File parent = new File(tempDirectory, "parent");
        File a = new File(parent, "a.txt");
        File b = new File(parent, "b.txt");
        File child = new File(parent, "child");
        File c = new File(child, "c.txt");
        File d = new File(child, "d.txt");

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
