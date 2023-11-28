package com.example.libraryapi.controller;

import com.example.libraryapi.dto.ClientDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.ClientService;

import java.util.List;

@Api(value = "User Management System", tags = { "User" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;

    @ApiOperation(value = "Create a new user")
    @PostMapping
    public ResponseEntity<ClientDto> createUser(
            @ApiParam(value = "Provide user data to create a new user", required = true) @RequestBody ClientDto userDto) {
        ClientDto createdUser = service.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "Get a user by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<ClientDto> getUserById(
            @ApiParam(value = "Id of the user", required = true) @PathVariable Long id) {
        ClientDto userDto = service.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all users")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientDto>> getAllUsers() {
        List<ClientDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a user by id")
    public ResponseEntity<ClientDto> updateUser(
            @ApiParam(value = "Id of the user", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated user data", required = true) @RequestBody ClientDto userDto) {
        ClientDto updatedUser = service.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a user by id")
    public ResponseEntity<Void> deleteUser(
            @ApiParam(value = "Id of the user", required = true) @PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
