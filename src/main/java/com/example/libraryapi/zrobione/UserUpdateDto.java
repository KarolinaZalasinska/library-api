package com.example.libraryapi.zrobione;

import jakarta.validation.constraints.Pattern;

import javax.validation.constraints.Size;

public record UserUpdateDto(
        String newUsername,

        @Size(min = 6, message = "Password must be at least 6 characters long.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password is not strong enough.")
        String newPassword,

        boolean enabled
) {
}