package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.dto.mapper.FileInfoMapper;
import ru.zhem.exception.FileNotFoundException;
import ru.zhem.exception.FileWithDuplicateNameException;
import ru.zhem.repository.FileRepository;
import ru.zhem.service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/service-api/v1/files")
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    private final FileInfoMapper fileInfoMapper;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile attachment) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.fileInfoMapper.fromEntity(fileService.upload(attachment)));
        } catch (IOException | FileWithDuplicateNameException exception) {
            return ResponseEntity.badRequest()
                    .body(ProblemDetail.forStatusAndDetail(
                            HttpStatus.BAD_REQUEST, exception.getMessage()
                    ));
        }
    }

    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<?> DeleteFileById(@PathVariable("fileId") long fileId) {
        try {
            fileService.deleteFileById(fileId);
            return ResponseEntity.ok()
                    .build();
        } catch (IOException | FileNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProblemDetail.forStatusAndDetail(
                            HttpStatus.NOT_FOUND, exception.getMessage()
                    ));
        }
    }

}
