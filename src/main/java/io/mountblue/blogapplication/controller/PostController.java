package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping
    public String getBlogPosts(Model model) {
        List<PostSummaryDTO> posts = postService.findAllPosts();
        model.addAttribute("posts", posts);
        return "blog_posts";
    }

    @GetMapping("/{id}")
    public String getPostbyId(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        return "post";
    }


    @PostMapping("/newpost")
    public String createPost(@ModelAttribute PostDTO postRequestDTO, Model model) {
        PostDTO postDTO = postService.savePost(postRequestDTO);
        model.addAttribute("post", postDTO);
        return "post";
    }

    @GetMapping("/newpost")
    public String createPost() {
        return "newpost";
    }

    @GetMapping("/{id}/edit")
    public String editPostbyId(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        String tags = String.join(", ", postDTO.getTagsList());
        model.addAttribute("tags", tags);
        return "editpost";
    }

    @PostMapping("/{id}/update")
    public String editPostbyId(@ModelAttribute PostDTO postRequestDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.updatePost(id, postRequestDTO);
        model.addAttribute("post", postDTO);
        return "post";
    }

}