package com.recipeapp2.recipeapp.service;

import com.recipeapp2.recipeapp.model.Comment;
import com.recipeapp2.recipeapp.model.Recipe;
import com.recipeapp2.recipeapp.model.User;
import com.recipeapp2.recipeapp.repository.CommentRepository;
import com.recipeapp2.recipeapp.repository.RecipeRepository;
import com.recipeapp2.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> listByRecipe(Long recipeId) {
        return commentRepository.findByRecipe_IdOrderByCreatedAtDesc(recipeId);
    }

    @Transactional(readOnly = true)
    public long countByRecipeId(Long recipeId) {
        return commentRepository.countByRecipe_Id(recipeId);
    }

    @Transactional
    public Comment add(Long recipeId, Long userId, String username, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be empty.");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + recipeId));

        User user;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        } else if (username != null && !username.isBlank()) {
            user = userRepository.findByUsername(username.trim())
                    .orElseThrow(() -> new IllegalArgumentException("User not found (username): " + username));
        } else {
            throw new IllegalArgumentException("User information is required for commenting. (userId or username).");
        }

        Comment c = Comment.builder()
                .recipe(recipe)
                .user(user)
                .content(content.trim())
                .build();

        return commentRepository.save(c);
    }

    @Transactional
    public Comment updateContent(Long id, String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be empty.");
        }
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + id));

        existing.setContent(newContent.trim());
        return commentRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("No comments found to delete: " + id);
        }
        commentRepository.deleteById(id);
    }
}
