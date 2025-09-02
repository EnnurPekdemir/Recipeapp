package com.recipeapp2.recipeapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Entity
@Table(
        name = "recipe_steps_entity",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_recipe_step_no",
                columnNames = {"recipe_id", "step_number"}
        )
)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Column(name = "step_number", nullable = false)
    private int stepNumber;

    @NotBlank
    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Recipe recipe;
}
