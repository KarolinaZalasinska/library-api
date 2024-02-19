package com.example.libraryapi.controller;

import com.example.libraryapi.dto.AuthorDto;
import io.swagger.annotations.*;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.AuthorService;

import java.util.List;
import java.util.Map;

@Api(value = "Author Management System", tags = {"Author"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    @ApiOperation(value = "Create new author", notes = "Creates a new author with the provided data.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<AuthorDto> createAuthor(
            @ApiParam(value = "Author data", required = true) @Valid @RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.createAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get author by id", notes = "Retrieves author information based on the provided author id.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthorDto> getAuthorById(
            @ApiParam(value = "Author id", required = true) @PathVariable final Long id) {
        AuthorDto authorDto = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all authors", notes = "Retrieves information about all authors in the system.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update author by id", notes = "Partially updates author information based on the provided fields.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<Void> updateAuthor(
            @ApiParam(value = "Author id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Fields to be updated along with their new values", required = true)
            @RequestBody Map<String, String> fieldsToUpdate) {
        authorService.updateAuthor(id, fieldsToUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete author by id", notes = "Deletes the author with the provided id.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteAuthor(
            @ApiParam(value = "Author id", required = true) @PathVariable final Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{authorId}/books/{bookId}")
    @ApiOperation(value = "Add a book to an author", notes = "Associates a book with an existing author.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<AuthorDto> addBookToAuthor(
            @ApiParam(value = "Author id", required = true) @PathVariable Long authorId,
            @ApiParam(value = "Book id", required = true) @PathVariable Long bookId) {
        return ResponseEntity.ok(authorService.addBookToAuthor(authorId, bookId));
    }

}
