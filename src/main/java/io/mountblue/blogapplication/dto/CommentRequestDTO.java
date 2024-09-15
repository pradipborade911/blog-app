package io.mountblue.blogapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    @Size(max = 100, message = "Name cannot be more than 100 characters")
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot be more than 255 characters")
    private String email;

    @NotBlank(message = "Comment is required")
    @Size(max = 5000, message = "Comment cannot be more than 5000 characters")
    private String comment;

}

