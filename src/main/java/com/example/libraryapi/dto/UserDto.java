package com.example.libraryapi.dto;

import com.example.libraryapi.model.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record UserDto(
        Long id,

        @NotBlank(message = "First name is required.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        String lastName,

        @Email(message = "Invalid email address.")
        @NotBlank(message = "Email is required.")
        String email,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{10,}$",
                message = "Password must be at least 10 characters long, including at least one uppercase letter, one digit, and one special character [@#$%^&+=!]."
        )
        @NotBlank(message = "Password is required.")
        String password,

        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
