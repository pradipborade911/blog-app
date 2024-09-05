package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.TagRepository;
import io.mountblue.blogapplication.service.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PostSummaryDTO> findAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("failed to find post by id"));

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        for (Tag tag : post.getTags()) {
            postDTO.addTag(tag.getName());
        }
        return postDTO;
    }

    @Override
    public PostDTO savePost(PostDTO postRequestDTO) {
        Post post = modelMapper.map(postRequestDTO, Post.class);

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        for (String tagName : postRequestDTO.getTagsList()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        }

        Post savedPost = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        for (Tag tag : savedPost.getTags()) {
            postDTO.addTag(tag.getName());
        }

        return postDTO;
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postRequestDTO) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find Post with this id"));

        post.setTitle(postRequestDTO.getTitle());
        post.setExcerpt(postRequestDTO.getExcerpt());
        post.setContent(postRequestDTO.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        post.getTags().clear();
        for (String tagName : postRequestDTO.getTagsList()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        }

        Post savedPost = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        for (Tag tag : savedPost.getTags()) {
            postDTO.addTag(tag.getName());
        }

        return postDTO;
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
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post dosen't exists"));

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPost(post);

        post.addComment(comment);
        Post savedPost = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        return postDTO;
    }

    @Override
    public PostDTO deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post dosen't exists"));
        Comment comment = post.getComments().stream()
                .filter(comment1 -> comment1.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The requested comment was not found for this post."
                ));

        post.removeComment(comment);
        post = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        return postDTO;
    }
}
