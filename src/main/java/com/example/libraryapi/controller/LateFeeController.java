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
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.LateFeeService;

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
    @PostMapping()
    public ResponseEntity<LateFeeDto> createNewLateFee(
            @ApiParam(value = "Provide late fee data to create a new late fee", required = true) @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto createdLateFee = service.createLateFee(lateFeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLateFee);
    }

    @ApiOperation("Get a late fee by ID")
    @GetMapping("/{lateFeeId}")
    public ResponseEntity<LateFeeDto> getLateFeeById(
            @ApiParam(value = "Late fee ID", required = true) @PathVariable final Long lateFeeId) {
        LateFeeDto lateFeeDto = service.getLateFeeById(lateFeeId);
        return ResponseEntity.ok(lateFeeDto);
    }

    @ApiOperation("Get all late fees")
    @GetMapping()
    public ResponseEntity<List<LateFeeDto>> showAllLateFees() {
        List<LateFeeDto> lateFees = service.getAllLateFees();
        return ResponseEntity.ok(lateFees);
    }

    @ApiOperation("Update a late fee by ID")
    @PutMapping("/{id}")
    public ResponseEntity<LateFeeDto> updateLateFee(
            @ApiParam(value = "Late fee ID", required = true) @PathVariable final Long id,
            @ApiParam(value = "Update late fee data", required = true) @RequestBody LateFeeDto lateFeeDto) {
        LateFeeDto updatedLateFee = service.updateLateFee(id, lateFeeDto);
        return ResponseEntity.ok(updatedLateFee);
    }

    @ApiOperation("Remove late fee by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLateFee(
            @ApiParam(value = "Late fee ID", required = true) @PathVariable final Long id) {
        service.deleteLateFee(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Get late fees by Borrow ID")
    @GetMapping("/by-borrow/{borrowId}")
    public ResponseEntity<List<LateFeeDto>> getLateFeesByBorrowId(
            @ApiParam(value = "Borrow ID", required = true) @PathVariable final Long borrowId) {
        List<LateFeeDto> lateFees = service.getLateFeesByBorrowID(borrowId);
        return ResponseEntity.ok(lateFees);
    }

    @Secured("ROLE_LIBRARIAN")
    @ApiOperation("Handle late returns and calculate late fees")
    @PostMapping("/handle-late-return")
    public ResponseEntity<Optional<LateFeeDto>> handleLateReturn(
            @ApiParam(value = "Borrow ID", required = true) @RequestParam Long borrowId,
            @ApiParam(value = "Return date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        Optional<LateFeeDto> lateFeeDto = service.calculateAndRecordLateFee(borrowId, returnDate);
        return ResponseEntity.ok(lateFeeDto);
    }
}
