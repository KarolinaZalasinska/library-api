package com.example.libraryapi.controller;

import com.example.libraryapi.dto.AuthorDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.AuthorService;

import java.util.List;
@Api(value = "Author Management System", tags = {"Authors"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @ApiOperation(value = "Create a new author")
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(
            @ApiParam(value = "Author data", required = true) @Valid @RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.createAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @ApiOperation(value = "Update an author by ID")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @ApiParam(value = "ID of the author", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated author data", required = true) @RequestBody AuthorDto updatedAuthor) {
        AuthorDto updated = authorService.updateAuthor(id, updatedAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @ApiOperation(value = "Get an author by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @ApiParam(value = "ID of the author", required = true) @PathVariable Long id) {
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
    public ResponseEntity<Void> deleteAuthor(
            @ApiParam(value = "ID of the author", required = true) @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
