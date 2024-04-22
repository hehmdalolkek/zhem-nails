package ru.zhem.service;

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
import ru.zhem.repository.ExampleRepository;
import ru.zhem.service.util.FileManager;

import java.io.IOException;
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
        String suffix = exampleDto.getImage()
                .getOriginalFilename()
                .substring(exampleDto.getImage().getOriginalFilename().lastIndexOf('.'));
        String fileName = UUID.randomUUID() + suffix;
        Example createdExample =
                this.exampleRepository.save(this.exampleMapper.fromCreationDto(exampleDto, fileName));
        this.fileManager.uploadFile(exampleDto.getImage(), fileName);

        return createdExample;
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
            String oldFileName = foundedExample.getFileName();
            String suffix = exampleDto.getImage()
                    .getOriginalFilename()
                    .substring(exampleDto.getImage().getOriginalFilename().lastIndexOf('.'));
            String newFileName = UUID.randomUUID() + suffix;
            foundedExample.setFileName(newFileName);
            this.fileManager.deleteFile(oldFileName);
            this.fileManager.uploadFile(exampleDto.getImage(), newFileName);
        }

        if (exampleDto.getTitle() != null && !exampleDto.getTitle().isBlank()) {
            foundedExample.setTitle(exampleDto.getTitle());
        }

        return this.exampleRepository.save(foundedExample);
    }
}
