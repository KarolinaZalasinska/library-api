package com.example.libraryapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record ClientDto(
        Long id,

        @NotBlank(message = "Client's first name cannot be empty.")
        String firstName,

        @NotBlank(message = "Client's last name cannot be empty.")
        String lastName,

        @Email(message = "Invalid email address.")
        @NotBlank(message = "Email is required.")
        String email,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{10,}$",
                message = "Password must be at least 10 characters long, including at least one uppercase letter, one digit, and one special character [@#$%^&+=!]."
        )
        @NotBlank(message = "Password is required.")
        String password

//        UserRole role
) {
}
