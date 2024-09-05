package com.blackduck.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.log.SilentIntLogger;
import com.blackduck.integration.util.CommonZipExpander;

public class CommonZipExpanderTest {
    private File testDirectory;

    @BeforeEach
    public void init() throws IOException {
        testDirectory = Files.createTempDirectory("unziptesting").toFile();
    }

    @AfterEach
    public void tearDown() throws IOException {
        FileUtils.forceDelete(testDirectory);
    }

    @Test
    public void testZipFileIsExtracted() throws Exception {
        assertEquals(0, Objects.requireNonNull(testDirectory.listFiles()).length);

        final IntLogger logger = new SilentIntLogger();
        final CommonZipExpander commonZipExpander = new CommonZipExpander(logger);
        try (InputStream zipFileStream = getClass().getResourceAsStream("/testArchive.zip")) {
            commonZipExpander.expand(zipFileStream, testDirectory);
        }

        assertEquals(1, Objects.requireNonNull(testDirectory.listFiles()).length);
        final File parent = new File(testDirectory, "parent");
        final File a = new File(parent, "a.txt");
        final File b = new File(parent, "b.txt");
        final File child = new File(parent, "child");
        final File c = new File(child, "c.txt");
        final File d = new File(child, "d.txt");

        assertTrue(parent.isDirectory() && parent.exists());
        assertTrue(child.isDirectory() && child.exists());

        assertEquals(3, Objects.requireNonNull(parent.listFiles()).length);
        assertEquals(2, Objects.requireNonNull(child.listFiles()).length);

        assertTrue(a.isFile() && a.exists());
        assertTrue(b.isFile() && b.exists());
        assertTrue(c.isFile() && c.exists());
        assertTrue(d.isFile() && d.exists());
    }

    @Test
    public void testExpanderClosesZipFile() {
        final ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();

        try {
            final File sourceArchiveFile = createSampleZip();
            final File targetExpansionDirectory = File.createTempFile("dest.zip", null);
            targetExpansionDirectory.deleteOnExit();

            try (PrintStream ps = new PrintStream(errOutputStream, true, "UTF-8")) {
                System.setErr(ps);

                CommonZipExpander expander = new CommonZipExpander(new SilentIntLogger());
                expander.expand(sourceArchiveFile, targetExpansionDirectory);

                System.gc();
                System.runFinalization();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String stderr = new String(errOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(StringUtils.isBlank(stderr), "Nothing should have been written to stderr, but this was: " + stderr);
    }

    private File createSampleZip() throws IOException, ArchiveException {
        final File sourceArchiveFile = File.createTempFile("example.zip", null);
        sourceArchiveFile.deleteOnExit();
        OutputStream archiveStream = new FileOutputStream(sourceArchiveFile);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, archiveStream);

        archive.finish();
        archiveStream.close();

        return sourceArchiveFile;
    }
}
