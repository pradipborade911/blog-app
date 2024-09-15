package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDetailsDTO;
import io.mountblue.blogapplication.dto.CommentRequestDTO;
import io.mountblue.blogapplication.dto.PostDetailsDTO;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    PostService postService;

    @PostMapping("/comments/{commentId}/update")
    @PreAuthorize("@commentService.isCreator(#commentId) || @commentService.isOwnerOfPost(#commentId) || hasRole('ADMIN')")
    public String updateComment(@Valid @PathVariable Long commentId, @ModelAttribute CommentRequestDTO commentRequestDTO) {
        Long postId = commentService.updateComment(commentId, commentRequestDTO);

        return "redirect:/" + postId;
    }

    @PostMapping("/{id}/addComment")
    public String addComment(@Valid @ModelAttribute CommentRequestDTO commentRequestDTO, @PathVariable Long id, Model model) {
        PostDetailsDTO postDetailDTO = postService.addComment(commentRequestDTO, id);
        model.addAttribute("post", postDetailDTO);

        return "redirect:/{id}";
    }

    @GetMapping("/{postId}/comments/{commentId}/edit")
    @PreAuthorize("hasRole('ADMIN') || @commentService.isCreator(#commentId) || @postService.isCreator(#postId)")
    public String editCommentForm(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        CommentDetailsDTO commentDetailsDTO = commentService.getCommentById(commentId);
        model.addAttribute("comment", commentDetailsDTO);

        return "edit_comment";
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    @PreAuthorize("hasRole('ADMIN') || @commentService.isCreator(#commentId) || @postService.isCreator(#postId)")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        PostDetailsDTO postDetailsDTO = commentService.deleteComment(postId, commentId);

        return "redirect:/" + postDetailsDTO.getId();
    }
}
