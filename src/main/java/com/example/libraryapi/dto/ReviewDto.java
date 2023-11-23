package com.example.libraryapi.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public record ReviewDto(
        Long id,

        @NotNull(message = "Book rating is required.")
        @Range(min = 1, max = 6, message = "Rating must be between 1 and 6.")
        Integer rating,

        @NotBlank(message = "Book review description is required.")
        @Size(max = 50, message = "Description cannot exceed 50 characters.")
        String description,

        LocalDateTime creationDate,

        Long bookId,

        Long userId
) {
}
