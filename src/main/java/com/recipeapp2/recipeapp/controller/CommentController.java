package com.recipeapp2.recipeapp.controller;

import com.recipeapp2.recipeapp.model.Comment;
import com.recipeapp2.recipeapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    public static record UserRef(Long id, String username) {}
    public static record CommentCreateRequest(UserRef user, String content) {}
    public static record CommentCreateDirectRequest(Long recipeId, UserRef user, String content) {}
    public static record CommentUpdateRequest(String content) {}


    public static record CommentDTO(
            Long id,
            Long recipeId,
            Long userId,
            String username,
            String content,
            LocalDateTime createdAt
    ) {}

    private CommentDTO toDTO(Comment c) {
        return new CommentDTO
                (c.getId(),
                (c.getRecipe() != null ? c.getRecipe().getId() : null),
                (c.getUser()   != null ? c.getUser().getId()   : null),
                (c.getUser()   != null ? c.getUser().getUsername() : null),
                c.getContent(),
                c.getCreatedAt()
        );
    }

    @GetMapping("/recipes/{recipeId}/comments")
    public List<CommentDTO> listByRecipe(@PathVariable Long recipeId) {
        return commentService.listByRecipe(recipeId).stream().map(this::toDTO).toList();
    }

    @PostMapping("/recipes/{recipeId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long recipeId,
                                                 @RequestBody CommentCreateRequest req) {
        if (req == null || req.content() == null || req.content().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Long userId   = (req.user() != null) ? req.user().id()       : null;
        String uname  = (req.user() != null) ? req.user().username() : null;

        Comment saved = commentService.add(recipeId, userId, uname, req.content());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest() //şu anki isteğimin url sini baz alıcam
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(toDTO(saved));
    }

    @GetMapping("/comments")
    public List<CommentDTO> getComments() {
        return commentService.getAllComments()
                .stream().map(this::toDTO).toList();
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id,
                                                    @RequestBody CommentUpdateRequest req) {
        if (req == null || req.content() == null || req.content().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Comment updated = commentService.updateContent(id, req.content());
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
