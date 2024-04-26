package ru.zhem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.entity.FileType;
import ru.zhem.entity.ZhemFile;
import ru.zhem.exception.FileInvalidTypeException;
import ru.zhem.repository.ZhemFileRepository;
import ru.zhem.service.interfaces.ZhemFileService;
import ru.zhem.service.util.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZhemFileServiceImpl implements ZhemFileService {

    private final ZhemFileRepository zhemFileRepository;

    private final FileManager fileManager;

    @Override
    @Transactional(rollbackOn = IOException.class)
    public List<ZhemFile> createFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<ZhemFile> files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            FileType fileType = this.getFileType(multipartFile.getContentType());
            String fileName = this.fileManager.uploadFile(multipartFile);

            files.add(ZhemFile.builder()
                    .type(fileType)
                    .path(fileName)
                    .build());
        }

        this.zhemFileRepository.saveAll(files);

        return files;
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public ZhemFile createFile(MultipartFile multipartFile) throws IOException {
        FileType fileType = this.getFileType(multipartFile.getContentType());
        String fileName = this.fileManager.uploadFile(multipartFile);

        ZhemFile file = ZhemFile.builder()
                .type(fileType)
                .path(fileName)
                .build();

        return this.zhemFileRepository.save(file);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void deleteFiles(List<ZhemFile> files) throws IOException {
        for (ZhemFile file : files) {
            this.fileManager.deleteFile(file.getPath());
        }

        this.zhemFileRepository.deleteAll(files);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void deleteFile(ZhemFile file) throws IOException {
        this.fileManager.deleteFile(file.getPath());
        this.zhemFileRepository.delete(file);
    }

    private FileType getFileType(String contentType) {
        if (contentType != null && contentType.startsWith("image/")) {
            return FileType.IMAGE;
        } else if (contentType != null && contentType.startsWith("video/")) {
            return FileType.VIDEO;
        } else {
            throw new FileInvalidTypeException("File type must be an image or video");
        }
    }


}
