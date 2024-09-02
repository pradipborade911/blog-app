package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("post")
@Controller
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping
    @ResponseBody
    public List<PostSummaryDTO> getBlogPosts(){
        return postService.findAllPosts();
    }
    @ResponseBody
    @GetMapping("/{id}")
    public PostDTO getPostbyId(@PathVariable Long id){
        return postService.findPostById(id);
    }


    @PostMapping
    @ResponseBody
    public PostDTO createPost(@ModelAttribute Post post){
        return postService.savePost(post);
    }

    @GetMapping("/new")
    public String createPost(){
        return "new_post";
    }

}