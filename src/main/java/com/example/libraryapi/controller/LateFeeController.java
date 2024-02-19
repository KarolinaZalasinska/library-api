package com.example.libraryapi.controller;

import com.example.libraryapi.dto.LateFeeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.LateFeeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(value = "Late fees Management System", tags = {"Late fee"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/late-fees")
public class LateFeeController {

    private final LateFeeService service;

    @PostMapping("/create")
    @ApiOperation(value = "Create new late fee", notes = "Creates a new late fee based on the provided data.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> createNewLateFee(
            @ApiParam(value = "Provide late fee data to create a new late fee", required = true) @Valid @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto createdLateFee = service.createLateFee(lateFeeDto);
        return ResponseEntity.ok(createdLateFee);
    }

    @GetMapping("/{lateFeeId}")
    @ApiOperation(value = "Get late fee by id", notes = "Retrieves information about a late fee based on its identifier.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> getLateFeeById(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long lateFeeId) {
        LateFeeDto lateFeeDto = service.getLateFeeById(lateFeeId);
        return ResponseEntity.ok(lateFeeDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all late fees", notes = "Retrieves a list of all late fees.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<List<LateFeeDto>> showAllLateFees() {
        List<LateFeeDto> lateFees = service.getAllLateFees();
        return ResponseEntity.ok(lateFees);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update late fee by id", notes = "Updates an existing late fee based on the provided data.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<Void> updateLateFee(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Update late fee data", required = true) @RequestBody Map<String, String> fieldsToUpdate) {
        service.updateLateFee(id, fieldsToUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove late fee by id", notes = "Deletes a late fee based on its identifier.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<Void> removeLateFee(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long id) {
        service.deleteLateFee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-borrow/{borrowId}")
    @ApiOperation(value = "Get late fees by borrow id", notes = "Retrieves a list of late fees associated with a specific borrow.")
    @PreAuthorize("isAuthenticated() and " +
            "(hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name()) or " +
            "hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).USER.name()))")
    public ResponseEntity<List<LateFeeDto>> getLateFeesByBorrowId(
            @ApiParam(value = "Borrow id", required = true) @PathVariable final Long borrowId) {
        List<LateFeeDto> lateFees = service.getLateFeesByBorrowId(borrowId);
        return ResponseEntity.ok(lateFees);
    }

    @PostMapping("/handle-late-return/{borrowId}")
    @ApiOperation(value = "Handle late returns and calculate late fees", notes = "Handles late returns for a specific borrow and calculates late fees if applicable.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<Optional<LateFeeDto>> handleLateReturn(
            @ApiParam(value = "Borrow id", required = true) @PathVariable final Long borrowId) {
        Optional<LateFeeDto> lateFeeDto = service.tryCalculateAndRecordLateFee(borrowId);
        return ResponseEntity.ok(lateFeeDto);
    }
}