package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.RecipeStep;
import com.recipeapp2.recipeapp.service.RecipeStepService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
public class RecipeStepController {

    private final RecipeStepService recipeStepService;

    @PostMapping("/add")
    public ResponseEntity<RecipeStep> addStep(
            @RequestParam Long recipeId,
            @RequestParam int stepNumber,
            @RequestParam String description) {

        RecipeStep step = recipeStepService.addStepToRecipe(recipeId, stepNumber, description);
        return ResponseEntity.ok(step);
    }

    @GetMapping("/recipe")
    public ResponseEntity<List<RecipeStep>> getStepsForRecipe(@RequestParam Long recipeId) {
        List<RecipeStep> steps = recipeStepService.getStepsForRecipe(recipeId);
        return ResponseEntity.ok(steps);
    }
}
