package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostFilterDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    CommentService commentService;

    @GetMapping
    public String getAllPosts(@ModelAttribute PostFilterDTO filterDTO, Model model) {
        Page<PostSummaryDTO> postsPage = postService.findAllPosts(filterDTO);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("authors", postService.findAllAuthors());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", filterDTO.getPageNumber());
        model.addAttribute("order", filterDTO.getOrder());

        return "blog_posts";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);

        return "post";
    }

    @GetMapping("/filter")
    public String filterPosts(@ModelAttribute PostFilterDTO filterDTO, Model model) {
        Page<PostSummaryDTO> postsPage = postService.findFilteredPosts(filterDTO);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("authors", postService.findAllAuthors());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", filterDTO.getPageNumber());
        model.addAttribute("selectedTags", filterDTO.getTags());
        model.addAttribute("selectedAuthors", filterDTO.getAuthors());
        model.addAttribute("order", filterDTO.getOrder());
        model.addAttribute("date", filterDTO.getDate());

        return "blog_posts";
    }

    @GetMapping("/search")
    public String searchPosts(@ModelAttribute PostFilterDTO filterDTO, Model model) {
        Page<PostSummaryDTO> postsPage = postService.searchQueryPosts(filterDTO);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("authors", postService.findAllAuthors());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", filterDTO.getPageNumber());
        model.addAttribute("order", filterDTO.getOrder());
        model.addAttribute("search_query", filterDTO.getSearchQuery());

        return "blog_posts";
    }

    @PostMapping("/newpost")
    public String createPost(@ModelAttribute PostDTO postRequestDTO, Model model) {
        PostDTO postDTO = postService.savePost(postRequestDTO);
        model.addAttribute("post", postDTO);

        return "post";
    }

    @GetMapping("/newpost")
    public String createPostForm() {
        return "newpost";
    }

    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        String tags = String.join(", ", postDTO.getTagsList());
        model.addAttribute("tags", tags);

        return "editpost";
    }

    @PostMapping("/{id}/update")
    public String updatePost(@ModelAttribute PostDTO postRequestDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.updatePost(id, postRequestDTO);

        return "redirect:/" + postDTO.getId();
    }

    @PostMapping("/{id}")
    public String deletePost(@PathVariable Long id, Model model) {
        postService.deletePostById(id);

        return "redirect:/";
    }

    @PostMapping("/{id}/addComment")
    public String addComment(@ModelAttribute CommentDTO commentDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.addComment(commentDTO, id);
        model.addAttribute("post", postDTO);

        return "redirect:/{id}";
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        PostDTO postDTO = postService.deleteComment(postId, commentId);

        return "redirect:/" + postDTO.getId();
    }

    @GetMapping("/{postId}/comments/{commentId}/edit")
    public String editCommentForm(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        CommentDTO commentDTO = commentService.getCommentById(commentId);
        model.addAttribute("comment", commentDTO);

        return "editcomment";
    }
}