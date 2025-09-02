package com.recipeapp2.recipeapp.service;
import com.recipeapp2.recipeapp.model.Category;
import com.recipeapp2.recipeapp.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public Category createCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(
                        Category.builder().name(name).build()
                ));
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
