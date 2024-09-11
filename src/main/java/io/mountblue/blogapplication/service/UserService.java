package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    void saveUser(User userDTO);
}
