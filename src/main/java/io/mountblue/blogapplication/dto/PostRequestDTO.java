package io.mountblue.blogapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PostRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @Size(max = 500, message = "Excerpt cannot be longer than 500 characters")
    @NotBlank(message = "Excerpt cannot be blank")
    private String excerpt;

    private List<String> tagsList = new ArrayList<>();

}
