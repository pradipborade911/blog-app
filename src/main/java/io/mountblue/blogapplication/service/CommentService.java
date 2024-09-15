package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.CommentDetailsDTO;
import io.mountblue.blogapplication.dto.CommentRequestDTO;
import io.mountblue.blogapplication.dto.PostDetailsDTO;

public interface CommentService {
    CommentDetailsDTO getCommentById(Long id);

    Long updateComment(Long id, CommentRequestDTO commentRequestDTO);

    CommentDetailsDTO addComment(Long postId, CommentRequestDTO commentDTO);

    PostDetailsDTO deleteComment(Long postId, Long commentId);
}
