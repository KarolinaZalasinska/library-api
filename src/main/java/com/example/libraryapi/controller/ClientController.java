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

@Api(value = "Client Management System", tags = { "Client" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;

    @ApiOperation(value = "Create a new client")
    @PostMapping
    public ResponseEntity<ClientDto> createClient(
            @ApiParam(value = "Provide user data to create a new client", required = true) @RequestBody ClientDto clientDto) {
        ClientDto createdUser = service.createClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "Get a client by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<ClientDto> getUserById(
            @ApiParam(value = "Id of the client", required = true) @PathVariable Long id) {
        ClientDto userDto = service.getClientById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all clients")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientDto>> getAllUsers() {
        List<ClientDto> users = service.getAllClients();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a user by id")
    public ResponseEntity<ClientDto> updateUser(
            @ApiParam(value = "Id of the user", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated user data", required = true) @RequestBody ClientDto userDto) {
        ClientDto updatedUser = service.updateClient(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a user by id")
    public ResponseEntity<Void> deleteUser(
            @ApiParam(value = "Id of the user", required = true) @PathVariable Long id) {
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
