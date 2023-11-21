package com.example.libraryapi.controller;

import com.example.libraryapi.dto.BookDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.BookService;

import java.util.List;

@Api(value = "Book Management System", tags = {"Book"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")

public class BookController {
    private final BookService bookService;

    @ApiOperation(value = "Create a new book")
    @PostMapping
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a book by ID")
    public ResponseEntity<BookDto> getBookById(
            @ApiParam(value = "Book ID", required = true) @PathVariable final Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a book by ID")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<BookDto> updateBook(
            @ApiParam(value = "Book ID", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated book data", required = true) @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a book by ID")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteBook(
            @ApiParam(value = "Book ID", required = true) @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
