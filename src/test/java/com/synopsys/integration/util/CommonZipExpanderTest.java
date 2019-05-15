package com.synopsys.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import com.synopsys.integration.log.LogLevel;
import com.synopsys.integration.log.PrintStreamIntLogger;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.SilentIntLogger;

public class CommonZipExpanderTest {
    @Test
    public void testZipFileIsExtracted() throws Exception {
        final Path tempDirectoryPath = Files.createTempDirectory("unziptesting");
        final File tempDirectory = tempDirectoryPath.toFile();
        assertEquals(0, tempDirectory.listFiles().length);

        final IntLogger logger = new SilentIntLogger();
        final CommonZipExpander commonZipExpander = new CommonZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            commonZipExpander.expand(zipFileStream, tempDirectory);
        }

        assertEquals(1, tempDirectory.listFiles().length);
        final File parent = new File(tempDirectory, "parent");
        final File a = new File(parent, "a.txt");
        final File b = new File(parent, "b.txt");
        final File child = new File(parent, "child");
        final File c = new File(child, "c.txt");
        final File d = new File(child, "d.txt");

        assertTrue(parent.isDirectory() && parent.exists());
        assertTrue(child.isDirectory() && child.exists());

        assertEquals(3, parent.listFiles().length);
        assertEquals(2, child.listFiles().length);

        assertTrue(a.isFile() && a.exists());
        assertTrue(b.isFile() && b.exists());
        assertTrue(c.isFile() && c.exists());
        assertTrue(d.isFile() && d.exists());

        FileUtils.deleteDirectory(tempDirectory);
    }

}
