package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostFilterDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
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

    List<String> findAllAuthors();

    Page<PostSummaryDTO> findAllPosts(PostFilterDTO filterDTO);

    Page<PostSummaryDTO> searchQueryPosts(PostFilterDTO filterDTO);

    Page<PostSummaryDTO> findFilteredPosts(PostFilterDTO filterDTO);
}
