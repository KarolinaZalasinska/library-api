package com.example.libraryapi.zrobione;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserService userService;

    public AuthController(RegistrationService registrationService, UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = registrationService.register(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getRoleName()
        );

        if (response.isSuccess()) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestParam String oldUsername,
            @RequestParam String newUsername,
            @RequestParam String newPassword,
            @RequestParam Set<String> newRoleNames
    ) {
        try {
            userService.updateUser(oldUsername, newUsername, newPassword, newRoleNames);
            return ResponseEntity.ok("User updated successfully");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body("Role not found: " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found: " + e.getMessage());
        }
    }


    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found: " + e.getMessage());
        }
    }
}

