package ru.zhem.service.interfaces;

import org.springframework.data.domain.Page;
import ru.zhem.dto.request.PostDto;
import ru.zhem.dto.response.PostCreationDto;

public interface PostService {

    Page<PostDto> findAllPosts(int size, int page);

    PostDto findPostById(long postId);

    void createPost(PostCreationDto postDto);

    void deleteById(long postId);

}
