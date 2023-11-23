package com.example.libraryapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public record LibraryDto(
        Long id,

        @NotBlank(message = "Library name is required.")
        String name,

        @NotBlank(message = "Library address is required.")
        String address,

        @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postal code format.")
        String postalCode,

        List<Long> bookIds
) {
}
