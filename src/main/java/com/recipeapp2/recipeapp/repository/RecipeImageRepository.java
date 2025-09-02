package com.recipeapp2.recipeapp.repository;

import com.recipeapp2.recipeapp.model.RecipeImage;
import com.recipeapp2.recipeapp.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {
    Optional<RecipeImage> findByRecipe(Recipe recipe);
}
