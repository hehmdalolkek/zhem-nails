package ru.zhem.service;

import org.springframework.web.multipart.MultipartFile;
import ru.zhem.entity.FileInfo;

import java.io.IOException;

public interface FileService {

    FileInfo upload(MultipartFile resource) throws IOException;

    void deleteFileById(long fileId) throws IOException;
}
