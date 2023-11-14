package com.example.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class AuthorDto {
    private Long id;

    @NotBlank(message = "Imię autora jest wymagane.")
    private String firstName;

    @NotBlank(message = "Nazwisko autora jest wymagane.")
    private String lastName;
}
