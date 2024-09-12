package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.*;
import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.TagRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import io.mountblue.blogapplication.service.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("postService")
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDTO findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        return postDTO;
    }

    @Override
    public PostDTO savePost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("User is not logged in."));

        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setPublishedAt(LocalDateTime.now());
        post.setPublished(true);

        postDTO.getTagsList().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        });

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));

        post.setTitle(postDTO.getTitle());
        post.setExcerpt(postDTO.getExcerpt());
        post.setContent(postDTO.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        post.getTags().clear();
        postDTO.getTagsList().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        });

        Post updatedPost = postRepository.save(post);
        return modelMapper.map(updatedPost, PostDTO.class);
    }

    @Override
    public List<PostSummaryDTO> deletePostById(Long id) {
        postRepository.deleteById(id);
        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO addComment(CommentDTO commentDTO, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));

        Comment comment = modelMapper.map(commentDTO, Comment.class);

        User user = getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("User is not logged in."));

        if(user != null)
            comment.setAuthor(user);

        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPost(post);

        post.addComment(comment);
        Post savedPost = postRepository.save(post);

        return modelMapper.map(savedPost, PostDTO.class);
    }

    @Override
    public PostDTO deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));

        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));

        post.removeComment(comment);
        Post updatedPost = postRepository.save(post);

        return modelMapper.map(updatedPost, PostDTO.class);
    }

    @Override
    public List<String> findAllTags() {
        return tagRepository.findAll().stream().map(Tag::getName).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findAllAuthors() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostSummaryDTO> findAllPosts(PostFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(filterDTO.getPageNumber(), filterDTO.getPageSize(), Sort.by(filterDTO.getOrder().equals("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt"));
        Page<Post> paginatedPosts = postRepository.findAll(pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    @Override
    public Page<PostSummaryDTO> searchQueryPosts(PostFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(filterDTO.getPageNumber(), filterDTO.getPageSize(), Sort.by(filterDTO.getOrder().equals("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt"));
        Page<Post> paginatedPosts = postRepository.searchPosts(filterDTO.getSearchQuery(), pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    @Override
    public Page<PostSummaryDTO> findFilteredPosts(PostFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(filterDTO.getPageNumber(),
                filterDTO.getPageSize(),
                Sort.by(filterDTO.getOrder().equals("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt"));

        Page<Post> paginatedPosts;

        if (filterDTO.getDate() == null && filterDTO.getAuthors() == null && filterDTO.getTags() == null) {
            paginatedPosts = postRepository.findAll(pageable);
        }else {
            paginatedPosts = postRepository.findByAuthorInOrTagsNameInOrCreatedAtBetween(
                    filterDTO.getAuthors(),
                    filterDTO.getTags(),
                    filterDTO.getStartOfDay(),
                    filterDTO.getEndOfDay(),
                    pageable);
        }

        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    private Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return Optional.of((User) authentication.getPrincipal());
        }

        return Optional.empty();
    }

    public boolean isCreator(Long id){
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("User is not logged in."));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        return user.getId() == post.getAuthor().getId();
    }

    public boolean isOwnerOfCommentedPost(Long id){
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("User is not logged in."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found."));

        return comment.getPost().getId() == user.getId();
    }
}
