package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.service.CommentService;
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

    @Autowired
    CommentService commentService;

    @GetMapping
    public String getBlogPosts(Model model) {
        List<PostSummaryDTO> posts = postService.findAllPosts();
        List<String> authorslist = postService.findAllAuthors();
        List<String> tagslist = postService.findAllTags();
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authorslist);
        model.addAttribute("tags", tagslist);
        return "blog_posts";
    }

    @GetMapping("/{id}")
    public String getPostbyId(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        return "post";
    }

    @PostMapping("/filter")
    public String getFilteredPosts(@RequestParam(value = "tags", required = false) List<String> tags, @RequestParam(value = "author", required = false) List<String> authors, Model model) {
        List<PostSummaryDTO> posts = postService.findByAuthorsOrTags(authors, tags);
        List<String> authorslist = postService.findAllAuthors();
        List<String> tagslist = postService.findAllTags();
        model.addAttribute("posts", posts);
        model.addAttribute("authors", authorslist);
        model.addAttribute("tags", tagslist);
        return "blog_posts";
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
        return "redirect/" + postDTO.getId();
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
        return "redirect:/" + postDTO.getId();
    }

    @GetMapping("/{postId}/{commentId}/edit")
    public String editComment(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        CommentDTO commentDTO = commentService.getCommentById(commentId);
        model.addAttribute("comment", commentDTO);
        return "editcomment";
    }

}