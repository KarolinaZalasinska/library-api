package controller;

import dto.BookDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookService;

import java.util.List;
@Api(value = "Book Management System", tags = { "Book" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")

public class BookController {
    private final BookService service;
    @ApiOperation(value = "Create a new book")
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto createdBook = service.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a book by ID")
    public ResponseEntity<BookDto> getBookById(
            @ApiParam(value = "ID of the book", required = true) @PathVariable final Long id) {
        BookDto bookDto = service.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = service.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a book by ID")
    public ResponseEntity<BookDto> updateBook(
            @ApiParam(value = "ID of the book", required = true) @PathVariable Long id,
            @RequestBody BookDto bookDto) {
        BookDto updatedBook = service.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a book by ID")
    public ResponseEntity<Void> deleteBook(
            @ApiParam(value = "ID of the book", required = true) @PathVariable Long id) {
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
