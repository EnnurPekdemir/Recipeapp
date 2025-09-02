package com.recipeapp2.recipeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

public class Recipe {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)//tarifi çektiğimde malzemlerde hemen gelsin
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient", length = 255)
    @OrderColumn(name="order_index")
    private List<String> ingredients;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "step")
    @OrderColumn(name="order_index")
    private List<String> steps;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="favorites_count")
    private Integer favoritesCount;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();

        if (favoritesCount == null) favoritesCount = 0;
    }
}
