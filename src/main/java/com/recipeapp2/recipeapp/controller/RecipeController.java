package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.service.FavoriteService;
import com.recipeapp2.recipeapp.service.RecipeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor

public class RecipeController {

    private final RecipeService recipeService;
    private final FavoriteService favoriteService;

    public static record RecipeDTO(
            Long id, String title, String description,
            List<String> ingredients, List<String> steps, String imageUrl,
            Long categoryId, Long userId,
            LocalDateTime createdAt, Integer favoritesCount
    ) {}

    private RecipeDTO toDTO(Recipe r) {
        return new RecipeDTO(
                r.getId(),
                r.getTitle(),
                r.getDescription(),
                r.getIngredients(),
                r.getSteps(),
                r.getImageUrl(),
                (r.getCategory() != null ? r.getCategory().getId() : null),
                (r.getUser() != null ? r.getUser().getId() : null),
                r.getCreatedAt(),
                r.getFavoritesCount()
        );
    }

    @GetMapping
    public List<RecipeDTO> getAllRecipes() {
        return recipeService.getAllRecipes().stream().map(this::toDTO).toList();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<RecipeDTO> addRecipe(@RequestBody Recipe recipe) {
        Recipe saved = recipeService.create(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDTO(recipeService.getRecipeById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public RecipeDTO updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        return toDTO(recipeService.updateRecipe(id, recipe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<RecipeDTO> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId) {
        return recipeService.searchRecipes(title, categoryId, userId)
                .stream().map(this::toDTO).toList();
    }

    @GetMapping("/sort")
    public Page<RecipeDTO> sortRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId,
            Pageable pageable) {
        return recipeService.searchRecipes(title, categoryId, userId, pageable)
                .map(this::toDTO);
    }

    public static record UserRef(Long id, String username) {}
    public static record FavoriteRequest(UserRef user) {}
    public static record FavoriteResponse(boolean favorited, long count) {}

    @PostMapping("/{id}/favorite")
    public ResponseEntity<FavoriteResponse> favorite(@PathVariable Long id,
                                                     @RequestBody FavoriteRequest req) {
        Long userId  = (req != null && req.user() != null) ? req.user().id()       : null;
        String uname = (req != null && req.user() != null) ? req.user().username() : null;

        favoriteService.add(id, userId, uname);
        long count = favoriteService.getFavoriteCount(id);
        return ResponseEntity.ok(new FavoriteResponse(true, count));
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<FavoriteResponse> unfavorite(@PathVariable Long id,
                                                       @RequestBody FavoriteRequest req) {
        Long userId  = (req != null && req.user() != null) ? req.user().id()       : null;
        String uname = (req != null && req.user() != null) ? req.user().username() : null;

        favoriteService.remove(id, userId, uname);
        long count = favoriteService.getFavoriteCount(id);
        return ResponseEntity.ok(new FavoriteResponse(false, count));
    }

    @GetMapping("/{id}/favorites/count")
    public ResponseEntity<Long> favoriteCount(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.getFavoriteCount(id));
    }
}
