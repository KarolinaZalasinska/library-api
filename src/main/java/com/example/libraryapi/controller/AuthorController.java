package com.example.libraryapi.controller;

import com.example.libraryapi.dto.AuthorDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.AuthorService;

import java.util.List;
@Api(value = "Author Management System", tags = {"Author"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @ApiOperation(value = "Create a new author")
    @PostMapping
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<AuthorDto> createAuthor(
            @ApiParam(value = "Author data", required = true) @Valid @RequestBody final AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.createAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @ApiOperation(value = "Update an author by ID")
    @PutMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<AuthorDto> updateAuthor(
            @ApiParam(value = "Author ID", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated author data", required = true) @RequestBody AuthorDto updatedAuthor) {
        AuthorDto updated = authorService.updateAuthor(id, updatedAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Get an author by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @ApiParam(value = "Author ID", required = true) @PathVariable Long id) {
        AuthorDto authorDto = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDto);
    }

    @ApiOperation(value = "Get all authors")
    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @ApiOperation(value = "Delete an author by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteAuthor(
            @ApiParam(value = "Author ID", required = true) @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
