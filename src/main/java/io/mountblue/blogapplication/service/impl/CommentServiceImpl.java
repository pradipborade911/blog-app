package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + id + " does not exist"));
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public Long updateComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment does not exist"));
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setComment(commentDTO.getComment());

        commentRepository.save(comment);

        return comment.getPost().getId();
    }
}
