package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;

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
    @PreAuthorize("@commentService.isCreator(#commentId) || @postService.isOwnerOfCommentedPost(#commentId) || hasRole('ADMIN')")
    public String updateComment(@PathVariable Long commentId, @ModelAttribute CommentDTO commentDTO) {
        long postId = commentService.updateComment(commentDTO);

        return "redirect:/" + postId;
    }

    @PostMapping("/{id}/addComment")
    public String addComment(@ModelAttribute CommentDTO commentDTO, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.addComment(commentDTO, id);
        model.addAttribute("post", postDTO);

        return "redirect:/{id}";
    }

    @GetMapping("/{postId}/comments/{commentId}/edit")
    @PreAuthorize("@commentService.isCreator(#commentId) || @postService.isCreator(#postId) || hasRole('ADMIN')")
    public String editCommentForm(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        CommentDTO commentDTO = commentService.getCommentById(commentId);
        model.addAttribute("comment", commentDTO);

        return "edit_comment";
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    @PreAuthorize("@commentService.isCreator(#commentId) || @postService.isCreator(#postId) || hasRole('ADMIN')")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        PostDTO postDTO = postService.deleteComment(postId, commentId);

        return "redirect:/" + postDTO.getId();
    }
}
