package me.markoutte.sandbox.profiling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateDirectories {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("dir");
        for (int i = 0; i < 100_000; i++) {
            Files.createDirectories(path);
        }
    }

}
