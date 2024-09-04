package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Optional<Post> findById(Long aLong);

    @Override
    List<Post> findAll();

}