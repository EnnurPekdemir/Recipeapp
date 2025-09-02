package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.model.RecipeImage;
import com.recipeapp2.recipeapp.repository.RecipeImageRepository;
import com.recipeapp2.recipeapp.repository.RecipeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeImageService {

    private final RecipeRepository recipeRepository;
    private final RecipeImageRepository recipeImageRepository;

    public RecipeImage uploadImage(Long recipeId, String imageUrl) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        Optional<RecipeImage> existingImage = recipeImageRepository.findByRecipe(recipe);

        RecipeImage image = existingImage.orElse(
                RecipeImage.builder()
                        .recipe(recipe)
                        .build()
        );

        image.setImageUrl(imageUrl);
        return recipeImageRepository.save(image);
    }

    public Optional<RecipeImage> getImageByRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return recipeImageRepository.findByRecipe(recipe);
    }
}

