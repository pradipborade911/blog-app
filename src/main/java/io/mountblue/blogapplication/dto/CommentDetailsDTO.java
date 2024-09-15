package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDetailsDTO {
    private Long id;

    private String name;

    private String email;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserDTO author;
}
