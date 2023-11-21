package com.example.libraryapi.dto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public record ReviewDto(
        Long id,

        @NotNull(message = "Rating is required.")
        @Min(value = 1, message = "Rating must be greater than or equal to 1.")
        @Max(value = 6, message = "Rating must be less than or equal to 6.")
        Integer rating,

        @NotBlank(message = "Description field is required.")
        @Size(max = 50, message = "Description cannot exceed 50 characters.")
        String description,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        Long bookId,
        Long userId
) {
}
