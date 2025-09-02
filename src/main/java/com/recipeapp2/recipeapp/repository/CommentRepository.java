package com.recipeapp2.recipeapp.repository;

import com.recipeapp2.recipeapp.model.Comment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByRecipe_IdOrderByCreatedAtDesc(Long recipeId);

    long countByRecipe_Id(Long recipeId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.recipe.id = :recipeId AND c.user.id = :userId")
    void deleteByRecipeIdAndUserId(@Param("recipeId") Long recipeId,
                                   @Param("userId")   Long userId);
}
