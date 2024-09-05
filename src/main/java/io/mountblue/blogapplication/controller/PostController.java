package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDTO;
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

    @PostMapping("/{id}")
    public String deletePostbyId(@PathVariable Long id, Model model) {
        postService.deletePostById(id);
        return "redirect:/";
    }

    @PostMapping("/{id}/comment")
    public String addCommentToPost(@ModelAttribute CommentDTO commentDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.addComment(commentDTO, id);
        model.addAttribute("post", postDTO);
        return "post";
    }

    @PostMapping("/{postId}/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        PostDTO postDTO = postService.deleteComment(postId, commentId);
        model.addAttribute("post", postDTO);
        return "post";
    }

}