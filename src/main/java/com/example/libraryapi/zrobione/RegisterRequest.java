package com.example.libraryapi.zrobione;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String username;

    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

    private String roleName;

}

