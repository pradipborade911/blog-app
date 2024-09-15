package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.PostDetailsDTO;
import io.mountblue.blogapplication.dto.PostRequestDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @GetMapping
    public String getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Model model) {
        Page<PostSummaryDTO> postsPage = postService.findAllPosts(page, size, sort);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("allAuthors", postService.findAllAuthors());
        model.addAttribute("allTags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort);

        return "blog_posts";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        PostDetailsDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);

        return "view_post";
    }

    @GetMapping("/filter")
    public String filterPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "tags", required = false) List<Long> tags,
            @RequestParam(value = "authors", required = false) List<Long> authors,
            @RequestParam(value = "start_date", required = false, defaultValue = "2000-01-01") LocalDate startDate,
            @RequestParam(value = "end_date", required = false, defaultValue = "2030-01-01") LocalDate endDate,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Model model) {
        Page<PostSummaryDTO> postsPage = postService.findFilteredPosts(page, size, tags, authors, startDate, endDate, sort);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("allAuthors", postService.findAllAuthors());
        model.addAttribute("allTags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("tags", tags);
        model.addAttribute("authors", authors);
        model.addAttribute("sort", sort);
        model.addAttribute("start_date", startDate);
        model.addAttribute("end_date", endDate);

        return "blog_posts";
    }

    @GetMapping("/search")
    public String searchPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "search_query") String searchQuery,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Model model) {
        Page<PostSummaryDTO> postsPage = postService.searchQueryPosts(page, size, searchQuery, sort);
        List<PostSummaryDTO> posts = postsPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("allAuthors", postService.findAllAuthors());
        model.addAttribute("allTags", postService.findAllTags());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort);
        model.addAttribute("search_query", searchQuery);

        return "blog_posts";
    }

    @PostMapping("/newpost")
    public String createPost(@Valid @ModelAttribute PostRequestDTO postRequestDTO, Model model) {
        PostDetailsDTO postDTO = postService.savePost(postRequestDTO);
        return "redirect:/" + postDTO.getId();
    }

    @GetMapping("/newpost")
    public String createPostForm(Model model) {
        model.addAttribute("post", new PostDetailsDTO());
        return "new_post";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN') || @postService.isCreator(#id)")
    public String editPostForm(@PathVariable Long id, Model model) {
        PostDetailsDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        String tags = String.join(", ", postDTO.getTags().stream().map(tagDTO -> tagDTO.getName()).collect(Collectors.toList()));
        model.addAttribute("tags", tags);

        return "edit_post";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN') || @postService.isCreator(#id)")
    public String updatePost(@Valid @ModelAttribute PostRequestDTO postRequestDTO, @PathVariable Long id, Model model) {
        PostDetailsDTO postDTO = postService.updatePost(id, postRequestDTO);

        return "redirect:/" + postDTO.getId();
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') || @postService.isCreator(#id)")
    public String deletePost(@PathVariable Long id, Model model) {
        postService.deletePostById(id);

        return "redirect:/";
    }

}