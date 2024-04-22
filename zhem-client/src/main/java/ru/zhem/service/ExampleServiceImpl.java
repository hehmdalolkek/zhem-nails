package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.zhem.client.ExampleRestClient;
import ru.zhem.dto.request.ExampleDto;
import ru.zhem.dto.response.ExampleCreationDto;
import ru.zhem.dto.response.ExampleUpdateDto;
import ru.zhem.exceptions.FileInvalidType;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRestClient exampleRestClient;

    @Override
    public Page<ExampleDto> findAllExamples(int size, int page) {
        return this.exampleRestClient.findAllExamples(size, page);
    }

    @Override
    public void createExample(ExampleCreationDto exampleDto) {
        String contentType = exampleDto.getImage().getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png").contains(contentType)) {
            throw new FileInvalidType("Файл должен быть изображением jpeg/png");
        }
        this.exampleRestClient.createExample(exampleDto);
    }

    @Override
    public void deleteById(long exampleId) {
        this.exampleRestClient.deleteById(exampleId);
    }
}
