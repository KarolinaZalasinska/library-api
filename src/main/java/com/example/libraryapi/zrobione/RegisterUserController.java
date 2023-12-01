package com.example.libraryapi.zrobione;

import com.example.libraryapi.users.RegisterResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegisterUserController {
    private final UserService userService;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterCommand command) {
        try {
            RegisterResponse response = userService.register(command.getUsername(), command.getPassword(), "ROLE_USER");
            if (response.isSuccess()) {
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.badRequest().body(response.getMessage());
            }
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User with this username already exists.");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body("Role not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterCommand command) {
        try {
            RegisterResponse response = userService.register(command.getUsername(), command.getPassword(), "ROLE_ADMIN");
            if (response.isSuccess()) {
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.badRequest().body(response.getMessage());
            }
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User with this username already exists.");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body("Role not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }
}
