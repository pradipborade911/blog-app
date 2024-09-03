package io.mountblue.blogapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.mountblue.blogapplication.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
