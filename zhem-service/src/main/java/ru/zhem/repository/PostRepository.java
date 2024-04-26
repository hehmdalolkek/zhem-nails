package ru.zhem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    @NonNull
    @EntityGraph(value = "post_entity-graph")
    Page<Post> findAll(Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(value = "post_entity-graph")
    Optional<Post> findById(Long aLong);
}
