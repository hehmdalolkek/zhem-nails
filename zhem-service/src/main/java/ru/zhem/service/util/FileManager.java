package ru.zhem.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.exception.EmptyFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileManager {

    @Value("${file.storage.path}")
    private String storageDirectory;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {
            throw new EmptyFileException("File must be not empty");
        }
        String suffix = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.'));
        String fileName = UUID.randomUUID() + suffix;

        Path directoryPath = Paths.get(this.storageDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectory(directoryPath);
        }
        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, multipartFile.getBytes());

        return fileName;
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(this.storageDirectory, fileName);
        Files.deleteIfExists(filePath);
    }
}
