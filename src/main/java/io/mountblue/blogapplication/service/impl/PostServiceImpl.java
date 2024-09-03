package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.TagRepository;
import io.mountblue.blogapplication.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<PostSummaryDTO> findAllPosts() {
        List<PostSummaryDTO> postsList = new ArrayList<>();

        postsList = postRepository.findAll()
                .stream().map(post -> modelMapper.map(post, PostSummaryDTO.class)).collect(Collectors.toList());

        System.out.println(postsList);
        return postsList;
    }

    public PostDTO findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("failed to find post by id"));
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        return postDTO;
    }

    public PostDTO savePost(PostDTO postRequestDTO) {
        Post post = modelMapper.map(postRequestDTO, Post.class);

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        for (String tagName : postRequestDTO.getTagsList()) {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        }

        Post savedPost = postRepository.save(post);

        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        for (Tag tag : savedPost.getTags()) {
            postDTO.addTag(tag.getName());
        }

        return postDTO;
    }

}