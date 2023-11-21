package com.example.libraryapi.dto;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public record AuthorDto(
        Long id,

        @NotBlank(message = "Author's first name cannot be empty.")
        String firstName,

        @NotBlank(message = "Author's last name cannot be empty.")
        String lastName,

        Set<BookDto> books
) {
}
