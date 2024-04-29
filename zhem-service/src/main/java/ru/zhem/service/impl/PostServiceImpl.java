package ru.zhem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.entity.Post;
import ru.zhem.entity.ZhemFile;
import ru.zhem.common.exception.FileInvalidTypeException;
import ru.zhem.common.exception.PostNotFoundException;
import ru.zhem.repository.PostRepository;
import ru.zhem.service.interfaces.PostService;
import ru.zhem.service.interfaces.ZhemFileService;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final ZhemFileService zhemFileService;

    private final PostRepository postRepository;


    @Override
    @Transactional
    public Page<Post> findAllPosts(Pageable pageable) {
        return this.postRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Post findPostById(long postId) {
        return this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public Post createPost(Post post, MultipartFile image) throws IOException {
        String contentType = image.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new FileInvalidTypeException("File must be an image");
        }

        ZhemFile savedImage = this.zhemFileService.createFile(image);
        post.setImage(savedImage);
        return this.postRepository.save(post);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void deleteById(long postId) throws IOException {
        Post foundedPost = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        this.postRepository.delete(foundedPost);
        this.zhemFileService.deleteFile(foundedPost.getImage());
    }
}
