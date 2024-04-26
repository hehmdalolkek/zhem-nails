package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.zhem.client.interfaces.PostRestClient;
import ru.zhem.dto.request.PostDto;
import ru.zhem.dto.response.PostCreationDto;
import ru.zhem.exceptions.PostNotFoundException;
import ru.zhem.service.interfaces.PostService;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRestClient postRestClient;

    @Override
    public Page<PostDto> findAllPosts(int size, int page) {
        return this.postRestClient.findAllPosts(size, page);
    }

    @Override
    public PostDto findPostById(long postId) {
        return this.postRestClient.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    @Override
    public void createPost(PostCreationDto postDto) {
        if (postDto.getContent() != null && postDto.getContent().isBlank()) {
            postDto.setContent(null);
        }
        this.postRestClient.createPost(postDto);
    }

    @Override
    public void deleteById(long postId) {
        this.postRestClient.deleteById(postId);
    }
}
