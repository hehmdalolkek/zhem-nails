package ru.zhem.client.interfaces;

import org.springframework.data.domain.Page;
import ru.zhem.dto.request.PostDto;
import ru.zhem.dto.response.PostCreationDto;

import java.util.Optional;

public interface PostRestClient {

    Page<PostDto> findAllPosts(int size, int page);

    Optional<PostDto> findPostById(long postId);

    void createPost(PostCreationDto postDto);

    void deleteById(long postId);

}
