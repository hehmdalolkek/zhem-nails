package ru.zhem.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileManager {

    @Value("${upload.path}")
    private String uploadPath;

    public void upload(byte[] resource, String fileName) throws IOException {
        Path directory = Paths.get(this.uploadPath);

        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }

        Path path = Paths.get(this.uploadPath, fileName);
        Path file = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(file.toString())) {
            stream.write(resource);
        }
    }

    public void delete(String fileName) throws IOException {
        Path path = Paths.get(this.uploadPath, fileName);
        Files.delete(path);
    }
}
