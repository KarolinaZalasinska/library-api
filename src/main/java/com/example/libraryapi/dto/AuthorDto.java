package com.example.libraryapi.dto;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AuthorDto {
    private Long id;

    @NotBlank(message = "Imię autora jest wymagane.")
    private String firstName;

    @NotBlank(message = "Nazwisko autora jest wymagane.")
    private String lastName;
}
