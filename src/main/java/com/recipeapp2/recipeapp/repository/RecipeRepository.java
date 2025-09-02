package com.recipeapp2.recipeapp.repository;

import com.recipeapp2.recipeapp.model.Recipe;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>,
        JpaSpecificationExecutor<Recipe> { }





