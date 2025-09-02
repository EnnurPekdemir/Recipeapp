package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.repository.RecipeRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;

@Service
@RequiredArgsConstructor

public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<Recipe> getAllRecipes() {
        List<Recipe> list = recipeRepository.findAll();
        return list;
    }
    @Transactional(readOnly = true)
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + id));
    }

    public Recipe create(Recipe recipe) {
        if (recipe.getCreatedAt() == null) {
            recipe.setCreatedAt(java.time.LocalDateTime.now());
        }
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        Recipe r = getRecipeById(id);
        r.setTitle(updatedRecipe.getTitle());
        r.setDescription(updatedRecipe.getDescription());
        r.setImageUrl(updatedRecipe.getImageUrl());
        r.setIngredients(updatedRecipe.getIngredients());
        r.setSteps(updatedRecipe.getSteps());
        r.setCategory(updatedRecipe.getCategory());
        r.setUser(updatedRecipe.getUser());
        return recipeRepository.save(r);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private Specification<Recipe> buildSpec(String title, Long categoryId, Long userId) {
        Specification<Recipe> spec = (root, q, cb) -> cb.conjunction();

        if (title != null && !title.trim().isEmpty()) {
            String like = "%" + title.trim().toLowerCase() + "%";
            spec = spec.and((root2, q2, cb2) -> cb2.like(cb2.lower(root2.get("title")), like));
        }
        if (categoryId != null) {
            spec = spec.and((root2, q2, cb2) -> cb2.equal(
                    root2.join("category", JoinType.LEFT).get("id"), categoryId));
        }
        if (userId != null) {
            spec = spec.and((root2, q2, cb2) -> cb2.equal(
                    root2.join("user", JoinType.LEFT).get("id"), userId));
        }
        return spec;
    }

    public List<Recipe> searchRecipes(String title, Long categoryId, Long userId) {
        return recipeRepository.findAll(buildSpec(title, categoryId, userId));
    }

    public Page<Recipe> searchRecipes(String title, Long categoryId, Long userId, Pageable pageable) {
        return recipeRepository.findAll(buildSpec(title, categoryId, userId), pageable);
    }
}
