package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.Rating;
import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.model.User;
import com.recipeapp2.recipeapp.repository.RatingRepository;
import com.recipeapp2.recipeapp.repository.RecipeRepository;
import com.recipeapp2.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepo;
    private final RecipeRepository recipeRepo;
    private final UserRepository userRepo;

    private User resolveUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + userId));
    }

    private Recipe resolveRecipe(Long recipeId) {
        return recipeRepo.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: id=" + recipeId));
    }

    private int normalizeScore(Integer score) {
        if (score == null) throw new IllegalArgumentException("Puan zorunlu");
        if (score < 1 || score > 5) throw new IllegalArgumentException("Puan 1 ile 5 arasında olmalı");
        return score;
    }

    @Transactional
    public Rating rate(Long userId, Long recipeId, Integer score) {
        int s = normalizeScore(score);
        User user = resolveUser(userId);
        Recipe recipe = resolveRecipe(recipeId);

        return ratingRepo.findByRecipeIdAndUserId(recipe.getId(), user.getId())
                .map(existing -> { existing.setScore(s); return ratingRepo.save(existing); })
                .orElseGet(() -> ratingRepo.save(
                        Rating.builder().user(user).recipe(recipe).score(s).build()
                ));
    }

    @Transactional
    public boolean unrate(Long userId, Long recipeId) {
        User user = resolveUser(userId);
        boolean existed = ratingRepo.existsByRecipeIdAndUserId(recipeId, user.getId());
        ratingRepo.deleteByRecipeIdAndUserId(recipeId, user.getId());
        return existed;
    }

    @Transactional(readOnly = true)
    public long count(Long recipeId) {
        return ratingRepo.countByRecipeId(recipeId);
    }

    @Transactional(readOnly = true)
    public double average(Long recipeId) {
        Double avg = ratingRepo.averageForRecipe(recipeId);
        return (avg == null) ? 0.0 : avg;
    }

    @Transactional(readOnly = true)
    public List<Rating> listByRecipe(Long recipeId) {
        return ratingRepo.findByRecipeIdOrderByUpdatedAtDesc(recipeId);
    }

    @Transactional(readOnly = true)
    public List<Rating> listByUser(Long userId) {
        return ratingRepo.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public Rating getMyRating(Long userId, Long recipeId) {
        return ratingRepo.findByRecipeIdAndUserId(recipeId, userId).orElse(null);
    }
}
