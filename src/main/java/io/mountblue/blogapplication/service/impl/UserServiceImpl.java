package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.repository.RoleRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import io.mountblue.blogapplication.security.Role;
import io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("AUTHOR");
        if(role == null){
            throw new ResourceNotFoundException("No such role exists");
        }

        user.addRole(role);
        userRepository.save(user);
    }
}
