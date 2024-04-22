package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.mapper.ExampleMapper;
import ru.zhem.dto.request.ExampleCreationDto;
import ru.zhem.dto.request.ExampleUpdateDto;
import ru.zhem.entity.Example;
import ru.zhem.exception.BadRequestException;
import ru.zhem.exception.ExampleNotFoundException;
import ru.zhem.exception.NotFoundException;
import ru.zhem.service.ExampleService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/examples")
public class ExampleRestController {

    private final ExampleService exampleService;

    private final ExampleMapper exampleMapper;

    @GetMapping
    public ResponseEntity<?> findAllExamples(Pageable pageable) {
        return ResponseEntity.ok()
                .body(this.exampleService.findAllExamples(pageable)
                        .map(this.exampleMapper::fromEntity));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createExample(@Valid ExampleCreationDto exampleDto,
                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Example createdExample = this.exampleService
                        .createExample(exampleDto);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(this.exampleMapper.fromEntity(createdExample));
            } catch (IOException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @PatchMapping(path = "/example/{exampleId:\\d+}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateExampleById(@PathVariable("exampleId") long exampleId, @Valid ExampleUpdateDto exampleDto,
                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Example updatedExample = this.exampleService
                        .updateExampleById(exampleId, exampleDto);
                return ResponseEntity.ok()
                        .body(this.exampleMapper.fromEntity(updatedExample));
            } catch (IOException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @DeleteMapping("/example/{exampleId:\\d+}")
    public ResponseEntity<?> deleteExample(@PathVariable("exampleId") long exampleId) {
        try {
            this.exampleService.deleteById(exampleId);
            return ResponseEntity.ok()
                    .build();
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage());
        } catch (ExampleNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
