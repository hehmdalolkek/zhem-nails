package ru.zhem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.dto.request.ExampleCreationDto;
import ru.zhem.dto.request.ExampleUpdateDto;
import ru.zhem.dto.response.ExampleDto;
import ru.zhem.entity.Example;

import java.io.IOException;
import java.util.Collection;

public interface ExampleService {

    Example createExample(ExampleCreationDto exampleDto) throws IOException;

    void deleteById(long exampleId) throws IOException;

    Page<Example> findAllExamples(Pageable pageable);

    Example updateExampleById(long exampleId, ExampleUpdateDto exampleDto) throws IOException;
}
