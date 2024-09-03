package io.mountblue.blogapplication.dto;

import io.mountblue.blogapplication.entity.Tag;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PostSummaryDTO {

    private Long id;

    private String title;

    private String excerpt;

    private String author;

    private LocalDateTime publishedAt;

    private Set<TagDTO> tags = new HashSet<>();
}
