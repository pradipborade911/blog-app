package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.PostTagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
   @Autowired
   PostRepository postRepository;

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
      return null;
   }

   public PostDTO savePost(Post post) {
      post = postRepository.save(post);
      return modelMapper.map(post, PostDTO.class);
   }
}