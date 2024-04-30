package ru.zhem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.PostMapper;
import ru.zhem.dto.request.PostCreationDto;
import ru.zhem.dto.response.PostDto;
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

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all posts",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Page.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Post", operation = "Get all posts")
    @GetMapping
    public ResponseEntity<?> findAllPosts(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok()
                .body(this.postService.findAllPosts(pageable)
                        .map(this.postMapper::fromEntity));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find post by id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = PostDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Post", operation = "Get post by id")
    @GetMapping("/post/{postId:\\d+}")
    public ResponseEntity<?> findPostById(@PathVariable("postId") long postId) {
        return ResponseEntity.ok()
                .body(this.postMapper.fromEntity(this.postService.findPostById(postId)));

    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = PostCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created post",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = PostDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Post", operation = "Create new post")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid PostCreationDto postDto) throws IOException {
        Post postToCreate = this.postMapper.fromCreationDto(postDto);
        Post createdPost = this.postService.createPost(postToCreate, postDto.getImage());
        return ResponseEntity.created(URI.create("/service-api/v1/posts/post/" + createdPost.getId()))
                .body(this.postMapper.fromEntity(createdPost));

    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted post"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Post", operation = "Delete post by id")
    @DeleteMapping("/post/{postId:\\d+}")
    public ResponseEntity<?> deletePostById(@PathVariable("postId") long postId) throws IOException {
        this.postService.deleteById(postId);
        return ResponseEntity.ok()
                .build();
    }

}
