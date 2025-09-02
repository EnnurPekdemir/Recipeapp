package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.dto.AuthRequest;
import com.recipeapp2.recipeapp.model.User;
import com.recipeapp2.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User signup(AuthRequest req) {
        String username = safe(req.getUsername());
        String email    = safe(req.getEmail()).toLowerCase();
        String password = safe(req.getPassword());

        if (username.isEmpty()) throw new IllegalArgumentException("Kullanıcı adı zorunlu");
        if (email.isEmpty())    throw new IllegalArgumentException("Email zorunlu");
        if (password.isEmpty()) throw new IllegalArgumentException("Şifre zorunlu");

        if (userRepository.existsByUsername(username))
            throw new IllegalArgumentException("Kullanıcı adı kullanımda");
        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("Email kullanımda");

        User u = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role("USER")
                .build();

        return userRepository.save(u);
    }

    public User login(String identifier, String passwordPlain) {
        String ident = safe(identifier);
        String pass  = safe(passwordPlain);

        var userOpt = userRepository.findByEmail(ident.toLowerCase());
        if (userOpt.isEmpty()) userOpt = userRepository.findByUsername(ident);

        var user = userOpt.orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        if (!user.getPassword().equals(pass))
            throw new IllegalArgumentException("Şifre hatalı");

        return user;
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }
}
