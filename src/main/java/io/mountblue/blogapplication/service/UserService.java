package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.dto.RegisterRequestDTO;

public interface UserService {
    void saveUser(RegisterRequestDTO registerRequestDTO);
}
