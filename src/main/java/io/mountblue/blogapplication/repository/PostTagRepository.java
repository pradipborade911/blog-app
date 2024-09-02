package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Override
    List<PostTag> findAll();
}
