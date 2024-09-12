package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostDTO savePost(PostDTO postRequestDTO);

    PostDTO findPostById(Long id);

    PostDTO updatePost(Long id, PostDTO postRequestDTO);

    List<PostSummaryDTO> deletePostById(Long id);

    PostDTO addComment(CommentDTO commentDTO, Long postId);

    PostDTO deleteComment(Long postId, Long commentId);

    List<String> findAllTags();

    List<UserDTO> findAllAuthors();

    Page<PostSummaryDTO> findAllPosts(PostFilterDTO filterDTO);

    Page<PostSummaryDTO> searchQueryPosts(PostFilterDTO filterDTO);

    Page<PostSummaryDTO> findFilteredPosts(PostFilterDTO filterDTO);

    public boolean isCreator(Long id);
}
