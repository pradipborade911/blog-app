package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.RegisterRequestDTO;
import io.mountblue.blogapplication.entity.Role;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.exception.ResourceAlreadyExistsException;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.repository.RoleRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import io.mountblue.blogapplication.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(RegisterRequestDTO registerRequestDTO) {
        User user = modelMapper.map(registerRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String defaultRole = "ROLE_AUTHOR";
        Optional<Role> role = roleRepository.findByName(defaultRole);

        if (role.isEmpty()) {
            throw new ResourceNotFoundException("No such role exists");
        } else {
            user.addRole(role.get());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email is already in use.");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username is already in use.");
        }

        userRepository.save(user);
    }
}
