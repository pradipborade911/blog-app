package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class PostSummaryDTO {

    private Long id;

    private String title;

    private String excerpt;

    private UserDTO author;

    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;

    private Set<TagDTO> tags;
}
