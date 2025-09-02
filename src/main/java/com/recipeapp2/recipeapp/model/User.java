package com.recipeapp2.recipeapp.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "`user`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Builder.Default
    @Column(name = "role", length = 255)
    private String role = "ROLE_USER";

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private java.util.List<Recipe> recipes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
