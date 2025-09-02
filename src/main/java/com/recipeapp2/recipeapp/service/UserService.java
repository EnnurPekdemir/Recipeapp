package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.User;
import com.recipeapp2.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required ");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required ");
        }

        userRepository.findByEmail(user.getEmail().trim())
                .ifPresent(u -> { throw new RuntimeException("Email already exists"); });

        userRepository.findByUsername(user.getUsername().trim())
                .ifPresent(u -> { throw new RuntimeException("Username already exists"); });

        if (user.getPassword() == null) {
            user.setPassword("");
        }

        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found : " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found : " + id));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
            user.setUsername(updatedUser.getUsername().trim());
        }
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        return userRepository.save(user);
    }
}
