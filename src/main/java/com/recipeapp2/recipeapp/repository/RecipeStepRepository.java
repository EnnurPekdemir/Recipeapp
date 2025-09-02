package com.recipeapp2.recipeapp.repository;

import com.recipeapp2.recipeapp.model.RecipeStep;
import com.recipeapp2.recipeapp.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
    List<RecipeStep> findByRecipeOrderByStepNumberAsc(Recipe recipe);
}
