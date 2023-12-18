package com.example.libraryapi.controller;

import com.example.libraryapi.dto.BookDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.BookService;

import java.util.List;
import java.util.Map;

@Api(value = "Book Management System", tags = {"Book"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")

public class BookController {
    private final BookService bookService;

    @PostMapping
    @ApiOperation(value = "Create new book", notes = "Creates a new book based on the provided book data.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<BookDto> createBook(
            @ApiParam(value = "Book data", required = true) @Valid @RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get book by id", notes = "Retrieves book information based on the provided book id.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BookDto> getBookById(
            @ApiParam(value = "Book id", required = true) @PathVariable final Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all books", notes = "Retrieves information about all books in the system.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update book by id", notes = "Updates an existing book based on the provided fields.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> updateBook(
            @ApiParam(value = "Book id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Fields to be updated along with their new values", required = true)
            @RequestBody Map<String, String> fieldsToUpdate) {
        bookService.updateBook(id, fieldsToUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete book by id", notes = "Deletes a book based on the provided book id.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteBook(
            @ApiParam(value = "Book id", required = true) @PathVariable final Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
