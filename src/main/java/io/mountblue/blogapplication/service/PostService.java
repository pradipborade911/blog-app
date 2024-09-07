package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<PostSummaryDTO> findAllPosts();

    PostDTO findPostById(Long id);

    PostDTO savePost(PostDTO postRequestDTO);

    PostDTO updatePost(Long id, PostDTO postRequestDTO);

    List<PostSummaryDTO> deletePostById(Long id);

    PostDTO addComment(CommentDTO commentDTO, Long postId);

    PostDTO deleteComment(Long postId, Long commentId);

    List<PostSummaryDTO> findAllPostsByAuthor(String author);

    List<String> findAllTags();

    Page<PostSummaryDTO> findByAuthorsOrTags(List<String> authors, List<String> tags, int pageNumber, int pageSize);

    List<String> findAllAuthors();

    Page<PostSummaryDTO> findPaginatedPosts(int pageNumber, int pageSize);
}
