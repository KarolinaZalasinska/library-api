package com.example.libraryapi.zrobione;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.libraryapi.users.RegisterResponse;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
class UsersController {
    private final UserService userService;

    // REJESTRACJA UŻYTKOWNIKÓW
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterCommand command) {
        RegisterResponse response = userService.register(command.getUsername(), command.getPassword(), "ROLE_USER");
        return buildResponse(response);
    }

    // REJESTRACJA ADMINISTRATORA
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterCommand command) {
        RegisterResponse response = userService.register(command.getUsername(), command.getPassword(), "ROLE_ADMIN");
        return buildResponse(response);
    }

    private ResponseEntity<?> buildResponse(RegisterResponse response) {
        if (response.isSuccess()) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Data
    static class RegisterCommand {
        @Email
        private String username;
        @Size(min = 3, max = 30)
        private String password;
    }
}
