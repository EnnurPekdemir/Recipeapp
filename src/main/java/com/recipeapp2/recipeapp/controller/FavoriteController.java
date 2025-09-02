package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.Favorite;
import com.recipeapp2.recipeapp.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    public record FavoriResponse(boolean success, long count) {}
    public record FavoriStatusResponse(boolean favorited, long count) {}


    @PostMapping("/add")
    public ResponseEntity<FavoriResponse> addFavorite(
            @RequestParam Long recipeId,
            @RequestParam Long userId
    ) {
        boolean added = favoriteService.addToFavorites(recipeId, userId);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriResponse(added, count));
    }

    @PostMapping("/add/by-username")
    public ResponseEntity<FavoriResponse> addFavoriteByUsername(
            @RequestParam Long recipeId,
            @RequestParam String username
    ) {
        boolean added = favoriteService.addToFavorites(recipeId, null, username);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriResponse(added, count));
    }


    @DeleteMapping("/remove")
    public ResponseEntity<FavoriResponse> removeFavorite(
            @RequestParam Long recipeId,
            @RequestParam Long userId
    ) {
        boolean existed = favoriteService.removeFromFavorites(recipeId, userId);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriResponse(existed, count));
    }

    @DeleteMapping("/remove/by-username")
    public ResponseEntity<FavoriResponse> removeFavoriteByUsername(
            @RequestParam Long recipeId,
            @RequestParam String username
    ) {
        boolean existed = favoriteService.removeFromFavorites(recipeId, null, username);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriResponse(existed, count));
    }

    @PostMapping("/toggle")
    public ResponseEntity<FavoriStatusResponse> toggleFavorite(
            @RequestParam Long recipeId,
            @RequestParam Long userId
    ) {
        boolean favoritedNow = favoriteService.toggleFavorite(recipeId, userId);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriStatusResponse(favoritedNow, count));
    }

    @PostMapping("/toggle/by-username")
    public ResponseEntity<FavoriStatusResponse> toggleFavoriteByUsername(
            @RequestParam Long recipeId,
            @RequestParam String username
    ) {
        boolean favoritedNow = favoriteService.toggleFavorite(recipeId, null, username);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriStatusResponse(favoritedNow, count));
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getFavoriteCount(@RequestParam Long recipeId) {
        return ResponseEntity.ok(favoriteService.getFavoriteCount(recipeId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Favorite>> getFavoritesByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @GetMapping("/status")
    public ResponseEntity<FavoriStatusResponse> isFavorited(
            @RequestParam Long recipeId,
            @RequestParam Long userId
    ) {
        boolean favorited = favoriteService.isFavorited(recipeId, userId);
        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(new FavoriStatusResponse(favorited, count));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
