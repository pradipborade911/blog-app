package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private Long id;

    private String name;

    private String email;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
