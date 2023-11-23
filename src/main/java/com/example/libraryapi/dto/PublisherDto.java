package com.example.libraryapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

public record PublisherDto(
        Long id,

        @NotBlank(message = "Publisher name is required.")
        String name,

        @NotBlank(message = "Publisher address is required.")
        String address,

        @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postal code format.")
        String postalCode,

        Set<BookDto> books
        ) {
}
