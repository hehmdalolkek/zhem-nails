package ru.zhem.service.interfaces;

import org.springframework.data.domain.Page;
import ru.zhem.dto.request.ExampleDto;
import ru.zhem.dto.response.ExampleCreationDto;

public interface ExampleService {

    Page<ExampleDto> findAllExamples(int size, int page);

    void createExample(ExampleCreationDto exampleDto);

    void deleteById(long exampleId);

}
