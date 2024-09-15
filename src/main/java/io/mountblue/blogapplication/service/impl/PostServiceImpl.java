package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.*;
import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.exception.UserNotAuthenticatedException;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service("postService")
public class PostServiceImpl implements PostService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDetailsDTO findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));

        PostDetailsDTO postDTO = modelMapper.map(post, PostDetailsDTO.class);
        return postDTO;
    }

    @Override
    public PostDetailsDTO savePost(PostRequestDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new UserNotAuthenticatedException("User is not logged in."));

        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setPublishedAt(LocalDateTime.now());

        if (post.isPublished()) {
            post.setPublishedAt(LocalDateTime.now());
        }

        postDTO.getTagsList().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        });

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDetailsDTO.class);
    }

    @Override
    public PostDetailsDTO updatePost(Long id, PostRequestDTO postRequestDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));

        existingPost.setTitle(postRequestDTO.getTitle());
        existingPost.setExcerpt(postRequestDTO.getExcerpt());
        existingPost.setContent(postRequestDTO.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        existingPost.getTags().clear();
        postRequestDTO.getTagsList().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> new Tag(tagName));
            existingPost.addTag(tag);
        });

        Post updatedPost = postRepository.save(existingPost);
        return modelMapper.map(updatedPost, PostDetailsDTO.class);
    }

    @Override
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }

        postRepository.deleteById(id);
    }


    @Override
    public PostDetailsDTO addComment(CommentRequestDTO commentRequestDTO, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));

        Comment comment = modelMapper.map(commentRequestDTO, Comment.class);

        Optional<User> user = getAuthenticatedUser();

        if (user.isPresent()) {
            comment.setAuthor(user.get());
            comment.setEmail(user.get().getEmail());
            comment.setName(user.get().getFullName());
        } else {
            User guest = userRepository.findById(8L).orElseThrow();
            comment.setAuthor(guest);
        }

        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPost(post);
        commentRepository.save(comment);

        post.addComment(comment);
        Post savedPost = postRepository.save(post);

        return modelMapper.map(savedPost, PostDetailsDTO.class);
    }

    @Override
    public Set<TagDTO> findAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    public List<UserDTO> findAllAuthors() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostSummaryDTO> findAllPosts(int pageNumber, int pageSize, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.equalsIgnoreCase("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt"));
        Page<Post> paginatedPosts = postRepository.findAll(pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    @Override
    public Page<PostSummaryDTO> searchQueryPosts(int pageNumber, int pageSize, String searchQuery, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.equalsIgnoreCase("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt"));
        Page<Post> paginatedPosts = postRepository.searchPosts(searchQuery, pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    @Override
    public Page<PostSummaryDTO> findFilteredPosts(int pageNumber, int pageSize, List<Long> tags, List<Long> authors, LocalDate startDate, LocalDate endDate, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(order.equalsIgnoreCase("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt"));

        Page<Post> paginatedPosts;

        if (tags == null && authors == null && startDate == null && endDate == null) {
            paginatedPosts = postRepository.findAll(pageable);
        } else {
            paginatedPosts = postRepository.findByAuthorInOrTagsNameInOrCreatedAtBetween(
                    tags,
                    authors,
                    startDate.atStartOfDay(),
                    endDate.plusDays(1).atStartOfDay(),
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

    public boolean isCreator(Long id) {
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new UserNotAuthenticatedException("User is not logged in."));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        return user.getId() == post.getAuthor().getId();
    }

}
