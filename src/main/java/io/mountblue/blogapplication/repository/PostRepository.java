package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.author u " +
            "WHERE (:query IS NULL OR :query = '' " +
            "OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.excerpt) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Post> searchPosts(@Param("query") String query, Pageable pageable);

    @Query("SELECT DISTINCT post FROM Post post " +
            "LEFT JOIN post.tags tags " +
            "LEFT JOIN post.author author " +
            "WHERE (:authors IS NULL OR author.id IN :authors )" +
            "AND (:tags IS NULL OR tags.id IN :tags) " +
            "AND (post.createdAt BETWEEN :startOfDay AND :endOfDay)")
    Page<Post> findByAuthorInOrTagsNameInOrCreatedAtBetween(
            @Param("tags") List<Long> tags,
            @Param("authors") List<Long> authors,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable);
}