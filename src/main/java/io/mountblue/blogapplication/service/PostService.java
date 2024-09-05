package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;

import java.util.List;

public interface PostService {
    List<PostSummaryDTO> findAllPosts();

    PostDTO findPostById(Long id);

    PostDTO savePost(PostDTO postRequestDTO);

    PostDTO updatePost(Long id, PostDTO postRequestDTO);

    List<PostSummaryDTO> deletePostById(Long id);
}
