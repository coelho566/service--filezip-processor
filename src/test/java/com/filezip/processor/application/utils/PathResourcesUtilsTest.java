package com.filezip.processor.application.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PathResourcesUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    public void testGetPathDirectoryExists() {
        try {
            Path existingDir = Paths.get(tempDir.toString(), "existingDir");
            Files.createDirectories(existingDir);

            Path result = PathResourcesUtils.getPath(existingDir.toString());

            assertTrue(Files.exists(result), "Directory should exist");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testGetPathDirectoryDoesNotExist() {
        Path newDir = Paths.get(tempDir.toString(), "newDir");

        Path result = PathResourcesUtils.getPath(newDir.toString());

        assertTrue(Files.exists(result), "Directory should be created");
    }
}