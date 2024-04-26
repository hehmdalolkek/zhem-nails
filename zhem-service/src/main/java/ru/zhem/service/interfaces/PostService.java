package ru.zhem.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.entity.Post;

import java.io.IOException;

public interface PostService {

    Page<Post> findAllPosts(Pageable pageable);

    Post findPostById(long postId);

    Post createPost(Post post, MultipartFile image) throws IOException;

    void deleteById(long postId) throws IOException;

}
