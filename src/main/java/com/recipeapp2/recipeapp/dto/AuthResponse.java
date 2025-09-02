package com.recipeapp2.recipeapp.dto;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {
    private Long userId;
    private String email;
    private String username;
}
