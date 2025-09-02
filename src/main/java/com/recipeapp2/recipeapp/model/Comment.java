package com.recipeapp2.recipeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Recipe recipe;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Transient //Bu alan db ye kaydedilmez
    @JsonProperty("recipeId")
    public Long getRecipeId() {
        return recipe != null ? recipe.getId() : null;
    }

    @Transient
    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @Transient
    @JsonProperty("username")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getUsername() {
        return (user != null ? user.getUsername() : null);
    }

    @PrePersist //db ye ilk kayıttan hemen önce çalışır
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate // db güncellenmeden hemen önce çalışır
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
