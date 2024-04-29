package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.PostMapper;
import ru.zhem.dto.request.PostCreationDto;
import ru.zhem.entity.Post;
import ru.zhem.service.interfaces.PostService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/posts")
public class PostRestController {

    private final PostService postService;

    private final PostMapper postMapper;

    @LogAnnotation(module = "Post", operation = "Get all posts")
    @GetMapping
    public ResponseEntity<?> findAllPosts(Pageable pageable) {
        return ResponseEntity.ok()
                .body(this.postService.findAllPosts(pageable)
                        .map(this.postMapper::fromEntity));
    }

    @LogAnnotation(module = "Post", operation = "Get post by id")
    @GetMapping("/post/{postId:\\d+}")
    public ResponseEntity<?> findPostById(@PathVariable("postId") long postId) {
        return ResponseEntity.ok()
                .body(this.postMapper.fromEntity(this.postService.findPostById(postId)));

    }

    @LogAnnotation(module = "Post", operation = "Create new post")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid PostCreationDto postDto) throws IOException {
        Post postToCreate = this.postMapper.fromCreationDto(postDto);
        Post createdPost = this.postService.createPost(postToCreate, postDto.getImage());
        return ResponseEntity.created(URI.create("/service-api/v1/posts/post/" + createdPost.getId()))
                .body(this.postMapper.fromEntity(createdPost));

    }

    @LogAnnotation(module = "Post", operation = "Delete post by id")
    @DeleteMapping("/post/{postId:\\d+}")
    public ResponseEntity<?> deletePostById(@PathVariable("postId") long postId) throws IOException {
        this.postService.deleteById(postId);
        return ResponseEntity.ok()
                .build();
    }

}
