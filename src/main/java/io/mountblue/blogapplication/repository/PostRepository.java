package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Override
    Optional<Post> findById(Long aLong);

    @Override
    Page<Post> findAll(Pageable pageable);

    List<Post> findAllByAuthor(String author);

    @Query("SELECT DISTINCT post.author FROM Post post")
    List<String> findAllAuthors();

    List<Post> findByTags_Name(String tagName);

    @Query("SELECT DISTINCT post FROM Post post LEFT JOIN post.tags tags " +
            "WHERE post.author IN :authors OR tags.name IN :tags")
    Page<Post> findByAuthorsOrTags(@Param("authors") List<String> authors, @Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE (:query IS NULL OR :query = '' " +
            "OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.excerpt) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.author) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Post> searchPosts(@Param("query") String query, Pageable pageable);

    @Query("SELECT DISTINCT post FROM Post post LEFT JOIN post.tags tags " +
            "WHERE post.author IN :authors " +
            "OR tags.name IN :tags " +
            "OR post.createdAt BETWEEN :startOfDay AND :endOfDay")
    Page<Post> findByAuthorInOrTagsNameInOrCreatedAtBetween(
            @Param("authors") List<String> authors,
            @Param("tags") List<String> tags,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable);

    Page<Post> findAll(Specification specs, Pageable pageable);

}