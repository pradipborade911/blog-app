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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @GetMapping
    public String getBlogPosts(
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "page_size", defaultValue = "2") int pageSize,
            @RequestParam(value = "order", defaultValue = "latest") String order,
            Model model) {
        Page<PostSummaryDTO> postsPage = postService.findPaginatedPosts(pageNumber, pageSize, order);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("authors", postService.findAllAuthors());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("order", order);

        return "blog_posts";
    }


    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        return "post";
    }

    @GetMapping("/filter")
    public String getFilteredPosts(@ModelAttribute PostFilterDTO filterDTO,
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "page_size", defaultValue = "2") int pageSize,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "authors", required = false) List<String> authors,
            @RequestParam(value = "order", defaultValue = "latest") String order,
            Model model) {
        List<String> authorslist = postService.findAllAuthors();
        List<String> tagslist = postService.findAllTags();

        if (filterDTO.getDate() == null && (filterDTO.getAuthors() == null || filterDTO.getAuthors().isEmpty()) && (filterDTO.getTags() == null || filterDTO.getTags().isEmpty())) {
            filterDTO.setAuthors(authorslist);
            filterDTO.setTags(tagslist);
        }

        Page<PostSummaryDTO> postsPage = postService.findByAuthorsOrTagsSpec(filterDTO.getAuthors(), filterDTO.getTags(), filterDTO.getPageNumber(), filterDTO.getPageSize(), filterDTO.getOrder());
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("authors", authorslist);
        model.addAttribute("tags", tagslist);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", filterDTO.getPageNumber());
        model.addAttribute("selectedTags", filterDTO.getTags());
        model.addAttribute("selectedAuthors", filterDTO.getAuthors());
        model.addAttribute("order", filterDTO.getOrder());

        return "blog_posts";
    }

    @GetMapping("/search")
    public String searchPosts(
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "page_size", defaultValue = "2") int pageSize,
            @RequestParam(value = "order", defaultValue = "latest") String order,
            @RequestParam(value = "search-query") String searchQuery,
            Model model) {
        List<String> authorslist = postService.findAllAuthors();
        List<String> tagslist = postService.findAllTags();

        Page<PostSummaryDTO> postsPage = postService.searchPaginatedPosts(searchQuery, pageNumber, pageSize, order);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("authors", authorslist);
        model.addAttribute("tags", tagslist);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("order", order);
        model.addAttribute("search_query", searchQuery);

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
    public String editPostById(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        String tags = String.join(", ", postDTO.getTagsList());
        model.addAttribute("tags", tags);
        return "editpost";
    }

    @PostMapping("/{id}/update")
    public String editPostById(@ModelAttribute PostDTO postRequestDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.updatePost(id, postRequestDTO);
        return "redirect:/" + postDTO.getId();
    }

    @PostMapping("/{id}")
    public String deletePostById(@PathVariable Long id, Model model) {
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
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
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