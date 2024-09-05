package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.log.SilentIntLogger;
import com.blackduck.integration.util.CleanupZipExpander;
import com.blackduck.integration.util.CommonZipExpander;

public class CleanupZipExpanderTest {
    private File testDirectory;

    @BeforeEach
    public void init() throws IOException {
        testDirectory = Files.createTempDirectory("unziptesting").toFile();

        final File extraDirectory = new File(testDirectory, "extra");
        final File extraChild = new File(extraDirectory, "child");
        final File anotherExtraDirectory = new File(testDirectory, "anotherextra");
        final File aFile = new File(testDirectory, "safe");

        assertTrue(extraChild.mkdirs());
        assertTrue(anotherExtraDirectory.mkdirs());
        assertTrue(aFile.createNewFile());
        assertEquals(3, Objects.requireNonNull(testDirectory.listFiles()).length);
    }

    @AfterEach
    public void tearDown() throws IOException {
        FileUtils.forceDelete(testDirectory);
    }

    @Test
    public void testOtherDirectoriesAreDeleted() throws Exception {
        final IntLogger logger = new SilentIntLogger();
        final CommonZipExpander zipExpander = new CleanupZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            zipExpander.expand(zipFileStream, testDirectory);
        }

        assertEquals(2, Objects.requireNonNull(testDirectory.listFiles()).length);
    }

    @Test
    public void testEverythingDeleted() throws Exception {
        final IntLogger logger = new SilentIntLogger();
        final CommonZipExpander zipExpander = new CleanupZipExpander(logger, true);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            zipExpander.expand(zipFileStream, testDirectory);
        }

        assertEquals(1, Objects.requireNonNull(testDirectory.listFiles()).length);
    }

}
