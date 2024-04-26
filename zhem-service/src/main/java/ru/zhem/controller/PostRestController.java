package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.mapper.PostMapper;
import ru.zhem.dto.request.PostCreationDto;
import ru.zhem.entity.Post;
import ru.zhem.exception.*;
import ru.zhem.service.interfaces.PostService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/posts")
public class PostRestController {

    private final PostService postService;

    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<?> findAllPosts(Pageable pageable) {
        return ResponseEntity.ok()
                .body(this.postService.findAllPosts(pageable)
                        .map(this.postMapper::fromEntity));
    }

    @GetMapping("/post/{postId:\\d+}")
    public ResponseEntity<?> findPostById(@PathVariable("postId") long postId) {
        try {
            return ResponseEntity.ok()
                    .body(this.postMapper.fromEntity(this.postService.findPostById(postId)));
        } catch (PostNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid PostCreationDto postDto,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Post postToCreate = this.postMapper.fromCreationDto(postDto);
                Post createdPost = this.postService.createPost(postToCreate, postDto.getImage());
                return ResponseEntity.created(URI.create("/service-api/v1/posts/post/" + createdPost.getId()))
                        .body(this.postMapper.fromEntity(createdPost));
            } catch (PostNotFoundException exception) {
                throw new NotFoundException(exception.getMessage());
            } catch (IOException | FileInvalidTypeException | EmptyFileException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }

    }

    @DeleteMapping("/post/{postId:\\d+}")
    public ResponseEntity<?> deletePostById(@PathVariable("postId") long postId) {
        try {
            this.postService.deleteById(postId);
            return ResponseEntity.ok()
                    .build();
        } catch (PostNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

}
