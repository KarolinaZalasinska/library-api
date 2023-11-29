package com.example.libraryapi.controller;

import com.example.libraryapi.dto.LateFeeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.LateFeeService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Api(value = "Late fees Management System", tags = {"Late fee"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/late-fees")
public class LateFeeController {

    private final LateFeeService service;

    @ApiOperation(value = "Create a new late fee")
    @PostMapping("/create")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> createNewLateFee(
            @ApiParam(value = "Provide late fee data to create a new late fee", required = true) @Valid @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto createdLateFee = service.createLateFee(lateFeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLateFee);
    }

    @ApiOperation("Get a late fee by id")
    @GetMapping("/{lateFeeId}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<LateFeeDto> getLateFeeById(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long lateFeeId) {
        LateFeeDto lateFeeDto = service.getLateFeeById(lateFeeId);
        return ResponseEntity.ok(lateFeeDto);
    }

    @ApiOperation("Get all late fees")
    @GetMapping()
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<LateFeeDto>> showAllLateFees() {
        List<LateFeeDto> lateFees = service.getAllLateFees();
        return ResponseEntity.ok(lateFees);
    }

    @ApiOperation("Update a late fee by id")
    @PutMapping("/{id}")
    public ResponseEntity<LateFeeDto> updateLateFee(
            @ApiParam(value = "Late fee id", required = true) @Valid @PathVariable final Long id,
            @ApiParam(value = "Update late fee data", required = true) @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto updatedLateFee = service.updateLateFee(id, lateFeeDto);
        return ResponseEntity.ok(updatedLateFee);
    }

    @ApiOperation("Remove late fee by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> removeLateFee(
            @ApiParam(value = "Late fee id", required = true) @PathVariable final Long id) {
        service.deleteLateFee(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Get late fees by borrow id")
    @GetMapping("/by-borrow/{borrowId}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<LateFeeDto>> getLateFeesByBorrowId(
            @ApiParam(value = "Borrow id", required = true) @PathVariable final Long borrowId) {
        List<LateFeeDto> lateFees = service.getLateFeesByBorrowID(borrowId);
        return ResponseEntity.ok(lateFees);
    }
    
    @ApiOperation("Handle late returns and calculate late fees")
    @PostMapping("/handle-late-return")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Optional<LateFeeDto>> handleLateReturn(
            @ApiParam(value = "Borrow id", required = true) @RequestParam Long borrowId) {
        Optional<LateFeeDto> lateFeeDto = service.calculateAndRecordLateFee(borrowId);
        return ResponseEntity.ok(lateFeeDto);
    }

}
