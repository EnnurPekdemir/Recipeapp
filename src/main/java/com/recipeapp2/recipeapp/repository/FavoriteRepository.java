package com.recipeapp2.recipeapp.repository;

import com.recipeapp2.recipeapp.model.Favorite;
import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);

    List<Favorite> findByUser(User user);

    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByRecipeId(Long recipeId);

    boolean existsByRecipeIdAndUserId(Long recipeId, Long userId);

    @Query("SELECT f FROM Favorite f WHERE f.recipe.id = :recipeId AND f.user.id = :userId")
    Optional<Favorite> find(@Param("recipeId") Long recipeId,
                            @Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Favorite f WHERE f.recipe.id = :recipeId AND f.user.id = :userId")
    void deleteByRecipeIdAndUserId(@Param("recipeId") Long recipeId,
                                   @Param("userId") Long userId);
}
