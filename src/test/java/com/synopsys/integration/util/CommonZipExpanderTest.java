package com.synopsys.integration.util;

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
import java.nio.file.Path;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Test
    public void testExpanderClosesZipFile() {
        final ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();

        try {
            final File sourceArchiveFile = createSampleZip();
            final File targetExpansionDirectory = File.createTempFile("dest.zip", null);

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
        OutputStream archiveStream = new FileOutputStream(sourceArchiveFile);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, archiveStream);

        archive.finish();
        archiveStream.close();

        return sourceArchiveFile;
    }
}
