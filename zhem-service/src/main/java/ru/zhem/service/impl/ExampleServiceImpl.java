package ru.zhem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zhem.dto.mapper.ExampleMapper;
import ru.zhem.dto.request.ExampleCreationDto;
import ru.zhem.dto.request.ExampleUpdateDto;
import ru.zhem.entity.Example;
import ru.zhem.exception.ExampleNotFoundException;
import ru.zhem.exception.FileInvalidType;
import ru.zhem.repository.ExampleRepository;
import ru.zhem.service.interfaces.ExampleService;
import ru.zhem.service.util.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    private final FileManager fileManager;

    private final ExampleMapper exampleMapper;

    @Override
    @Transactional(rollbackOn = IOException.class)
    public Example createExample(ExampleCreationDto exampleDto) throws IOException {
        String contentType = exampleDto.getImage().getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png").contains(contentType)) {
            throw new FileInvalidType("File must be an image");
        }

        String fileName = this.fileManager.uploadFile(exampleDto.getImage());
        return this.exampleRepository.save(this.exampleMapper.fromCreationDto(exampleDto, fileName));
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void deleteById(long exampleId) throws IOException {
        Example foundedExample = this.exampleRepository.findById(exampleId)
                .orElseThrow(() -> new ExampleNotFoundException("File not found"));
        this.exampleRepository.deleteById(exampleId);
        this.fileManager.deleteFile(foundedExample.getFileName());
    }

    @Override
    public Page<Example> findAllExamples(Pageable pageable) {
        return this.exampleRepository.findAll(pageable);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public Example updateExampleById(long exampleId, ExampleUpdateDto exampleDto) throws IOException {
        Example foundedExample = this.exampleRepository.findById(exampleId)
                .orElseThrow(() -> new ExampleNotFoundException("Example not found"));

        if (exampleDto.getImage() != null) {
            String contentType = exampleDto.getImage().getContentType();
            if (contentType == null || !List.of("image/jpeg", "image/png").contains(contentType)) {
                throw new FileInvalidType("File must be an image");
            }
            String oldFileName = foundedExample.getFileName();
            String newFileName = this.fileManager.uploadFile(exampleDto.getImage());
            foundedExample.setFileName(newFileName);
            this.fileManager.deleteFile(oldFileName);
        }

        if (exampleDto.getTitle() != null && !exampleDto.getTitle().isBlank()) {
            foundedExample.setTitle(exampleDto.getTitle());
        }

        return this.exampleRepository.save(foundedExample);
    }
}
