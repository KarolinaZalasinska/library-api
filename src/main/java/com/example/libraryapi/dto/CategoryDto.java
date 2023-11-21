package com.example.libraryapi.dto;

import javax.validation.constraints.NotBlank;

public record CategoryDto(
        Long id,
        @NotBlank(message = "Category name is required.") String name
) {
}
