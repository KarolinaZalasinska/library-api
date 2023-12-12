package com.example.libraryapi.controller;

import com.example.libraryapi.dto.LateFeeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.LateFeeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(value = "Late fees Management System", tags = {"Late fee"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/late-fees")
public class LateFeeController {

    private final LateFeeService service;

    @PostMapping("/create")
    @ApiOperation(value = "Create new late fee")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> createNewLateFee(
            @ApiParam(value = "Provide late fee data to create a new late fee", required = true) @Valid @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto createdLateFee = service.createLateFee(lateFeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLateFee);
    }

    @GetMapping("/{lateFeeId}")
    @ApiOperation("Get late fee by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> getLateFeeById(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long lateFeeId) {
        LateFeeDto lateFeeDto = service.getLateFeeById(lateFeeId);
        return ResponseEntity.ok(lateFeeDto);
    }

    @GetMapping
    @ApiOperation("Get all late fees")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<LateFeeDto>> showAllLateFees() {
        List<LateFeeDto> lateFees = service.getAllLateFees();
        return ResponseEntity.ok(lateFees);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update late fee by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> updateLateFee(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Update late fee data", required = true) @Valid @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto updatedLateFee = service.updateLateFee(id, lateFeeDto);
        return ResponseEntity.ok(updatedLateFee);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Remove late fee by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> removeLateFee(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long id) {
        service.deleteLateFee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-borrow/{borrowId}")
    @ApiOperation("Get late fees by borrow id")
    @PreAuthorize("isAuthenticated() and " +
            "(hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name()) or " +
            "hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name()))")
    public ResponseEntity<List<LateFeeDto>> getLateFeesByBorrowId(
            @ApiParam(value = "Borrow id", required = true) @PathVariable final Long borrowId) {
        List<LateFeeDto> lateFees = service.getLateFeesByBorrowID(borrowId);
        return ResponseEntity.ok(lateFees);
    }

    @PostMapping("/handle-late-return")
    @ApiOperation("Handle late returns and calculate late fees")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Optional<LateFeeDto>> handleLateReturn(
            @ApiParam(value = "Borrow id", required = true) @RequestParam final Long borrowId) {
        Optional<LateFeeDto> lateFeeDto = service.calculateAndRecordLateFee(borrowId);
        return ResponseEntity.ok(lateFeeDto);
    }

}
