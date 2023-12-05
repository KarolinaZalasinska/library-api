package com.example.libraryapi.zrobione;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserUpdateDto {

    @NotBlank(message = "Old username is required.")
    private String oldUsername;

    @NotBlank(message = "New username is required.")
    private String newUsername;

    @NotBlank(message = "New password is required.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password is not strong enough.")
    private String newPassword;

}
