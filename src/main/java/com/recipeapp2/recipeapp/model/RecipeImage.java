package com.recipeapp2.recipeapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
