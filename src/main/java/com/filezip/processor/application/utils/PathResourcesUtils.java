package com.filezip.processor.application.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathResourcesUtils {

    public static Path getPath(String path) {
        try {
            var resourcePath = Paths.get(path);
            if (!Files.exists(resourcePath))
                Files.createDirectories(resourcePath);

            return resourcePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
