package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.request.PostCreationDto;
import ru.zhem.dto.response.PostDto;
import ru.zhem.entity.Post;

@Component
public class PostMapper {

    public Post fromCreationDto(PostCreationDto postDto) {
        return Post.builder()
                .content(postDto.getContent())
                .build();
    }

    public PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .image(post.getImage())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
