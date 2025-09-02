package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.RecipeImage;
import com.recipeapp2.recipeapp.service.RecipeImageService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class RecipeImageController {

    private final RecipeImageService recipeImageService;

    @PostMapping("/upload")
    public ResponseEntity<RecipeImage> uploadImage(
            @RequestParam Long recipeId,
            @RequestParam String imageUrl) {

        RecipeImage image = recipeImageService.uploadImage(recipeId, imageUrl);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getImageByRecipe(@RequestParam Long recipeId) {
        Optional<RecipeImage> image = recipeImageService.getImageByRecipe(recipeId);

        return image.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
