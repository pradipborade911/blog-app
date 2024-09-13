package io.mountblue.blogapplication.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;
}