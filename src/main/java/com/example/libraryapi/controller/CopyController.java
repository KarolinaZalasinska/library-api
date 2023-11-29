package com.example.libraryapi.controller;

import com.example.libraryapi.dto.CopyDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.CopyService;

import java.util.List;

@Api(value = "Copy Management System", tags = {"Copy"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/copies")
public class CopyController {
    private final CopyService service;

    @ApiOperation("Create a new copy")
    @PostMapping()
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<CopyDto> createCopy(
            @ApiParam(value = "Provide copy data to create a new copy", required = true) @RequestBody CopyDto copyDto) {
        CopyDto createdCopy = service.createCopy(copyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCopy);
    }

    @ApiOperation("Get a copy by id")
    @GetMapping("/{id}")
    public ResponseEntity<CopyDto> getCopyById(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id) {
        CopyDto copyDto = service.getCopyById(id);
        return ResponseEntity.ok(copyDto);
    }

    @ApiOperation("Get all copies")
    @GetMapping()
    public ResponseEntity<List<CopyDto>> getAllCopies() {
        List<CopyDto> copies = service.getAllCopies();
        return ResponseEntity.ok(copies);
    }

    @ApiOperation("Update a copy by id")
    @PutMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<CopyDto> updateCopy(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Updated copy data", required = true) @RequestBody CopyDto copyDto) {
        CopyDto updatedCopy = service.updateCopy(id, copyDto);
        return ResponseEntity.ok(updatedCopy);
    }

    @ApiOperation("Delete a copy by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteCopy(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id) {
        service.deleteCopy(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Get available copies now")
    @GetMapping("/available-now")
    public ResponseEntity<List<CopyDto>> getAvailableCopiesNow() {
        List<CopyDto> availableCopiesNow = service.getAvailableCopiesNow();
        return ResponseEntity.ok(availableCopiesNow);
    }

    @ApiOperation("Get copies for a specific book")
    @GetMapping("/for-book")
    public ResponseEntity<List<CopyDto>> getCopiesForBook(
            @ApiParam(value = "Book id", required = true) @RequestParam Long bookId) {
        List<CopyDto> copiesForBook = service.getCopiesForBook(bookId);
        return ResponseEntity.ok(copiesForBook);
    }


    @ApiOperation("Get available copies for a specific book")
    @GetMapping("/available-for-book")
    public ResponseEntity<List<CopyDto>> getAvailableCopiesForBook(
            @ApiParam(value = "Book id", required = true) @RequestParam Long bookId) {
        List<CopyDto> availableCopiesForBook = service.getAvailableCopiesForBook(bookId);
        return ResponseEntity.ok(availableCopiesForBook);
    }

    @ApiOperation("Get borrowed copies for a specific client")
    @GetMapping("/borrowed-for-client")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getBorrowedCopiesForClient(
            @ApiParam(value = "Client id", required = true) @RequestParam Long clientId) {
        List<CopyDto> borrowedCopiesForUser = service.getBorrowedCopiesForClient(clientId);
        return ResponseEntity.ok(borrowedCopiesForUser);
    }

    @ApiOperation("Get overdue copies")
    @GetMapping("/overdue")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getOverdueCopies() {
        List<CopyDto> overdueCopies = service.getOverdueCopies();
        return ResponseEntity.ok(overdueCopies);
    }

    @ApiOperation("Get copy details by ID")
    @GetMapping("/details/{id}")
    public ResponseEntity<CopyDto> getCopyDetails(
            @ApiParam(value = "Copy id", required = true) @PathVariable Long id) {
        CopyDto copyDetails = service.getCopyDetails(id);
        return ResponseEntity.ok(copyDetails);
    }

    @ApiOperation("Get currently borrowed copies")
    @GetMapping("/currently-borrowed")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getCurrentlyBorrowedCopies() {
        List<CopyDto> currentlyBorrowedCopies = service.getCurrentlyBorrowedCopies();
        return ResponseEntity.ok(currentlyBorrowedCopies);
    }


}
