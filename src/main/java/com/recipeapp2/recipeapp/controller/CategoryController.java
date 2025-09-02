package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.Category;
import com.recipeapp2.recipeapp.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestParam String name) {
        Category category = categoryService.createCategory(name);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
