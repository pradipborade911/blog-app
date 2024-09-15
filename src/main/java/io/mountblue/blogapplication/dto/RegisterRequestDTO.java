package io.mountblue.blogapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot be more than 255 characters")
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Username is required")
    private String password;
}
