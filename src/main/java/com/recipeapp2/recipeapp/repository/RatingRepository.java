package com.recipeapp2.recipeapp.repository;

import com.recipeapp2.recipeapp.model.Rating;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRecipeIdAndUserId(Long recipeId, Long userId);

    List<Rating> findByRecipeIdOrderByUpdatedAtDesc(Long recipeId);
    List<Rating> findByUserIdOrderByUpdatedAtDesc(Long userId);

    long countByRecipeId(Long recipeId);

    @Query("SELECT COALESCE(AVG(r.score), 0) FROM Rating r WHERE r.recipe.id = :recipeId")
    Double averageForRecipe(@Param("recipeId") Long recipeId);

    boolean existsByRecipeIdAndUserId(Long recipeId, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Rating r WHERE r.recipe.id = :recipeId AND r.user.id = :userId")
    void deleteByRecipeIdAndUserId(@Param("recipeId") Long recipeId,
                                   @Param("userId")   Long userId);
}
