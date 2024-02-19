package com.example.libraryapi.users;

import jakarta.validation.constraints.Pattern;

import javax.validation.constraints.Size;
import java.util.Set;

public record UserDto(
        String username,

        @Size(min = 6, message = "Password must be at least 6 characters long.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password is not strong enough.")
        String password,

        Set<String> roles,

        String newUsername,

        @Size(min = 6, message = "New password must be at least 6 characters long.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password is not strong enough.")
        String newPassword,

        boolean enabled
) {
}
