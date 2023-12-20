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
import java.util.Map;
import java.util.Set;

@Api(value = "Copy Management System", tags = {"Copy"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/copies")
public class CopyController {
    private final CopyService service;

    @PostMapping
    @ApiOperation("Create new copy")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<CopyDto> createCopy(
            @ApiParam(value = "Provide copy data to create a new copy", required = true) @RequestBody CopyDto copyDto) {
        CopyDto createdCopy = service.createCopy(copyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCopy);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get copy by id")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CopyDto> getCopyById(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id) {
        CopyDto copyDto = service.getCopyById(id);
        return ResponseEntity.ok(copyDto);
    }

    @GetMapping
    @ApiOperation("Get all copies")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CopyDto>> getAllCopies() {
        List<CopyDto> copies = service.getAllCopies();
        return ResponseEntity.ok(copies);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update copy by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> updateCopy(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Updated copy data", required = true) @RequestBody Map<String, String> copyUpdate) {
        service.updateCopy(id, copyUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete copy by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteCopy(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id) {
        service.deleteCopy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-now")
    @ApiOperation("Get available copies now")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<CopyDto>> getAvailableCopiesNow() {
        Set<CopyDto> availableCopiesNow = service.getAvailableCopiesNow();
        return ResponseEntity.ok(availableCopiesNow);
    }

    @GetMapping("/for-book")
    @ApiOperation("Get all copies for the book with the given id")
    @PreAuthorize("isAuthenticated() and " +
            "(hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name()) or " +
            "hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name()))")
    public ResponseEntity<List<CopyDto>> getCopiesForBook(
            @ApiParam(value = "Book id", required = true) @RequestParam final Long bookId) {
        List<CopyDto> copiesForBook = service.getCopiesForBook(bookId);
        return ResponseEntity.ok(copiesForBook);
    }

    @GetMapping("/available-for-book")
    @ApiOperation("Get available copies for a specific book")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CopyDto>> getAvailableCopiesForBook(
            @ApiParam(value = "Book id", required = true) @RequestParam final Long bookId) {
        List<CopyDto> availableCopiesForBook = service.getAvailableCopiesForBook(bookId);
        return ResponseEntity.ok(availableCopiesForBook);
    }

    @GetMapping("/borrowed-for-client")
    @ApiOperation("Get borrowed copies for a specific client")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getBorrowedCopiesForClient(
            @ApiParam(value = "Client id", required = true) @RequestParam final Long clientId) {
        List<CopyDto> borrowedCopiesForClient = service.getBorrowedCopiesForClient(clientId);
        return ResponseEntity.ok(borrowedCopiesForClient);
    }

    @GetMapping("/overdue")
    @ApiOperation("Get overdue copies")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getOverdueCopies() {
        List<CopyDto> overdueCopies = service.getOverdueCopies();
        return ResponseEntity.ok(overdueCopies);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("Get copy details by id")
    @PreAuthorize("isAuthenticated() and " +
            "(hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name()) or " +
            "hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name()))")
    public ResponseEntity<CopyDto> getCopyDetails(
            @ApiParam(value = "Copy id", required = true) @PathVariable final Long id) {
        CopyDto copyDetails = service.getCopyDetails(id);
        return ResponseEntity.ok(copyDetails);
    }

    @GetMapping("/currently-borrowed")
    @ApiOperation("Get currently borrowed copies")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getCurrentlyBorrowedCopies() {
        List<CopyDto> currentlyBorrowedCopies = service.getCurrentlyBorrowedCopies();
        return ResponseEntity.ok(currentlyBorrowedCopies);
    }
}
