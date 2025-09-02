package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.Favorite;
import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.model.User;
import com.recipeapp2.recipeapp.repository.FavoriteRepository;
import com.recipeapp2.recipeapp.repository.RecipeRepository;
import com.recipeapp2.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final RecipeRepository recipeRepo;
    private final UserRepository userRepo;

    private User resolveUser(Long userId, String username) {
        if (userId != null) {
            return userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found id=" + userId));
        }
        if (username != null && !username.isBlank()) {
            return userRepo.findByUsername(username.trim())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: username=" + username));
        }
        throw new IllegalArgumentException("User id or username is necessary");
    }

    private Recipe resolveRecipe(Long recipeId) {
        return recipeRepo.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: id=" + recipeId));
    }

    @Transactional
    public boolean add(Long recipeId, Long userId, String username) {
        Recipe recipe = resolveRecipe(recipeId);
        User user = resolveUser(userId, username);
        if (favoriteRepo.existsByRecipeIdAndUserId(recipe.getId(), user.getId())) return false;
        favoriteRepo.save(Favorite.builder().recipe(recipe).user(user).build());
        return true;
    }

    @Transactional
    public boolean remove(Long recipeId, Long userId, String username) {
        User user = resolveUser(userId, username);
        boolean existed = favoriteRepo.existsByRecipeIdAndUserId(recipeId, user.getId());
        favoriteRepo.deleteByRecipeIdAndUserId(recipeId, user.getId());
        return existed;
    }

    @Transactional(readOnly = true)
    public long getFavoriteCount(Long recipeId) {
        return favoriteRepo.countByRecipeId(recipeId);
    }

    @Transactional(readOnly = true)
    public List<Favorite> getFavoritesByUser(Long userId) {
        return favoriteRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public boolean addToFavorites(Long recipeId, Long userId) {
        return add(recipeId, userId, null);
    }

    @Transactional
    public boolean addToFavorites(Long recipeId, Long userIdOrNull, String username) {
        return add(recipeId, userIdOrNull, username);
    }

    @Transactional
    public boolean removeFromFavorites(Long recipeId, Long userId) {
        return remove(recipeId, userId, null);
    }

    @Transactional
    public boolean removeFromFavorites(Long recipeId, Long userIdOrNull, String username) {
        return remove(recipeId, userIdOrNull, username);
    }

    @Transactional
    public boolean toggleFavorite(Long recipeId, Long userId) {
        return toggleFavorite(recipeId, userId, null);
    }

    @Transactional
    public boolean toggleFavorite(Long recipeId, Long userIdOrNull, String username) {
        User user = resolveUser(userIdOrNull, username);
        boolean already = favoriteRepo.existsByRecipeIdAndUserId(recipeId, user.getId());
        if (already) {
            favoriteRepo.deleteByRecipeIdAndUserId(recipeId, user.getId());
            return false;
        } else {
            Recipe recipe = resolveRecipe(recipeId);
            favoriteRepo.save(Favorite.builder().recipe(recipe).user(user).build());
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean isFavorited(Long recipeId, Long userId) {
        return favoriteRepo.existsByRecipeIdAndUserId(recipeId, userId);
    }
}
