package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.dto.AuthRequest;
import com.recipeapp2.recipeapp.model.User;
import com.recipeapp2.recipeapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthRequest req) {
        User saved = authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Kayıt başarılı: " + saved.getUsername());
    }

    @GetMapping("/ping")
    public String ping() { return "auth ok"; }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String identifier = body.getOrDefault("identifier", "");
        String password   = body.getOrDefault("password", "");

        var u = authService.login(identifier, password);

        Map<String, Object> resp = new HashMap<>();
        resp.put("message",  "Giriş Başarılı");
        resp.put("userId",   u.getId());
        resp.put("username", u.getUsername());
        resp.put("email",    u.getEmail());
        return ResponseEntity.ok(resp);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
