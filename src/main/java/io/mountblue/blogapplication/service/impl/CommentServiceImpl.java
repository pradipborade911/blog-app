package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.CommentDetailsDTO;
import io.mountblue.blogapplication.dto.CommentRequestDTO;
import io.mountblue.blogapplication.dto.PostDetailsDTO;
import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.exception.UserNotAuthenticatedException;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import io.mountblue.blogapplication.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CommentDetailsDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + id + " does not exist"));
        return modelMapper.map(comment, CommentDetailsDTO.class);
    }

    @Override
    public Long updateComment(Long id, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));

        comment.setUpdatedAt(LocalDateTime.now());
        comment.setComment(commentRequestDTO.getComment());

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getPost().getId();
    }

    @Override
    public CommentDetailsDTO addComment(Long postId, CommentRequestDTO commentCreateDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));

        Comment comment = modelMapper.map(commentCreateDTO, Comment.class);

        System.out.println(comment);

        Optional<User> user = getAuthenticatedUser();

        if (user.isPresent()) {
            comment.setAuthor(user.get());
            comment.setEmail(user.get().getEmail());
            comment.setName(user.get().getFullName());
        } else {
            User guest = userRepository.findById(8L).orElseThrow(); //GuestUserId = 8L;
            comment.setAuthor(guest);
        }

        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        return modelMapper.map(savedComment, CommentDetailsDTO.class);
    }

    @Override
    public PostDetailsDTO deleteComment(Long postId, Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment with id " + commentId + " not found");
        }
        commentRepository.deleteById(commentId);
        return modelMapper.map(postRepository.findById(postId), PostDetailsDTO.class);
    }

    public boolean isCreator(Long id) {
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("User is not logged in."));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found."));
        return user.getId() == comment.getAuthor().getId();
    }

    public boolean isOwnerOfPost(Long id) {
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new UserNotAuthenticatedException("User is not logged in."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
        System.out.println("pending");
        boolean returnvalue = comment.getPost().getAuthor().getId() == user.getId();
        System.out.println("done");
        return returnvalue;
    }

    private Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return Optional.of((User) authentication.getPrincipal());
        }

        return Optional.empty();
    }
}
