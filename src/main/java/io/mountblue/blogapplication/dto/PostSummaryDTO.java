package io.mountblue.blogapplication.dto;

import io.mountblue.blogapplication.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostSummaryDTO {

    private Long id;

    private String title;

    private String excerpt;

    private String author;

    private LocalDateTime publishedAt;

    private Set<Tag> tags;
}
