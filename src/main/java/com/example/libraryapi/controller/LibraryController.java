package com.example.libraryapi.controller;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.LibraryDto;
import com.example.libraryapi.model.Library;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.LibraryService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(value = "Library Management System", tags = {"Library"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService service;

    @PostMapping
    @ApiOperation(value = "Create new library")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<LibraryDto> createLibrary(
            @ApiParam(value = "Provide library data to create a new library", required = true) @Valid @RequestBody LibraryDto libraryDto) {
        LibraryDto createdLibrary = service.createLibrary(libraryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLibrary);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a library by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<LibraryDto> getLibraryById(
            @ApiParam(value = "Library id", required = true) @PathVariable final Long id) {
        LibraryDto libraryDto = service.getLibraryById(id);
        return ResponseEntity.ok(libraryDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all libraries")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<LibraryDto>> getAllLibraries() {
        List<LibraryDto> libraries = service.getAllLibraries();
        return ResponseEntity.ok(libraries);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update specified fields for a library with the given library id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Library> updateLibraryFields(
            @ApiParam(value = "Library id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Fields to update", required = true) @Valid @RequestBody Map<String, String> fieldsToUpdate) {
        Library updatedLibrary = service.updateLibrary(id, fieldsToUpdate);
        return ResponseEntity.ok(updatedLibrary);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete library by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteLibrary(
            @ApiParam(value = "Library id", required = true) @PathVariable final Long id) {
        service.deleteLibrary(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")
    @ApiOperation(value = "Get books in a library by id")
    @PreAuthorize("isAuthenticated() and " +
            "(hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name()) or " +
            "hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name()))")
    public ResponseEntity<Set<BookDto>> getBooksInLibrary(
            @ApiParam(value = "Library id", required = true) @PathVariable final Long id) {
        Set<BookDto> books = service.getBooksInLibrary(id);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/{libraryId}/add-book/{bookId}")
    @ApiOperation(value = "Add book to library by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<LibraryDto> addBookToLibrary(
            @ApiParam(value = "Library id", required = true) @PathVariable final Long libraryId,
            @ApiParam(value = "Book id", required = true) @PathVariable final Long bookId) {
        LibraryDto updatedLibrary = service.addBookToLibrary(libraryId, bookId);
        return ResponseEntity.ok(updatedLibrary);
    }
}
