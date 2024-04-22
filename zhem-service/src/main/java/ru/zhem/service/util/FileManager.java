package ru.zhem.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileManager {

    @Value("${file.storage.path}")
    private String storageDirectory;

    public void uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        Path directoryPath = Paths.get(this.storageDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectory(directoryPath);
        }
        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, multipartFile.getBytes());
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(this.storageDirectory, fileName);
        Files.deleteIfExists(filePath);
    }
}
