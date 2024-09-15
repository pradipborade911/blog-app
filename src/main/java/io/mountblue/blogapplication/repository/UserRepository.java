package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    User save(User user);

//    void deleteById(Long id);

    Optional<Object> findByEmail(String email);
}

