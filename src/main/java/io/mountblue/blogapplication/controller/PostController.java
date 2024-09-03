package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/newpost")
    public String createPost(@ModelAttribute PostDTO postRequestDTO, Model model){
        PostDTO postDTO = postService.savePost(postRequestDTO);
        model.addAttribute("post",postDTO);
        return "post";
    }

    @GetMapping("/newpost")
    public String createPost(){
        return "newpost";
    }

}