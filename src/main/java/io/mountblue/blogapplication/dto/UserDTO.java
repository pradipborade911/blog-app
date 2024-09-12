package io.mountblue.blogapplication.dto;

import io.mountblue.blogapplication.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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