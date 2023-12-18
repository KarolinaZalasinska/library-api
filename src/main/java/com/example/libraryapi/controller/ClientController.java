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
import java.util.Map;

@Api(value = "Client Management System", tags = {"Client"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;

    @PostMapping
    @ApiOperation(value = "Create new client", notes = "Creates a new client based on the provided data.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<ClientDto> createClient(
            @ApiParam(value = "Provide client data to create a new client", required = true) @RequestBody ClientDto clientDto) {
        ClientDto createdClient = service.createClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a client by id", notes = "Retrieves information about a client based on the provided id.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<ClientDto> getUserById(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long id) {
        ClientDto clientDto = service.getClientById(id);
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all clients", notes = "Retrieves information about all clients in the system.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = service.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update client by id", notes = "Updates an existing client based on the provided data.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<ClientDto> updateClient(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Updated client data", required = true) @RequestBody Map<String, String> fieldsToUpdate) {
        service.updateClient(id, fieldsToUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete client by id", notes = "Deletes a client based on the provided id.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteClient(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long id) {
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
