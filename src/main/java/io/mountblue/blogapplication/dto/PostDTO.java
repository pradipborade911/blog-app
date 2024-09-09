package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PostDTO {
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    private String excerpt;

    private String author = "Sachin Sharma";

    private List<String> tagsList = new ArrayList<>();

    private List<CommentDTO> comments = new ArrayList<>();

    public void addTag(String tag) {
        tagsList.add(tag);
    }

}
