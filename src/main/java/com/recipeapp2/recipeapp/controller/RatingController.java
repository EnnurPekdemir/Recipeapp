package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.Rating;
import com.recipeapp2.recipeapp.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    public record RatingYaniti(boolean success, Integer myScore, double average, long count) {}

    @PostMapping("/rate")
    public ResponseEntity<RatingYaniti> rate(
            @RequestParam Long userId,
            @RequestParam Long recipeId,
            @RequestParam Integer score
    ) {
        Rating r = ratingService.rate(userId, recipeId, score);
        double avg = ratingService.average(recipeId);
        long count = ratingService.count(recipeId);
        return ResponseEntity.ok(new RatingYaniti(true, r.getScore(), avg, count));
    }

    @PostMapping("/set")
    public ResponseEntity<RatingYaniti> set(
            @RequestParam Long userId,
            @RequestParam Long recipeId,
            @RequestParam Integer score
    ) {
        return rate(userId, recipeId, score);
    }

    @DeleteMapping("/unrate")
    public ResponseEntity<RatingYaniti> unrate(
            @RequestParam Long userId,
            @RequestParam Long recipeId
    ) {
        boolean existed = ratingService.unrate(userId, recipeId);
        double avg = ratingService.average(recipeId);
        long count = ratingService.count(recipeId);
        return ResponseEntity.ok(new RatingYaniti(existed, null, avg, count));
    }

    @GetMapping("/average")
    public ResponseEntity<Double> average(@RequestParam Long recipeId) {
        return ResponseEntity.ok(ratingService.average(recipeId));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(@RequestParam Long recipeId) {
        return ResponseEntity.ok(ratingService.count(recipeId));
    }

    @GetMapping("/recipe")
    public ResponseEntity<List<Rating>> listByRecipe(@RequestParam Long recipeId) {
        return ResponseEntity.ok(ratingService.listByRecipe(recipeId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Rating>> listByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(ratingService.listByUser(userId));
    }

    @GetMapping("/mine")
    public ResponseEntity<Rating> myRating(
            @RequestParam Long userId,
            @RequestParam Long recipeId
    ) {
        return ResponseEntity.ok(ratingService.getMyRating(userId, recipeId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
