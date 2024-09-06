package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Optional<Post> findById(Long aLong);

    @Override
    List<Post> findAll();

    List<Post>  findAllByAuthor(String author);

    @Query("SELECT DISTINCT post.author FROM Post post")
    List<String> findAllAuthors();

    List<Post> findByTags_Name(String tagName);

    @Query("SELECT DISTINCT post FROM Post post LEFT JOIN post.tags tags " +
            "WHERE post.author IN :authors OR tags.name IN :tags")
    List<Post> findByAuthorsOrTags(@Param("authors") List<String> authors, @Param("tags") List<String> tags);

}