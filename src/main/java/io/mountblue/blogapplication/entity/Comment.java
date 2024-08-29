package io.mountblue.blogapplication.entity;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String name;
    private String email;
    private String comment;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
