package com.example.libraryapi.zrobione;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Registration Management System", tags = {"Registration"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @ApiOperation(value = "Register a new user")
    @PostMapping
    public ResponseEntity<RegisterResponse> registerUser(
            @ApiParam(value = "Username", required = true) @RequestParam String username,
            @ApiParam(value = "Password", required = true) @RequestParam String password,
            @ApiParam(value = "Role name", required = true) @RequestParam UserRole role) {
        RegisterResponse response = registrationService.register(username, password, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


