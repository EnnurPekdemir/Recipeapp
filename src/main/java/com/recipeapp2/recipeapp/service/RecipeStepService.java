package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.model.RecipeStep;
import com.recipeapp2.recipeapp.repository.RecipeRepository;
import com.recipeapp2.recipeapp.repository.RecipeStepRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;

    public RecipeStep addStepToRecipe(Long recipeId, int stepNumber, String description) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        RecipeStep step = RecipeStep.builder()
                .stepNumber(stepNumber)
                .description(description)
                .recipe(recipe)
                .build();

        return recipeStepRepository.save(step);
    }

    public List<RecipeStep> getStepsForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        return recipeStepRepository.findByRecipeOrderByStepNumberAsc(recipe);
    }
}
