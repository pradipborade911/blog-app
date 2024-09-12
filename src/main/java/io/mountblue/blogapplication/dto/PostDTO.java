package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
public class PostDTO {
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    private String excerpt;

    private UserDTO author;

    private Set<TagDTO> tags;

    private List<String> tagsList = new ArrayList<>();

    private List<CommentDTO> comments = new ArrayList<>();

    public void addTag(String tag) {
        tagsList.add(tag);
    }

}
