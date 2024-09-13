package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostFilterDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

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

        return "view_post";
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

        return "view_post";
    }

    @GetMapping("/newpost")
    public String createPostForm(Model model){
        model.addAttribute("post", new PostDTO());
        return "new_post";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("@postService.isCreator(#id) || hasRole('ADMIN')")
    public String editPostForm(@PathVariable Long id, Model model) {
        PostDTO postDTO = postService.findPostById(id);
        model.addAttribute("post", postDTO);
        String tags = String.join(", ", postDTO.getTags().stream().map(tagDTO -> tagDTO.getName()).collect(Collectors.toList()));
        model.addAttribute("tags", tags);

        return "edit_post";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("@postService.isCreator(#id) || hasRole('ADMIN')")
    public String updatePost(@ModelAttribute PostDTO postRequestDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.updatePost(id, postRequestDTO);

        return "redirect:/" + postDTO.getId();
    }

    @PostMapping("/{id}")
    @PreAuthorize("@postService.isCreator(#id) || hasRole('ADMIN')")
    public String deletePost(@PathVariable Long id, Model model) {
        postService.deletePostById(id);

        return "redirect:/";
    }

}