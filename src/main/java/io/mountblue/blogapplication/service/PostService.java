package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PostService {
    PostDetailsDTO savePost(PostRequestDTO postRequestDTO);

    PostDetailsDTO findPostById(Long id);

    PostDetailsDTO updatePost(Long id, PostRequestDTO postRequestDTO);

    void deletePostById(Long id);

    PostDetailsDTO addComment(CommentRequestDTO commentDTO, Long postId);

    Set<TagDTO> findAllTags();

    List<UserDTO> findAllAuthors();

    Page<PostSummaryDTO> findAllPosts(int pageNumber, int pageSize, String order);

    Page<PostSummaryDTO> searchQueryPosts(int pageNumber, int pageSize, String searchQuery, String order);

    Page<PostSummaryDTO> findFilteredPosts(int pageNumber, int pageSize, List<Long> tags, List<Long> authors, LocalDate startDate, LocalDate endDate, String order);

    boolean isCreator(Long id);
}
