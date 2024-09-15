package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class PostDetailsDTO {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    private String excerpt;

    private UserDTO author;

    private Set<TagDTO> tags;

    private List<CommentDetailsDTO> comments = new ArrayList<>();
}
