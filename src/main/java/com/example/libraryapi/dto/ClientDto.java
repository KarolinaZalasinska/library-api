package com.example.libraryapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record ClientDto(
        Long id,

        @NotBlank(message = "Client's first name cannot be empty.")
        String firstName,

        @NotBlank(message = "Client's last name cannot be empty.")
        String lastName,

        @Email(message = "Invalid email address.")
        @NotBlank(message = "Email is required.")
        String email

) {
}
