package controller;

import dto.BookDto;
import exception.ObjectNotFoundInRepositoryException;
import exception.SomeCustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Api(value = "Book Controller", tags = { "Books" })
public class BookController {
    private final BookService service;

    @PostMapping
    @ApiOperation("Create a new book")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto createdBook = service.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a book by ID")
    public ResponseEntity<BookDto> getBookById(
            @ApiParam(value = "ID of the book", required = true) @PathVariable Long id) {
        BookDto bookDto = service.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping
    @ApiOperation("Get all books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = service.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a book by ID")
    public ResponseEntity<BookDto> updateBook(
            @ApiParam(value = "ID of the book", required = true) @PathVariable Long id,
            @RequestBody BookDto bookDto) {
        BookDto updatedBook = service.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a book by ID")
    public ResponseEntity<Void> deleteBook(
            @ApiParam(value = "ID of the book", required = true) @PathVariable Long id) {
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ObjectNotFoundInRepositoryException.class)
    public ResponseEntity<Object> handleObjectNotFoundInRepository(ObjectNotFoundInRepositoryException e) {
        Long id = e.getId();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Object not found with ID " + id);
    }

    @ExceptionHandler(SomeCustomException.class)
    public ResponseEntity<Object> handleCustomException(SomeCustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create: " + e.getMessage());
    }
}
