package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;

public interface CommentService {
    CommentDTO getCommentById(Long id);

    Long updateComment(CommentDTO commentDTO);
}
