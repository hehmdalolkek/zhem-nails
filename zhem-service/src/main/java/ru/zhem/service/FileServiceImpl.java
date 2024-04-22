package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.entity.FileInfo;
import ru.zhem.exception.FileNotFoundException;
import ru.zhem.exception.FileWithDuplicateNameException;
import ru.zhem.repository.FileRepository;
import ru.zhem.service.util.FileManager;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final FileManager fileManager;

    @Transactional(rollbackOn = IOException.class)
    @Override
    public FileInfo upload(MultipartFile resource) throws IOException {
        String suffix = resource
                .getOriginalFilename()
                .substring(resource.getOriginalFilename().lastIndexOf('.'));
        String fileName = UUID.randomUUID() + suffix;
        FileInfo createdFile = FileInfo.builder()
                .name(fileName)
                .type(resource.getContentType())
                .build();
        boolean isExists = this.fileRepository.existsByName(fileName);
        if (isExists) {
            throw new FileWithDuplicateNameException("File is already exists");
        }
        createdFile = this.fileRepository.save(createdFile);
        this.fileManager.upload(resource.getBytes(), fileName);

        return createdFile;
    }

    @Transactional(rollbackOn = IOException.class)
    @Override
    public void deleteFileById(long fileId) throws IOException {
        FileInfo foundedFile = this.fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
        this.fileManager.delete(foundedFile.getName());

        this.fileRepository.delete(foundedFile);
    }

}
