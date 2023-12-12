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

    @PostMapping
    @ApiOperation(value = "Create new author")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<AuthorDto> createAuthor(
            @ApiParam(value = "Author data", required = true) @Valid @RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.createAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get author by id")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthorDto> getAuthorById(
            @ApiParam(value = "Author id", required = true) @PathVariable final Long id) {
        AuthorDto authorDto = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all authors")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update author by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<AuthorDto> updateAuthor(
            @ApiParam(value = "Author id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Updated author data", required = true) @Valid @RequestBody AuthorDto updatedAuthor) {
        AuthorDto updated = authorService.updateAuthor(id, updatedAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete author by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteAuthor(
            @ApiParam(value = "Author id", required = true) @PathVariable final Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
