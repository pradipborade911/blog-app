package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/comments/{id}/update")
    public String updateComment(@ModelAttribute CommentDTO commentDTO){
        long postId = commentService.updateComment(commentDTO);
        return "redirect:/"+postId;
    }
}
